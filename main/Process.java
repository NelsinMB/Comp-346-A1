
public class Process {

    private Processor processor;
    int processID;
    int[] IORequestAtInstruction;
    int[] IODeviceRequested;
    PCB pcb;
    int numberOfInstructions;

    public Process(Processor processor, int processID, int numberOfInstructions, int[] IORequestAtInstruction,
            int[] IODeviceRequested) {
        this.processor = processor;
        this.processID = processID;
        this.IORequestAtInstruction = IORequestAtInstruction;
        this.IODeviceRequested = IODeviceRequested;
        this.pcb = new PCB(this);
        this.numberOfInstructions = numberOfInstructions; // ***Remove this field in process (i.e. just in PCB?)
    }

    /*
     * IORequestAtInstruction array stores 0,1,2 for where value =
     * IORequestAtInstruction[instructionNumber] denotes the following:
     * value = 0 => no I/O
     * value = 1 => I/O device 1
     * value = 2 => I/O device 2
     */
    public int executeInstruction() {
        int nextInstruction = getPCB().getProgramCounter();

        if (nextInstruction == numberOfInstructions - 1) {
            getPCB().setProcessState(State.TERMINATED);
        }

        getPCB().setProgramCounter(getPCB().getProgramCounter() + 1); // Increment program counter
        if (IODeviceRequested[nextInstruction] == 1) {
            return 1;
        } else if (IODeviceRequested[nextInstruction] == 2) {
            return 2;
        } else {
            return 0;
        }
    }

    public PCB getPCB() {
        return this.pcb;
    }

    public Processor getProcessor() {
        return this.processor;
    }

    public int getProcessID() {
        return this.processID;
    }
}
