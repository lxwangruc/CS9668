import java.util.Vector; //We need this for the Vector class.

/* Leader election algorithm for a synchronous line network. */
/* A leader is chosen who has the largest ID and all nodes are */
/* designated as "Leader" or "Not Leader". */
public class A2Q2LeaderElection extends Algorithm {


    public Object run() {
        // Our electLeader algorithm returns a string "Leader" or "Not Leader". 
        // This is the result/solution that our run method must return.
        String status = electLeader(getID());
        return status;
    }

    public String electLeader(String id) {
        // ===Initial Setup===
        Vector<String> v = neighbours(); // Set of neighbours of this node.
        String leftNeighbour = (String) v.elementAt(0);
        String rightNeighbour = (String) v.elementAt(1); // First neighbour will be assumed
                                                         // as neighbour on the right.
        boolean leftmostProcessor, rightmostProcessor;
        if (equal(leftNeighbour,"0")) leftmostProcessor = true;
        else leftmostProcessor = false;
        if (equal(rightNeighbour,"0")) rightmostProcessor = true;
        else rightmostProcessor = false;

        // Message to send on the first round.
        // makeMessage takes a destination and our data as a string.
        // In this case we are sending our ID to the neighbour on the right.
        Message mssg = null;
        if (leftmostProcessor)
            mssg = makeMessage(rightNeighbour, id);
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
                    if(equal(mssg.destination(),leftNeighbour))
                        return status;
                }
                mssg = null;

                // ===Receive Phase===
                m = receive();
                if (m != null) {
                    if(rightmostProcessor){
                        if (larger(m.data(),id)) {
                            mssg = makeMessage(leftNeighbour,m.data());
                            printMessage("** Process "+id+" is not the leader");
                            status = "Not leader";
                        }
                        else{
                            mssg = makeMessage(leftNeighbour,id);
                            printMessage("** Process "+id+" is the leader");
                            status = "Leader";
                        } 
                    }
                    else if(leftmostProcessor){
                        if (larger(m.data(),id)) {
                            printMessage("** Process "+id+" is not the leader");
                            status = "Not leader";
                        }
                        else { 
                            printMessage("** Process "+id+" is the leader");
                            status = "Leader";
                        }
                        return status;
                    }
                    else{
                        if(equal(m.source(),leftNeighbour)){
                            if (larger(m.data(),id)) 
                               mssg = makeMessage(rightNeighbour,m.data()); 
                            else
                                mssg = makeMessage(rightNeighbour,id); 
                        }
                        else if(equal(m.source(),rightNeighbour)){
                            mssg = makeMessage(leftNeighbour,m.data());
                            if (larger(m.data(),id)){ 
                               printMessage("** Process "+id+" is not the leader");
                                status = "Not leader";
                            } 
                            else{
                                printMessage("** Process "+id+" is the leader");
                                status = "Leader";
			    }
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
