import java.util.Random;

public class Register {

    private int value;
    private Processor processor;

    public Register(Processor processor) {
        //Generate random value
        Random rand = new Random();
        this.value = rand.nextInt();

    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
    
}
