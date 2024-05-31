/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package maggiorenne;
import java.util.Scanner;
import java.time.Year;

/**
 *
 * @author jacopo.careddu
 */
public class Maggiorenne {

    public static void main(String[] args) {
        Scanner myObj = new Scanner(System.in);
        int year = Year.now().getValue();
        System.out.println("Inserisci il tuo anno di nascita");
        int annonascita = myObj.nextInt();

        if (year - annonascita >= 18) {
            System.out.println("Sei maggiorenne");
        } 
        else {
            System.out.println("Sei minorenne");
        }
    }
}
