/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package pet;

/**
 *
 * @author jacopo.careddu
 */
public class PetShop {

    
    public static void main(String[] args) {
        Cane Fritz = new Cane("mastino", 6.5, "grigio");
        Cane Shana = new Cane("volpino", 1.5, "nero");
        
        Fritz.nutrire(50);
        Shana.nutrire(250);
        
        System.out.println(Fritz.comunica());
        System.out.println(Shana.comunica());
    }
    
}
