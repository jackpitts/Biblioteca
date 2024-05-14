package biblioteca;

import javax.xml.transform.TransformerException;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static Map<String, String> prestiti = new HashMap<>(); // Mappa per tracciare i prestiti

    public static void main(String[] args) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String date = LocalDateTime.now().format(formatter);
        LibraryRepository libraryRepo = new LibraryRepositoryXML("src\\biblioteca\\libri.xml");
        LibraryService libraryService = new LibraryService(libraryRepo);
        UserRepository userRepo = new UserRepositoryXML("src\\biblioteca\\utenti.xml");
        UserService userService = new UserService(userRepo);
        Scanner scanner = new Scanner(System.in);
        boolean authenticated = false;
        User user = null;

        while (!authenticated) {
            System.out.println("Benvenuto! Vuoi accedere o registrarti?");
            System.out.println("1. Accedere");
            System.out.println("2. Registrarti");
            System.out.print("Scelta: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> {
                    System.out.print("Inserisci il tuo nome utente: ");
                    String name = scanner.nextLine();
                    System.out.print("Inserisci la tua password: ");
                    String password = scanner.nextLine();
                    System.out.print("\n");

                    try {
                        user = userService.authUser(name, password);
                        authenticated = true;
                    } catch (Exception ex) {
                        System.out.println("\nNome utente o password errati\n");
                    }
                }
                case "2" -> {
                    System.out.println("Registrazione utente:");
                    System.out.print("Inserisci il nome utente che vuoi registrare: ");
                    String name = scanner.nextLine();
                    System.out.print("Inserisci la tua password: ");
                    String password = scanner.nextLine();
                    userService.addUser(name, password);
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
            System.out.println("5. Situazione prestiti/restituzioni");
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

                        // Verifica se il libro esiste già nell'archivio
                        if (libraryService.hasBook(title)) {
                            // Se il libro esiste già, aumenta semplicemente la quantità disponibile
                            System.out.println("Il libro esiste gia' nell'archivio.");
                            System.out.print("Inserisci la quantita' di copie da aggiungere: ");
                            int quantity = scanner.nextInt();
                            try {
                                Book book = libraryService.getBook(title);
                                libraryService.addOrUpdateBook(book.getTitle(), book.getAuthor(), book.getPublisher(), book.getGenre(), book.getYear(), quantity);
                            } catch (Exception ex) {
                                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            scanner.nextLine(); // Consuma il carattere di nuova riga residuo
                        } else {
                            // Se il libro non esiste, aggiungilo all'archivio
                            System.out.print("Inserisci l'autore del libro che desideri aggiungere: ");
                            String author = scanner.nextLine();
                            System.out.print("Inserisci la casa editrice del libro che desideri aggiungere: ");
                            String publisher = scanner.nextLine();
                            System.out.print("Inserisci il genere del libro che desideri aggiungere: ");
                            String genre = scanner.nextLine();
                            System.out.print("Inserisci l'anno di rilascio del libro che desideri aggiungere: ");
                            int year = scanner.nextInt();
                            System.out.print("Inserisci quante copie sono disponibili del libro che desideri aggiungere: ");
                            int quantity = scanner.nextInt();
                            libraryService.addOrUpdateBook(title, author, publisher, genre, year, quantity);
                            scanner.nextLine(); // Consuma il carattere di nuova riga residuo
                        }
                    }
                    System.out.println("Libro aggiunto con successo!");
                    break; // Esci dal case "1" dopo aver aggiunto i libri richiesti
                }

                case "2" -> {
                    System.out.print("Quanti libri vuoi eliminare? ");
                    int n = scanner.nextInt();
                    scanner.nextLine(); // Consuma il carattere di nuova riga residuo
                    for (int i = 0; i < n; i++) {
                        System.out.print("Inserisci il titolo del libro che desideri eliminare: ");
                        String title = scanner.nextLine();
                        if (libraryService.hasBook(title)) {
                            System.out.print("Inserisci il numero di copie che desideri eliminare: ");
                            int quantity = scanner.nextInt();
                            libraryService.removeQuantities(title, quantity);
                            System.out.print("Libri eliminati con successo!");
                        } else {
                            System.out.println("Libro non trovato");
                        }
                    }
                    scanner.nextLine();
                }

                case "3" -> {
                    // Prendi un libro in prestito
                    System.out.print("Quanti libri vuoi prendere in prestito? ");
                    int n = scanner.nextInt();
                    scanner.nextLine(); // Consuma il carattere di nuova riga residuo
                    if (n > libraryService.countBooks()) {
                        System.out.println("\nHai selezionato un numero di libri maggiore rispetto a quelli presenti");
                        break;
                    }
                    for (int i = 0; i < n; i++) {
                        System.out.print("Inserisci il titolo del libro che desideri prendere in prestito: ");
                        String title = scanner.nextLine();

                        // Ottieni la quantità disponibile di questo libro dal documento XML
                        int currentQuantity = libraryService.getBookQuantity(title);
                        if (currentQuantity == 0) {
                            System.out.println("\nLibro non disponibile");
                            break; // Interrompi il ciclo se il libro non è disponibile
                        }

                        // Stampa la quantità disponibile nel messaggio per l'utente
                        System.out.print("Inserisci la quantita' di libri che desideri prendere in prestito (" + currentQuantity + " disponibili): ");
                        int quantity = scanner.nextInt();
                        scanner.nextLine(); // Consuma il carattere di nuova riga residuo

                        try {
                            libraryService.borrow(title, quantity);
                            userService.addBook(user, title, quantity);
                            System.out.println("\nLibro preso in prestito con successo!");
                        } catch (Exception ex) {
                            System.out.println("\nQuantita' di libri non disponibile");
                        }
                    }
                }
//                case "4" -> {
//                    System.out.print("Quanti libri vuoi restituire? ");
//                    int n = scanner.nextInt();
//                    scanner.nextLine(); // Consuma il carattere di nuova riga residuo
//
//                    for (int i = 0; i < n; i++) {
//                        System.out.print("Inserisci il titolo del libro che desideri restituire: ");
//                        String titoloRestituzione = scanner.nextLine();
//
//                        if (!prestiti.containsKey(titoloRestituzione)) {
//                            System.out.println("Non hai in prestito questo libro.");
//                            break; // Termina l'operazione senza chiedere la quantità di libri da restituire
//                        }
//
//                        // Consentire all'utente di restituire fino al limite massimo
//                        System.out.print("Inserisci la quantita' di copie che desideri restituire (" + archive.quantiLibriPossoRestituire(utenteAutenticato) + " disponibili): ");
//                        int quantitaRestituzione = scanner.nextInt();
//                        scanner.nextLine(); // Consuma il carattere di nuova riga residuo
//
//                        // Ottienere il numero massimo di libri che l'utente può restituire per questo titolo
//                        int maxRestituzione = archive.getMaxRestituzioneForUser(utenteAutenticato, quantitaRestituzione);
//
//                        // Restituire solo fino al limite massimo
//                        if (quantitaRestituzione > maxRestituzione) {
//                            System.out.println("Hai superato il limite massimo di restituzione per questo libro.");
//                            return;
//                        }
//
//                        // Effettuare la restituzione dei libri
//                        if (archive.restituisciLibro(titoloRestituzione, utenteAutenticato, quantitaRestituzione)) {
//                            prestiti.remove(titoloRestituzione); // Rimuovi l'associazione del prestito
//                            try {
//                                archive.makeArchivePersistent();
//                                System.out.println("Libro restituito con successo!");
//                            } catch (TransformerException ex) {
//                                System.err.println("Errore durante la scrittura del file XML: " + ex.getMessage());
//                            }
//                        } else {
//                            System.out.println("Impossibile restituire il libro!");
//                        }
//                    }
//                    // Interrompi il case "4" e vai alla prossima iterazione del ciclo
//                }
//
//                case "5" -> {
//                    biblioteca.eseguiBiblioteca(utenteAutenticato, archive); // Passa l'oggetto archive
//                    break;
//                }
//                case "0" ->
//                    System.out.println("Arrivederci!");
//                default ->
//                    System.out.println("Comando non riconosciuto");
            }

        } while (!scelta.equals(
                "0"));
    }
}
