package entites;

import java.io.Serializable;



/**
 * The User class represents a user with various attributes such as ID, username, password, 
 * first name, last name, email, phone, type, login status, and district.
 * This class implements Serializable to allow its objects to be serialized.
 * 
 * @author yosra
 */
public class User implements Serializable {
	
	/**
     * A unique identifier for the serialization runtime to associate with the User class.
     * This value ensures that a deserialized object is compatible with the current class definition.
     */
	private static final long serialVersionUID = 1L;
	
	/**
     * The user ID.
     */
    private int id;
    
    /**
     * The username of the user.
     */
    private String username;
    
    /**
     * The password of the user.
     */
    private String password;
    
    /**
     * The first name of the user.
     */
    private String firstName;
    
    /**
     * The last name of the user.
     */
    private String lastName;
    
    /**
     * The email address of the user.
     */
    private String email;
    
    /**
     * The phone number of the user.
     */
    private String phone;
    
    /**
     * The type of the user (e.g., customer, employee).
     */
    private String type;
    
    /**
     * The district associated with the user.
     */
    private String district;
    
    /**
     * The login status of the user (1 if logged in, 0 otherwise).
     */
    private int isLoggedIn;
    
    
    /**
     * Constructor to initialize a User object with all attributes.
     * 
     * @param id the user ID
     * @param username the username
     * @param password the password
     * @param firstName the first name
     * @param lastName the last name
     * @param email the email address
     * @param phone the phone number
     * @param type the user type
     * @param isLoggedIn the login status (1 if logged in, 0 otherwise)
     * @param district the district
     */
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
    
    /**
     * Constructor to initialize a User object with username and password.
     * 
     * @param username the username
     * @param password the password
     */
    public User(String username, String password) {
    	this.username = username;
    	this.password = password;
    }
    
    /**
     * Constructor to initialize a User object with only the user ID.
     * 
     * @param id the user ID
     */
    public User(int id) {
    	this.id = id;
    }

    /**
     * Gets the user ID.
     * 
     * @return the user ID
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the username.
     * 
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the password.
     * 
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets the first name.
     * 
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Gets the last name.
     * 
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Gets the email address.
     * 
     * @return the email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets the phone number.
     * 
     * @return the phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Gets the user type.
     * 
     * @return the user type
     */
    public String getType() {
        return type;
    }

    /**
     * Gets the login status.
     * 
     * @return the login status (1 if logged in, 0 otherwise)
     */
    public int getIsLoggedIn() {
        return isLoggedIn;
    }
    
    /**
     * Gets the district.
     * 
     * @return the district
     */
    public String getDistrict() {
    	return district;
    }
}
