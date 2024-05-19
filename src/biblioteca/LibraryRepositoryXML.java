package biblioteca;

import java.io.File;
import java.util.ArrayList;
import java.io.IOException;
import java.util.List;
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

public class LibraryRepositoryXML implements LibraryRepository {

    private Document doc;
    private String archivePath;

    public LibraryRepositoryXML(String path) {

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
                doc.appendChild(doc.createElement("books"));
            }
        } catch (ParserConfigurationException | IOException | org.xml.sax.SAXException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Book> getBooks() {
        List<Book> books = new ArrayList<Book>();
        NodeList bookNodes = doc.getElementsByTagName("book");
        for (int i = 0; i < bookNodes.getLength(); i++) {
            Element bookElement = (Element) bookNodes.item(i);
            String title = bookElement.getAttribute("title");
            String author = bookElement.getAttribute("author");
            String genre = bookElement.getAttribute("genre");
            int year = Integer.parseInt(bookElement.getAttribute("year"));
            String publisher = bookElement.getAttribute("publisher");

            Book book = new Book(title, author, publisher, genre, year);
            books.add(book);
        }
        return books;
    }

    @Override
    public List<String> getBookTitles() {
        List<String> bookTitles = new ArrayList<String>();
        NodeList bookNodes = doc.getElementsByTagName("book");
        for (int i = 0; i < bookNodes.getLength(); i++) {
            Element bookElement = (Element) bookNodes.item(i);
            String title = bookElement.getAttribute("title");
            bookTitles.add(title);
        }
        return bookTitles;
    }

    @Override
    public String getBookTitlesAsString()throws Exception {
        List<String> bookTitles = getBookTitles();
        if (bookTitles.isEmpty()) {
        throw new Exception();
    }
        StringBuilder sb = new StringBuilder();
        for (String title : bookTitles) {
            sb.append("Libro: ").append(title).append(", numero di copie: ").append(getBookQuantity(title)).append("\n"); // Aggiungi ogni titolo seguito da un a capo
        }
        return sb.toString();
    }

    @Override
    public int getBookQuantity(String title) {
        NodeList bookNodes = doc.getElementsByTagName("book");

        for (int i = 0; i < bookNodes.getLength(); i++) {
            Element bookElement = (Element) bookNodes.item(i);
            if (bookElement.getAttribute("title").equals(title)) {
                //Se il libro esiste già, aumenta semplicemente la quantità disponibile
                int currentQuantity = Integer.parseInt(bookElement.getAttribute("quantity"));
                return currentQuantity;
            }
        }
        return 0;
    }

    @Override
    public void addOrUpdateBook(Book book, int quantity) {
        NodeList bookNodes = doc.getElementsByTagName("book");

        for (int i = 0; i < bookNodes.getLength(); i++) {
            Element bookElement = (Element) bookNodes.item(i);
            if (bookElement.getAttribute("title").equals(book.getTitle())) {
                //Se il libro esiste già, aumenta semplicemente la quantità disponibile
                int currentQuantity = Integer.parseInt(bookElement.getAttribute("quantity"));
                bookElement.setAttribute("quantity", String.valueOf(currentQuantity + quantity));
                try {
                    this.makeArchivePersistent();
                } catch (TransformerException ex) {
                    Logger.getLogger(LibraryRepositoryXML.class.getName()).log(Level.SEVERE, null, ex);
                }
                return;
            }
        }

        Element booksElement = doc.getDocumentElement();
        Element newBookElement = doc.createElement("book");
        newBookElement.setAttribute("title", book.getTitle());
        newBookElement.setAttribute("author", book.getAuthor());
        newBookElement.setAttribute("publisher", book.getPublisher());
        newBookElement.setAttribute("genre", book.getGenre());
        newBookElement.setAttribute("year", String.valueOf(book.getYear()));
        newBookElement.setAttribute("quantity", String.valueOf(quantity));
        booksElement.appendChild(newBookElement);
        try {
            this.makeArchivePersistent();
        } catch (TransformerException ex) {
            Logger.getLogger(LibraryRepositoryXML.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateQuantity(String title, int quantity) {
        NodeList bookNodes = doc.getElementsByTagName("book");
        for (int i = 0; i < bookNodes.getLength(); i++) {
            Element bookElement = (Element) bookNodes.item(i);
            if (bookElement.getAttribute("title").equals(title)) {
                //Se il libro esiste già, aumenta semplicemente la quantità disponibile
                int currentQuantity = Integer.parseInt(bookElement.getAttribute("quantity"));
                bookElement.setAttribute("quantity", String.valueOf(currentQuantity + quantity));
                try {
                    this.makeArchivePersistent();
                } catch (TransformerException ex) {
                    Logger.getLogger(LibraryRepositoryXML.class.getName()).log(Level.SEVERE, null, ex);
                }
                return;
            }
        }
    }

    @Override
    public Book getBook(String title) throws Exception {
        NodeList bookNodes = doc.getElementsByTagName("book");

        for (int i = 0; i < bookNodes.getLength(); i++) {
            Element bookElement = (Element) bookNodes.item(i);
            if (bookElement.getAttribute("title").equals(title)) {
                //Se il libro esiste già, aumenta semplicemente la quantità disponibile
                String author = bookElement.getAttribute("author");
                String genre = bookElement.getAttribute("genre");
                int year = Integer.parseInt(bookElement.getAttribute("year"));
                String publisher = bookElement.getAttribute("publisher");

                Book book = new Book(title, author, publisher, genre, year);
                return book;
            }
        }
        throw new Exception();
    }

    @Override
    public boolean deleteBook(String title) {
        NodeList bookNodes = doc.getElementsByTagName("book");
        for (int i = 0; i < bookNodes.getLength(); i++) {
            Element bookElement = (Element) bookNodes.item(i);
            if (bookElement.getAttribute("title").equals(title)) {
                bookElement.getParentNode().removeChild(bookElement);
                try {
                    this.makeArchivePersistent();
                } catch (TransformerException ex) {
                    Logger.getLogger(LibraryRepositoryXML.class.getName()).log(Level.SEVERE, null, ex);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void searchBook(String title) throws Exception {

        NodeList bookNodes = doc.getElementsByTagName("book");
        for (int i = 0; i < bookNodes.getLength(); i++) {
            Element bookElement = (Element) bookNodes.item(i);
            if (bookElement.getAttribute("title").equals(title)) {
                String author = bookElement.getAttribute("author");
                String genre = bookElement.getAttribute("genre");
                String publisher = bookElement.getAttribute("publisher");
                int year = Integer.parseInt(bookElement.getAttribute("year"));
                int quantity = Integer.parseInt(bookElement.getAttribute("quantity"));
                System.out.println("Titolo: " + title + "\nAutore: " + author + "\nGenere: " + genre + "\nCasa editrice: " + publisher + "\nAnno di pubblicazione: " + year + "\nCopie disponibili: " + quantity + "\n");
                return;
            }
        }
        throw new Exception();
    }

    @Override
    public void hasBook(String title
    ) {
        throw new UnsupportedOperationException("Not supported yet.");

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
