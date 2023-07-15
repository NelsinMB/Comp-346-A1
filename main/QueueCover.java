import java.util.LinkedList;
import java.util.Queue;

/*
 * Queue acts as a shell for the Queue implementation. 
 */

public class QueueCover {
    Queue<String> queue = new LinkedList<String>();;

    public QueueCover(){
        this.queue.clear(); //Ensure queue is empty upon creation.
    }
    
}
