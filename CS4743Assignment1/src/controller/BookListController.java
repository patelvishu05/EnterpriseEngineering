package controller;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import database.BookGateway;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
	private ArrayList<Book> bookArrayList;
	@FXML private Button delete;
	private ObservableList<Book> booksObservableList;
	private static Logger logger = LogManager.getLogger(BookListController.class);
	
	//pre fills the listview with the
	//fake book data
	public void initialize() 
	{	
		booksObservableList = FXCollections.observableArrayList();
		bookArrayList = BookGateway.getInstance().getBooks(); 
		booksObservableList.addAll(bookArrayList);	
		booklist.setItems(booksObservableList);	
	 }
	
	@FXML
	public void deleteBook(ActionEvent event)
	{
		int bookSelected = booklist.getSelectionModel().getSelectedIndex();
		BookGateway.getInstance().delete(booksObservableList.get(bookSelected));
		initialize();
	}
	
	@FXML
    public void handleBookClick(MouseEvent event) 
	{
		//Handles mouse click events perfectly and switches view to detailed view only if
		//you double click on the book
		String bookSelected = booklist.getSelectionModel().getSelectedItem().toString();
		if(event.getClickCount() == 2)
		{
			BookGateway.getInstance().getBooks();
			logger.info(bookSelected + " book Selected.");
			for(Book book: booksObservableList)
			{
				if (book.getTitle().equals(bookSelected))
				{
					BookDetailViewController.book = book;
					MainController.getInstance().switchView(ViewType.VIEW2);
				}	//end of inner if statement
			}	//end of for loop
		}	//end outer-if statement
	}	//end of handleBookClick method
}	//end of BookListController class
