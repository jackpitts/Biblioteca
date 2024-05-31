/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package array;

import java.util.Arrays;

/**
 *
 * @author jacopo.careddu
 */
public class OperazioniVettore {

    // Metodo per stampare gli elementi del vettore
    public void stampaArray(int[] vettore) {
        System.out.println("Elementi del vettore:");
        for (int elemento : vettore) {
            System.out.print(elemento + " ");
        }
        System.out.println("\n");
    }

    // Metodo per stampare gli elementi in posizione pari del vettore
    public void stampaPosizionePari(int[] vettore) {
        System.out.println("Elementi in posizione pari:");
        for (int i = 0; i < vettore.length; i += 2) {
            System.out.print(vettore[i] + " ");
        }
        System.out.println("\n");
    }

    // Metodo per stampare gli elementi in posizione dispari del vettore
    public void stampaPosizioneDispari(int[] vettore) {
        System.out.println("Elementi in posizione dispari:");
        for (int i = 1; i < vettore.length; i += 2) {
            System.out.print(vettore[i] + " ");
        }
        System.out.println("\n");
    }

    // Metodo per stampare gli elementi del vettore dall'ultimo al primo
    public void stampaInvertita(int[] vettore) {
        System.out.println("Elementi del vettore dall'ultimo al primo:");
        for (int i = vettore.length - 1; i >= 0; i--) {
            System.out.print(vettore[i] + " ");
        }
        System.out.println("\n");
    }
    
    public void stampaOrdinato(int[] vettore){
        Arrays.sort(vettore);
        System.out.println("Il vettore ordinato e': ");
        for (int num : vettore) {
            System.out.print(num + " ");
        }
        System.out.println("\n");
    }
}
