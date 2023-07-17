import java.util.HashMap;
import java.util.Map;

public class Processor {

    QueueCover readyQueue = new QueueCover();
    Register register1;
    Register register2;
    Process currentProcess;

    public Processor(HashMap<Integer, Integer> numberOfInstructions, HashMap<Integer, int[]> IORequestAtInstruction, HashMap<Integer, int[]> IODeviceRequested) {
        System.out.println("Processor: Processor active.");
        System.out.println("Processor: Creating processes from inputs");
        for (Map.Entry<Integer, Integer> entry : numberOfInstructions.entrySet()) {
            if (entry.getKey() != 0) { //numberOfInstructions is not 0
                Process process = new Process(entry.getKey(), IORequestAtInstruction.get(entry.getKey()), IODeviceRequested.get(entry.getKey()));
                this.readyQueue.queue.add(process);
                //printArray(this.readyQueue.queue.element().IORequestAtInstruction);
                //System.out.println(this.readyQueue.queue.element().processID);
                //this.readyQueue.queue.remove();
              
            }
        }
        System.out.println("Processor: Processes have been added to readyQueue");
        System.out.println("Processor: Execution may now begin.");
        Loop();
        
    }



    public void Loop() {
        int currentProcessIterations = 0;
        while (true) {
            if (currentProcessIterations < 2) {
                System.out.println("The current process being executed is" + currentProcess.processID);
                System.out
            }

            if (readyQueue.queue.element() != null) {
                Process nextProcess = readyQueue.queue.element();
                
            }





        }
    }



    // **** Auxilliary methods ****

    public void printArray(int[] input) {
        int length = input.length;
        for (int index = 0; index < length; index++){System.out.print(input[index]);}
        System.out.println("");
    }

    
}
