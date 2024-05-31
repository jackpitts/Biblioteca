package progetton1;

import java.util.Scanner;

public class Progetton1 {

    public static void main(String[] args) {
        Scanner myObj = new Scanner(System.in);
        int scelta = 1;

        do {
            System.out.println("Inserisci il primo numero");
            int numero1 = myObj.nextInt();
            System.out.println("Inserisci il secondo numero");
            int numero2 = myObj.nextInt();

            if (numero1 == numero2) {
                System.out.println("I due numeri sono uguali");
            } else if (numero1 > numero2) {
                System.out.println("Il primo numero e' maggiore");
            } else {
                System.out.println("il secondo numero e' maggiore");
            }
            System.out.println("Continuare? 1 = si, 2 = no");
            scelta = myObj.nextInt();
            
        } while (scelta == 1);
    }

}
