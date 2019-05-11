package authenticator;

import java.nio.charset.Charset;
import java.security.MessageDigest;

public class Encryptor 
{
	final protected static char[] hexArray = "0123456789abcdef".toCharArray();
	
	public static String bytesToHex(byte[] bytes) 
	{
	    char[] hexChars = new char[bytes.length * 2];
	    int v;
	    for ( int j = 0; j < bytes.length; j++ ) 
	    {
	        v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}
	
	public static String encryptSHA(String msg) 
	{
		if(msg == null || msg.length() < 1)
			return null;
		String h = null;

		try 
		{
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(msg.getBytes(Charset.forName("UTF-8")));
			byte[] hash = md.digest();

			h = bytesToHex(hash);
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return h;
	}
}
