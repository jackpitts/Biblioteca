package array2;
import java.util.Scanner;

public class Array2 {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // Input della dimensione del vettore
        System.out.print("Inserisci la dimensione del vettore: ");
        int n = scanner.nextInt();

        // Creazione del vettore
        int[] vettore = new int[n];

        // Input dei valori del vettore
        System.out.println("Inserisci i valori del vettore:");

        for (int i = 0; i < n; i++) {
            System.out.print("Elemento " + (i + 1) + ": ");
            vettore[i] = scanner.nextInt();
        }

        // Verifica se il vettore è strettamente crescente, strettamente decrescente o né l'uno né l'altro
        boolean crescente = true;
        boolean decrescente = true;

        for (int i = 0; i < n - 1; i++) {
            if (vettore[i] >= vettore[i + 1]) {
                crescente = false;
            }

            if (vettore[i] <= vettore[i + 1]) {
                decrescente = false;
            }
        }

        // Output del risultato
        if (crescente) {
            System.out.println("I valori inseriti sono strettamente crescenti.");
        } else if (decrescente) {
            System.out.println("I valori inseriti sono strettamente decrescenti.");
        } else {
            System.out.println("I valori inseriti non sono ne strettamente crescenti ne strettamente decrescenti.");
        }

    }
}
