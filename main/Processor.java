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

    public Processor(HashMap<Integer, Integer> numberOfInstructions, HashMap<Integer, int[]> IORequestAtInstruction,
            HashMap<Integer, int[]> IODeviceRequested, IODevice IO1, IODevice IO2) {
        this.IO1 = IO1;
        this.IO2 = IO2;
        // System.out.println("Processor: Processor active.");
        // System.out.println("Processor: Creating processes from inputs");
        for (Map.Entry<Integer, Integer> entry : numberOfInstructions.entrySet()) {
            if (entry.getKey() != 0) { // numberOfInstructions is not 0
                Process process = new Process(this, entry.getKey(), numberOfInstructions.get(entry.getKey()),
                        IORequestAtInstruction.get(entry.getKey()), IODeviceRequested.get(entry.getKey()));
                this.readyQueue.queue.add(process);
                // printArray(this.readyQueue.queue.element().IORequestAtInstruction);
                // System.out.println(this.readyQueue.queue.element().processID);
                // this.readyQueue.queue.remove();

            }
        }
        // System.out.println("Processor: Processes have been added to readyQueue");
        // System.out.println("Processor: Execution may now begin.");
        Loop();

    }

    public void Loop() {
        int counter = 2; // Whatever, start with 2
        while (true) {
            if (counter > 1) {
                currentProcess = readyQueue.queue.remove(); // Load new process
                counter = 0; // Start back at 0
            } else {
                // Maintain current process
            }
            System.out.println("counter: " + counter);

            if (currentProcess != null) {
                int executionResult = currentProcess.executeInstruction(); //Necessary to not call function multiple times.
                if (executionResult == 0) {
                    counter++;
                } else if (executionResult == 1) {
                    this.IO1.waitQueue.queue.add(currentProcess);
                    counter++;

                } else if (executionResult  == 2) {
                    this.IO2.waitQueue.queue.add(currentProcess);
                    counter++;
                }

                System.out.println("Name of process being executed now:" + currentProcess.processID);
                System.out.println("Content of the ready queue:");
                printReadyQueue();
                System.out.println("The content of the wait queue for IO device 1:");
                printWaitQueue1();
                System.out.println("The content of the wait queue for IO device 2:");
                printWaitQueue2();

                // Before or after printing to console??

                if (counter == 1) {
                    if (currentProcess.isDone() == 0) { // notDone
                        readyQueue.queue.add(currentProcess);
                    } else { // Done

                    }
                }

            }

        }

    }

    public void printReadyQueue() {

        Iterator<Process> iterator = readyQueue.queue.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next().processID);
        }

    }

    public void printWaitQueue1() {
        Iterator<Process> iterator = IO1.waitQueue.queue.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next().processID);
        }
    }

    public void printWaitQueue2() {
        Iterator<Process> iterator = IO2.waitQueue.queue.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next().processID);
        }
    }

    // **** Auxilliary methods ****

    public void printArray(int[] input) {
        int length = input.length;
        for (int index = 0; index < length; index++) {
            System.out.print(input[index]);
        }
        System.out.println("");
    }

}
