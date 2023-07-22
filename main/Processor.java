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
                Process process = new Process(this, entry.getKey(),
                        getComputer().getNumberOfInstructionsHashMap().get(entry.getKey()),
                        getComputer().getIORequestAtInstructionHashMap().get(entry.getKey()),
                        getComputer().getIODeviceRequestedHashMap().get(entry.getKey()));
                readyQueue.add(process);
            }
        }
        Loop();
    }

    public void Loop() {
        if (currentProcess == null) {

            try {
                currentProcess = readyQueue.remove();
            } catch (Exception e) {

            }
        }
        while (true) {

            if (currentProcess == null) {
                if (getComputer().getIO1().getWaitQueue().isEmpty()
                        && getComputer().getIO2().getWaitQueue().isEmpty() && readyQueue.isEmpty()) {
                    System.exit(0);
                } else {
                    output(ticks, "No active process");
                    ticks++;
                }
            } else {

                int executeInstruction = currentProcess.executeInstruction();
                output(ticks, String.valueOf(currentProcess.getProcessID()));
                switch (executeInstruction) {
                    case 0: // No I/O
                        if (currentProcess.getPCB().getProcessState() == State.TERMINATED) {
                            contextSwitch(currentProcess, State.TERMINATED);
                        } else {
                            if (ticksOnCurrentProcess == 2) {
                                contextSwitch(currentProcess, State.READY);
                            }
                        }
                        break;

                    case 1: // I/O 1
                        contextSwitch(currentProcess, State.WAITING1);
                        break;

                    case 2: // I/O 2
                        contextSwitch(currentProcess, State.WAITING2);
                        break;

                }

                ticksOnCurrentProcess++;
                ticks++;
            }
            updateWaitQueues();
            
        }

    }

    public void contextSwitch(Process oldProcess, State oldProcessState) {

    

        oldProcess.getPCB().getRegister1().setValue(register1.getValue());
        oldProcess.getPCB().getRegister2().setValue(register2.getValue());

        if (oldProcessState == State.WAITING1) {
            getComputer().getIO1().getWaitQueue().put(oldProcess, 5);
        } else if (oldProcessState == State.WAITING2) {
            getComputer().getIO2().getWaitQueue().put(oldProcess, 5);
        } else if (oldProcessState == State.READY) {
            readyQueue.add(oldProcess);
        } else if (oldProcessState == State.TERMINATED) {
            // Do nothing
        }

        try {
            currentProcess = readyQueue.remove();
            register1.setValue(currentProcess.getPCB().getRegister1().getValue());
            register2.setValue(currentProcess.getPCB().getRegister2().getValue());
            ticksOnCurrentProcess = 0;
        } catch (Exception e) {
           currentProcess = null;
        }

    }

    /*
     * Handles print to console
     */
    public void output(int ticks, String currentProcessID) {
        System.out.println("Tick number: " + ticks);
        System.out.println("Process ID:  " + currentProcessID);
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
            System.out.println(
                    "--The process " + processID + " has " + timeToCompletion + " time units till completion.");
        }
        System.out.println("");
    }

    public void printWaitQueue2() {
        for (Map.Entry<Process, Integer> entry : getComputer().getIO2().waitQueue.entrySet()) {
            int processID = entry.getKey().processID;
            int timeToCompletion = entry.getValue();
            System.out.println(
                    "--The process " + processID + " has " + timeToCompletion + " time units till completion.");
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
