package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import model.Book;

public class BookDetailViewController implements MyController, Initializable
{
	@FXML private Button save;
	@FXML private TextArea bookSummary;
	@FXML private TextArea bookYear;
	@FXML private TextArea bookISBN;
	@FXML private TextArea bookTitle;
	public static Book book;

    @FXML
    void saveBook(ActionEvent event) 
    {
    	
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		bookTitle.setText(book.getTitle());
		bookSummary.setText(book.getGenre());
		bookISBN.setText(book.getISBN());
		bookYear.setText(book.getAuthor());
		beautify();
		
	}
	
	public void beautify()
	{
		bookTitle.setStyle("-fx-font-size: 3ex");
		bookSummary.setStyle("-fx-font-size: 3ex");
		bookISBN.setStyle("-fx-font-size: 3ex");
		bookYear.setStyle("-fx-font-size: 3ex");
	}

}
