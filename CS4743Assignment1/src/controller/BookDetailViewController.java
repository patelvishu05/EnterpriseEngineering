package controller;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import model.Book;

/**
 * BookDetailViewController controls events that
 * occurs on the BookDetailView and handles events
 * accordingly.
 * 
 * @author Vishalkumar Patel
 * @author Juan-Diaz Sada
 *
 */
public class BookDetailViewController implements MyController, Initializable
{
	@FXML private Button save;
	@FXML private TextArea bookSummary;
	@FXML private TextArea bookYear;
	@FXML private TextArea bookISBN;
	@FXML private TextArea bookTitle;
	public static Book book;
	private static Logger logger = LogManager.getLogger(BookDetailViewController.class);

	//saveBook handles the event when the save button
	//is clicked and logs the event
    @FXML
    void saveBook(ActionEvent event) 
    {
    	logger.info("Save Clicked !!");
    }

    //initialize method before loading the view populates the 
    //text views to show the book details
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		bookTitle.setText(book.getTitle());
		bookSummary.setText(book.getSummary());
		bookISBN.setText(book.getISBN());
		bookYear.setText(""+book.getYear());
		beautify();
	}
	
	//beautify method applies font styles and sizes
	//to the text view fields
	public void beautify()
	{
		bookTitle.setStyle("-fx-font-size: 3ex");
		bookSummary.setStyle("-fx-font-size: 3ex");
		bookISBN.setStyle("-fx-font-size: 3ex");
		bookYear.setStyle("-fx-font-size: 3ex");
	}
	
}	//end of BookDetailViewController class
