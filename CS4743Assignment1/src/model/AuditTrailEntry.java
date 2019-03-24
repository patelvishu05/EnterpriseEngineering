package model;

import java.time.LocalDateTime;

/**
 * Audit Trail Entry model
 * 
 * @author Vishalkumar Patel
 * @author Juan-Diaz Sada
 *
 */
public class AuditTrailEntry 
{
	private int id;
	private LocalDateTime dateAdded;
	private String message;
	
	public AuditTrailEntry() {
		
	}
	
	public AuditTrailEntry(int i, LocalDateTime d, String m)
	{
		this.id = i;
		this.dateAdded = d;
		this.message = m;
	}

	//----------------ACCESSORS------------------//
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDateTime getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(LocalDateTime dateAdded) {
		this.dateAdded = dateAdded;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}	//end of AuditTrailEntry class
