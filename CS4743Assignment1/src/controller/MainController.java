package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import application.Main;
import database.BookGateway;
import exception.DBException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import model.Author;
import model.Book;
import model.ViewType;

/**
 * MainController handles all events occuring
 * on the Main view
 * 
 * @author Vishalkumar Patel
 * @author Juan-Diaz Sada
 *
 */
public class MainController implements Initializable
{
	@FXML private BorderPane borderPane;
	@FXML private MenuItem exit;
	@FXML private MenuItem bookList;
	@FXML private MenuItem addBook;
	@FXML private MenuItem addAuthor;

	public static ViewType currentView;  // I added this variable to keep track of current view to prompt for user to save changes if user tries to exit while in BookDetailView

	private static Logger logger = LogManager.getLogger(MainController.class);
	private static MainController instance = null;
	
	//----
	public static Book editedBook;
	public static Book previousBook;
	public static String auditChange;
	//---
	
	//______
	public Author selectedAuthor;
	//______

	//private constructor
	private MainController() {

	}

	//public getInstance method to return 
	//the instance object, if it does not
	//exist it creates a new instance
	public static MainController getInstance()
	{
		if(instance == null)
			instance = new MainController();
		return instance;
	}

	//switchView provided a view 
	//switches to that view accordingly
	public void switchView(ViewType view,Book book)
	{
		String viewString="";
		MyController controller = null;
		switch (view)
		{
			case VIEW1: viewString = "../view/BookListView.fxml";
						setDisplayLabelText("Book List");
						List<Book> books = BookGateway.getInstance().getBooks();
						controller = new BookListController(books);
						currentView = ViewType.VIEW1; 
						System.out.println("Current view changed to VIEW1"); 
						break;

			case VIEW2: viewString = "../view/BookDetailView.fxml";
						setDisplayLabelText("Book Detail View");
						controller = new BookDetailViewController(book);
						currentView = ViewType.VIEW2;
						System.out.println("Current view changed to VIEW2"); 
						break;	

			case VIEW3: viewString = "../view/AuditTrailView.fxml";
						setDisplayLabelText("Audit Trail View");
						AuditTrailController.book = book;
						controller = new AuditTrailController(book.getAuditTrail());
						currentView = ViewType.VIEW3;
						System.out.println("Current view changed to VIEW3");
						break;
						
			case VIEW4: viewString = "../view/AuthorDetailView.fxml";
						setDisplayLabelText("Author Detail View");
						controller = new AuthorDetailController(selectedAuthor);
						currentView = ViewType.VIEW4;
						System.out.println("Current View changed to VIEW4");
						break;
			
//			case HOME: viewString = "../view/MainView.fxml";
//						controller = this.getInstance();
						
		}
		try
		{
			URL url = this.getClass().getResource(viewString);
			FXMLLoader loader = new FXMLLoader(url);
			loader.setController(controller);
			Parent viewNode = loader.load();
			borderPane.setCenter(viewNode);
		}
		catch (IOException e)
		{
			logger.error("Error occured in Loading View in switchView()");
			e.printStackTrace();
		}
	}	//end method switchView

	//whenever a view is changed the Title of the
	//application is changed automatically
	public void setDisplayLabelText(String name)
	{
		Main.stage.setTitle(name);
	}

	/**
	 * When booklist option is clicked, display all the books from the database
	 */
	@FXML
	void clickedBookList(ActionEvent event) 
	{
		try
		{
			if (currentView == ViewType.VIEW2) 
			{
				if(!Book.equalsBook(editedBook,previousBook)) 
				{
					int choice = BookDetailViewController.displayPopup();
					if (choice == 1) 
						saveHelper();
					if (choice == 0) 
						return;
				}
			} 
			switchView(ViewType.VIEW1,new Book());
		}
		catch(Exception e)
		{
			BookDetailViewController.errorAlert("An error occured while saving the book. Please try again after some time");
			return;
		}
	}	//end of clickedBookList method
	
	/**
	 * when add book option is clicked display add book form 
	 * where the user fills in data for the book to be saved to the 
	 * database
	 * @param event
	 */
	@FXML
	void clickedAddBook(ActionEvent event)
	{
		try
		{
			if (currentView == ViewType.VIEW2) 
			{
				if(!Book.equalsBook(editedBook,previousBook)) 
				{
					int choice = BookDetailViewController.displayPopup();
					if (choice == 1)
						saveHelper();
					if (choice == 0)
						return;
				}
			}
			BookDetailViewController.addBook = true;
			switchView(ViewType.VIEW2,new Book());
		}
		catch(Exception e)
		{
			BookDetailViewController.errorAlert("An error occured while saving the book. Please try again after some time");
			return;
		}
	}	//end of clickedAddBook method

	/**
	 * Exit application if the user clicks exit
	 * @param event
	 */
	@FXML
	void exitApplication(ActionEvent event) 
	{
		try
		{
			if (currentView == ViewType.VIEW2) 
			{
				if(!Book.equalsBook(editedBook,previousBook)) 
				{
					int choice = BookDetailViewController.displayPopup();
					if (choice == 1)
						saveHelper();
					if (choice == 0)
						return;
				}
			}
			Platform.exit();
		}
		catch(Exception e)
		{
			BookDetailViewController.errorAlert("An error occured while saving the book. Please try again after some time");
			return;
		}
	}	//end of exitApplication
	
	/**
	 * saveHelper method helps save book when the user misses to click save and 
	 * moves away from the view
	 * @throws Exception
	 */
	public void saveHelper() throws Exception
	{
		try 
		{
			editedBook.setLastModified(previousBook.getLastModified());
			editedBook.save(previousBook,editedBook);
		}
		catch (DBException e) {
			BookDetailViewController.errorAlert(e.getErrorMessage());
		}
		catch(SQLException e) {
			BookDetailViewController.errorAlert("Either the book does not exist or there is error while updating the book.");
		}
		catch(Exception e) {
			BookDetailViewController.displaySaveErrorAlert();
		}
	}	//end of saveHelper method
	
	@FXML
	void addAuthorClicked(ActionEvent event) {
		this.selectedAuthor = new Author();
		switchView(ViewType.VIEW4,new Book());
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}

	//-------------ACCESSORS-------------//
	public BorderPane getBorderPane() {
		return borderPane;
	}

	public void setBorderPane(BorderPane borderPane) {
		this.borderPane = borderPane;
	}


}	//end of class MainController