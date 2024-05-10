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
import java.util.ArrayList;
import org.w3c.dom.Node;

public class XMLArchive {

    private static final String ARCHIVE_PATH = "src\\biblioteca\\libri.xml";
    private static final String UTENTI_FILE_PATH = "src/biblioteca/utenti.xml";
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
        Document doc = archive.getDocument();
        NodeList libroNodes = doc.getElementsByTagName("libro");
        for (int i = 0; i < libroNodes.getLength(); i++) {
            Element libroElement = (Element) libroNodes.item(i);
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

    public boolean prestitoLibro(String titolo, String utente) {
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

                    // Aggiungi attributo per il nome dell'utente che ha preso in prestito il libro
                    libroElement.setAttribute("utentePrestito", utente);

                    return true;
                } else {
                    return false; // Libro già in prestito
                }
            }
        }
        return false; // Libro non trovato
    }

    public boolean restituisciLibro(String titolo, String utente) {
        NodeList libroNodes = doc.getElementsByTagName("libro");
        for (int i = 0; i < libroNodes.getLength(); i++) {
            Element libroElement = (Element) libroNodes.item(i);
            if (libroElement.getAttribute("titolo").equals(titolo)) {
                String statoPrestito = libroElement.getAttribute("inPrestito");
                if (statoPrestito.equals("true")) {
                    String utentePrestito = libroElement.getAttribute("utentePrestito");
                    if (utentePrestito.equals(utente)) {
                        libroElement.setAttribute("data_restituzione", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))); // Imposta la data di restituzione
                        return true; // Restituzione avvenuta con successo
                    } else {
                        return false; // Utente non autorizzato a restituire il libro
                    }
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

    public static void registraUtente(String nuovoUtente, String password) {
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
            nuovoUtenteElement.setAttribute("password", password); // Memorizza la password

            doc.getDocumentElement().appendChild(nuovoUtenteElement);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);

            System.out.println("Utente registrato con successo!");

        } catch (ParserConfigurationException | IOException | org.xml.sax.SAXException | TransformerException e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<String> caricaUtentiAutorizzati() {
        ArrayList<String> utentiAutorizzati = new ArrayList<>();
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new File("src/biblioteca/utenti.xml"));
            doc.getDocumentElement().normalize();
            NodeList utentiNodeList = doc.getElementsByTagName("utente");
            for (int i = 0; i < utentiNodeList.getLength(); i++) {
                Node utenteNode = utentiNodeList.item(i);
                if (utenteNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element utenteElement = (Element) utenteNode;
                    String username = utenteElement.getAttribute("username");
                    String password = utenteElement.getAttribute("password");
                    utentiAutorizzati.add(username + ":" + password);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return utentiAutorizzati;
    }

    public static boolean autentica(String username, String password) {
        ArrayList<String> utentiAutorizzati = caricaUtentiAutorizzati();
        for (String utente : utentiAutorizzati) {
            String[] userInfo = utente.split(":");
            if (userInfo.length == 2) {
                String storedUsername = userInfo[0];
                String storedPassword = userInfo[1];
                if (storedUsername.equals(username) && storedPassword.equals(password)) {
                    return true;
                }
            }
        }
        return false;
    }

    public ArrayList<String> getStoricoAzioniUtente(String utenteCorrente) {
        ArrayList<String> storico = new ArrayList<>();
        NodeList libroNodes = doc.getElementsByTagName("libro");
        for (int i = 0; i < libroNodes.getLength(); i++) {
            Element libroElement = (Element) libroNodes.item(i);
            String utenteAzione = libroElement.getAttribute("utentePrestito");
            if (utenteAzione.equals(utenteCorrente)) {
                String titoloLibro = libroElement.getAttribute("titolo");
                String dataPrestito = libroElement.getAttribute("data_prestito");
                String dataRestituzione = libroElement.getAttribute("data_restituzione");
                if (!dataPrestito.isEmpty()) {
                    storico.add("Libro: " + titoloLibro + ", Utente: " + utenteAzione + ", Azione: Prestito, Data: " + dataPrestito);
                }
                if (!dataRestituzione.isEmpty()) {
                    storico.add("Libro: " + titoloLibro + ", Utente: " + utenteAzione + ", Azione: Restituzione, Data: " + dataRestituzione);
                }
            }
        }
        return storico;
    }
}
