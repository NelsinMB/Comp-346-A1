public class Process {

    int processID;
    int[] IORequestAtInstruction;
    int[] IODeviceRequested;

    public Process(int processID, int[] IORequestAtInstruction, int[] IODeviceRequested) {
        this.processID = processID;
        this.IORequestAtInstruction = IORequestAtInstruction;
        this.IODeviceRequested = IODeviceRequested;
    }
    
}
