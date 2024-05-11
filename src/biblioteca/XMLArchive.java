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

    public void addBook(String titolo, String autore, String casaEditrice, String genere, int anno, int quantitaDisponibile) {
        Document doc = getDocument();
        NodeList libroNodes = doc.getElementsByTagName("libro");
        boolean libroEsistente = false;

        for (int i = 0; i < libroNodes.getLength(); i++) {
            Element libroElement = (Element) libroNodes.item(i);
            if (libroElement.getAttribute("titolo").equals(titolo)) {
                // Se il libro esiste già, aumenta semplicemente la quantità disponibile
                int quantitaAttuale = Integer.parseInt(libroElement.getAttribute("quantita_disponibile"));
                libroElement.setAttribute("quantita_disponibile", String.valueOf(quantitaAttuale + quantitaDisponibile));
                System.out.println("Libro già presente. Quantità disponibile aggiornata.");
                libroEsistente = true;
                break; // Esci dal ciclo una volta aggiornata la quantità disponibile
            }
        }

        // Se il libro non esiste già, crea un nuovo elemento libro
        if (!libroEsistente) {
            Element libriElement = doc.getDocumentElement();
            Element nuovoLibroElement = doc.createElement("libro");
            nuovoLibroElement.setAttribute("titolo", titolo);
            nuovoLibroElement.setAttribute("autore", autore);
            nuovoLibroElement.setAttribute("casa_editrice", casaEditrice);
            nuovoLibroElement.setAttribute("genere", genere);
            nuovoLibroElement.setAttribute("anno", String.valueOf(anno));
            nuovoLibroElement.setAttribute("quantita_disponibile", String.valueOf(quantitaDisponibile));
            nuovoLibroElement.setAttribute("inPrestito", "false");
            nuovoLibroElement.setAttribute("quantita_in_prestito", "0");
            libriElement.appendChild(nuovoLibroElement);
        }

    }

    public boolean prestitoLibro(String titolo, String utente, int quantitaRichiesta) {
        NodeList libroNodes = doc.getElementsByTagName("libro");
        for (int i = 0; i < libroNodes.getLength(); i++) {
            Element libroElement = (Element) libroNodes.item(i);
            if (libroElement.getAttribute("titolo").equals(titolo)) {
                int quantitaDisponibile = Integer.parseInt(libroElement.getAttribute("quantita_disponibile"));
                int quantitaInPrestito = Integer.parseInt(libroElement.getAttribute("quantita_in_prestito"));
                // Controllo se la quantità richiesta è disponibile
                if (quantitaDisponibile - quantitaRichiesta >= 0) {
                    // Aggiorno la quantità disponibile e in prestito
                    libroElement.setAttribute("quantita_disponibile", String.valueOf(quantitaDisponibile - quantitaRichiesta));
                    libroElement.setAttribute("quantita_in_prestito", String.valueOf(quantitaInPrestito + quantitaRichiesta));

                    // Aggiorno l'attributo inPrestito
                    libroElement.setAttribute("inPrestito", "true");

                    // Aggiorno il numero di libri presi in prestito dall'utente corrente
                    String utentiPrestito = libroElement.getAttribute("utentePrestito");
                    String[] utenti = utentiPrestito.split(",");
                    boolean utentePresente = false;
                    for (int j = 0; j < utenti.length; j++) {
                        String[] userInfo = utenti[j].split(":");
                        if (userInfo[0].equals(utente)) {
                            int quantitaPrestitoUtente = Integer.parseInt(userInfo[1]);
                            utenti[j] = utente + ":" + (quantitaPrestitoUtente + quantitaRichiesta);
                            utentePresente = true;
                            break;
                        }
                    }
                    if (!utentePresente) {
                        utentiPrestito += "," + utente + ":" + quantitaRichiesta;
                        libroElement.setAttribute("utentePrestito", utentiPrestito);
                    } else {
                        // Aggiorno la lista degli utenti in prestito nel documento XML
                        libroElement.setAttribute("utentePrestito", String.join(",", utenti));
                    }
                    // Aggiorno la data di prestito
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String dataPrestito = LocalDateTime.now().format(formatter);
                    libroElement.setAttribute("data_prestito", dataPrestito);
                    try {
                        makeArchivePersistent();
                        return true; // Prestito avvenuto con successo
                    } catch (TransformerException ex) {
                        System.err.println("Errore durante la scrittura del file XML: " + ex.getMessage());
                        return false; // Errore durante il salvataggio dell'archivio XML
                    }
                } else {
                    // Se la quantità disponibile è uguale a 0, restituisco false
                    return false; // Quantità richiesta superiore alla quantità disponibile
                }
            }
        }
        // Se il libro non viene trovato, restituisco false
        return false;
    }

    public boolean restituisciLibro(String titolo, String utente, int quantita) {
        NodeList libroNodes = doc.getElementsByTagName("libro");
        boolean restituzioneAvvenuta = false; // Flag per tenere traccia se almeno un libro è stato restituito

        for (int i = 0; i < libroNodes.getLength(); i++) {
            Element libroElement = (Element) libroNodes.item(i);
            if (libroElement.getAttribute("titolo").equals(titolo)) {
                // Controllo se l'utente ha effettivamente preso in prestito il libro
                String utentiPrestito = libroElement.getAttribute("utentePrestito");
                if (!utentiPrestito.isEmpty()) {
                    String[] utenti = utentiPrestito.split(",");
                    for (int j = 0; j < utenti.length; j++) {
                        String userInfo = utenti[j];
                        String[] userAndQuantity = userInfo.split(":");
                        if (userAndQuantity[0].equals(utente)) {
                            int quantitaUtente = Integer.parseInt(userAndQuantity[1]);
                            if (quantitaUtente >= quantita) {
                                // Aggiorna la quantità in prestito solo per l'utente corrente
                                int nuovaQuantitaUtente = quantitaUtente - quantita;
                                utenti[j] = utente + ":" + nuovaQuantitaUtente;
                                libroElement.setAttribute("utentePrestito", String.join(",", utenti));

                                // Aggiorna la quantità in prestito per il libro solo se è l'utente corrente
                                int quantitaInPrestito = Integer.parseInt(libroElement.getAttribute("quantita_in_prestito"));
                                libroElement.setAttribute("quantita_in_prestito", String.valueOf(quantitaInPrestito - quantita));

                                // Aggiorna la quantità disponibile per il libro
                                int quantitaDisponibile = Integer.parseInt(libroElement.getAttribute("quantita_disponibile"));
                                libroElement.setAttribute("quantita_disponibile", String.valueOf(quantitaDisponibile + quantita));

                                // Rimuovi l'utente dalla lista degli utenti in prestito solo se è l'utente corrente
                                if (nuovaQuantitaUtente == 0) {
                                    utentiPrestito = utentiPrestito.replace(userInfo + ",", "").replace("," + userInfo, "");
                                    libroElement.setAttribute("utentePrestito", utentiPrestito);
                                }

                                restituzioneAvvenuta = true; // Restituzione avvenuta con successo
                                break;
                            } else {
                                // Avvisiamo che il limite massimo di restituzione è stato superato
                                System.out.println("Hai superato il limite massimo di restituzione per questo libro.");
                                return false;
                            }
                        }
                    }
                }
                break; // Esci dal ciclo dopo aver gestito il libro corrente
            }
        }

        // Se la restituzione è avvenuta con successo, rendi l'archivio persistente
        if (restituzioneAvvenuta) {
            try {
                makeArchivePersistent();
                return true; // Restituzione avvenuta con successo
            } catch (TransformerException ex) {
                System.err.println("Errore durante la scrittura del file XML: " + ex.getMessage());
                return false; // Errore durante il salvataggio dell'archivio XML
            }
        } else {
            // Restituzione non riuscita
            System.out.println("Non hai abbastanza libri in prestito per restituire questa quantità.");
            return false;
        }
    }

    public ArrayList<String> getStoricoAzioniUtente(String utenteCorrente) {
        ArrayList<String> storico = new ArrayList<>();
        NodeList libroNodes = doc.getElementsByTagName("libro");
        for (int i = 0; i < libroNodes.getLength(); i++) {
            Element libroElement = (Element) libroNodes.item(i);
            String utenteAzione = libroElement.getAttribute("utentePrestito");
            if (utenteAzione.contains(utenteCorrente)) {
                String titoloLibro = libroElement.getAttribute("titolo");
                String dataPrestito = libroElement.getAttribute("data_prestito");
                String dataRestituzione = libroElement.getAttribute("data_restituzione");
                if (!dataPrestito.isEmpty()) {
                    storico.add("Libro: " + titoloLibro + ", Utente: " + utenteCorrente + ", Azione: Prestito, Data: " + dataPrestito);
                }
                if (!dataRestituzione.isEmpty()) {
                    storico.add("Libro: " + titoloLibro + ", Utente: " + utenteCorrente + ", Azione: Restituzione, Data: " + dataRestituzione);
                }
            }
        }

        // Se lo storico risulta vuoto, aggiungi un messaggio
        if (storico.isEmpty()) {
            storico.add("Nessun libro da restituire");
        }

        return storico;
    }

    public int countBooks() {
        NodeList libroNodes = doc.getElementsByTagName("libro");
        return libroNodes.getLength();
    }

    public int countBooksInPrestito(String utente) {
        NodeList libroNodes = doc.getElementsByTagName("libro");
        int count = 0;
        for (int i = 0; i < libroNodes.getLength(); i++) {
            Element libroElement = (Element) libroNodes.item(i);
            String utentiPrestito = libroElement.getAttribute("utentePrestito");
            if (utentiPrestito.contains(utente)) {
                // Conta solo se l'utente specificato è tra quelli che hanno preso in prestito il libro
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

    public int getQuantitaDisponibile(XMLArchive archive, String titoloLibro) {
        NodeList libroNodes = archive.getDocument().getElementsByTagName("libro");
        for (int i = 0; i < libroNodes.getLength(); i++) {
            Element libroElement = (Element) libroNodes.item(i);
            if (libroElement.getAttribute("titolo").equals(titoloLibro)) {
                return Integer.parseInt(libroElement.getAttribute("quantita_disponibile"));
            }
        }
        return 0; // Se il libro non viene trovato, restituisci 0
    }

    public int getQuantitaDisponibilePrestito(XMLArchive archive, String titoloLibro) {
        NodeList libroNodes = archive.getDocument().getElementsByTagName("libro");
        for (int i = 0; i < libroNodes.getLength(); i++) {
            Element libroElement = (Element) libroNodes.item(i);
            if (libroElement.getAttribute("titolo").equals(titoloLibro)) {
                return Integer.parseInt(libroElement.getAttribute("quantita_in_prestito"));
            }
        }
        return 0; // Se il libro non viene trovato, restituisci 0
    }

    public boolean checkIfBookExists(String title) {
        NodeList libroNodes = doc.getElementsByTagName("libro");
        for (int i = 0; i < libroNodes.getLength(); i++) {
            Element libroElement = (Element) libroNodes.item(i);
            if (libroElement.getAttribute("titolo").equals(title)) {
                return true;
            }
        }
        return false;
    }

    public void incrementQuantitaDisponibile(String title, int quantitaAggiuntiva) {
        NodeList libroNodes = doc.getElementsByTagName("libro");
        for (int i = 0; i < libroNodes.getLength(); i++) {
            Element libroElement = (Element) libroNodes.item(i);
            if (libroElement.getAttribute("titolo").equals(title)) {
                int quantitaAttuale = Integer.parseInt(libroElement.getAttribute("quantita_disponibile"));
                int quantitaAggiornata = quantitaAttuale + quantitaAggiuntiva;
                libroElement.setAttribute("quantita_disponibile", String.valueOf(quantitaAggiornata));

                // Chiamata al metodo per rendere l'archivio persistente
                try {
                    makeArchivePersistent();
                } catch (TransformerException ex) {
                    System.err.println("Errore durante la scrittura del file XML: " + ex.getMessage());
                }

                break; // Esci dal ciclo una volta aggiornata la quantità disponibile
            }
        }
    }

    public int getMaxRestituzioneForUser(String username, int quantitaInPrestito) {
        NodeList libroNodes = doc.getElementsByTagName("libro");
        for (int i = 0; i < libroNodes.getLength(); i++) {
            Element libroElement = (Element) libroNodes.item(i);
            String utentiPrestito = libroElement.getAttribute("utentePrestito");
            if (utentiPrestito.contains(username)) {
                String[] userInfo = utentiPrestito.split(",");
                for (String info : userInfo) {
                    String[] userAndQuantity = info.split(":");
                    if (userAndQuantity[0].equals(username)) {
                        int quantitaPosseduta = Integer.parseInt(userAndQuantity[1]);
                        return Math.min(quantitaInPrestito, quantitaPosseduta);
                    }
                }
            }
        }
        // Se l'utente non ha preso in prestito alcun libro, restituisci 0
        return 0;
    }

    public String getCurrentBooksUtentiPrestito() {
        StringBuilder utentiPrestito = new StringBuilder();

        NodeList nodeList = doc.getElementsByTagName("libro");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element elementoLibro = (Element) node;
                String utentePrestito = elementoLibro.getAttribute("utentePrestito");
                if (!utentePrestito.isEmpty()) {
                    if (utentiPrestito.length() > 0) {
                        utentiPrestito.append(",");
                    }
                    utentiPrestito.append(utentePrestito);
                }
            }
        }

        return utentiPrestito.toString();
    }

    public int quantiLibriPossoRestituire(String username) {
        int quantiLibriPossoRestituire = 0;
        NodeList libriList = doc.getElementsByTagName("libro");
        for (int i = 0; i < libriList.getLength(); i++) {
            Element libro = (Element) libriList.item(i);
            String utentiPrestitoString = libro.getAttribute("utentePrestito");
            if (!utentiPrestitoString.isEmpty()) {
                String[] utentiPrestito = utentiPrestitoString.split(",");
                for (String utentePrestito : utentiPrestito) {
                    String[] infoPrestito = utentePrestito.split(":");
                    String utente = infoPrestito[0];
                    if (utente.equals(username)) {
                        quantiLibriPossoRestituire = Integer.parseInt(infoPrestito[1]);
                    }
                }
            }
        }
        return quantiLibriPossoRestituire;
    }

}
