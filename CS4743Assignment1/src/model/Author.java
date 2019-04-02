package model;

import java.time.LocalDate;

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
