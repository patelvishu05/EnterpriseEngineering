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
import authenticator.AccessPolicy;
import authenticator.Authenticator;
import authenticator.User;
import database.BookGateway;
import exception.DBException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.util.Callback;
import model.Author;
import model.AuthorBook;
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
	@FXML private MenuItem authorList;
	@FXML private MenuItem login;
	@FXML private MenuItem logout;

	public static ViewType currentView;  // I added this variable to keep track of current view to prompt for user to save changes if user tries to exit while in BookDetailView

	private static Logger logger = LogManager.getLogger(MainController.class);
	private static MainController instance = null;
	
	//----
	public static Book editedBook;
	public static Book previousBook;
	public static String auditChange;
	//---
	
	//______
	public static Author selectedAuthor;
	public static boolean royaltyChanged = false;
	public static AuthorBook rab;
	public static Author previousAuthor;
	public static Author editedAuthor;
	//______
	
	//~~~~~~~~~~~
	int sessionId;
	private Authenticator authenticator;
	public static String userType;
	//~~~~~~~~~~~

	//private constructor
	private MainController() {
		this.authenticator = new Authenticator();
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
			
			case VIEW5: viewString = "../view/AuthorListView.fxml";
						setDisplayLabelText("Author List View");
						controller = new AuthorListController();
						currentView = ViewType.VIEW5;
						System.out.println("Current View changed to VIEW5");
						break;
								
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
			if (currentView == ViewType.VIEW2 && !(userType.equals(AccessPolicy.INTERN))) 
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
			if (currentView == ViewType.VIEW2 && !(userType.equals(AccessPolicy.INTERN))) 
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
			if (currentView == ViewType.VIEW2 && !(userType.equals(AccessPolicy.INTERN))) 
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
	
	@FXML
	void clickedAuthorList(ActionEvent event) {
		switchView(ViewType.VIEW5, new Book());
	}
	
	
	@FXML
	void clickedLogin(ActionEvent event){
		Dialog<User> dialog = new Dialog<User>();
		GridPane grid = new GridPane();
		TextField username = new TextField();
		PasswordField password = new PasswordField();
		
		dialog.setHeaderText("Login to the Book Management System");
		grid.add(new Label("Username :"), 1, 1);
		grid.add(username, 2, 1);
		grid.add(new Label("Password :"), 1, 2);
		grid.add(password, 2, 2);
		dialog.getDialogPane().setContent(grid);
		
		ButtonType ok = new ButtonType("Okay", ButtonData.OK_DONE);
		ButtonType cancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		
		dialog.getDialogPane().getButtonTypes().add(ok);
		dialog.getDialogPane().getButtonTypes().add(cancel);
		
		dialog.setResultConverter(new Callback<ButtonType, User>(){

			@Override
			public User call(ButtonType b) {
				if(b == ok && username.getText() != null && password.getText() != null)
				{
					User user = new User();
					user.setUsername(username.getText());
					user.setPassword(password.getText());
					return user;
				}
				return null;
			}
			
		});
		
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.initOwner(Main.stage);
		Optional<User> result = dialog.showAndWait();
		
		if(result.isPresent())
		{
			User loggedUser = result.get();
			this.sessionId = authenticator.validateLogin(loggedUser.getUsername(), loggedUser.getPassword());
			if(this.sessionId == 0)
			{
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("Invalid Login Credentials");
				alert.setContentText("Either the username or pasword is incorrect.\n" + 
						"Or the User is not authenticated to use the system");
				alert.show();
			}
			else
			{
				VBox vbox = new VBox();
				Label userLabel = new Label("Hello " + Authenticator.loggedUserName);
				vbox.getChildren().add(userLabel);
				Label welcomeLabel = new Label("Welcome to the Book Management System !!");
				vbox.getChildren().add(welcomeLabel);
				userLabel.setStyle("-fx-font-size:2.5em;");
				welcomeLabel.setStyle("-fx-font-size:2.5em;");
				borderPane.setCenter(vbox);				
			}
			System.out.println(sessionId + "\t" + loggedUser);
		}
		updateGUI();
	}
	
	@FXML
	void clickedLogout(ActionEvent event) {
		this.sessionId = 0;
		switchToHomeView();
		updateGUI();
	}
	
	/**
	 * switchToHomeView method helps switch to home view
	 */
	public void switchToHomeView() {
		try 
		{
			URL url = Main.class.getResource("../view/MainView.fxml");
			FXMLLoader loader = new FXMLLoader(url);
			MainController controller = MainController.getInstance();
			loader.setController(controller);
			Parent rootNode = loader.load();			
			controller.setBorderPane((BorderPane) rootNode);
			Main.stage.setScene(new Scene(rootNode));
			Main.stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		updateGUI();
	}
	
	public void updateGUI()
	{
		if(sessionId == 0)
		{
			login.setDisable(false);
		}
		else
		{
			login.setDisable(true);
		}
		
		//if not logged in, logout should be disabled
		if(sessionId == 0)
			logout.setDisable(true);
		else
			logout.setDisable(false);
		
		if(authenticator.hasAccess(this.sessionId, AccessPolicy.ADMIN))
		{
			userType = AccessPolicy.ADMIN;
			authorList.setDisable(false);
			bookList.setDisable(false);
			addAuthor.setDisable(false);
			addBook.setDisable(false);
		}
		else if(authenticator.hasAccess(this.sessionId, AccessPolicy.DATA_ENTRY))
		{
			userType = AccessPolicy.DATA_ENTRY;
			authorList.setDisable(false);
			bookList.setDisable(false);
			addAuthor.setDisable(false);
			addBook.setDisable(false);
		}
		else if(authenticator.hasAccess(this.sessionId, AccessPolicy.INTERN))
		{
			userType = AccessPolicy.INTERN;
			authorList.setDisable(false);
			bookList.setDisable(false);
		}
		else
		{
			userType = AccessPolicy.INTERN;
			authorList.setDisable(true);
			bookList.setDisable(true);
			addBook.setDisable(true);
			addAuthor.setDisable(true);
		}
	}

	//-------------ACCESSORS-------------//
	public BorderPane getBorderPane() {
		return borderPane;
	}

	public void setBorderPane(BorderPane borderPane) {
		this.borderPane = borderPane;
	}


}	//end of class MainController