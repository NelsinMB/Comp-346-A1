import java.util.Random;

public class Register {

    public int value;
    private Processor processor;

    public Register(Processor processor) {
        //Generate random value
        Random rand = new Random();
        this.value = rand.nextInt();

    }
    
}
