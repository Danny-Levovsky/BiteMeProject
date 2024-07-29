package tempForMini;

//Class based on DB's Table

public class User {
    private int id;
    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String type;
    private boolean isLoggedIn;
    
    public User(int id, String userName, String password, String firstName, String lastName, 
            String email, String phone, String type, boolean isLoggedIn) {
    this.id = id;
    this.userName = userName;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.phone = phone;
    this.type = type;
    this.isLoggedIn = isLoggedIn;
    }

    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getType() {
        return type;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }
}