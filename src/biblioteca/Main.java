package biblioteca;

import java.util.ArrayList;
import javax.xml.transform.TransformerException;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;

public class Main {

    private static Map<String, String> prestiti = new HashMap<>(); // Mappa per tracciare i prestiti

    public static void main(String[] args) {
        Biblioteca biblioteca = new Biblioteca(); // Crea un'istanza di Biblioteca
        XMLArchive archive = new XMLArchive();
        Scanner scanner = new Scanner(System.in);
        boolean autenticato = false;
        String utenteAutenticato = null;

        while (!autenticato) {
            System.out.println("Benvenuto! Vuoi accedere o registrarti?");
            System.out.println("1. Accedere");
            System.out.println("2. Registrarti");
            System.out.print("Scelta: ");
            String sceltaAccesso = scanner.nextLine();

            switch (sceltaAccesso) {
                case "1" -> {
                    System.out.print("Inserisci il tuo nome utente: ");
                    String username = scanner.nextLine();
                    System.out.print("Inserisci la tua password: ");
                    String password = scanner.nextLine();
                    autenticato = archive.autentica(username, password);
                    if (autenticato) {
                        utenteAutenticato = username; // Memorizza il nome utente autenticato
                        System.out.println("\nAccesso consentito!\n");
                    } else {
                        System.out.println("\nNome utente o password non validi.\n");
                    }
                }
                case "2" -> {
                    System.out.println("Registrazione utente:");
                    System.out.print("Inserisci il nome utente che vuoi registrare: ");
                    String nuovoUtente = scanner.nextLine();
                    System.out.print("Inserisci la tua password: ");
                    String password = scanner.nextLine();
                    archive.registraUtente(nuovoUtente, password);
                    System.out.println("Registrazione completata con successo!\n");
                }
                default ->
                    System.out.println("Scelta non valida. Riprova.");
            }
        }

        System.out.println("Ciao, benvenuto nella biblioteca, cosa vuoi fare?");
        String scelta;
        do {
            System.out.println("\n1. Aggiungi un libro");
            System.out.println("2. Elimina un libro");
            System.out.println("3. Prendi un libro in prestito");
            System.out.println("4. Restituisci un libro");
            System.out.println("5. Visualizza lo storico");
            System.out.println("0. Esci");

            System.out.print("Scelta: ");
            scelta = scanner.nextLine();

            switch (scelta) {
                case "1" -> {
                    System.out.print("Quanti libri vuoi aggiungere? ");
                    int n = scanner.nextInt();
                    scanner.nextLine(); // Consuma il carattere di nuova riga residuo

                    for (int i = 0; i < n; i++) {
                        System.out.print("Inserisci il titolo del libro che desideri aggiungere: ");
                        String title = scanner.nextLine();
                        archive.addBook(archive, title);
                        try {
                            archive.makeArchivePersistent();
                            System.out.println("Libro aggiunto con successo!");
                        } catch (TransformerException ex) {
                            System.err.println("Errore durante la scrittura del file XML: " + ex.getMessage());
                        }
                    }
                }
                case "2" -> {
                    System.out.print("Quanti libri vuoi eliminare? ");
                    int n = scanner.nextInt();
                    if (n > archive.countBooks()) {
                        System.out.println("Hai selezionato un numero di libri maggiore rispetto a quelli presenti");
                        break;
                    }
                    scanner.nextLine(); // Consuma il carattere di nuova riga residuo
                    for (int i = 0; i < n; i++) {
                        System.out.print("Inserisci il titolo del libro che desideri eliminare: ");
                        String titoloDaEliminare = scanner.nextLine();
                        if (archive.eliminaLibro(archive, titoloDaEliminare)) {
                            try {
                                archive.makeArchivePersistent();
                                System.out.println("Libro eliminato con successo!");
                            } catch (TransformerException ex) {
                                System.err.println("Errore durante la scrittura del file XML: " + ex.getMessage());
                            }
                        } else {
                            System.out.println("Libro non trovato");
                        }
                    }
                }
                case "3" -> {
                    System.out.print("Quanti libri vuoi prendere in prestito? ");
                    int n = scanner.nextInt();
                    scanner.nextLine(); // Consuma il carattere di nuova riga residuo
                    if (n > archive.countBooks()) {
                        System.out.println("Hai selezionato un numero di libri maggiore rispetto a quelli presenti");
                        break;
                    }
                    for (int i = 0; i < n; i++) {
                        System.out.print("Inserisci il titolo del libro che desideri prendere in prestito: ");
                        String titoloPrestito = scanner.nextLine();
                        if (archive.prestitoLibro(titoloPrestito, utenteAutenticato)) {
                            prestiti.put(titoloPrestito, utenteAutenticato); // Associa il prestito all'utente autenticato
                            try {
                                archive.makeArchivePersistent();
                                System.out.println("Libro in prestito con successo!");
                            } catch (TransformerException ex) {
                                System.err.println("Errore durante la scrittura del file XML: " + ex.getMessage());
                            }
                        } else {
                            System.out.println("Libro non disponibile.");
                        }
                    }
                }
                case "4" -> {
                    System.out.print("Quanti libri vuoi restituire? ");
                    int n = scanner.nextInt();
                    if (n > archive.countBooksInPrestito()) {
                        System.out.println("Hai selezionato un numero di libri maggiore rispetto a quelli in prestito");
                        scanner.nextLine(); // Consuma il carattere di nuova riga residuo
                        break; // Interrompi il case "4" e vai alla prossima iterazione del ciclo
                    }
                    scanner.nextLine(); // Consuma il carattere di nuova riga residuo
                    for (int i = 0; i < n; i++) {
                        System.out.print("Inserisci il titolo del libro che desideri restituire: ");
                        String titoloRestituzione = scanner.nextLine();
                        if (archive.restituisciLibro(titoloRestituzione, utenteAutenticato)) {
                            prestiti.remove(titoloRestituzione); // Rimuovi l'associazione del prestito
                            try {
                                archive.makeArchivePersistent();
                                System.out.println("Libro restituito con successo!");
                            } catch (TransformerException ex) {
                                System.err.println("Errore durante la scrittura del file XML: " + ex.getMessage());
                            }
                        } else {
                            System.out.println("Impossibile restituire il libro!");
                        }
                    }
                    // Interrompi il case "4" e vai alla prossima iterazione del ciclo
                }
                case "5" -> {
                    biblioteca.eseguiBiblioteca(utenteAutenticato, archive); // Passa l'oggetto archive
                    break;
                }
                case "0" ->
                    System.out.println("Arrivederci!");
                default ->
                    System.out.println("Comando non riconosciuto");
            }
        } while (!scelta.equals("0"));
    }
}
