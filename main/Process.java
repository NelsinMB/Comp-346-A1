
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
        int instructionToBeExecuted = pcb.getProgramCounter();
        if ((numberOfInstructions - 1) == pcb.getProgramCounter()) {
            this.getPCB().setProcessState(State.TERMINATED);
            return 4;
        } else if (IORequestAtInstruction[instructionToBeExecuted] == 0) {
            pcb.setProgramCounter(pcb.getProgramCounter() + 1);
            if (this.getPCB().programCounter == numberOfInstructions) {
                pcb.setProcessState(State.TERMINATED);
            }
            return 0;
        } else {
            pcb.setProgramCounter(pcb.getProgramCounter() + 1);
            pcb.setProcessState(State.WAITING); // Process is now waiting
            // Do we terminate if I/O is only necessary?
            if (this.getPCB().programCounter == numberOfInstructions) {
                pcb.setProcessState(State.TERMINATED);
            }
            return IODeviceRequested[instructionToBeExecuted];
        }

    }

    public PCB getPCB() {
        return this.pcb;
    }

    public Processor getProcessor() {
        return this.processor;
    }

}
