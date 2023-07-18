import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

public class Processor {

    QueueCover readyQueue = new QueueCover();
    Register register1;
    Register register2;
    Process currentProcess;
    IODevice IO1;
    IODevice IO2;
    Computer computer;

    public Processor(Computer computer, HashMap<Integer, Integer> numberOfInstructions, HashMap<Integer, int[]> IORequestAtInstruction,
            HashMap<Integer, int[]> IODeviceRequested, IODevice IO1, IODevice IO2) {
        this.IO1 = IO1;
        this.IO2 = IO2;
        this.computer = computer;
        computer.processor = this; //Ensure processor field is not null in Commputer
        // System.out.println("Processor: Processor active.");
        // System.out.println("Processor: Creating processes from inputs");
        for (Map.Entry<Integer, Integer> entry : numberOfInstructions.entrySet()) {
            if (entry.getKey() != 0) { // numberOfInstructions is not 0
                Process process = new Process(this, entry.getKey(), numberOfInstructions.get(entry.getKey()),
                        IORequestAtInstruction.get(entry.getKey()), IODeviceRequested.get(entry.getKey()));
                this.readyQueue.queue.add(process);
                // printArray(this.readyQueue.queue.element().IORequestAtInstruction);
                // System.out.println(this.readyQueue.queue.element().processID);
                // this.readyQueue.queue.remove();

            }
        }
        // System.out.println("Processor: Processes have been added to readyQueue");
        // System.out.println("Processor: Execution may now begin.");
        Loop();

    }

    public void Loop() {
        int counter = 2; // Whatever, start with 2
        while (true) {
            if (counter > 1) {
                try {
                    currentProcess = readyQueue.queue.remove(); // Load new process
                    counter = 0; // Start back at 0
                } catch (NoSuchElementException e) {
                    //Check whether there is active I/O, if not, we are done.
                    if (IO1.waitQueue.isEmpty() && IO2.waitQueue.isEmpty() && counter == 2) {
                        System.out.println("No more processes or IO. Terminating.");
                        break;

                    }
                }
            } else {
                // Maintain current process
            }
            System.out.println("counter: " + counter);

            if (currentProcess != null) {
                int executionResult = currentProcess.executeInstruction(); //Necessary to not call function multiple times.
                if (executionResult == 0) {
                    counter++;
                } else if (executionResult == 1) {
                    this.IO1.waitQueue.put(currentProcess, 5); //Begins at 5
                    currentProcess.getPCB().setProcessState(State.WAITING);
                    counter++;

                } else if (executionResult  == 2) {
                    this.IO2.waitQueue.put(currentProcess, 5); //Begins at 5
                    currentProcess.getPCB().setProcessState(State.WAITING);
                    counter++;
                } else if (executionResult == 4) { //End of process
                    counter = 2; //Same as reaching current limit
                }

                System.out.println("Name of process being executed now:" + currentProcess.processID);
                System.out.println("Content of the ready queue:");
                printReadyQueue();
                System.out.println("The content of the wait queue for IO device 1:");
                printWaitQueue1();
                System.out.println("The content of the wait queue for IO device 2:");
                printWaitQueue2();


                // Before or after printing to console??

                /*
                 * Methods to run on each cycle: 
                 * - Check whether a process is terminated.
                 * - Update wait queue.
                 * - Transfer from wait queue to ready queue.
                 */

                updateWaitQueues();
                if (counter == 2) { //Only on last cycle do we consider requeing. 
                    reload();
                }


            } 

        }

    }

    /*
     * Decrements the time remaining for processes in the wait queues of IO devices by 1. To be called when a time unit has passed.
     */
    public void updateWaitQueues() {
        this.IO1.decrementWaits();
        this.IO2.decrementWaits();
    }

    /*
     * Checks whether the currentProcess is eligible for termination (programCounter = numberOfInstructions)
     */
    public void reload() {
       if (currentProcess.getPCB().getProcessState() != State.TERMINATED && currentProcess.getPCB().getProcessState() != State.WAITING) {
        readyQueue.queue.add(currentProcess);
       }
    }

    /*
     * Insert a process into the readyQueue
     */
    public void addToQueue(Process process) {
        readyQueue.queue.add(process);
    }

    public void printReadyQueue() {

        Iterator<Process> iterator = readyQueue.queue.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next().processID);
        }

    }

    public void printWaitQueue1() {
        for (Map.Entry<Process, Integer> entry : IO1.waitQueue.entrySet()) {
            int processID = entry.getKey().processID;
            int timeToCompletion = entry.getValue();
            System.out.println("The process" + processID + " has " + timeToCompletion + " time units till completion.");
        }
    }

    public void printWaitQueue2() {
        for (Map.Entry<Process, Integer> entry : IO2.waitQueue.entrySet()) {
            int processID = entry.getKey().processID;
            int timeToCompletion = entry.getValue();
            System.out.println("The process" + processID + " has " + timeToCompletion + " time units till completion.");
        }
    }

    // **** Auxilliary methods ****

    public void printArray(int[] input) {
        int length = input.length;
        for (int index = 0; index < length; index++) {
            System.out.print(input[index]);
        }
        System.out.println("");
    }

}
