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
    int ticksOnCurrentProcess = 0;
    int ticks = 0;
    boolean nextProcess = true;

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
        while (true) {
            /*
             * The following if statement handles switching of processes.
             */
            if (nextProcess) {
                try {
                    newProcess();
                } catch (NoSuchElementException e) { // Indicates readyQueue is empty.
                    if (getComputer().getIO1().waitQueue.isEmpty() && getComputer().getIO2().waitQueue.isEmpty() && ticksOnCurrentProcess == 2) { 
                        // readyQueue, waitQueues are empty => Terminate.
                        System.out.println("No more processes or IO. Terminating.");
                        break;
                    } else { //readyQueue empty, but not waitQueues. CPU will not be used.sss
                        currentProcess = new Process(this, -1, 0, new int[1], new int[1]); //Idle CPU indicated by process with Process ID -1
                    }
                }
            } else {
                //Do nothing
            }

            int executionResult = currentProcess.executeInstruction(); // Assign output of executeInstruction to
                                                                       // variable to avoid multiple calls.
            if (executionResult == 0) { // Indicates no I/O was called, and process is not complete.
            } else if (executionResult == 1) { // Indicates I/O device 1 was called => Set current process state to
                                               // WAITING. Add to queue with 5 ticks remaining.
                transitionToWaitIO1(currentProcess);
                nextProcess = true;
            } else if (executionResult == 2) { // Indicates I/O device 2 was called => Set current process state to
                                               // WAITING. Add to queue with 5 ticks remaining.
                transitionToWaitIO2(currentProcess);
                nextProcess = true;
            } else if (executionResult == 4) { // Indicates no I/O was called and process is complete => Set current process state to TERMINATED. 
                transitionToTerminated(currentProcess);
                nextProcess = true;
            }

            output(ticks, currentProcess.processID);
            ticksOnCurrentProcess++; //Output complete, increment ticks on current process


            /*
             * With output complete, and ticks on current process incremented, need to do the following. 
             * - Update wait queues of IO devices.
             * - Update status of current process.
             * - Update status of processes on wait queues. 
             */

            updateWaitQueues();
            if (ticksOnCurrentProcess == 2) { 
                transitionToReady(currentProcess);
                nextProcess = true;
            }
            ticks++;
        }

    }

    public void newProcess() {
        currentProcess = readyQueue.remove();
        ticksOnCurrentProcess = 0; // Reset counter for new process. 
        reloadState();
        ticksOnCurrentProcess = 0;
        nextProcess = false;
    }

    public void transitionToReady(Process process) {
        saveState(process);
        readyQueue.add(process);
        process.getPCB().setProcessState(State.READY);
    }

    /*
     * Transitions the state of the process to WAITING.
     */
    public void transitionToWaitIO1(Process process) {
        saveState(process);
        getComputer().getIO1().waitQueue.put(currentProcess, 5);
        process.getPCB().setProcessState(State.WAITING);
    }

    public void transitionToWaitIO2(Process process) {
        saveState(process);
        getComputer().getIO2().waitQueue.put(currentProcess, 5);
        process.getPCB().setProcessState(State.WAITING);
    }

    /*
     * Transitions the state of the process to TERMINATED.
     */
    public void transitionToTerminated(Process process) {
        process.getPCB().setProcessState(State.TERMINATED);
    }

    /*
     * Saves values of processor to process.
     */
    public void saveState(Process process) {
        process.getPCB().getRegister1().setValue(register1.getValue());
        process.getPCB().getRegister2().setValue(register2.getValue());
    }
    
    /*
     * Restores values of process to processor.
     */
    public void reloadState() {
        register1.setValue(currentProcess.getPCB().getRegister1().getValue());
        register2.setValue(currentProcess.getPCB().getRegister1().getValue());
    }

    /*
     * Handles print to console
     */
    public void output(int ticks, int currentProcessID) {
        System.out.println("Tick number: " + ticks);
            System.out.println("Process ID:  " + currentProcess.processID);
            System.out.print("Ready queue: ");
            printReadyQueue();
            System.out.println("IO device 1 wait queue: ");
            printWaitQueue1();
            System.out.println("IO device 2 wait queue: ");
            printWaitQueue2();
            System.out.println("");
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
            System.out.print(iterator.next().processID);
        }
        System.out.println("");

    }

    public void printWaitQueue1() {
        for (Map.Entry<Process, Integer> entry : getComputer().getIO1().waitQueue.entrySet()) {
            int processID = entry.getKey().processID;
            int timeToCompletion = entry.getValue();
            System.out.println("--The process " + processID + " has " + timeToCompletion + " time units till completion.");
        }
        System.out.println("");
    }

    public void printWaitQueue2() {
        for (Map.Entry<Process, Integer> entry : getComputer().getIO2().waitQueue.entrySet()) {
            int processID = entry.getKey().processID;
            int timeToCompletion = entry.getValue();
            System.out.println("--The process " + processID + " has " + timeToCompletion + " time units till completion.");
        }
        System.out.println("");
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
