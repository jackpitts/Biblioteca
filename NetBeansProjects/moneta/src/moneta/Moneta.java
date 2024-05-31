/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package moneta;

import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author jacopo.careddu
 */
public class Moneta {

    public static void main(String[] args) {
        
        // Variabili
        
        Scanner myObj = new Scanner(System.in);
        String continuare;
        int numVittorie = 0;
        int numSconfitte = 0;
        int scelta;
        int numLanci = 0;
        int numTesta = 0;
        int numCroce = 0;
        double percentualeTesta;
        double percentualeCroce;

        do {
            System.out.println("Scegli se scommetere testa o croce (T/C)");
            String scommessa = myObj.nextLine();

            Random rand = new Random();
            int moneta = rand.nextInt(2);

            // transformazione della scommessa (string) in scelta (int)
            
            if (scommessa.equalsIgnoreCase("T")) {
                scelta = 0;
            } else if (scommessa.equalsIgnoreCase("C")) {
                scelta = 1;
            } else {
                return;
            }

            // verifica dell' uguaglianza tra l'output della moneta e la scelta dell'utente
            
            if (moneta == scelta) {
                System.out.println("Hai vinto!" + "\n");
                numVittorie = numVittorie + 1;
            } else {
                numSconfitte = numSconfitte + 1;
                System.out.println("Hai perso!" + "\n");
            }

            // Aumento numero testa e croce
            
            if (moneta == 0) {
                numTesta = numTesta + 1;
            } else {
                numCroce = numCroce + 1;
            }

            // Aumento numero lanci
            
            numLanci = numLanci + 1;

            System.out.println("Continuare? (Y/N)");
            continuare = myObj.nextLine();

        } while (continuare.equalsIgnoreCase("Y"));

        if (continuare.equalsIgnoreCase("N")) {

            // Calcolo percentuale testa e croce
            
            percentualeTesta = (double) numTesta / numLanci * 100;
            percentualeCroce = (double) numCroce / numLanci * 100;

            // Approssimazione
            
            String resTesta = String.format("%.1f", percentualeTesta);
            String resCroce = String.format("%.1f", percentualeCroce);

            // Stampa finale
            
            System.out.println("\n" + "Numero lanci: " + numLanci + "\n" + "Numero testa: " + numTesta + "\n" + "Numero croce: " + numCroce + "\n" + "Numero vittorie: " + numVittorie + "\n" + "Numero sconfitte: " + numSconfitte + "\n" + "Percentuale testa: " + resTesta + "%" + "\n" + "Percentuale croce: " + resCroce + "%");
        }
    }

}
