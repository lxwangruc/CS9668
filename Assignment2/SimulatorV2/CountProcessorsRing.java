import java.util.Vector; //We need this for the Vector class.

/* Leader election algorithm for a synchronous ring network. */
/* A leader is chosen who has the largest ID and all nodes are */
/* designated as "Leader" or "Not Leader". */
public class CountProcessorsRing extends Algorithm {


    public Object run() {
        // Our electLeader algorithm returns a string "Leader" or "Not Leader". 
        // This is the result/solution that our run method must return.
        String status = countProcessor(getID());
        return status;
    }

    public String countProcessor(String id) {
        // ===Initial Setup===
        Vector<String> v = neighbours(); // Set of neighbours of this node.
        String leftNeighbour = (String) v.elementAt(0);
        String rightNeighbour = (String) v.elementAt(1); // First neighbour will be assumed
                                                         // as neighbour on the right.

        // Message to send on the first round.
        // makeMessage takes a destination and our data as a string.
        // In this case we are sending our ID to the neighbour on the right.
        int d = 1;
        int dist;
        String message = String.format("%s %d", id, d);
        Message mssg = makeMessage(rightNeighbour, message);
		Message m;
        int numberOfProcessors = 0;
        
        // ===Main Loop===
        try {
            while (waitForNextRound()) { // A synchronous algorithm must wait for the 
                                         // beginning of the next round before sending
                                         // messages.
                
                // ===Send Phase===
                if (mssg != null) {
                    send(mssg); // The destination and data is inside the Message.
                    
                    // If we are sent an END message terminate after.
                    if (equal(mssg.data().split("\\s")[0],"END")) {
                        // ===Terminate===
                        return mssg.data().split("\\s")[1];
                    }
                }
                mssg = null;

                // ===Receive Phase===
                m = receive();
                if (m != null) {
                	String[] m_split = m.data().split("\\s");
                    if (equal(m_split[0],"END")) 
                        mssg = makeMessage(rightNeighbour,m.data());
                    else {
                        if (equal(m.source(),leftNeighbour)) {
                        	if (larger(m_split[0],id)){
                        		if(equal(m_split[1],"1"))
                            		mssg = makeMessage(leftNeighbour,m_split[0]);
                            	else {
                            		message = String.format("%s %d",m_split[0],Integer.parseInt(m_split[1])-1);
                            		mssg = makeMessage(rightNeighbour,message);
                            	}
                            }
                            else if(equal(m_split[0],id)){
                            	numberOfProcessors = d - Integer.parseInt(m_split[1]) + 1;
                            	message = String.format("%s %d","END",numberOfProcessors);
                            	mssg = makeMessage(rightNeighbour,message);
                            }
                        } 
                        else if (equal(m.data(),id)){
                        	d = 2*d;
                        	message = String.format("%s %d",id,d);
                        	mssg = makeMessage(rightNeighbour,message);
                        }
                        else{
                        	mssg = makeMessage(leftNeighbour,m.data());
                        }
                    }
                }
            }
        } catch(SimulatorException e){
            System.out.println("ERROR: " + e.toString());
        }
    
        // If we got here, something went wrong! (Exception, node failed, etc.)
        // Return no result.
        return null;
    }

}
