package biblioteca;

public class History {
    
   private String title;
   private int quantity;
   private UserAction userAction;
   private String date;

    public History(String title, int quantity, UserAction userAction, String date) {
        this.title = title;
        this.quantity = quantity;
        this.userAction = userAction;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setBookTitle(String bookTitle) {
        this.title = title;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public UserAction getUserAction() {
        return userAction;
    }

    public void setUserAction(UserAction userAction) {
        this.userAction = userAction;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
