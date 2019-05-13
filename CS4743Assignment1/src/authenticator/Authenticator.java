package authenticator;

/**
 * Authenticator class helps us authenticate a user 
 * and tells us whether they have access to specific
 * areas in our awesome application
 * 
 * @author Vishalkumar Patel
 * @author Juan-Diaz Sada
 */
import java.util.ArrayList;
import java.util.List;

public class Authenticator 
{
	private List<User> userList;
	private List<Session> sessionList;
	private AccessPolicy policy;
	public static String loggedUserName;
	
	public Authenticator()
	{
		this.sessionList = new ArrayList<Session>();
		this.policy = new AccessPolicy();
		this.userList = allowedUsers(); 
	}
	
	/**
	 * validateLogin checks to see if username and password matches
	 * with the ones in our system and accordingly initiates a session
	 * if it is valid
	 * 
	 * @param username
	 * @param password
	 * @return int
	 */
	public int validateLogin(String username, String password)
	{
		for(User user : userList)
		{
			if(user.getUsername().equals(username) && user.getPassword().equals(password))
			{
				loggedUserName = user.getName();
				Session session = new Session(user);
				sessionList.add(session);
				return session.getSessionId();
			}
		}
		return 0;
	}
	
	/**
	 * logout logs the user out of the application
	 * @param sessionId
	 */
	public void logout (int sessionId)
	{
		for (int i = sessionList.size() - 1; i>=0; i-- ) {
			Session s = sessionList.get(i);
			if(s.getSessionId() == sessionId)
				sessionList.remove(i);
		}
	}
	
	/**
	 * allowedUsers adds in allowed users to the HashMao which
	 * can be later used to see for potential existing users
	 * in our system to verify their credentials
	 * 
	 * @return ArrayList<User>
	 */
	public ArrayList<User> allowedUsers()
	{
		ArrayList<User> userList = new ArrayList<User>();
		
		User wilma = new User("Wilma Williams","wilma","arugula");
		userList.add(wilma);
		this.policy.createUserPolicy(wilma.getUsername(),true,false,false);
		
		User leroy = new User("Leroy Jenkins","leroy","wipeout");
		userList.add(leroy);
		this.policy.createUserPolicy(leroy.getUsername(), false,true,false);
		
		User sasquatch = new User("Sasquatch Jones","sasquatch","jerky");
		userList.add(sasquatch);
		this.policy.createUserPolicy(sasquatch.getUsername(), false,false,true);
		
		return userList;
	}
	
	/**
	 * getUserNameFromSessionId helps get user name from the current
	 * session id
	 * 
	 * @param sessionId
	 * @return String
	 */
	public String getUserNameFromSessionId(int sessionId)
	{
		if (sessionId == 0)
			return "Not logged in";
		for(Session s : sessionList)
		{
			if(s.getSessionId() == sessionId)
				return s.getSessionUser().getUsername();
		}
		return "";
	}
	
	/**
	 * canAccess function tells us whether a particular
	 * session can access can access specific features of
	 * our app or not
	 * 
	 * @param sessionId
	 * @param features
	 * @return
	 */
	public boolean hasAccess(int sessionId, String features)
	{
		for(Session s : sessionList)
		{
			if(s.getSessionId() == sessionId)
				return this.policy.canUserAccess(s.getSessionUser().getUsername(), features);
		}
		return false;
	}

}	//end of class Authenticator
