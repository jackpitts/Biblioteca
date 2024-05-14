package biblioteca;

interface UserRepository {
    
    public void addUser(User user);
    public User getUser(String name) throws Exception;
    public void addBook(User user, String title, int quantity);   
}
