package model;

import java.time.LocalDateTime;

public class Publisher 
{
	private int publisherID;
	private String publisherName;
	private LocalDateTime dateAdded;
	
	
	public Publisher() {
		
	}
	
	public Publisher(int id, String name, LocalDateTime d)
	{
		this.publisherID = id;
		this.publisherName = name;
		this.dateAdded = d;
	}
	
	public String toString() {
		return this.publisherName;
	}

	
	//----------------------------ACCESSORS-------------------------//	
	
	public int getPublisherID() {
		return publisherID;
	}

	public void setPublisherID(int publisherID) {
		this.publisherID = publisherID;
	}

	public String getPublisherName() {
		return publisherName;
	}

	public void setPublisherName(String publisherName) {
		this.publisherName = publisherName;
	}

	public LocalDateTime getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(LocalDateTime dateAdded) {
		this.dateAdded = dateAdded;
	}

}
