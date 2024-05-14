package biblioteca;

import java.util.logging.Level;
import java.util.logging.Logger;

public class UserService {

    UserRepository userRepo;

    public UserService(UserRepository repo) {
        this.userRepo = repo;
    }

    public void addUser(String name, String password) {
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
    
    public void addBook(User user, String title, int quantity){
        this.userRepo.addBook(user, title, quantity);
    }
}
