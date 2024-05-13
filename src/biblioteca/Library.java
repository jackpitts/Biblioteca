package biblioteca;

import java.util.*;

class Library {

    private List<Book> books;
    private Map<User, List<Book>>borrowedBooks ;

    public Library() {
        this.books = new ArrayList<>();
        this.borrowedBooks = new HashMap<>();
    }
    
}
