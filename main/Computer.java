import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Computer {

    Processor processor;
    IODevice IO1;
    IODevice IO2;
    //The following Hashmaps are used to store the input. 
    HashMap<Integer, Integer> numberOfInstructions = new HashMap<Integer, Integer>();
    HashMap<Integer, int[]> IORequestAtInstruction = new HashMap<Integer, int[]>();
    HashMap<Integer, int[]> IODevicesRequested = new HashMap<Integer, int[]>();
    
    public Computer(HashMap<Integer, Integer> input1, HashMap<Integer, int[]> input2, HashMap<Integer, int[]> input3) {
        this.IO1 = new IODevice(this);
        this.IO2 = new IODevice(this);
        this.numberOfInstructions = input1;
        this.IORequestAtInstruction = input2;
        this.IODevicesRequested = input3;

        System.out.println("Computer is now online.");
        System.out.println("The following input was provided:");
        for (int ID = 1; ID < numberOfInstructions.size()+1; ID++) {
            System.out.println("\nProcessID: \n" + numberOfInstructions.get(ID) + "\nIORequestAtTimes:");
            printArray(IORequestAtInstruction.get(ID));
            System.out.println("\nIODevicesRequested:");
            printArray(IODevicesRequested.get(ID));
        }

        System.out.println("-------");
        System.out.println("Engaging processor. ");
        //Pass the relevant HashMaps to the processor, as well as IODevices
        this.processor = new Processor(this, numberOfInstructions, IORequestAtInstruction, IODevicesRequested, IO1, IO2); //I think the assignment only occurs after constructor. 

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











    //Prints an array of integers. 
    public void printArray(int[] input) {
        int length = input.length;
        for (int index = 0; index < length; index++) {System.out.print(input[index]);}
    }

}
   
