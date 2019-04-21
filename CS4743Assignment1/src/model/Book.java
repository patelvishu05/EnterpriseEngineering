package model;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import controller.MainController;
import database.BookGateway;
import exception.DBException;

/**
 * Book model
 *  
 * @author Vishalkumar Patel
 * @author Juan-Diaz Sada
 *
 */
public class Book 
{
	private int id;
	private String title;
	private String summary;
	private int year;
	private String ISBN;
	private int publisher;
	private LocalDateTime lastModified; 

	public Book() {
		this.id = 0;
		this.title = "";
		this.summary = "";
		this.year = 0;
		this.ISBN = "";
		this.publisher = 0;
		
	}

	public Book(int id, String t, String s, int y, int p, String i)
	{
		this.id = id;
		this.title = t;
		this.summary = s;
		this.year = y;
		this.publisher = p;
		this.ISBN = i;
		this.lastModified =  null; 
	}

	public String toString() {
		return title; 
	}
	
	//FOR DEBUG PURPOSE ONLY : Custom print
	public String something() {
		return this.title + "\t" + this.id + "\t" + this.summary + "\t" + this.year + "\t" 
				+ this.publisher + "\t" + this.lastModified;
	}

	/**
	 * checks if values for two books are equal or not and based on the result
	 * returns a true or false value
	 * @param book1
	 * @param book2
	 * @return true or false
	 */
	public static boolean equalsBook(Book b1, Book b2)
	{
		return 	((b1.getId() == b2.getId() ) &&
				(b1.getTitle().equals(b2.getTitle()) ) &&
				(b1.getSummary().equals(b2.getSummary()) ) &&
				(b1.getYear() == b2.getYear() ) &&
				(b1.getISBN().equals(b2.getISBN())) &&
				(b1.getPublisher() == b2.getPublisher()));
	}	//end of equalsBook method
	
	/**
	 * getChanges checks for any values changed for the book and if
	 * so it returns it for saving it to the audit trails
	 * @param book1
	 * @param book2
	 * @return
	 */
	public static String getChanges(Book b1, Book b2)
	{
		String ret="";
		if(b1.getId()!= b2.getId())
			ret += "Book Id changed from " + b2.getId() + " to " + b1.getId() + "\n";
		if(!(b1.getTitle().equals(b2.getTitle())))
			ret += "Book Title changed from " + b2.getTitle() + " to " + b1.getTitle() + "\n";
		if(!(b1.getSummary().equals(b2.getSummary())))
			ret += "Book Summary changed from " + b2.getSummary() + " to " + b1.getSummary() + "\n";
		if(b1.getYear() != b2.getYear())
			ret += "Book Year changed from " + b2.getYear() + " to " + b1.getYear() + "\n";
		if(!(b1.getISBN().equals(b2.getISBN())))
			ret += "Book ISBN changed from " + b2.getISBN() + " to " +b1.getISBN() + "\n";
		if(b1.getPublisher() != b2.getPublisher())
			ret += "Book Publisher changed from " + b2.getPublisher() + " to " + b1.getPublisher() + "\n";
		return ret;
	}	//end of getChanges method

	/**
	 * save method helps us save book to the database by asking the BookGateway to do it
	 * @param l
	 * @param book
	 * @throws DBException
	 * @throws SQLException
	 */
	public void save(Book l, Book book) throws DBException, SQLException
	{
		if(!isValidTitle())
			throw new DBException(DBException.getInvalidTitleMessage());

		if(!isValidSummary())
			throw new DBException(DBException.getInvalidSummaryMessage());

		if(!isValidYear())
			throw new DBException(DBException.getInvalidYearMessage());

		if(!isValidISBN())
			throw new DBException(DBException.getInvalidISBNMessage());

		MainController.auditChange = getChanges(book,l);
		ArrayList<Integer> primaryKeys = new ArrayList<Integer>();
		for(Book b : BookGateway.getInstance().getBooks())
			primaryKeys.add(b.getId());
		if(!(primaryKeys.contains(book.getId()))) 
		{
			System.out.println("1" + l + "\t" + book);
			BookGateway.getInstance().deleteBook(l);
			BookGateway.getInstance().insertBook(book);
		}
		else 
		{
			System.out.println("2" + l + "\t" + book);
			BookGateway.getInstance().updateBook(this);
		}
	}	//end of save method

	//---------------------ACCESSORS---------------------//
	public List<AuditTrailEntry> getAuditTrail()
	{
		return BookGateway.getInstance().getAudit(this);
	}
	
	public List<AuthorBook> getAuthors() 
	{
		return BookGateway.getInstance().getAuthorsForBook(this);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getISBN() {
		return ISBN;
	}

	public void setISBN(String iSBN) {
		ISBN = iSBN;
	}

	public int getPublisher() {
		return publisher;
	}

	public void setPublisher(int publisher) {
		this.publisher = publisher;
	}

	public void setLastModified(LocalDateTime lastModified) {	
		this.lastModified = lastModified; 	
	}
	public LocalDateTime getLastModified() {
		return this.lastModified; 
	}

	//------------VALIDATORS-------------//
	public boolean isValidTitle() {
		return this.title.length() >= 1 && this.title.length() < 255;
	}

	public boolean isValidSummary() {
		return (this.summary.length() < 65536) || this.summary.isEmpty();
	}

	public boolean isValidYear() {
		return this.year >= 1455 && this.year <= 2019;
	}

	public boolean isValidISBN() {
		return this.ISBN.length() <= 13 || this.ISBN.equals("");
	}

}	//end of Book Class
