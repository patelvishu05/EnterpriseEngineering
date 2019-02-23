package exception;

public class DBException extends Exception 
{
	private String errorMessage;
	
	public DBException(String error) {
		this.errorMessage = error;
	}
	
	public DBException(Exception e) {
		super(e);
	}
	
	public String getErrorMessage() {
		return this.errorMessage;
	}
	
	public static String getInvalidSummaryMessage() {
		return "Not a valid Summary.\nValid summary length should be less than 65536 or it can be empty";
	}
	
	public static String getInvalidTitleMessage() {
		return "Not a valid Title.\nValid Title length should be > 1 and < 255 characters.";
	}
	
	public static String getInvalidYearMessage() {
		return "Not a valid Year.\nValid Year should be in range from 1455 to 2019";
	}
	
	public static String getInvalidISBNMessage() {
		return "Not a valid ISBN.\nValid ISBN should be less than 13 characters or it can be empty";
	}
	
}
