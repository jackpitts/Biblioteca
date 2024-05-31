/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pet;

/**
 *
 * @author jacopo.careddu
 */
public class Cane {
    private String stato;
    private String colore;
    private double peso;
    private String razza;
    
    public Cane (String razza, double peso, String colore){
        this.stato = "dorme";
        this. colore = colore;
        this.peso = peso;
        this.razza = razza;
    }
    
    public void nutrire (double grammi){
        if (grammi > this.peso/10){
            this.stato = "soddisfatto";
        } else {
            this.stato = "affamato";
        }
    }
    
    public String comunica(){
        return "razza = " + this.razza + "\n" + "peso = " + this.peso + "\n" + "colore = " + this.colore + "\n" + "stato = " + this.stato;
    }
    
}
