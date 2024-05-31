package array04;
import java.util.Arrays;

public class Calcoli {

    public static int[] creaVettoreCasuale() {
        int[] vettore = new int[15];
        for (int i = 0; i < vettore.length; i++) {
            vettore[i] = (int) (Math.random() * (30)) + 1;
        }
        return vettore;
    }

    public static int calcolaSomma(int[] vettore) {
        int somma = 0;
        for (int valore : vettore) {
            somma += valore;
        }
        return somma;
    }

    public static double calcolaMedia(int[] vettore) {
        return (double) calcolaSomma(vettore) / vettore.length;
    }
    
    public static double calcolaMediana(int[] vettore) {
        Arrays.sort(vettore);
        if (vettore.length % 2 == 0) {
            int indiceMediano1 = vettore.length / 2 - 1;
            int indiceMediano2 = vettore.length / 2;
            return (vettore[indiceMediano1] + vettore[indiceMediano2]) / 2.0;
        } else {
            int indiceMediano = vettore.length / 2;
            return vettore[indiceMediano];
        }
    }

    public static int calcolaMinimo(int[] vettore) {
        int minimo = vettore[0];
        for (int valore : vettore) {
            if (valore < minimo) {
                minimo = valore;
            }
        }
        return minimo;
    }

    public static int calcolaMassimo(int[] vettore) {
        int massimo = vettore[0];
        for (int valore : vettore) {
            if (valore > massimo) {
                massimo = valore;
            }
        }
        return massimo;
    }
}
