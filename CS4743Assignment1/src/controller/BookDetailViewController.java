package controller;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import application.Main;
import database.BookGateway;
import database.PublisherTableGateway;
import exception.DBException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import model.Book;
import model.Publisher;
import model.ViewType;

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
	@FXML private ComboBox<Publisher> bookPublisher;
	private ObservableList<Publisher> publisherObservableList;
	private Book book;
	private static Logger logger = LogManager.getLogger(BookDetailViewController.class);

	public BookDetailViewController(Book book)
	{
		this.book = book;
		
	}

	//saveBook handles the saving of book to a database by calling
	//insert or update method from BookGateway class. If the book already
	//exists in the database, update it else insert a new entry.
	@FXML
	void saveBook(ActionEvent event) 
	{
		logger.info("Save Clicked !!");
		try 
		{	
			Book l = this.book; 
			Book book = parseTextArea();
			book.setLastModified(l.getLastModified());
			book.save(l,book);
			MainController.getInstance().switchView(ViewType.VIEW1,new Book());
		} 
		catch (DBException e) {
			errorAlert(e.getErrorMessage());
		}
		catch(SQLException e) {
			errorAlert("Either the book does not exist or there is error while updating the book.");
		}
		catch(Exception e) {
			errorAlert("All required fields cannot be left empty and needs valid data to proceed.");
		}

	}
	
	@FXML
	String getSelectedPublisher()
	{
		return "";
	}
	
	
	public static void displaySaveErrorAlert() {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Cannot Save!");
		alert.setHeaderText("Record has changed since this view loaded");
		alert.setContentText("Please go back to the booklist to fetch a fresh copy of the book!");
		alert.setResizable(false);
		alert.showAndWait();
	}
	
	public static  int displayPopup() {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Exit without saving?");
		alert.setHeaderText("Some of your changes have not been saved,");
		alert.setContentText("Would you like to save before exiting this book?");
		alert.setResizable(false);
		alert.getButtonTypes().setAll(ButtonType.YES, 
                ButtonType.NO, 
                ButtonType.CANCEL);
		Optional<ButtonType> result =  alert.showAndWait(); 		
		
		ButtonType button = result.orElse(ButtonType.CANCEL);
		if (button == ButtonType.YES) {
			//save book here
			//How do i save book here? IDK :(
		    System.out.println("Ok pressed");
		    return 1; 
		} else if (button == ButtonType.NO) {
		    System.out.println("Don't save");
			
			return 1; 
		}
		return 0;
	}
	
	

	/**
	 * errorAlert class takes in an error message string
	 * and displays it onto a JavaFX Alert box of type alert
	 * 
	 * @param errorMessage
	 */
	public void errorAlert(String errorMessage)
	{
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Invalid Data Entered");
		alert.setHeaderText(null);
		alert.setContentText(errorMessage);
		alert.showAndWait();
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
		
		publisherObservableList  = FXCollections.observableArrayList();
		List<Publisher> publisherArrayList = PublisherTableGateway.getInstance().fetchPublishers();
		publisherObservableList.addAll(publisherArrayList);
		bookPublisher.setItems(publisherObservableList);
		bookPublisher.getSelectionModel().selectFirst();
		beautify();
	}

	/**
	 * parseTextArea reads from the TextArea fields and
	 * then saves it to a book object
	 * 
	 * @return Book object
	 * @throws Exception
	 */
	public Book parseTextArea() throws Exception
	{
		int id, year, publisher;
		String title, summary, ISBN;
		Book book = new Book();

		id = Integer.parseInt(bookId.getText());
		title = (bookTitle.getText() == null) ? "" : bookTitle.getText();
		summary = (bookSummary.getText() == null) ? "" : bookSummary.getText();
		year = Integer.parseInt(bookYear.getText());
		ISBN = (bookISBN.getText() == null) ? "" : bookISBN.getText();
		Publisher p = bookPublisher.getSelectionModel().getSelectedItem();
		publisher = p.getPublisherID();

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
