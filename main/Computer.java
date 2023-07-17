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
    //HashMap<Integer, List<Integer>> IORequestAtInstruction = new HashMap<Integer, List<Integer>>();
    //HashMap<Integer, List<Integer>> IODevicesRequested = new HashMap<Integer, List<Integer>>();
    int[] IORequestAtInstruction; 
    int[] IODevicesRequested;
    
    public Computer(HashMap<Integer, Integer> input1, int[] input2, int[] input3) {
        this.numberOfInstructions = input1;
        this.IORequestAtInstruction = input2;
        this.IODevicesRequested = input3;
        System.out.println("Computer is now online.");
        System.out.println("The following input was provided:");
        for (int ID = 1; ID < numberOfInstructions.size()+1; ID++) {
            System.out.println("\nProcessID: \n" + numberOfInstructions.get(ID) + "\nIORequestAtTimes:");
            printArray(IORequestAtInstruction);
        }
        


    }

    public void printArray(int[] input) {
        int length = input.length;
        for (int index = 0; index < length; index++) {System.out.print(input[index]);}
    }

}
   
