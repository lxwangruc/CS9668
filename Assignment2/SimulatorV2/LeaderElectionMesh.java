import java.util.Vector; //We need this for the Vector class.

/* Leader election algorithm for a synchronous line network. */
/* A leader is chosen who has the largest ID and all nodes are */
/* designated as "Leader" or "Not Leader". */
public class LeaderElectionMesh extends Algorithm {


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
        String topNeighbour = (String) v.elementAt(1); 
        String rightNeighbour = (String) v.elementAt(2);
        String bottomNeighbour = (String) v.elementAt(3);// First neighbour will be assumed
                                                         // as neighbour on the right.
        boolean leftProcessor, rightProcessor,topProcessor,bottomProcessor;
        if (equal(leftNeighbour,"0")) leftProcessor = true;
        else leftProcessor = false;
        if (equal(rightNeighbour,"0")) rightProcessor = true;
        else rightProcessor = false;
        if (equal(topNeighbour,"0")) topProcessor = true;
        else topProcessor = false;
        if (equal(bottomNeighbour,"0")) bottomProcessor = true;
        else bottomProcessor = false;

        // Message to send on the first round.
        // makeMessage takes a destination and our data as a string.
        // In this case we are sending our ID to the neighbour on the right.
        Message mssg = null;
        if (leftProcessor)
            mssg = makeMessage(rightNeighbour, id);
		Message m;
        String status = "unknown";
        String id_l;
        int roundLeft = -1;
        // ===Main Loop===
        try {
            while (waitForNextRound()) { // A synchronous algorithm must wait for the 
                                         // beginning of the next round before sending
                                         // messages.
                
                // ===Send Phase===
                if (mssg != null) {
                    send(mssg); // The destination and data is inside the Message.
                    if(equal(mssg.destination(),leftNeighbour))
                        roundLeft = 0;
                    if(equal(mssg.destination(),topNeighbour)){
                        roundLeft = 1;
                        mssg = makeMessage(leftNeighbour,mssg.data());
                    }
                    else
                        mssg = null;
                }

                // ===Receive Phase===
                m = receive();
                
                if (m != null) {
                    if (larger(m.data(),id))
                        id_l = m.data();
                    else
                        id_l = id;
                    if (leftProcessor) {
                        if (larger(id_l,id)) {
                            printMessage("** Process "+id+" is not the leader");
                            status = "Not leader";
                        }
                        else{
                            printMessage("** Process "+id+" is the leader");
                            status = "Leader";
                        }
                        roundLeft = 0;
                    }   
                    else if (rightProcessor) {
                        if(equal(m.source(),leftNeighbour)){
                            if (topProcessor) {
                                mssg = makeMessage(bottomNeighbour,id_l);
                            }
                        }
                        else if (equal(m.source(),topNeighbour)) {
                            if (bottomProcessor) {
                                if (larger(id_l,id)){
                                    status = "Not leader";
                                }
                                else{
                                    status = "Leader";
                                }
                                mssg = makeMessage(topNeighbour,id_l);
                            }
                            else{
                                mssg = makeMessage(bottomNeighbour,id_l);
                            }
                        }
                        else if(equal(m.source(),bottomNeighbour)){
                            if (larger(id_l,id)){
                                status = "Not leader";
                            }
                            else{
                                status = "Leader";
                            }
                            if (topProcessor) {
                                mssg = makeMessage(leftNeighbour,id_l);
                            }
                            else
                                mssg = makeMessage(topNeighbour,id_l);
                        }                   
                    }
                    else{
                        if (equal(m.source(),leftNeighbour)) {
                             mssg = makeMessage(rightNeighbour,id_l);
                        }
                        else if(equal(m.source(),rightNeighbour)){
                            if (larger(id_l,id)){
                                status = "Not leader";
                            }
                            else{
                                status = "Leader";
                            }
                            mssg = makeMessage(leftNeighbour,id_l);
                        }
                    }
                }
                if (roundLeft == 0) {
                    return status;
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
