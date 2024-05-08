package biblioteca;

import java.util.Scanner;
import javax.xml.transform.TransformerException;

public class Main {

    public static void main(String[] args) {
        XMLArchive archive = new XMLArchive();
        Scanner scanner = new Scanner(System.in);
        System.out.println("ciao, benvenuto nella biblioteca, cosa vuoi fare?");

        String scelta = scanner.nextLine();
        switch (scelta) {
            case "add" -> {
                // Aggiungi un libro all'archivio
            }
            case "del" -> {
                System.out.println("Quanti libri vuoi eliminare?");
                int n = scanner.nextInt();
                scanner.nextLine(); // Consuma il carattere di nuova riga residuo

                for (int i = 0; i < n; i++) {
                    System.out.print("Titolo del libro da eliminare " + (i + 1) + ": ");
                    String titoloDaEliminare = scanner.nextLine();
                    if (eliminaLibro(archive, titoloDaEliminare)) {
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
                // Operazioni per prestito
            }
            case "restituzione" -> {
                // Operazioni per restituzione
            }
            case "storico" -> {
                // Operazioni per visualizzare lo storico
            }
            default -> System.out.println("Comando non riconosciuto");
        }
    }

    private static boolean eliminaLibro(XMLArchive archive, String titolo) {
        var doc = archive.getDocument();
        var libroNodes = doc.getElementsByTagName("libro");
        for (int i = 0; i < libroNodes.getLength(); i++) {
            var libroElement = (org.w3c.dom.Element) libroNodes.item(i);
            if (libroElement.getAttribute("titolo").equals(titolo)) {
                libroElement.getParentNode().removeChild(libroElement);
                return true;
            }
        }
        return false;
    }
}
