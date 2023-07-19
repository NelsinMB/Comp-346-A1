import java.util.HashMap;

public class Computer {

    Processor processor;
    IODevice IO1;
    IODevice IO2;
    //The following Hashmaps are used to store the input. 
    HashMap<Integer, Integer> numberOfInstructions = new HashMap<Integer, Integer>();
    HashMap<Integer, int[]> IORequestAtInstruction = new HashMap<Integer, int[]>();
    HashMap<Integer, int[]> IODeviceRequested = new HashMap<Integer, int[]>();
    
    public Computer(HashMap<Integer, Integer> numberOfInstructions, HashMap<Integer, int[]> IORequestAtInstruction, HashMap<Integer, int[]> IODeviceRequested) {
        this.IO1 = new IODevice(this);
        this.IO2 = new IODevice(this);
        this.numberOfInstructions = numberOfInstructions;
        this.IORequestAtInstruction = IORequestAtInstruction;
        this.IODeviceRequested = IODeviceRequested;

        //System.out.println("Computer is now online.");
        //System.out.println("The following input was provided:");
        //for (int ID = 1; ID < numberOfInstructions.size()+1; ID++) {
        //System.out.println("\nProcessID: \n" + numberOfInstructions.get(ID) + "\nIORequestAtTimes:");
        //printArray(IORequestAtInstruction.get(ID));
        //System.out.println("\nIODevicesRequested:");
        //printArray(IODeviceRequested.get(ID));
        //}

        //System.out.println("-------");
        //System.out.println("Engaging processor. ");


        //Pass the relevant HashMaps to the processor, as well as IODevices
        this.processor = new Processor(this); //I think the assignment only occurs after constructor. 

    }

    public Processor getProcessor() {
        return this.processor;
    }

    public IODevice getIO1() {
        return this.IO1;
    }

    public IODevice getIO2() {
        return this.IO2;
    }


    public HashMap<Integer, Integer> getNumberOfInstructionsHashMap() {
        return this.numberOfInstructions;
    }



    public HashMap<Integer, int[]> getIORequestAtInstructionHashMap() {
        return this.IORequestAtInstruction;
    }

    public HashMap<Integer, int[]> getIODeviceRequestedHashMap() {
        return this.IODeviceRequested;
    }

    //Prints an array of integers. 
    public void printArray(int[] input) {
        int length = input.length;
        for (int index = 0; index < length; index++) {System.out.print(input[index]);}
    }

    public void setProcessor(Processor processor) {
        this.processor = processor;
    }

}
   
