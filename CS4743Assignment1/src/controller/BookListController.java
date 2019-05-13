package controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import authenticator.AccessPolicy;
import database.BookGateway;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableNumberValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
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
	@FXML private TextField searchBox;
	@FXML private Button delete;
	@FXML private Button next;
	@FXML private Button last;
	@FXML private Button prev;
	@FXML private Button first;
	@FXML private Button search;
	@FXML private Label fetchLabel;

	private List<Book> bookArrayList;
	private ObservableList<Book> booksObservableList;
	private static Logger logger = LogManager.getLogger(BookListController.class);

	//--------------
	public boolean flag = false;
	public static String findBook;
	public int size;
	public int fromSize = 1;
	public int toSize = 50;
	public int startIndex;
	public int endIndex;
	public int prevIndex;
	public int nextIndex;
	//--------------



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
		//		bookArrayList = BookGateway.getInstance().getBooks(); 
		booksObservableList.addAll(bookArrayList);	
		booklist.setItems(booksObservableList);
		booklist.setStyle("-fx-font-size:1.5em;");
		int temp = toSize > MainController.allSize ? MainController.allSize : toSize;
		fetchLabel.setText("Fetched Records " + fromSize + " to " + temp + " out of " + MainController.allSize);
		if(!MainController.userType.equals(AccessPolicy.ADMIN))
		{
			delete.setDisable(true);
		}
		if(MainController.start <= 0)
			prev.setDisable(true);
		else
			prev.setDisable(false);

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
		if(bookSelected >= 0 && bookSelected < booksObservableList.size())
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
		Book bookSelected = booklist.getSelectionModel().getSelectedItem();
		System.out.println(bookSelected);
		if(event.getClickCount() == 2 && bookSelected != null)
		{
			//			BookGateway.getInstance().getBooks();
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

	/**
	 * clickedBookSearch method helps us find a book that was searched for
	 * @param event
	 */
	@FXML
	void clickedBookSearch(ActionEvent event)
	{
		flag = true;
		startIndex = 0;
		endIndex = 50;
		findBook = searchBox.getText();
		System.out.println(findBook);
		this.bookArrayList = BookGateway.getInstance().searchBooks(findBook);
		reload();
		prev.setDisable(true);
		buttonChecks();
	}

	/**
	 * reload is a helper method to other functions in this class
	 * that helps refresh the listview of books
	 */
	public void reload()
	{
		booksObservableList.clear();
		size = this.bookArrayList.size();
		booksObservableList.addAll(this.bookArrayList.subList(startIndex, endIndex > size ? size : endIndex ));
		booklist.setItems(booksObservableList);
//		fetchLabel.setText("Fetched Records " + (size - (size - 1)) + " to " + booksObservableList.size() + " out of " + size);
		fetchLabel.setText("Fetched Records " + (startIndex + 1) + " to " + (endIndex > size ? size : endIndex) + " out of " + size);
		flag = true;
	}
	
	/**
	 * buttonChecks method helps enable or disable buttons if it just 
	 * has 1 page load of books
	 */
	void buttonChecks()
	{
		if(size <= 50) 
		{
			prev.setDisable(true);
			first.setDisable(true);
			next.setDisable(true);
			last.setDisable(true);
		}
		else
		{
			prev.setDisable(false);
			first.setDisable(false);
			next.setDisable(false);
			last.setDisable(false);
		}
	}

	/**
	 * clickedFirst method helps go to the 1st 50 books in the filtered list
	 * @param event
	 */
	@FXML
	void clickedFirst(ActionEvent event) 
	{
		if(flag)
		{
			startIndex = 0;
			endIndex = 50;
			reload();
		}
		else
		{
			MainController.start = 0;
			fromSize = 1;
			toSize = 50;
			bookArrayList = BookGateway.getInstance().getBooks(MainController.start,MainController.end);
			initialize();
		}
		next.setDisable(false);
		prev.setDisable(true);
	}

	/**
	 * clickedPrev method helps go to the previous 50 books in the filtered list
	 * @param event
	 */
	@FXML
	void clickedPrev(ActionEvent event) 
	{
		if(flag)
		{
			startIndex -= 50;
			endIndex = startIndex + 50;
			reload();
		}
		else
		{
			MainController.start-=50;
			fromSize -= 50;
			toSize = fromSize + 50;
			bookArrayList = BookGateway.getInstance().getBooks(MainController.start,MainController.end);
			initialize();
		}
		next.setDisable(false);
		prev.setDisable(false);
	}

	/**
	 * clickedNext method helps go to the next 50 books in the filtered list
	 * @param event
	 */
	@FXML
	void clickedNext(ActionEvent event) 
	{
		if(flag)
		{
			startIndex += 50;
			endIndex += 50;
			reload();
		}
		else
		{
			fromSize += 50;
			toSize += 50;
			MainController.start+=50;
			bookArrayList = BookGateway.getInstance().getBooks(MainController.start,MainController.end);
			initialize();
		}
		next.setDisable(false);
		prev.setDisable(false);
	}

	/**
	 * clickedLast method helps go to the last 50 books in the filtered list
	 * @param event
	 */
	@FXML
	void clickedLast(ActionEvent event) 
	{
		if(flag)
		{
			startIndex = (size - (size % 50));
			endIndex = (size % 50);
			reload();
		}
		else
		{
			fromSize = (MainController.allSize - (MainController.allSize % 50));
			toSize = MainController.allSize;
			MainController.start = (MainController.allSize -(MainController.allSize % 50));
			bookArrayList = BookGateway.getInstance().getBooks(MainController.start,MainController.end);
			initialize();
		}
		next.setDisable(true);
		prev.setDisable(false);
	}

}	//end of BookListController class
