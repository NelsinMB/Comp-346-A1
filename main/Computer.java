import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Computer {

    Processor processor;
    IODevice IO1;
    IODevice IO2;
    //The following Hashmaps are used to store the input. 
    HashMap<Integer, Integer> numberOfInstructions = new HashMap<Integer, Integer>();
    HashMap<Integer, List<Integer>> IORequestAtInstruction = new HashMap<Integer, List<Integer>>();
    HashMap<Integer, List<Integer>> IODevicesRequested = new HashMap<Integer, List<Integer>>();
    
    


    public Computer() {
    }

}
   
