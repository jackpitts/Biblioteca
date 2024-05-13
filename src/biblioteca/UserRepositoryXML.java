package biblioteca;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class UserRepositoryXML implements UserRepository {

    private Document doc;
    private String archivePath;

    public UserRepositoryXML(String path) {

        archivePath = path;

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            File file = new File(archivePath);
            if (file.exists()) {
                doc = dBuilder.parse(file);
                doc.getDocumentElement().normalize();
            } else {
                doc = dBuilder.newDocument();
                doc.appendChild(doc.createElement("users"));
            }
        } catch (ParserConfigurationException | IOException | org.xml.sax.SAXException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addUser(User user) {
        NodeList userNodes = doc.getElementsByTagName("user");

        for (int i = 0; i < userNodes.getLength(); i++) {
            Element userElement = (Element) userNodes.item(i);
            if (userElement.getAttribute("name").equals(user.getName())) {
                return;
            }
        }

        Element usersElement = doc.getDocumentElement();
        Element newUserElement = doc.createElement("user");
        newUserElement.setAttribute("name", user.getName());
        newUserElement.setAttribute("password", user.getPassword());
        usersElement.appendChild(newUserElement);
        try {
            this.makeArchivePersistent();
        } catch (TransformerException ex) {
            Logger.getLogger(LibraryRepositoryXML.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public User getUser(String name) throws Exception {
        NodeList userNodes = doc.getElementsByTagName("user");

        for (int i = 0; i < userNodes.getLength(); i++) {
            Element userElement = (Element) userNodes.item(i);
            if (userElement.getAttribute("name").equals(name)) {
                String n = userElement.getAttribute("name");
                String password = userElement.getAttribute("password");

                User user = new User(name, password);
                return user;
            }
        }
        throw new Exception();
    }

    private void makeArchivePersistent() throws TransformerException {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(archivePath));
            transformer.transform(source, result);
        } catch (TransformerException e) {
            e.printStackTrace();
            throw e;
        }
    }

}
