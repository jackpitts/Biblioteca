package biblioteca;

import java.util.List;
enum UserAction {
  prestito,
  restituzione
}

interface UserRepository {
    
    public void addUser(User user)throws Exception;
    public User getUser(String name) throws Exception;
    public void addBook(User user, String title, int quantity);   
    public void returnBook(User user, String title, int quantity)throws Exception;
    public int getBookQuantity(User user, String title);
    public List<String> getBookTitles(User user);
    public String getBookTitlesAsString(User user);
    public void addHistory(User user, String title, int quantity, UserAction userAction);
    public List<History> getHistory(User user);
    
}
