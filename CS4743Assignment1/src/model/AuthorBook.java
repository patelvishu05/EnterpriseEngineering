package model;

/**
 * AuthorBook model
 * 
 * @author Vishalkumar Patel
 * @author Juan-Diaz Sada
 *
 */
public class AuthorBook {
	
	private Author author;
	private Book book;
	private int royalty;
	private boolean newRecord;
	
	public AuthorBook(Author a, Book b, int r, boolean n)
	{
		this.author = a;
		this.book = b;
		this.royalty = r;
		this.newRecord = n;
	}
	
	public AuthorBook(){
		this.author = new Author(); 
		this.book = new Book();  
		this.royalty=0; 
		this.newRecord = true; 
	}
	
	public String toString() {
		return this.author + " " + this.book + " " + this.royalty + this.newRecord;
	}
	
	//--------------ACCESSORS--------------//
		
	public Author getAuthor() {
		return author;
	}
	public void setAuthor(Author author) {
		this.author = author;
	}
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	public int getRoyalty() {
		return royalty;
	}
	public void setRoyalty(int royalty) {
		this.royalty = royalty;
	}
	public boolean isNewRecord() {
		return newRecord;
	}
	public void setNewRecord(boolean newRecord) {
		this.newRecord = newRecord;
	} 
	
	

}
