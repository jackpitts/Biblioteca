package dado;

import java.util.Random;

public class Dado {

    public static void main(String[] args) {

        Random rand = new Random();

        System.out.println(rand.nextInt(6) + 1);

    }

}
