package model;

import java.time.LocalDate;
import java.util.ArrayList;

import controller.MainController;
import database.AuthorTableGateway;
import database.BookGateway;

/**
 * Author model
 * 
 * @author Vishalkumar Patel
 * @author Juan-Diaz Sada
 *
 */
public class Author {
	private int id;
	private String firstName;
	private String lastName;
	private LocalDate dateOfBirth;
	private String gender;
	private String website;
	
	public Author() {
		this.id = 0; 
		this.firstName= ""; 
		this.lastName = ""; 
		this.dateOfBirth = LocalDate.MIN; 
		this.gender = ""; 
		this.website = ""; 		
	}
	
	public Author(int id, String fn, String ln, LocalDate bd, String g, String w)
	{
		this.id = id;
		this.firstName = fn;
		this.lastName = ln;
		this.dateOfBirth = bd;
		this.gender = g;
		this.website = w;
	}
	
	public String toString() {
		return this.firstName + " " + this.lastName;
	}
	
	public void save(Author a, Author b) {
		ArrayList<Integer> primaryKeys = new ArrayList<Integer>();
		for(Author temp : AuthorTableGateway.getInstance().getAuthors())
			primaryKeys.add(temp.getId());
		if(!(primaryKeys.contains(b.getId()))) 
		{
			System.out.println("1" + a + "\t" + b);
			AuthorTableGateway.getInstance().deleteAuthor(a);
			AuthorTableGateway.getInstance().insertAuthor(b);
		}
		else 
		{
			System.out.println("2" + a + "\t" + b);
			AuthorTableGateway.getInstance().updateAuthor(b);
		}
	}
	
	//--------------ACCESSORS--------------//

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}



	public String getGender() {
		return gender;
	}



	public void setGender(String gender) {
		this.gender = gender;
	}
	
	
	
}
