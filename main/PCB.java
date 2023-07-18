public class PCB {

    State processState; //Could use ENUM to restrict choices?
    int programCounter;
    int numberOfInstructions;
    //Why does the PCB store information about CPU registers, scheduling, memory management, accounting, I/O?

    public PCB (int numberOfInstructions) {
        this.processState = State.READY; //Is the state 'NEW' necessary?
        this.programCounter = 0; //"You can assume that the instructions of each process start at instruction 0"
        this.numberOfInstructions = numberOfInstructions;
    }

    public void setProcessState(State processState) {
        this.processState = processState;
    }

    public void setProgramCounter(int programCounter) {
        this.programCounter = programCounter;
    }

    public int getProgramCounter() {
        return this.programCounter;
    }

    public State getProcessState() {
        return processState;
    }


    //Using enums restricts possible options (compared to Strings)
    
    
    
}
