public class Process {

    int processID;
    int[] IORequestAtInstruction;
    int[] IODeviceRequested;
    PCB pcb;
    int numberOfInstructions;

    public Process(Processor processor, int processID, int numberOfInstructions, int[] IORequestAtInstruction, int[] IODeviceRequested) {
        this.processID = processID;
        this.IORequestAtInstruction = IORequestAtInstruction;
        this.IODeviceRequested = IODeviceRequested;
        this.pcb = new PCB(numberOfInstructions);
    }


    //0 for execute instruction
    //1 for IO1
    //2 for IO2

    /*
     * Thoughts:
     * Check whether the instruction is an IO request. If not,
     * - Increment program counter.
     * - Check whether program counter = number of instructions, if yes, change pcb.state to TERMINATED.
     * - In the processor, have it check the status of the process, add to correct queue.
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
        int instructionToBeExecuted = this.pcb.programCounter;
        if (IORequestAtInstruction[instructionToBeExecuted] == 0) {
            pcb.setProgramCounter(pcb.getProgramCounter() + 1);
            return 0;
        } else {
            this.pcb.programCounter = this.pcb.programCounter++;
            return IODeviceRequested[instructionToBeExecuted];
        }
    }

    public int isDone() {
        if (this.pcb.programCounter == this.pcb.numberOfInstructions) {
            this.pcb.setProcessState(this.pcb.processState.TERMINATED);
            return 1;
        } else {
            return 0;
        }
    }
    
}
