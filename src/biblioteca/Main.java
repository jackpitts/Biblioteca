package biblioteca;

import javax.xml.transform.TransformerException;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

public class Main {

    private static Map<String, String> prestiti = new HashMap<>(); // Mappa per tracciare i prestiti
    private static final String[] UTENTI_AUTORIZZATI = {"utente1", "utente2", "utente3"}; // Elenca tutti gli utenti autorizzati

    public static void main(String[] args) {
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
                case "1":
                    System.out.println("Inserisci il tuo nome utente:");
                    String username = scanner.nextLine();
                    autenticato = autentica(username);
                    if (autenticato) {
                        utenteAutenticato = username; // Memorizza il nome utente autenticato
                        System.out.println("Accesso consentito. Benvenuto!");
                    } else {
                        System.out.println("Nome utente non valido. Riprova.");
                    }
                    break;
                case "2":
                    System.out.println("Registrazione utente:");
                    System.out.println("Inserisci il nome utente che vuoi registrare:");
                    String nuovoUtente = scanner.nextLine();
                    registraUtente(nuovoUtente);
                    System.out.println("Registrazione completata con successo!");
                    break;
                default:
                    System.out.println("Scelta non valida. Riprova.");
                    break;
            }
        }

        // Ora l'utente è autenticato, puoi procedere con il resto del programma
        XMLArchive archive = new XMLArchive();

        System.out.println("Ciao, benvenuto nella biblioteca, cosa vuoi fare?");
        String scelta;
        do {
            System.out.println("Scegli un'opzione:");
            System.out.println("1. Aggiungi un libro");
            System.out.println("2. Elimina un libro");
            System.out.println("3. Prendi un libro in prestito");
            System.out.println("4. Restituisci un libro");
            System.out.println("5. Visualizza lo storico");
            System.out.println("0. Esci");

            scelta = scanner.nextLine();

            switch (scelta) {
                case "1":
                    System.out.println("Quanti libri vuoi aggiungere?");
                    int n = scanner.nextInt();
                    scanner.nextLine(); // Consuma il carattere di nuova riga residuo

                    for (int i = 0; i < n; i++) {
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
                    break;
                case "2":
                    System.out.println("Quanti libri vuoi eliminare?");
                    n = scanner.nextInt();
                    if (n > archive.countBooks()) {
                        System.out.println("Hai selezionato un numero di libri maggiore rispetto a quelli presenti");
                        break;
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
                    break;
                case "3":
                    System.out.println("Quanti libri vuoi prendere in prestito?");
                    n = scanner.nextInt();
                    scanner.nextLine(); // Consuma il carattere di nuova riga residuo
                    if (n > archive.countBooks()) {
                        System.out.println("Hai selezionato un numero di libri maggiore rispetto a quelli presenti");
                        break;
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
                    break;
                case "4":
                    System.out.println("Quanti libri vuoi restituire?");
                    n = scanner.nextInt();
                    if (n > archive.countBooksInPrestito()) {
                        System.out.println("Hai selezionato un numero di libri maggiore rispetto a quelli in prestito");
                        break;
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
                    break;
                case "5":
                    System.out.println("Funzionalità non implementata");
                    break;
                case "0":
                    System.out.println("Arrivederci!");
                    break;
                default:
                    System.out.println("Comando non riconosciuto");
                    break;
            }
        } while (!scelta.equals("0"));
    }

private static boolean autentica(String username) {
    for (String utente : UTENTI_AUTORIZZATI) {
        System.out.println("Utente autorizzato: " + utente);
        System.out.println("Utente inserito: " + username);
        if (utente.equalsIgnoreCase(username)) {
            return true;
        }
    }
    return false;
}

    private static void registraUtente(String nuovoUtente) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            File file = new File("src/biblioteca/utenti.xml");
            Document doc;

            if (file.exists()) {
                doc = dBuilder.parse(file);
            } else {
                doc = dBuilder.newDocument();
                Element utentiElement = doc.createElement("utenti");
                doc.appendChild(utentiElement);
            }

            Element nuovoUtenteElement = doc.createElement("utente");
            nuovoUtenteElement.setAttribute("username", nuovoUtente);

            doc.getDocumentElement().appendChild(nuovoUtenteElement);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);

            System.out.println("Utente registrato con successo: " + nuovoUtente);
            

        } catch (ParserConfigurationException | IOException | org.xml.sax.SAXException | TransformerException e) {
            e.printStackTrace();
        }
    }
}
