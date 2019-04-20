package controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import model.Author;
import model.Book;

public class AuthorDetailController implements Initializable, MyController 
{
	@FXML private Button ok;
	@FXML private Button cancel;
	@FXML private Label addAuthorBookDisplay;
	@FXML private TextArea authorGender;
	@FXML private TextArea authorWebsite;
	@FXML private TextArea authorLastName;
	@FXML private TextArea authorFirstName;
	@FXML private TextArea authorId;
	@FXML private TextArea authorBday;
	@FXML private TextArea authorRoyalty;

	private Author author;

	public AuthorDetailController(Author a)
	{
		this.author = a;
	}
	
	@FXML
	void clickedOk(ActionEvent event) 
	{
		if(authorWebsite.getText() != null)
		{
			
		}
		String website = authorWebsite.getText() == null ? "" : authorWebsite.getText();
		String last = authorLastName.getText() == null ? "" : authorLastName.getText();
		String first = authorFirstName.getText() == null ? "" : authorFirstName.getText();
		int id = authorId.getText() == null ? 0 : Integer.parseInt(authorId.getText());
		LocalDate ld = authorBday.getText() == null ? LocalDate.MIN : LocalDate.MAX;
		int royalty = authorRoyalty.getText() == null ? 0 : Integer.parseInt(authorRoyalty.getText());
	}

	@FXML
	void clickedCancel(ActionEvent event) 
	{

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{
		
	}

}
