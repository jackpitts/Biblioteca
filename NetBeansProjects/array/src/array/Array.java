package array;

import java.util.Scanner;
import java.util.Arrays;

public class Array {

    public static void main(String[] args) {

        Scanner tastiera = new Scanner(System.in);

        // Chiedi all'utente la dimensione dell'array
        System.out.print("Inserire la dimensione dell'array: ");
        int dim = tastiera.nextInt();

        // Creazione dell'array
        int[] array = new int[dim];

        // Inserimento dei valori nell'array
        for (int i = 0; i < dim; i++) {
            System.out.print("Inserire un numero per la posizione " + i + ": ");
            array[i] = tastiera.nextInt();
        }
        System.out.println("\n");

        OperazioniVettore operazioni = new OperazioniVettore();

        // Stampa gli elementi dell'array
        operazioni.stampaArray(array);

        // Stampa gli elementi in posizione pari
        operazioni.stampaPosizionePari(array);

        // Stampa gli elementi in posizione dispari
        operazioni.stampaPosizioneDispari(array);

        // Stampa gli elementi dell'array dall'ultimo al primo
        operazioni.stampaInvertita(array);

        // Stampa gli elementi dell'array in ordine crescente
        operazioni.stampaOrdinato(array);
    }
}
