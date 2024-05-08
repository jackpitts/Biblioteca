package biblioteca;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLArchive {

    private static final String ARCHIVE_PATH = "src\\biblioteca\\libri.xml";
    DocumentBuilderFactory dbFactory;
    DocumentBuilder dBuilder;
    Document doc;
    File file;

    public XMLArchive() {
        try {
            dbFactory = DocumentBuilderFactory.newInstance();
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(XMLArchive.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void createArchive() {
        file = new File(ARCHIVE_PATH);
        if (file.exists()) {
            try {
                doc = dBuilder.parse(file);
                doc.getDocumentElement().normalize();
            } catch (Exception ex) {
                Logger.getLogger(XMLArchive.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            doc = dBuilder.newDocument();
            doc.appendChild(doc.createElement("libri"));
        }
    }

    public void addBook(Libro libro) {
        Element libroElement = doc.createElement("libro");
        libroElement.setAttribute("titolo", libro.getTitolo()); 
        libroElement.setAttribute("autore", libro.getTitolo()); 
        libroElement.setAttribute("casaEditrice", libro.getTitolo()); 
        libroElement.setAttribute("anno", libro.getTitolo()); 
        doc.getDocumentElement().appendChild(libroElement);
    }

    public void makeArchivePersistent() {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(XMLArchive.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(XMLArchive.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void readArchive() {        
        NodeList libroNodes = doc.getElementsByTagName("libro");
        for (int i = 0; i < libroNodes.getLength(); i++) {
            Node libroNode = libroNodes.item(i);
            if (libroNode.getNodeType() == Node.ELEMENT_NODE) {
                Element libroElement = (Element) libroNode;
                String titoloLibro = libroElement.getAttribute("titolo"); 
                System.out.println("- " + titoloLibro);
            }
        }
    }
}
