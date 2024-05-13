package biblioteca;

import java.util.List;

public class LibraryService {
        
    private LibraryRepository libraryRepo;

    public LibraryService(LibraryRepository repo) {
        this.libraryRepo = repo;
    }
    
    public void addOrUpdateBook(String title, String author, String publisher, String genre, int year, int quantity){
        
        Book book = new Book(title, author, publisher, genre, year);
        this.libraryRepo.addOrUpdateBook(book, quantity);
        
    }
    
    public List<Book> getBooks() {
    
        return this.libraryRepo.getBooks();
    }
    
    public boolean hasBook(String title){
        return this.libraryRepo.getBookQuantity(title) > 0;
    }
    
    public Book getBook(String title) throws Exception {
        return this.libraryRepo.getBook(title);
    }
    
    public void removeQuantities(String title, int quantity){
        int currentQuantity = this.libraryRepo.getBookQuantity(title);
        if (quantity >= currentQuantity){
            this.libraryRepo.deleteBook(title);
        } else {
            this.libraryRepo.updateQuantity(title, -quantity);
        }
    }
    
}
