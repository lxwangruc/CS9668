import java.util.Vector; //We need this for the Vector class.

/* Your class must extend the Algorithm class from Simulator.jar */
/* The name of your source file should be the same as your class name (but ending in .java) */
public class MyClassName extends Algorithm {
    
    /* You MUST implement a run method that returns an Object (should be String, a primitive type or null). */
    /* This method should call your algorithm with any required initial parameters. */
    public Object run() {
        Object myResult = myAlgorithm(getID() /* Put any other initial inputs/parameters to your algorithm here. */);
        return myResult; // Return can be a String, primitive type, or null
    }

    /* This method represents your algorithm. */
    public Object myAlgorithm(String id) {
        // ===Initial Setup===
        // Set up any varibles and values you will need in the main loop of your algorithm

        
        // ===Main Loop===
        try {
            /* Will wait for the round to start before continuing the loop. */
            /* waitForNextRound() will return false if your algorithm should stop. */
            /* You should call waitForNextRound() once per loop iteration. */
            while (waitForNextRound()) {
                
                
                // ===Send Phase===
                // Send any messages to other nodes using the send(Message msg) method.
                // Create messages with the makeMessage(String destination, String data) method.
                // Note that you can only send one message per link per round.


                // ===Receive Phase===
                // Receive and process any messages sent to this node.
                // Use the receive() method to get a single message sent to this node.
                // You will have to call receive() once for each message sent to this node.
                // receive() will wait for a message or return null if the round is ending.
                
                // ===Terminate===
                // Once your algorithm is complete you should terminate and return your result.
                // You can do this with the return keyword as is normal in java.
                // This can be done in the Send or Receive phase.

            }
        /* Several of the Algorithm class methods throw SimulatorExceptions, you should catch them here. */
        } catch(SimulatorException e){
            System.out.println("ERROR: " + e.toString());
            //Use e.printStacktrace() to print more debugging information.
        }
    
        /* At this point something likely went wrong. If you do not have a result you can return null */
        return null;
    }
}
