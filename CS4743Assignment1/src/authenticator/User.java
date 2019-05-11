package authenticator;

public class User 
{
	private String name;
	private String username;
	private String password;
	
	public User (){
		
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
	
	

}
