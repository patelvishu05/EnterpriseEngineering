package model;

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
	
	public Book() {
		
	}
	
	public Book(int id, String t, String s, int y, int p, String i)
	{
		this.id = id;
		this.title = t;
		this.summary = s;
		this.year = y;
		this.publisher = p;
		this.ISBN = i;
	}

	public String toString() {

		return title; 
	}

	public void save() throws DBException 
	{
		if(!isValidTitle(this.getTitle()))
			throw new DBException(DBException.getInvalidTitleMessage());
		
		if(!isValidSummary(this.getSummary()))
			throw new DBException(DBException.getInvalidSummaryMessage());
		
		if(!isValidYear(this.getYear()))
			throw new DBException(DBException.getInvalidYearMessage());
		
		if(!isValidISBN(this.getISBN()))
			throw new DBException(DBException.getInvalidISBNMessage());
	}
	
	
	//---------------------ACCESSORS---------------------//

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
	
	//------------VALIDATORS-------------//
	public boolean isValidTitle(String title) {
		return title.length() >= 1 && title.length() < 255;
	}
	
	public boolean isValidSummary(String summary) {
		return (summary.length() < 65536) || summary.isEmpty();
	}
	
	public boolean isValidYear(int year) {
		return year >= 1455 && year <= 2019;
	}
	
	public boolean isValidISBN(String isbn) {
		return isbn.length() <= 13 || isbn.isEmpty();
	}
	
	

}	//end of Book Class
