
public class Process {

    int processID;
    int[] IORequestAtInstruction;
    int[] IODeviceRequested;
    PCB pcb;
    int numberOfInstructions;
    Processor processor;

    public Process(Processor processor, int processID, int numberOfInstructions, int[] IORequestAtInstruction,
            int[] IODeviceRequested) {
                this.processor = processor;
        this.processID = processID;
        this.IORequestAtInstruction = IORequestAtInstruction;
        this.IODeviceRequested = IODeviceRequested;
        this.pcb = new PCB(numberOfInstructions);
    }

    // 0 for execute instruction
    // 1 for IO1
    // 2 for IO2

    /*
     * Thoughts:
     * Check whether the instruction is an IO request. If not,
     * - Increment program counter.
     * - Check whether program counter = number of instructions, if yes, change
     * pcb.state to TERMINATED.
     * - In the processor, have it check the status of the process, add to correct
     * queue.
     * - Return 0.
     *
     * If yes,
     * - Increment program counter.
     * - Return 1/2 depending on I/O device requested.
     * - Change pcb.state to WAITING.
     * - Add to correct wait queue in IODevice.
     * - Will need a counter.
     */

    public int executeInstruction() {
        int instructionToBeExecuted = pcb.getProgramCounter();
        if (pcb.numberOfInstructions == pcb.getProgramCounter()) {
            this.getPCB().setProcessState(State.TERMINATED);
            return 4;
        } else if (IORequestAtInstruction[instructionToBeExecuted] == 0) {
            pcb.setProgramCounter(pcb.getProgramCounter() + 1);
            if (this.getPCB().programCounter == pcb.numberOfInstructions) {
                pcb.setProcessState(State.TERMINATED);
            }
            return 0;
        } else {
            pcb.setProgramCounter(pcb.getProgramCounter() + 1);
            pcb.setProcessState(State.WAITING); // Process is now waiting
            // Do we terminate if I/O is only necessary?
            if (this.getPCB().programCounter == pcb.numberOfInstructions) {
                pcb.setProcessState(State.TERMINATED);
            }
            return IODeviceRequested[instructionToBeExecuted];
        }

    }

    public PCB getPCB() {
        return this.pcb;
    }

}
