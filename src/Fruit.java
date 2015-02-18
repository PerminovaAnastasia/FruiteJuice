import java.io.IOException;

/**
 * Created by Anastasia on 11.02.2015.
 */
public class Fruit {
    public static void main(String[] args) throws IOException{

        Juicer arrayOfJuicers = new Juicer();
        arrayOfJuicers.input("juice.in");
        arrayOfJuicers.outputDif("juice1.out");
        new Thread(arrayOfJuicers).start();
        arrayOfJuicers.algorithmKuna("juice3.out");
    }
}
