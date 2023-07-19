import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;

public class Processor {

    private Queue<Process> readyQueue = new LinkedList<Process>();
    private Register register1;
    private Register register2;
    private Process currentProcess;
    private Computer computer;

    public Processor(Computer computer) {
        this.computer = computer;
        getComputer().setProcessor(this); 
        this.register1 = new Register(this);
        this.register2 = new Register(this);

        for (Map.Entry<Integer, Integer> entry : getComputer().numberOfInstructions.entrySet()) {
            if (entry.getKey() != 0) { // numberOfInstructions is not 0
                Process process = new Process(this, entry.getKey(), getComputer().getNumberOfInstructionsHashMap().get(entry.getKey()),
                        getComputer().getIORequestAtInstructionHashMap().get(entry.getKey()), getComputer().getIODeviceRequestedHashMap().get(entry.getKey()));
                readyQueue.add(process);
            }
        }
        Loop();
    }

    public void Loop() {
        int ticksOnCurrentProcess = -1; // ticksOnCycle
        while (true) {
            if (ticksOnCurrentProcess > 1 || ticksOnCurrentProcess == -1) {
                try {
                    currentProcess = readyQueue.remove(); // Either two ticks of time spent on current process, or
                                                                // initialization => Load new process if possible.
                    ticksOnCurrentProcess = 0; // Reset counter for new process.
                } catch (NoSuchElementException e) { // Indicates readyQueue is empty.

                    if (getComputer().getIO1().waitQueue.isEmpty() && getComputer().getIO2().waitQueue.isEmpty() && ticksOnCurrentProcess == 2) { 
                        // readyQueue, waitQueues are empty => Terminate.
                        System.out.println("No more processes or IO. Terminating.");
                        break;

                    } else {
                        currentProcess = new Process(this, -1, 0, new int[1], new int[1]);
                    }
                }
            } else {
                // Two ticks have not yet been spent on current process => Increment
                // ticksCounter.
            }

            // if (currentProcess != null) {
            ticksOnCurrentProcess++;
            int executionResult = currentProcess.executeInstruction(); // Assign output of executeInstruction to
                                                                       // variable to avoid multiple calls.
            if (executionResult == 0) { // Indicates no I/O was called, and process is not complete.
            } else if (executionResult == 1) { // Indicates I/O device 1 was called => Set current process state to
                                               // WAITING. Add to queue with 5 ticks remaining.
                getComputer().getIO1().waitQueue.put(currentProcess, 5);
                currentProcess.getPCB().setProcessState(State.WAITING);
                ticksOnCurrentProcess = 2; // ticksOnCurrentProcess to 2 to signify time for next process.
            } else if (executionResult == 2) { // Indicates I/O device 2 was called => Set current process state to
                                               // WAITING. Add to queue with 5 ticks remaining.
                getComputer().getIO2().waitQueue.put(currentProcess, 5);
                currentProcess.getPCB().setProcessState(State.WAITING);
                ticksOnCurrentProcess = 2; // ticksOnCurrentProcess to 2 to signify time for next process.
            } else if (executionResult == 4) { // Indicates no I/O was called and process is complete => Set
                ticksOnCurrentProcess = 2; // ticksOnCurrentProcess to 2 to signify time for next process.
            }

            System.out.println("Process ID:  " + currentProcess.processID);
            System.out.println("Ready queue: ");
            printReadyQueue();
            System.out.println("IO device 1 wait queue: ");
            printWaitQueue1();
            System.out.println("IO device 2 wait queue: ");
            printWaitQueue2();

            /*
             * Methods to run on each cycle:
             * - Check whether a process is terminated.
             * - Update wait queue.
             * - Transfer from wait queue to ready queue.
             * Before or after printing to console??
             */

            updateWaitQueues();
            if (ticksOnCurrentProcess == 2) { // Indicates the second cycle has past for the current process => Consider
                                              // requeing.
                reinsert();
            }

            // }

        }

    }

    /*
     * Decrements the time remaining for processes in the wait queues of IO devices
     * by 1. To be called when a time unit has passed.
     */
    public void updateWaitQueues() {
        getComputer().getIO1().decrementWaits();
        getComputer().getIO2().decrementWaits();
    }

    /*
     * Checks whether the currentProcess is eligible for termination (programCounter
     * = numberOfInstructions)
     */
    public void reinsert() {
        if (currentProcess.getPCB().getProcessState() != State.TERMINATED
                && currentProcess.getPCB().getProcessState() != State.WAITING) {
            readyQueue.add(currentProcess);
        }
    }

    /* Auxilliary functions */

    public void addToQueue(Process process) {
        readyQueue.add(process);
    }

    public void printReadyQueue() {
        Iterator<Process> iterator = readyQueue.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next().processID);
        }

    }

    public void printWaitQueue1() {
        for (Map.Entry<Process, Integer> entry : getComputer().getIO1().waitQueue.entrySet()) {
            int processID = entry.getKey().processID;
            int timeToCompletion = entry.getValue();
            System.out.println("The process " + processID + " has " + timeToCompletion + " time units till completion.");
        }
    }

    public void printWaitQueue2() {
        for (Map.Entry<Process, Integer> entry : getComputer().getIO2().waitQueue.entrySet()) {
            int processID = entry.getKey().processID;
            int timeToCompletion = entry.getValue();
            System.out.println("The process " + processID + " has " + timeToCompletion + " time units till completion.");
        }
    }

    public void printArray(int[] input) {
        int length = input.length;
        for (int index = 0; index < length; index++) {
            System.out.print(input[index]);
        }
        System.out.println("");
    }

    public Computer getComputer() {
        return this.computer;
    }

    public Register getRegister1() {
        return this.register1;
    }

    public Register getRegister2() {
        return this.register2;
    }

}
