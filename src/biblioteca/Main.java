package biblioteca;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main  {

    public static void main(String[] args) {
        try {
            // Caricamento del documento XML esistente
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc;

            File file = new File("src\\biblioteca\\libri.xml");
            if (file.exists()) {
                doc = dBuilder.parse(file);
                doc.getDocumentElement().normalize();
            } else {
                doc = dBuilder.newDocument();
                doc.appendChild(doc.createElement("libri"));
            }

            // Input dei nuovi dati dall'utente
            Scanner scanner = new Scanner(System.in);
            System.out.println("Inserisci i titoli dei libri da aggiungere: ");

            for (int i = 0; i < 3; i++) {
                System.out.print("Titolo del libro " + (i + 1) + ": ");
                String titoloLibro = scanner.nextLine();

                Element libroElement = doc.createElement("libro");
                libroElement.setAttribute("titolo", titoloLibro); // Aggiunta dell'attributo titolo
                doc.getDocumentElement().appendChild(libroElement);
            }

            // Richiesta dei titoli dei libri da eliminare
            System.out.println("Inserisci i titoli dei libri da eliminare: ");

            for (int i = 0; i < 2; i++) {
                System.out.print("Titolo del libro da eliminare " + (i + 1) + ": ");
                String titoloDaEliminare = scanner.nextLine();

                NodeList libroNodes = doc.getElementsByTagName("libro");

                for (int j = 0; j < libroNodes.getLength(); j++) {
                    Node libroNode = libroNodes.item(j);
                    if (libroNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element libroElement = (Element) libroNode;
                        String titoloLibro = libroElement.getAttribute("titolo"); // Ottenere l'attributo titolo
                        if (titoloLibro.equals(titoloDaEliminare)) {
                            libroElement.getParentNode().removeChild(libroElement);
                            break;
                        }
                    }
                }
            }

            // Salva il documento XML aggiornato su un file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);
            System.out.println("File XML aggiornato con successo!");

            // Lettura del file XML aggiornato
            System.out.println("Titoli dei libri nel file XML:");
            NodeList libroNodes = doc.getElementsByTagName("libro");

            for (int i = 0; i < libroNodes.getLength(); i++) {
                Node libroNode = libroNodes.item(i);
                if (libroNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element libroElement = (Element) libroNode;
                    String titoloLibro = libroElement.getAttribute("titolo"); // Ottenere l'attributo titolo
                    System.out.println("- " + titoloLibro);
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
            e.printStackTrace();
        }
    }
}
