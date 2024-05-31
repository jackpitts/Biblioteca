package array3;

import java.util.Arrays;
import java.util.Random;

public class Array3 {

        public static void main(String[] args) {
            
            // Creazione del vettore di 15 interi con numeri casuali tra 1 e 30
            int[] vettore = new int[15];
            Random rand = new Random();

            for (int i = 0; i < vettore.length; i++) {
                vettore[i] = rand.nextInt(30) + 1;
            }

            // Stampa del vettore
            System.out.println("Array generato: " + Arrays.toString(vettore));

            // Calcolo delle statistiche
            int somma = 0;
            int min = vettore[0];
            int max = vettore[0];

            for (int numero : vettore) {
                somma += numero;

                if (numero < min) {
                    min = numero;
                }

                if (numero > max) {
                    max = numero;
                }
            }

            double media = (double) somma / vettore.length;

            // Stampa delle statistiche
            System.out.println("Somma: " + somma);
            System.out.println("Media: " + media);
            System.out.println("Minimo: " + min);
            System.out.println("Massimo: " + max);
        }
    }
