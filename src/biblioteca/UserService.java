package biblioteca;

import java.util.List;

public class UserService {

    UserRepository userRepo;

    public UserService(UserRepository repo) {
        this.userRepo = repo;
    }

    public void addUser(String name, String password)throws Exception {
        User user = new User(name, password);
        this.userRepo.addUser(user);
    }

    public User authUser(String name, String password) throws Exception {
        User user = this.userRepo.getUser(name);
        if (password.equals(user.getPassword())) {
            return user;
        }
        throw new Exception();
    }

    public void addBook(User user, String title, int quantity) {
        this.userRepo.addBook(user, title, quantity);
        this.userRepo.addHistory(user, title, quantity, UserAction.prestito);
    }

    public void returnBook(User user, String title, int quantity) throws Exception {
        this.userRepo.returnBook(user, title, quantity);
        this.userRepo.addHistory(user, title, quantity, UserAction.restituzione);
    }

    public int getBookQuantity(User user, String title) {
        return this.userRepo.getBookQuantity(user, title);
    }

    public List<String> getBookTitles(User user) {
        return this.userRepo.getBookTitles(user);
    }

    public String getBookTitlesAsString(User user) {
        return this.userRepo.getBookTitlesAsString(user);
    }
    
    public List<History> getHistory(User user){
        return  this.userRepo.getHistory(user);
    }

}
