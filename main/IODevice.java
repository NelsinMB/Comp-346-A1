import java.util.HashMap;
import java.util.Map;

public class IODevice {

    /*
     * Column 0: ID
     * Column 1: Time left (begins at 5). Once 0 is reached, remove from list, add to readyQueue. ***Might need to consider whether it was the last instruction
     */
    HashMap<Process, Integer> waitQueue;
    Computer computer;

    public IODevice (Computer computer) { 
        this.computer = computer;
        this.waitQueue = new HashMap<Process, Integer>();
    }
    
    public void decrementWaits() {
        for (Map.Entry<Process, Integer> entry : waitQueue.entrySet()) { 
            if (entry.getValue() != 0) {
                entry.setValue(entry.getValue()-1); //Decrement by 1
            } else { //Process has been in waitQueue for 5 ticks.
                waitQueue.remove(entry.getKey()); 
                
                if (entry.getKey().getPCB().getProgramCounter() ==  entry.getKey().numberOfInstructions - 1) {
                    entry.getKey().getPCB().setProcessState(State.TERMINATED);
                } else {
                    entry.getKey().getPCB().setProcessState(State.READY);
                    computer.getProcessor().addToQueue(entry.getKey());
                }
                                
            }
        }


    }

    public Computer getComputer() {
        return this.computer;
    }

    public HashMap<Process, Integer> getWaitQueue() {
        return this.waitQueue;
    }

}
