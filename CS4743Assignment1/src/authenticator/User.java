package authenticator;

/**
 * User class is a model to our Authenticator system
 * for defining User specific attributes to the auth system
 * 
 * @author Vishalkumar Patel
 * @author Juan-Diaz Sada
 */
public class User 
{
	private String name;
	private String username;
	private String password;
	
	public User () {
		
	}
	
	public User(String n, String uid, String pass) 
	{
		this.name = n;
		this.username = uid;
		this.password = Encryptor.encryptSHA(pass);
	}
	
	public String toString() {
		return this.name + "\t" + this.username + "\t" + this.password;
	}

	//---------------ACCESSORS-------------------//
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = Encryptor.encryptSHA(password);
	}

}	//end of class User
