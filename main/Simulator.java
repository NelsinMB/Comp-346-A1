import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Simulator {

    public static void main(String args[]) throws FileNotFoundException {
        Computer computer = new Computer();
        getInput(computer);
        System.out.println("Number of instructions: " + computer.numberOfInstructions);
    }

    public static void getInput(Computer computer) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("/Users/nelsin/Desktop/Code/Comp 346 - Assignment 1/main/text.txt"));
        scanner.nextLine();
        int counter = 1;
        while (scanner.hasNext()) {
            System.out.println("ProcessID: " + counter );
            scanner.next();
            String numberOfInstructionsAsString = scanner.next();
            String IORequestAtInstructionAsString = scanner.next();
            String IODevicesRequestedAsString = scanner.next();
            if (scanner.hasNextLine()) {scanner.nextLine();}

            computer.numberOfInstructions.put(counter, parseNumberOfInstructionsInput(numberOfInstructionsAsString));
            computer.IORequestAtInstruction =  parseIORequestAtTimes(computer.numberOfInstructions.get(counter), IORequestAtInstructionAsString);
            computer.IODevicesRequested =  parseIODevicesRequested(computer, computer.numberOfInstructions.get(counter), IODevicesRequestedAsString);
            
        
            
            for (int i = 1; i < computer.numberOfInstructions.get(counter); i++) {
                System.out.println(computer.IODevicesRequested[i]);
            }
            
        

            counter++;
        }
        scanner.close();
    }
    
    public static int parseNumberOfInstructionsInput(String two) {
        int numberOfInstructions = Integer.valueOf(two.substring(0, two.length()-1));
        return numberOfInstructions;
    }

    public static int[] parseIORequestAtTimes(Integer sizeOfArray, String three) {
        int[] returnArray = new int[sizeOfArray]; //Maximum size is the number of instructions
        Arrays.fill(returnArray, 0);
        String[] split = three.split(","); //Split the IORequestAtInstruction input using , as a delimiter
        split[0] = split[0].substring(1, split[0].length()); //Remove the starting '[' from the first value in array (array presented as String)
        split[split.length-1] = split[split.length-1].substring(0, split[split.length-1].length()-1); //Remove the ending ']' from the last value in array (array presented as String)
        for (String s: split) {
            try {
                int value = Integer.valueOf(s); //Get integer values
                returnArray[value] = 1; //1 represents IO instruction there. Array for O(1) access time. 
            } catch (NumberFormatException e) {
                //Occurs with empty input array
            }
        }
        return returnArray;
    }

    public static int[] parseIODevicesRequested(Computer computer, Integer sizeOfArray, String four) {
        int[] returnArray = new int[sizeOfArray];
        Arrays.fill(returnArray, 0);
        String[] split = four.split(","); //Split the IORequestAtInstruction input using , as a delimiter
        split[0] = split[0].substring(1, split[0].length()); //Remove the starting '[' from the first value in array (array presented as String)
        split[split.length-1] = split[split.length-1].substring(0, split[split.length-1].length()-1); //Remove the ending ']' from the last value in array (array presented as String)
        int counter=0;
        for (int i = 0; i < sizeOfArray; i++) {
            if (computer.IORequestAtInstruction[i] == 1) {
                returnArray[i] = Integer.valueOf(split[counter]);
                System.out.println(Integer.valueOf(split[counter]));
                counter++;
            }
        }
        return returnArray;
    }

    
}

    

