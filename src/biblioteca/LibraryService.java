package biblioteca;

import java.util.List;

public class LibraryService {

    private LibraryRepository libraryRepo;

    public LibraryService(LibraryRepository repo) {
        this.libraryRepo = repo;
    }

    public void addOrUpdateBook(String title, String author, String publisher, String genre, int year, int quantity) {
        Book book = new Book(title, author, publisher, genre, year);
        this.libraryRepo.addOrUpdateBook(book, quantity);
    }

    public List<Book> getBooks() {
        return this.libraryRepo.getBooks();
    }

    public int countBooks() {
        return this.getBooks().size();
    }

    public boolean hasBook(String title) {
        return this.libraryRepo.getBookQuantity(title) > 0;
    }

    public Book getBook(String title) throws Exception {
        return this.libraryRepo.getBook(title);
    }

    public void removeQuantities(String title, int quantity) {
        int currentQuantity = this.libraryRepo.getBookQuantity(title);
        if (quantity >= currentQuantity) {
            this.libraryRepo.deleteBook(title);
        } else {
            this.libraryRepo.updateQuantity(title, -quantity);
        }
    }

    public void borrow(String title, int quantity) throws Exception {

        if (quantity <= this.libraryRepo.getBookQuantity(title)) {
            this.libraryRepo.updateQuantity(title, -quantity);
            return;
        }
        throw new Exception();
    }

    public int getBookQuantity(String title) {
        return this.libraryRepo.getBookQuantity(title);
    }

    public List<String> getBookTitles() {
        return this.libraryRepo.getBookTitles();
    }

    public String getBookTitlesAsString() throws Exception {
        return this.libraryRepo.getBookTitlesAsString();
    }

    public void searchBook(String title) throws Exception {
        this.libraryRepo.searchBook(title);
    }

    public void updateQuantity(String title, int quantity) {
        this.libraryRepo.updateQuantity(title, quantity);
    }
}