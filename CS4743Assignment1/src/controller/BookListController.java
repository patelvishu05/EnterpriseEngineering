package controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import database.BookGateway;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import model.Book;
import model.ViewType;

/**
 * BookListController displays the list of books
 * in a listview
 * 
 * @author Vishalkumar Patel
 * @author Juan-Diaz Sada
 *
 */
public class BookListController implements MyController
{
	@FXML private ListView<Book> booklist;
	private List<Book> bookArrayList;
	@FXML private Button delete;
	private ObservableList<Book> booksObservableList;
	private static Logger logger = LogManager.getLogger(BookListController.class);
	
	public BookListController(List<Book> books)
	{
		this.bookArrayList = books;
	}

	/**
	* Fill the list view with the books from our database
	*/
	public void initialize() 
	{	
		booksObservableList = FXCollections.observableArrayList();
		bookArrayList = BookGateway.getInstance().getBooks(); 
		booksObservableList.addAll(bookArrayList);	
		booklist.setItems(booksObservableList);
	}

	/**
	 * deleteBook method deletes the book from database by calling the delete 
	 * method from the BookGateway class.
	 * 
	 * @param event
	 */
	@FXML
	public void deleteBook(ActionEvent event)
	{
		int bookSelected = booklist.getSelectionModel().getSelectedIndex();
		if(bookSelected > 0 && bookSelected < booksObservableList.size())
		{
			BookGateway.getInstance().deleteBook(booksObservableList.get(bookSelected));
			initialize();
		}
	}

	/**
	 * handleBookClick method displays a specific book
	 * when a book is double clicked.
	 * 
	 * @param event
	 */
	@FXML
	public void handleBookClick(MouseEvent event) 
	{		
		//Handles mouse click events perfectly and switches view to detailed view only if
		//you double click on the book
		//String bookSelected = booklist.getSelectionModel().getSelectedItem().getTitle();
		Book bookSelected = booklist.getSelectionModel().getSelectedItem();
		System.out.println(bookSelected);
		if(event.getClickCount() == 2 && bookSelected != null)
		{
			BookGateway.getInstance().getBooks();
			logger.info(bookSelected + " book Selected.");
			for(Book book: booksObservableList)
			{
				if (book.getTitle().equals(bookSelected.getTitle()))
				{
					MainController.getInstance().switchView(ViewType.VIEW2,book);
				}	//end of inner if statement
			}	//end of for loop
		}	//end outer-if statement
	}	//end of handleBookClick method

}	//end of BookListController class
