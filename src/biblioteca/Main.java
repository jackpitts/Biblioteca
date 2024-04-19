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

public class Main {

    public static void main(String[] args) {
        try {
            // Creazione del documento XML
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            // Creazione dell'elemento radice
            Element rootElement = doc.createElement("dati");
            doc.appendChild(rootElement);

            // Input dinamico dei dati dall'utente
            Scanner scanner = new Scanner(System.in);
            ArrayList<String[]> dati = new ArrayList<>();
            System.out.println("Inserisci i dati. Digita 'fine' per terminare l'inserimento.");

            while (true) {
                System.out.print("Nome elemento: ");
                String nomeElemento = scanner.nextLine();
                if (nomeElemento.equals("fine")) {
                    break;
                }

                System.out.print("Nome attributo: ");
                String attributo = scanner.nextLine();

                System.out.print("Valore attributo: ");
                String valoreAttributo = scanner.nextLine();

                dati.add(new String[]{nomeElemento, attributo, valoreAttributo});
            }

            // Creazione degli elementi XML con gli attributi
            for (String[] elemento : dati) {
                String nomeElemento = elemento[0];
                String attributo = elemento[1];
                String valoreAttributo = elemento[2];

                Element xmlElement = doc.createElement(nomeElemento);
                xmlElement.setAttribute(attributo, valoreAttributo);
                rootElement.appendChild(xmlElement);
            }

            // Salva il documento XML su un file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("C:\\\\Users\\\\mirko.cuter\\\\Desktop\\\\Biblioteca\\\\src\\\\biblioteca\\\\store.xml"));

            // Scrittura dei dati sul file XML
            transformer.transform(source, result);
            System.out.println("File XML creato con successo!");

            // Lettura del file XML
            File inputFile = new File("C:\\\\Users\\\\mirko.cuter\\\\Desktop\\\\Biblioteca\\\\src\\\\biblioteca\\\\store.xml");
            Document document = dBuilder.parse(inputFile);
            document.getDocumentElement().normalize();

            // Accesso ai dati
            NodeList nodeList = document.getElementsByTagName("dati");
            for (int temp = 0; temp < nodeList.getLength(); temp++) {
                Node node = nodeList.item(temp);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String attributo = element.getAttribute("attributo");
                    System.out.println("Attributo: " + attributo);
                    String testo = element.getTextContent();
                    System.out.println("Testo: " + testo);
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
            e.printStackTrace();
        }
    }
}

