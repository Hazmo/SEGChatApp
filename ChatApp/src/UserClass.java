public class UserClass {
    String userName;
    boolean isAdmin;

    public UserClass(String userName, boolean isAdmin) {
        this.userName = userName;
        this.isAdmin = isAdmin;
    }

    @Override
    public String toString() {
        return userName;
    }
    
    
}
