package biblioteca;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class XMLArchive {

    private static final String ARCHIVE_PATH = "src\\biblioteca\\libri.xml";
    private Document doc;

    public XMLArchive() {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            File file = new File(ARCHIVE_PATH);
            if (file.exists()) {
                doc = dBuilder.parse(file);
                doc.getDocumentElement().normalize();
            } else {
                doc = dBuilder.newDocument();
                doc.appendChild(doc.createElement("libri"));
            }
        } catch (ParserConfigurationException | IOException | org.xml.sax.SAXException e) {
            e.printStackTrace();
        }
    }

    public Document getDocument() {
        return doc;
    }

    public void makeArchivePersistent() throws TransformerException {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(ARCHIVE_PATH));
            transformer.transform(source, result);
        } catch (TransformerException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void readArchive() {
        NodeList libroNodes = doc.getElementsByTagName("libro");
        for (int i = 0; i < libroNodes.getLength(); i++) {
            Element libroElement = (Element) libroNodes.item(i);
            String titoloLibro = libroElement.getAttribute("titolo");
            System.out.println("- " + titoloLibro);
        }
    }

    public static boolean eliminaLibro(XMLArchive archive, String titolo) {
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

    public static boolean addBook(XMLArchive archive, String titolo) {
        Document doc = archive.getDocument();
        Element libriElement = doc.getDocumentElement();
        Element nuovoLibroElement = doc.createElement("libro");
        nuovoLibroElement.setAttribute("titolo", titolo);

        // Aggiungi attributo inPrestito come booleano
        nuovoLibroElement.setAttribute("inPrestito", "false");

        libriElement.appendChild(nuovoLibroElement);

        return true;
    }

    public boolean prestitoLibro(String titolo) {
        NodeList libroNodes = doc.getElementsByTagName("libro");
        for (int i = 0; i < libroNodes.getLength(); i++) {
            Element libroElement = (Element) libroNodes.item(i);
            if (libroElement.getAttribute("titolo").equals(titolo)) {
                String statoPrestito = libroElement.getAttribute("inPrestito");
                if (statoPrestito.equals("false")) {
                    libroElement.setAttribute("inPrestito", "true");

                    // Aggiungi attributo data e ora corrente
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String dataOraCorrente = LocalDateTime.now().format(formatter);
                    libroElement.setAttribute("data_prestito", dataOraCorrente);
                    return true;
                } else {
                    return false; // Libro già in prestito
                }
            }
        }
        return false; // Libro non trovato
    }

    public boolean restituisciLibro(String titolo) {
        NodeList libroNodes = doc.getElementsByTagName("libro");
        for (int i = 0; i < libroNodes.getLength(); i++) {
            Element libroElement = (Element) libroNodes.item(i);
            if (libroElement.getAttribute("titolo").equals(titolo)) {
                String statoPrestito = libroElement.getAttribute("inPrestito");
                if (statoPrestito.equals("true")) {
                    libroElement.setAttribute("inPrestito", "false");
                    libroElement.removeAttribute("data_prestito");
                    return true; // Restituzione avvenuta con successo
                } else {
                    return false; // Libro non in prestito
                }
            }
        }
        return false; // Libro non trovato
    }

    public int countBooks() {
        NodeList libroNodes = doc.getElementsByTagName("libro");
        return libroNodes.getLength();
    }

    public int countBooksInPrestito() {
        NodeList libroNodes = doc.getElementsByTagName("libro");
        int count = 0;
        for (int i = 0; i < libroNodes.getLength(); i++) {
            Element libroElement = (Element) libroNodes.item(i);
            if (libroElement.getAttribute("inPrestito").equals("true")) {
                count++;
            }
        }
        return count;
    }

}
