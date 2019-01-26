package model;

public class Book 
{
	private String title;
	private String author;
	private String genre;
	private String ISBN;
	
	public Book(String t, String a, String g, String i)
	{
		this.title = t;
		this.author = a;
		this.genre = g;
		this.ISBN = i;
	}

	//---------------------ACCESSORS---------------------//
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getISBN() {
		return ISBN;
	}

	public void setISBN(String iSBN) {
		ISBN = iSBN;
	}
	public String toString() {
		
		return title; 
	}
	
	

}
