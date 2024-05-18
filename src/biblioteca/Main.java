package biblioteca;

import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) {
        LibraryRepository libraryRepo = new LibraryRepositoryXML("src\\biblioteca\\libri.xml");
        LibraryService libraryService = new LibraryService(libraryRepo);
        UserRepository userRepo = new UserRepositoryXML("src\\biblioteca\\utenti.xml");
        UserService userService = new UserService(userRepo);
        Scanner scanner = new Scanner(System.in);
        boolean authenticated = false;
        User user = null;

        while (!authenticated) {

            System.out.println("Benvenuto! Vuoi accedere o registrarti?");
            System.out.println("+--------------------+");
            System.out.println("|   1. Accedere      |");
            System.out.println("|   2. Registrarti   |");
            System.out.println("|   0. Esci          |");
            System.out.println("+--------------------+");
            System.out.print("\nScelta: ");
            String choice = scanner.nextLine();

            switch (choice.replaceAll("\\s+", "")) {
                case "1" -> {
                    System.out.print("Inserisci il tuo nome utente: ");
                    String name = scanner.nextLine().replaceAll("\\s+", "");
                    System.out.print("Inserisci la tua password: ");
                    String password = scanner.nextLine().replaceAll("\\s+", "");
                    System.out.print("\n");

                    try {
                        user = userService.authUser(name, password);
                        authenticated = true;
                    } catch (Exception ex) {
                        System.out.println("Nome utente o password errati\n");
                    }
                }
                case "2" -> {
                    System.out.println("Registrazione utente:");
                    System.out.print("Inserisci il nome utente che vuoi registrare: ");
                    String name = scanner.nextLine().replaceAll("\\s+", "");
                    System.out.print("Inserisci la tua password: ");
                    String password = scanner.nextLine().replaceAll("\\s+", "");
                    userService.addUser(name, password);
                    System.out.println("Registrazione completata con successo!\n");
                }
                case "0" -> {
                    System.out.println("Arrivederci!");
                    return;
                }
                default ->
                    System.out.println("Comando non riconosciuto");
            }
        }

        System.out.println("Ciao " + user.getName() + ", benvenuto nella biblioteca, cosa vuoi fare?");
        String choice;
        do {
            System.out.println("+-----------------------------------+");
            System.out.println("|    1. Aggiungi libri              |");
            System.out.println("|    2. Elimina libri               |");
            System.out.println("|    3. Prendi un prestito          |");
            System.out.println("|    4. Effettua una restituzione   |");
            System.out.println("|    5. Visualizza catalogo         |");
            System.out.println("|    6. Cerca libri                 |");
            System.out.println("|    7. Situazione prestiti         |");
            System.out.println("|    8. Storico azioni              |");
            System.out.println("|    0. Esci                        |");
            System.out.println("+-----------------------------------+");

            System.out.print("\nScelta: ");
            choice = scanner.nextLine();

            switch (choice.replaceAll("\\s+", "")) {
                case "1" -> {
                    System.out.print("Quanti libri vuoi aggiungere? ");
                    int n = scanner.nextInt();
                    scanner.nextLine();

                    for (int i = 0; i < n; i++) {
                        System.out.print("Inserisci il titolo del libro che desideri aggiungere: ");
                        String title = scanner.nextLine();

                        // Verifica se il libro esiste già nell'archivio
                        if (libraryService.hasBook(title)) {
                            // Se il libro esiste già, aumenta semplicemente la quantità disponibile
                            System.out.print("Il libro esiste gia' nell'archivio, inserisci la quantita' di copie da aggiungere: ");
                            int quantity = scanner.nextInt();
                            try {
                                Book book = libraryService.getBook(title);
                                libraryService.addOrUpdateBook(book.getTitle(), book.getAuthor(), book.getPublisher(), book.getGenre(), book.getYear(), quantity);
                            } catch (Exception ex) {
                                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            scanner.nextLine();
                        } else {
                            // Se il libro non esiste, aggiungilo all'archivio
                            System.out.print("Inserisci l'autore del libro che desideri aggiungere: ");
                            String author = scanner.nextLine();
                            System.out.print("Inserisci la casa editrice del libro che desideri aggiungere: ");
                            String publisher = scanner.nextLine();
                            System.out.print("Inserisci il genere del libro che desideri aggiungere: ");
                            String genre = scanner.nextLine();
                            System.out.print("Inserisci l'anno di pubblicazione del libro che desideri aggiungere: ");
                            int year = scanner.nextInt();
                            System.out.print("Inserisci quante copie sono disponibili del libro che desideri aggiungere: ");
                            int quantity = scanner.nextInt();
                            libraryService.addOrUpdateBook(title, author, publisher, genre, year, quantity);
                            scanner.nextLine();
                        }
                    }
                    System.out.println("Catalogo aggiornato con successo!\n");
                    break;
                }

                case "2" -> {
                    System.out.print("Quanti libri vuoi eliminare? ");
                    int n = scanner.nextInt();
                    scanner.nextLine();
                    for (int i = 0; i < n; i++) {
                        System.out.print("Inserisci il titolo del libro che desideri eliminare: ");
                        String title = scanner.nextLine();
                        if (libraryService.hasBook(title)) {
                            System.out.print("Inserisci il numero di copie che desideri eliminare (" + libraryService.getBookQuantity(title) + " disponibili): ");
                            int quantity = scanner.nextInt();
                            libraryService.removeQuantities(title, quantity);
                            System.out.print("Catalogo aggiornato con successo!\n");
                            scanner.nextLine();
                        } else {
                            System.out.println("Libro non trovato!\n");
                            break;
                        }
                    }
                }

                case "3" -> {
                    System.out.print("Quanti libri vuoi prendere in prestito? ");
                    int n = scanner.nextInt();
                    scanner.nextLine();
                    if (n > libraryService.countBooks()) {
                        System.out.println("Hai selezionato un numero di libri maggiore rispetto a quelli presenti nel catalogo");
                        break;
                    }
                    for (int i = 0; i < n; i++) {
                        System.out.print("Inserisci il titolo del libro che desideri prendere in prestito: ");
                        String title = scanner.nextLine();

                        // Ottieni la quantità disponibile di questo libro dal documento XML
                        int currentQuantity = libraryService.getBookQuantity(title);
                        if (currentQuantity == 0) {
                            System.out.println("Libro attualmente non disponibile\n");
                            break; // Interrompi il ciclo se il libro non è disponibile
                        }

                        // Stampa la quantità disponibile nel messaggio per l'utente
                        System.out.print("Inserisci la quantita' di copie che desideri prendere in prestito (" + currentQuantity + " disponibili): ");
                        int quantity = scanner.nextInt();
                        scanner.nextLine();

                        try {
                            libraryService.borrow(title, quantity);
                            userService.addBook(user, title, quantity);
                            System.out.println("Prestito effettuato con successo!\n");
                        } catch (Exception ex) {
                            ex.printStackTrace(new java.io.PrintStream(System.out));
                            System.out.println("La quantita' di copie inserita è maggiore di quelle disponibili\n");
                        }
                    }
                }

                case "4" -> {
                    System.out.print("Quanti libri vuoi restituire? ");
                    int n = scanner.nextInt();
                    scanner.nextLine();

                    for (int i = 0; i < n; i++) {
                        System.out.print("Inserisci il titolo del libro che desideri restituire: ");
                        String title = scanner.nextLine();

                        // Consentire all'utente di restituire fino al limite massimo di libri in possesso
                        int userBookQuantity = userService.getBookQuantity(user, title);
                        // Se l'utente non ha il libro
                        if (userBookQuantity == 0) {
                            System.out.println("Non possiedi il libro!\n");
                            break;
                        }
                        System.out.print("Inserisci la quantita' di copie che desideri restituire (" + userBookQuantity + " disponibili): ");
                        int quantity = scanner.nextInt();
                        scanner.nextLine();

                        // Restituire solo fino al limite massimo di copie in possesso
                        if (quantity > userBookQuantity) {
                            System.out.println("Hai selezionato una quantita' di copie maggiore rispetto a quelle in tuo possesso\n");
                            break;
                        }
                        try {
                            userService.returnBook(user, title, quantity);
                            libraryService.updateQuantity(title, quantity);
                            System.out.println("Restituzione effettuata con successo!\n");
                        } catch (Exception ex) {
                            System.out.println("Errore\n");
                        }
                    }
                }

                case "5" -> {
                    try {
                        System.out.println("I libri presenti nella biblioteca sono i seguenti: ");
                        System.out.println(libraryService.getBookTitlesAsString());
                    } catch (Exception ex) {
                        System.out.println("Nessun libro presente nel catalogo della biblioteca attualmente\n");
                    }
                }

                case "6" -> {
                    System.out.print("Inserisci il titolo del libro che desideri cercare: ");
                    String title = scanner.nextLine();
                    try {
                        System.out.println("Ecco i dettagli del libro richiesto: ");
                        libraryService.searchBook(title);
                    } catch (Exception ex) {
                        System.out.println("Libro non trovato nel catalogo\n");
                    }
                }
                case "7" -> {
                    try {
                        String bookTitles = userService.getBookTitlesAsString(user);
                        if (bookTitles.isEmpty()) {
                            System.out.println("Attualmente non hai nessun libro in prestito\n");
                        } else {
                            System.out.println("I libri che hai attualmente in prestito sono i seguenti: ");
                            System.out.println(bookTitles);
                        }
                    } catch (Exception ex) {
                        // Gestione di un eventuale eccezione
                        System.out.println("Si è verificato un errore nel recuperare i libri in prestito\n");
                    }
                }

                case "8" -> {
                    List<History> history = userService.getHistory(user);
                    if (history.isEmpty()) {
                        System.out.println("Nessuno storico disponibile");
                    } else {
                        System.out.println("Ecco lo storico delle azioni:");
                        for (int i = 0; i < history.size(); i++) {
                            System.out.println("Azione: " + history.get(i).getUserAction() + ", Titolo: " + history.get(i).getTitle() + ", numero di copie: " + history.get(i).getQuantity() + ", in data: " + history.get(i).getDate());
                        }
                    }
                    System.out.print("\n");
                }

                case "0" ->
                    System.out.println("Arrivederci!");

                default ->
                    System.out.println("Comando non riconosciuto\n");
            }

        } while (!choice.equals("0"));
    }
}
