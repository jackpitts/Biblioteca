package biblioteca;

import java.util.*;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Biblioteca biblioteca = new Biblioteca();
        
        System.out.println("Inserisci il tuo libro:");
        String title = scanner.nextLine();
        
        System.out.println("Inserisci il tuo libro:");
        String title2 = scanner.nextLine();
        
        System.out.println("Inserisci il tuo libro:");
        String title3 = scanner.nextLine();

        Libro libro1 = new Libro(title);
        Libro libro2 = new Libro(title2);
        Libro libro3 = new Libro(title3);

        biblioteca.acquisisce(libro1);
        biblioteca.acquisisce(libro2);
        biblioteca.acquisisce(libro3);

        Persona persona1 = new Persona("Mario");
        Persona persona2 = new Persona("Luigi");

        biblioteca.prestito(libro1, persona1);
        biblioteca.prestito(libro2, persona2);
        biblioteca.prestito(libro3, persona1);

        System.out.println("Libri in prestito per " + persona1.getNome() + " :" + biblioteca.libriInPrestitoPerPersona(persona1));
        System.out.println("Libri in prestito per " + persona1.getNome() + " :" + biblioteca.libriInPrestitoPerPersona(persona2));
                
        biblioteca.restituzione(libro1);

        System.out.println("Libri in prestito per Mario dopo la restituzione: " + biblioteca.libriInPrestitoPerPersona(persona1));

        System.out.println("Titolo del primo libro: " + libro1.getTitolo());
        System.out.println("Titolo del secondo libro: " + libro2.getTitolo());
        System.out.println("Titolo del terzo libro: " + libro3.getTitolo());
    }
}
