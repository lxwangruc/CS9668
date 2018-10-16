import java.util.Vector; //We need this for the Vector class.

/* Leader election algorithm for a synchronous ring network. */
/* A leader is chosen who has the largest ID and all nodes are */
/* designated as "Leader" or "Not Leader". */
public class LeaderElection extends Algorithm {


    public Object run() {
        // Our electLeader algorithm returns a string "Leader" or "Not Leader". 
        // This is the result/solution that our run method must return.
        String status = electLeader(getID());
        return status;
    }

    public String electLeader(String id) {
        // ===Initial Setup===
        Vector<String> v = neighbours(); // Set of neighbours of this node.
        String rightNeighbour = (String) v.elementAt(1); // First neighbour will be assumed
                                                         // as neighbour on the right.

        // Message to send on the first round.
        // makeMessage takes a destination and our data as a string.
        // In this case we are sending our ID to the neighbour on the right.
        Message mssg = makeMessage(rightNeighbour, id);
		Message m;
        String status = "unknown";
        
        // ===Main Loop===
        try {
            while (waitForNextRound()) { // A synchronous algorithm must wait for the 
                                         // beginning of the next round before sending
                                         // messages.
                
                // ===Send Phase===
                if (mssg != null) {
                    send(mssg); // The destination and data is inside the Message.
                    
                    // If we are sent an END message terminate after.
                    if (equal(mssg.data(),"END")) {
                        // ===Terminate===
                        return status;
                    }
                }
                mssg = null;

                // ===Receive Phase===
                m = receive();
                if (m != null) {

                    if (equal(m.data(),"END")) 
                        mssg = makeMessage(rightNeighbour,"END");
                    else {
                        if (equal(m.data(),id)) { // We got our own message back, we must be the leader!
                            mssg = makeMessage(rightNeighbour,"END");
                            status = "Leader";
                            printMessage("** Process "+id+" is the leader");
                        } else if (larger(m.data(),id)) {
                            mssg = makeMessage(rightNeighbour,m.data());

                            if (equal(status,"unknown")) {
                                printMessage("** Process "+id+" is not the leader");
                                status = "Not leader";
                            }
                        } else { 
                            mssg = null;
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
