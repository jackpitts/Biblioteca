package biblioteca;

import java.util.List;

interface UserRepository {
    
    public void addUser(User user);
    public User getUser(String name) throws Exception;
    public void addBook(User user, String title, int quantity);   
    public void returnBook(User user, String title, int quantity)throws Exception;
    public int getBookQuantity(User user, String title);
    public List<String> getBookTitles(User user);
    public String getBookTitlesAsString(User user);

}
