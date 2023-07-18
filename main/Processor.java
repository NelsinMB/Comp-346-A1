import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Processor {

    QueueCover readyQueue = new QueueCover();
    Register register1;
    Register register2;
    Process currentProcess;
    IODevice IO1;
    IODevice IO2;

    public Processor(HashMap<Integer, Integer> numberOfInstructions, HashMap<Integer, int[]> IORequestAtInstruction, HashMap<Integer, int[]> IODeviceRequested, IODevice IO1, IODevice IO2) {
        System.out.println("Processor: Processor active.");
        System.out.println("Processor: Creating processes from inputs");
        for (Map.Entry<Integer, Integer> entry : numberOfInstructions.entrySet()) {
            if (entry.getKey() != 0) { //numberOfInstructions is not 0
                Process process = new Process(this, entry.getKey(), numberOfInstructions.get(entry.getKey()), IORequestAtInstruction.get(entry.getKey()), IODeviceRequested.get(entry.getKey()));
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

        while (true) {
            currentProcess = readyQueue.queue.remove(); //remove head of queue
            
            if (currentProcess != null) {
            for (int index = 0; index < 2; index++) { //Maximum two instructions at a time
                if (currentProcess.executeInstruction() == 0) {
                } else if (currentProcess.executeInstruction() == 1) {
                    this.IO1.waitQueue.queue.add(currentProcess);

                } else if (currentProcess.executeInstruction() == 2) {
                    this.IO2.waitQueue.queue.add(currentProcess);
                } 
            }

            if (currentProcess.isDone() == 0) { //notDone
                readyQueue.queue.add(currentProcess);
            } else { //Done

            }


        }
        }

        /* 
        Iterator<Process> iterator = readyQueue.queue.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next().pcb.processState.toString());
        }
        */


        
    }



    // **** Auxilliary methods ****

    public void printArray(int[] input) {
        int length = input.length;
        for (int index = 0; index < length; index++){System.out.print(input[index]);}
        System.out.println("");
    }

    
}
