package biblioteca;

import java.util.List;

interface LibraryRepository{
    
    public List<Book> getBooks();
    public void addOrUpdateBook(Book book, int quantity);
    public boolean deleteBook(String title);
    public void hasBook(String title);
    public int getBookQuantity(String title);
    public Book getBook(String title) throws Exception;
    public void updateQuantity(String title, int quantity);
    public List<String> getBookTitles();
    public String getBookTitlesAsString()throws Exception;
    public void searchBook(String title) throws Exception;
    
}
