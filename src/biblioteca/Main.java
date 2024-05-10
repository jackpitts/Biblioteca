package biblioteca;

import java.util.Scanner;
import javax.xml.transform.TransformerException;
import java.util.Map;
import java.util.HashMap;

public class Main {

    private static Map<String, String> prestiti = new HashMap<>(); // Mappa per tracciare i prestiti
    private static final String[] UTENTI_AUTORIZZATI = {"utente1", "utente2", "utente3"}; // Elenca tutti gli utenti autorizzati

    public static void main(String[] args) {
        XMLArchive archive = new XMLArchive();
        Scanner scanner = new Scanner(System.in);
        boolean autenticato = false;
        String utenteAutenticato = null;

        while (!autenticato) {
            System.out.println("Inserisci il tuo nome utente:");
            String username = scanner.nextLine();

            autenticato = autentica(username);
            if (autenticato) {
                utenteAutenticato = username; // Memorizza il nome utente autenticato
            } else {
                System.out.println("Nome utente non valido. Riprova.");
            }
        }
        // Ora l'utente è autenticato, puoi procedere con il resto del programma
        System.out.println("Accesso consentito. Benvenuto!");
        // Continua con il resto del programma...

        System.out.println(
                "Ciao, benvenuto nella biblioteca, cosa vuoi fare?");
        String scelta = scanner.nextLine();
        switch (scelta) {
            case "add" -> {

                System.out.println("Quanti libri vuoi aggiungere?");
                int n = scanner.nextInt();
                scanner.nextLine(); // Consuma il carattere di nuova riga residuo

                for (int i = 0; i < n - 1; i++) {
                    System.out.println("Dimmi il titolo del libro da aggiungere:");
                    String title = scanner.nextLine();
                    archive.addBook(archive, title);
                    try {
                        archive.makeArchivePersistent();
                        System.out.println("Libro aggiunto con successo");
                    } catch (TransformerException ex) {
                        System.err.println("Errore durante la scrittura del file XML: " + ex.getMessage());
                    }
                }

                // Aggiungi un libro all'archivio
                System.out.println("Dimmi il titolo:");
                String title = scanner.nextLine();
                archive.addBook(archive, title);
                try {
                    archive.makeArchivePersistent();
                    System.out.println("Libro aggiunto con successo");
                } catch (TransformerException ex) {
                    System.err.println("Errore durante la scrittura del file XML: " + ex.getMessage());
                }
            }
            case "del" -> {
                System.out.println("Quanti libri vuoi eliminare?");
                int n = scanner.nextInt();
                if (n > archive.countBooks()) {
                    System.out.println("Hai selezionato un numero di libri maggiore rispetto a quelli presenti");
                    return;
                }
                scanner.nextLine(); // Consuma il carattere di nuova riga residuo

                for (int i = 0; i < n; i++) {
                    System.out.print("Titolo del libro da eliminare: ");
                    String titoloDaEliminare = scanner.nextLine();
                    if (archive.eliminaLibro(archive, titoloDaEliminare)) {
                        try {
                            archive.makeArchivePersistent();
                            System.out.println("Libro eliminato con successo");
                        } catch (TransformerException ex) {
                            System.err.println("Errore durante la scrittura del file XML: " + ex.getMessage());
                        }
                    } else {
                        System.out.println("Libro non trovato: " + titoloDaEliminare);
                    }
                }
            }
            case "prestito" -> {
                System.out.println("Quanti libri vuoi prendere in prestito?");
                int n = scanner.nextInt();
                scanner.nextLine(); // Consuma il carattere di nuova riga residuo
                if (n > archive.countBooks()) {
                    System.out.println("Hai selezionato un numero di libri maggiore rispetto a quelli presenti");
                    return;
                }
                for (int i = 0; i < n; i++) {
                    System.out.println("Inserisci il titolo del libro che desideri prendere in prestito:");
                    String titoloPrestito = scanner.nextLine();
                    if (archive.prestitoLibro(titoloPrestito, utenteAutenticato)) {
                        prestiti.put(titoloPrestito, utenteAutenticato); // Associa il prestito all'utente autenticato
                        try {
                            archive.makeArchivePersistent();
                            System.out.println("Libro in prestito con successo");
                        } catch (TransformerException ex) {
                            System.err.println("Errore durante la scrittura del file XML: " + ex.getMessage());
                        }
                    } else {
                        System.out.println("Libro non disponibile per il prestito o non trovato: " + titoloPrestito);
                    }
                }
            }

            case "restituzione" -> {
                System.out.println("Quanti libri vuoi restituire?");
                int n = scanner.nextInt();
                if (n > archive.countBooksInPrestito()) {
                    System.out.println("Hai selezionato un numero di libri maggiore rispetto a quelli in prestito");
                    return;
                }
                scanner.nextLine(); // Consuma il carattere di nuova riga residuo
                for (int i = 0; i < n; i++) {
                    System.out.println("Inserisci il titolo del libro che desideri restituire:");
                    String titoloRestituzione = scanner.nextLine();
                    if (archive.restituisciLibro(titoloRestituzione, utenteAutenticato)) {
                        prestiti.remove(titoloRestituzione); // Rimuovi l'associazione del prestito
                        try {
                            archive.makeArchivePersistent();
                            System.out.println("Libro restituito con successo");
                        } catch (TransformerException ex) {
                            System.err.println("Errore durante la scrittura del file XML: " + ex.getMessage());
                        }
                    } else {
                        System.out.println("Non sei autorizzato a restituire questo libro o il libro non è in prestito.");
                    }
                }
            }

            case "storico" -> {
                // Operazioni per visualizzare lo storico
            }
            default ->
                System.out.println("Comando non riconosciuto");
        }
    }

    private static boolean autentica(String username) {
        for (String utente : UTENTI_AUTORIZZATI) {
            if (utente.equals(username)) {
                return true;
            }
        }
        return false;
    }
}
