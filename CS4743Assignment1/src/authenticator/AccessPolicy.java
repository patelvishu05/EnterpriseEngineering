package authenticator;

/**
 * Access Policy class helps defines different levels of
 * access policies for the potntial users using our system
 * 
 * @author Vishalkumar Patel
 * @author Juan-Diaz Sada
 *
 */
import java.util.HashMap;

public class AccessPolicy 
{
	public static final String ADMIN = "admin";
	public static final String DATA_ENTRY = "dataEntry";
	public static final String INTERN = "intern";
	
	public HashMap<String,HashMap<String,Boolean>> policies;

	public AccessPolicy()
	{
		this.policies = new HashMap<String,HashMap<String,Boolean>>();
		createUserPolicy("default",false,false,false);
	}
	
	/**
	 * createUserPolicy helps create policy for the given user with
	 * various provided access definitions
	 * @param login
	 * @param entries
	 */
	public void createUserPolicy(String login, boolean ... entries)
	{
		HashMap<String, Boolean> userTable = new HashMap<String, Boolean>();

		userTable.put(ADMIN, entries[0]);
		userTable.put(DATA_ENTRY, entries[1]);
		userTable.put(INTERN, entries[2]);

		policies.put(login, userTable);
	}
	
	/**
	 * canUserAccess function tells us whether a particular
	 * user access to specific feature in our system or not
	 * 
	 * @param username
	 * @param features
	 * @return
	 */
	public boolean canUserAccess(String username, String features)
	{
		if(!this.policies.containsKey("default"))
			return false;
		HashMap<String,Boolean> userTable = this.policies.get("default");
		
		if(this.policies.containsKey(username))
			userTable = this.policies.get(username);
	
		if(userTable.containsKey(features))
			return userTable.get(features);
		
		return false;
	}

	//-----------------ACCESSORS--------------//
	
	public HashMap<String, HashMap<String, Boolean>> getPolicies() {
		return policies;
	}

	public void setPolicies(HashMap<String, HashMap<String, Boolean>> policies) {
		this.policies = policies;
	}
}	//end of AccessPolicy class
