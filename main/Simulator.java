import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Simulator {

    public static void main(String args[]) {
        try {
            Scanner scanner = new Scanner(new File("/Users/nelsin/Desktop/Code/Comp 346 - Assignment 1/main/text copy.txt"));
            scanner.nextLine();
            HashMap<Integer, Integer> temp = new HashMap<Integer, Integer>();
            HashMap<Integer, int[]> IORequestAtInstruction = new HashMap<Integer, int[]>();
            HashMap<Integer, int[]> IODevicesRequested = new HashMap<Integer, int[]>();

            int counter = 1;
            while (scanner.hasNext()) {
                // System.out.println("ProcessID: " + counter );
                scanner.next();
                String numberOfInstructionsAsString = scanner.next();
                String IORequestAtInstructionAsString = scanner.next();
                String IODevicesRequestedAsString = scanner.next();
                if (scanner.hasNextLine()) {
                    scanner.nextLine();
                }

                temp.put(counter, parseNumberOfInstructionsInput(numberOfInstructionsAsString));
                IORequestAtInstruction.put(counter,
                        parseIORequestAtTimes(temp.get(counter), IORequestAtInstructionAsString));
                IODevicesRequested.put(counter, parseIODevicesRequested(IORequestAtInstruction.get(counter),
                        temp.get(counter), IODevicesRequestedAsString));

                counter++;
            }
            scanner.close();
            Computer computer = new Computer(temp, IORequestAtInstruction, IODevicesRequested);
        } catch (Exception e) {
            System.out.println(e.toString());
            System.out.println(e.getStackTrace()[0].getClassName());
            System.out.println(e.getStackTrace()[0].getLineNumber());


        }

    }

    // Moved to main
    public static void getInput() throws FileNotFoundException {

    }

    public static int parseNumberOfInstructionsInput(String two) {
        int numberOfInstructions = Integer.valueOf(two.substring(0, two.length() - 1));
        return numberOfInstructions;
    }

    public static int[] parseIORequestAtTimes(Integer sizeOfArray, String three) {
        int[] returnArray = new int[sizeOfArray]; // Maximum size is the number of instructions
        Arrays.fill(returnArray, 0);
        String[] split = three.split(","); // Split the IORequestAtInstruction input using , as a delimiter
        split[0] = split[0].substring(1, split[0].length()); // Remove the starting '[' from the first value in array
                                                             // (array presented as String)
        split[split.length - 1] = split[split.length - 1].substring(0, split[split.length - 1].length() - 1); // Remove
                                                                                                              // the
                                                                                                              // ending
                                                                                                              // ']'
                                                                                                              // from
                                                                                                              // the
                                                                                                              // last
                                                                                                              // value
                                                                                                              // in
                                                                                                              // array
                                                                                                              // (array
                                                                                                              // presented
                                                                                                              // as
                                                                                                              // String)
        for (String s : split) {
            try {
                int value = Integer.valueOf(s); // Get integer values
                returnArray[value] = 1; // 1 represents IO instruction there. Array for O(1) access time.
            } catch (NumberFormatException e) {
                // Occurs with empty input array
            }
        }
        return returnArray;
    }

    public static int[] parseIODevicesRequested(int[] IORequestAtInstruction, Integer sizeOfArray, String four) {
        int[] returnArray = new int[sizeOfArray];
        Arrays.fill(returnArray, 0);
        String[] split = four.split(","); // Split the IORequestAtInstruction input using , as a delimiter
        split[0] = split[0].substring(1, split[0].length()); // Remove the starting '[' from the first value in array
                                                             // (array presented as String)
        split[split.length - 1] = split[split.length - 1].substring(0, split[split.length - 1].length() - 1); // Remove
                                                                                                              // the
                                                                                                              // ending
                                                                                                              // ']'
                                                                                                              // from
                                                                                                              // the
                                                                                                              // last
                                                                                                              // value
                                                                                                              // in
                                                                                                              // array
                                                                                                              // (array
                                                                                                              // presented
                                                                                                              // as
                                                                                                              // String)
        int counter = 0;
        for (int i = 0; i < sizeOfArray; i++) {
            if (IORequestAtInstruction[i] == 1) {
                returnArray[i] = Integer.valueOf(split[counter]);
                // System.out.println(Integer.valueOf(split[counter]));
                counter++;
            }
        }
        return returnArray;
    }

}
