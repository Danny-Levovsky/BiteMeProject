package entites;

import java.io.Serializable;



//Class based on DB's Table

public class User implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    private int id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String type;
    private String district;
    private int isLoggedIn;
    
    
    //constructor1
    public User(int id, String username, String password, String firstName, String lastName, 
            String email, String phone, String type, int isLoggedIn, String district) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.phone = phone;
    this.type = type;
    this.isLoggedIn = isLoggedIn;
    this.district = district;
    }
    
    //constructor2
    public User(String username, String password) {
    	this.username = username;
    	this.password = password;
    }
    

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
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

    public int getIsLoggedIn() {
        return isLoggedIn;
    }
    
    public String getDistrict() {
    	return district;
    }
}