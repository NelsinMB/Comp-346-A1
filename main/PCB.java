import java.util.ArrayList;

public class PCB {

    Process process;
    State processState; //Could use ENUM to restrict choices?
    int programCounter;
    ArrayList<Register> registers = new ArrayList<Register>(2);
    int CPUUsed;
    
    // *** Why does the PCB store information about CPU registers, scheduling, memory management, accounting, I/O?

    public PCB (Process process) {
        this.process = process;
        this.processState = State.READY; // *** Is the state 'NEW' necessary?
        this.programCounter = 0; //"You can assume that the instructions of each process start at instruction 0"
        this.registers.add(process.getProcessor().getRegister1());
        this.registers.add(process.getProcessor().getRegister2());
        this.CPUUsed = 0;
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

    public Register getRegister1() {
        return this.registers.get(0);
    }

    public Register getRegister2() {
        return this.registers.get(1);
    }

    public int CPUUsed() {
        return this.CPUUsed;
    }

    public void setCPUUsed(int CPUUsed) {
        this.CPUUsed = CPUUsed;
    }

    public void printPCB() {
        System.out.println("--Process ID: " + process.getProcessID());
        System.out.println("--Process state: " + processState.toString());
        System.out.println("--CPU ticks used: " + CPUUsed);
    }




}
