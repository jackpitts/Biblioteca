/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package replacenondigit;

import java.util.Scanner;

/**
 *
 * @author jacopo.careddu
 */
public class ReplaceNonDigit {

    public static void main(String[] args) {

        Scanner myObj = new Scanner(System.in);
        System.out.println("Inserisci il primo numero");
        String numero1 = myObj.nextLine();

        System.out.println(numero1.replaceAll("[^\\d.]", ""));

    }
}