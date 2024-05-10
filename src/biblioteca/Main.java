package biblioteca;

import java.util.Scanner;
import javax.xml.transform.TransformerException;

public class Main {

    public static void main(String[] args) {
        XMLArchive archive = new XMLArchive();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ciao, benvenuto nella biblioteca, cosa vuoi fare?");

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
                scanner.nextLine(); // Consuma il carattere di nuova riga residuo

                for (int i = 0; i < n; i++) {
                    System.out.print("Titolo del libro da eliminare " + (i + 1) + ": ");
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
                System.out.println("Inserisci il titolo del libro che desideri prendere in prestito:");
                String titoloPrestito = scanner.nextLine();

                if (archive.prestitoLibro(titoloPrestito)) {
                    try {
                        archive.makeArchivePersistent();
                        System.out.println("Libro in prestito con successo");
                    } catch (TransformerException ex) {
                        System.err.println("Errore durante la scrittura del file XML: " + ex.getMessage());
                    }
                } else {
                    System.out.println("Libro non disponibile per il prestito: " + titoloPrestito);
                }

            }
            case "restituzione" -> {
                // Operazioni per restituzione
            }
            case "storico" -> {
                // Operazioni per visualizzare lo storico
            }
            default ->
                System.out.println("Comando non riconosciuto");
        }
    }
}
