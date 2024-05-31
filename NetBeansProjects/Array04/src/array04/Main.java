/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package array04;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        
        // Creazione del vettore di 15 interi casuali compresi tra 1 e 30
        int[] vettore = Calcoli.creaVettoreCasuale();
        Arrays.sort(vettore);

        // Stampare le statistiche
        System.out.println("Vettore: " + Arrays.toString(vettore));
        System.out.println("Vettore ordinato: " + Arrays.toString(vettore));
        System.out.println("Somma: " + Calcoli.calcolaSomma(vettore));
        System.out.println("Media: " + Calcoli.calcolaMedia(vettore));
        System.out.println("Mediana: " + Calcoli.calcolaMediana(vettore));
        System.out.println("Minimo: " + Calcoli.calcolaMinimo(vettore));
        System.out.println("Massimo: " + Calcoli.calcolaMassimo(vettore));
    }
    
}
