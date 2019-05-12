package authenticator;

import java.util.ArrayList;
import java.util.List;

public class Authenticator 
{
	private List<User> userList;
	private List<Session> sessionList;
	private AccessPolicy policy;
	
	public Authenticator()
	{
		this.sessionList = new ArrayList<Session>();
		this.policy = new AccessPolicy();
		this.userList = allowedUsers(); 
	}
	
	public int validateLogin(String username, String password)
	{
		for(User user : userList)
		{
			if(user.getUsername().equals(username) && user.getPassword().equals(password))
			{
				Session session = new Session(user);
				sessionList.add(session);
				return session.getSessionId();
			}
		}
		return 0;
	}
	
	public void logout (int sessionId)
	{
		for (int i = sessionList.size() - 1; i>=0; i-- ) {
			Session s = sessionList.get(i);
			if(s.getSessionId() == sessionId)
				sessionList.remove(i);
		}
	}
	
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
	
	public boolean hasAccess(int sessionId, String features)
	{
		for(Session s : sessionList)
		{
			if(s.getSessionId() == sessionId)
				return this.policy.canUserAccess(s.getSessionUser().getUsername(), features);
		}
		return false;
	}

}
