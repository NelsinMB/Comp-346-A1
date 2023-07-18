import java.lang.reflect.Array;
import java.sql.Time;
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

    public Processor(Computer computer, HashMap<Integer, Integer> numberOfInstructions,
            HashMap<Integer, int[]> IORequestAtInstruction,
            HashMap<Integer, int[]> IODeviceRequested, IODevice IO1, IODevice IO2) {
        this.IO1 = IO1;
        this.IO2 = IO2;
        this.computer = computer;
        computer.processor = this; // Ensure processor field is not null in Commputer
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
        int ticksOnCurrentProcess = -1; // ticksOnCycle
        while (true) {
            if (ticksOnCurrentProcess > 1 || ticksOnCurrentProcess == -1) {
                try {
                    currentProcess = readyQueue.queue.remove(); // Either two ticks of time spent on current process, or
                                                                // initialization => Load new process if possible.
                    ticksOnCurrentProcess = 0; // Reset counter for new process.
                } catch (NoSuchElementException e) { // Indicates readyQueue is empty.

                    if (IO1.waitQueue.isEmpty() && IO2.waitQueue.isEmpty() && ticksOnCurrentProcess == 2) { // readyQueue,
                                                                                                            // waitQueues
                        // are empty => Terminate.
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
                this.IO1.waitQueue.put(currentProcess, 5);
                currentProcess.getPCB().setProcessState(State.WAITING);
                ticksOnCurrentProcess = 2; // ticksOnCurrentProcess to 2 to signify time for next process.
            } else if (executionResult == 2) { // Indicates I/O device 2 was called => Set current process state to
                                               // WAITING. Add to queue with 5 ticks remaining.
                this.IO2.waitQueue.put(currentProcess, 5);
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
        this.IO1.decrementWaits();
        this.IO2.decrementWaits();
    }

    /*
     * Checks whether the currentProcess is eligible for termination (programCounter
     * = numberOfInstructions)
     */
    public void reinsert() {
        if (currentProcess.getPCB().getProcessState() != State.TERMINATED
                && currentProcess.getPCB().getProcessState() != State.WAITING) {
            readyQueue.queue.add(currentProcess);
        }
    }

    /* Auxilliary functions */

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
            System.out.println("The process " + processID + " has " + timeToCompletion + " time units till completion.");
        }
    }

    public void printWaitQueue2() {
        for (Map.Entry<Process, Integer> entry : IO2.waitQueue.entrySet()) {
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

}
