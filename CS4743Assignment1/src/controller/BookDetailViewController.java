package controller;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import application.Main;
import database.BookGateway;
import exception.DBException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
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
	@FXML private TextArea bookId;
	@FXML private TextArea bookTitle;
	@FXML private TextArea bookSummary;
	@FXML private TextArea bookYear;
	@FXML private TextArea bookISBN;
	@FXML private TextArea bookPublisher;

	private Book book;
	private static Logger logger = LogManager.getLogger(BookDetailViewController.class);

	public BookDetailViewController(Book book)
	{
		this.book = book;
	}

	//saveBook handles the event when the save button
	//is clicked and logs the event
	@FXML
	void saveBook(ActionEvent event) 
	{
		logger.info("Save Clicked !!");
		Book book = parseTextArea();
		try 
		{
			book.save();
			ArrayList<Integer> primaryKeys = new ArrayList<Integer>();
			for(Book b : BookGateway.getInstance().getBooks())
			{
				System.out.println(b.getId());
				primaryKeys.add(b.getId());
			}
			if(!(primaryKeys.contains(book.getId())))
			{
				BookGateway.getInstance().insert(book);
				System.out.println("From id");
			}
			else
			{
				BookGateway.getInstance().update(book);
				System.out.println("From else");
			}
		} 
		catch (DBException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Invalid Data Entered");
			alert.setHeaderText(null);
			alert.setContentText(e.getErrorMessage());
			alert.showAndWait();
		}

	}

	//initialize method before loading the view populates the 
	//text views to show the book details
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		Main.stage.setTitle(book.getTitle());
		bookId.setText(""+book.getId());
		bookTitle.setText(book.getTitle());
		bookSummary.setText(book.getSummary());
		bookISBN.setText(book.getISBN());
		bookYear.setText(""+book.getYear());
		bookPublisher.setText(""+book.getPublisher());
		beautify();
	}

	public Book parseTextArea()
	{
		int id, year, publisher;
		String title, summary, ISBN;
		Book book = new Book();

		id = Integer.parseInt(bookId.getText());
		title = bookTitle.getText();
		summary = bookSummary.getText();
		year = Integer.parseInt(bookYear.getText());
		ISBN = bookISBN.getText();
		publisher = Integer.parseInt(bookPublisher.getText());
		book = new Book(id,title,summary,year,publisher,ISBN);

		return book;
	}

	//beautify method applies font styles and sizes
	//to the text view fields
	public void beautify()
	{
		bookId.setStyle("-fx-font-size: 3ex");
		bookTitle.setStyle("-fx-font-size: 3ex");
		bookSummary.setStyle("-fx-font-size: 3ex");
		bookISBN.setStyle("-fx-font-size: 3ex");
		bookYear.setStyle("-fx-font-size: 3ex");
		bookPublisher.setStyle("-fx-font-size: 3ex");
	}

}	//end of BookDetailViewController class
