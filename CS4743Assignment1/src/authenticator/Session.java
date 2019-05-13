package authenticator;

/**
 * Session is a model for defining a session for
 * our authentication system
 * 
 * @author Vishalkumar Patel
 * @author Juan-Diaz Sada
 */
public class Session 
{
	public static int nextId = 1;
	private int sessionId;
	private User sessionUser;
	
	public Session(User user)
	{
		this.sessionUser = user;
		this.sessionId = nextId++;
	}
	
	//--------------------ACCESSORS-----------------------//

	public int getSessionId() {
		return sessionId;
	}

	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}

	public User getSessionUser() {
		return sessionUser;
	}

	public void setSessionUser(User sessionUser) {
		this.sessionUser = sessionUser;
	}
	
}	//end of class Session
