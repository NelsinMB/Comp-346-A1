import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Simulator {

    public static void main(String args[]) throws FileNotFoundException {
        Computer computer = new Computer();
        getInput(computer);

    }

    public static void getInput(Computer computer) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("/Users/nelsin/Desktop/Code/Comp 346 - Assignment 1/main/text.txt"));
        scanner.nextLine();
        int counter = 1;
        while (scanner.hasNext()) {
            scanner.next();
            String numberOfInstructionsAsString = scanner.next();
            String IORequestAtInstructionAsString = scanner.next();
            String IODevicesRequestedAsString = scanner.next();
            if (scanner.hasNextLine()) {scanner.nextLine();}

            computer.numberOfInstructions.put(counter, parseInputs(numberOfInstructionsAsString));
            counter++;
        }
        scanner.close();
    }
    
    public static int parseInputs(String two) {
        int numberOfInstructions = Integer.valueOf(two.substring(0, two.length()-1));
        return numberOfInstructions;
    }
}

    

