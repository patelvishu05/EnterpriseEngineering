package controller;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import application.Main;
import authenticator.AccessPolicy;
import database.AuthorTableGateway;
import database.BookGateway;
import database.PublisherTableGateway;
import exception.DBException;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.util.Callback;
import model.Author;
import model.AuthorBook;
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
	@FXML private Button auditTrail;
	@FXML private TextField bookId;
	@FXML private TextField bookTitle;
	@FXML private TextField bookSummary;
	@FXML private TextField bookYear;
	@FXML private TextField bookISBN;
	@FXML private ComboBox<Publisher> bookPublisher;

	@FXML private TableView<AuthorBook> authorTable;
	@FXML private TableColumn<AuthorBook, String> author;
	@FXML private TableColumn<AuthorBook, String> royalty;
	private ObservableList<AuthorBook> obsList;
	@FXML private Button addAuthor;
	@FXML private Button deleteAuthor;

	//---

	//---


	private ObservableList<Publisher> publisherObservableList;
	private Book book;
	public static boolean addBook;
	public static Book previousBook;
	public static Book editedbook;
	private static Logger logger = LogManager.getLogger(BookDetailViewController.class);

	public BookDetailViewController(Book book)
	{
		this.book = book;
		previousBook = book;
		MainController.previousBook = book;
	}

	public BookDetailViewController() {

	}


	/**
	 * saveBook handles the saving of book to a database by calling
	 * insert or update method from BookGateway class. If the book already
	 * exists in the database, update it else insert a new entry.
	 * @param event
	 */
	@FXML
	void saveBook(ActionEvent event) 
	{
		logger.info("Save Clicked !!");
		try 
		{	
			previousBook = this.book;
			editedbook = parseTextArea();
			System.out.println("~~~~~" + previousBook + "\t" + editedbook);
			//if(!Book.equalsBook(editedbook,previousBook)) 
			//{
			editedbook.setLastModified(previousBook.getLastModified());
			editedbook.save(previousBook,editedbook);
			//}
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setHeaderText("Save Successful !!");
			alert.setContentText("The book " + editedbook.getTitle() + " has been successfully saved to database !!");
			alert.show();
			MainController.start = 0;
			MainController.end = 50;
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

	/**
	 * If the user clicks audit trail go to audit trail view to display
	 * audit trails for that specific book
	 * 
	 * @param event
	 */
	@FXML
	void clickedAuditTrail(ActionEvent event)
	{
		MainController.getInstance().switchView(ViewType.VIEW3, this.book);
	}


	/**
	 * If an error occurs while saving book, display them this error
	 * which will ask them to fetch a fresh copy of the book
	 * before moving further
	 */
	public static void displaySaveErrorAlert() 
	{
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Cannot Save!");
		alert.setHeaderText("Record has changed since this view loaded");
		alert.setContentText("Please go back to the booklist to fetch a fresh copy of the book!");
		alert.setResizable(false);
		alert.showAndWait();
	}	//end of displaySaveErrorAlert method

	/**
	 * If user forgets to click save before moving out
	 * to a different view, display them a dialog box which will
	 * ask them if they want to save the book or not
	 * 
	 * @return button choice
	 */
	public static int displayPopup() 
	{
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Exit without saving?");
		alert.setHeaderText("Some of your changes have not been saved,");
		alert.setContentText("Would you like to save before exiting this book?");
		alert.setResizable(false);
		alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
		Optional<ButtonType> result =  alert.showAndWait(); 		

		ButtonType button = result.orElse(ButtonType.CANCEL);

		if (button == ButtonType.YES) 
		{
			System.out.println("Ok pressed");
			return 1; 
		} 
		else if (button == ButtonType.NO) 
		{
			System.out.println("Don't save");
			return -1; 
		}
		return 0;
	}	//end of displayPopup method


	/**
	 * errorAlert class takes in an error message string
	 * and displays it onto a JavaFX Alert box of type alert
	 * 
	 * @param errorMessage
	 */
	public static void errorAlert(String errorMessage)
	{
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Invalid Data Entered");
		alert.setHeaderText(null);
		alert.setContentText(errorMessage);
		alert.showAndWait();
	}	//end of errorAlert method


	/**
	 * initialize method before loading the view populates the
	 * text views to show the book details
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		this.auditTrail.setDisable(false);
		publisherObservableList  = FXCollections.observableArrayList();
		List<Publisher> publisherArrayList = PublisherTableGateway.getInstance().fetchPublishers();
		publisherObservableList.addAll(publisherArrayList);
		bookPublisher.setItems(publisherObservableList);
		startListening();
		Main.stage.setTitle(book.getTitle());
		bookId.setText(""+book.getId());
		bookTitle.setText(book.getTitle());
		bookSummary.setText(book.getSummary());
		bookISBN.setText(book.getISBN());
		bookYear.setText(""+book.getYear());
		bookPublisher.getSelectionModel().select(book.getPublisher());
		//		System.out.println("--->" + this.book);

		if(addBook == true)
			auditTrail.setDisable(true);
		else
			auditTrail.setDisable(false);
		addBook = false;

		beautify();
		populateAuthorTable();
		if(MainController.userType.equals(AccessPolicy.INTERN))
		{
			deleteAuthor.setDisable(true);
			addAuthor.setDisable(true);
			save.setDisable(true);
		}
		
	}	//end of initialize method


	public void populateAuthorTable()
	{
		List<AuthorBook> authorBookList = book.getAuthors();
		//		System.out.println(authorBookList);
		obsList = FXCollections.observableArrayList();
		obsList.addAll(authorBookList);
		author.setCellValueFactory(new PropertyValueFactory("Author"));
//		royalty.setCellValueFactory(new PropertyValueFactory("Royalty"));		
				
		royalty.setCellValueFactory(new Callback<CellDataFeatures<AuthorBook,String>, ObservableValue<String>>() {
			@Override
		     public ObservableValue<String> call(CellDataFeatures<AuthorBook, String> p) {
				String s = (((double)p.getValue().getRoyalty()) / 100000) + "%";
		         return new ReadOnlyObjectWrapper(s);
		     }
		  });
		
		if(obsList.size() <= 0)
			deleteAuthor.setDisable(true);
		else
			deleteAuthor.setDisable(false);
		authorTable.setItems(obsList);
		authorTable.setStyle("-fx-font-size: 3ex");
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

		id = Integer.parseInt(bookId.getText().replaceAll("[^0-9]", "").equals("") ? "0" : bookId.getText());
		title = (bookTitle.getText() == null) ? "" : bookTitle.getText();
		summary = (bookSummary.getText() == null) ? "" : bookSummary.getText();
		year = Integer.parseInt(bookYear.getText().replaceAll("[^0-9]", "").equals("") ? "0" : bookYear.getText());
		ISBN = (bookISBN.getText() == null) ? "" : bookISBN.getText();
		Publisher p = bookPublisher.getSelectionModel().getSelectedItem();
		publisher = p.getPublisherID();

		book = new Book(id,title,summary,year,publisher,ISBN);
		return book;
	}	//end of parseTextArea method

	/**
	 * startListening method sets listeners for all textAreas, 
	 * so any change to them is keep tracked of
	 */
	@SuppressWarnings(value = { "unchecked","rawtypes" })
	public void startListening()
	{
		MainController.editedBook = new Book();

		bookId.textProperty().addListener(new ChangeListener() { @Override
			public void changed(ObservableValue arg0, Object arg1, Object arg2) {
			MainController.editedBook.setId(Integer.parseInt(bookId.getText().replaceAll("[^0-9]", "").equals("") ? "0" : bookId.getText()) );
		} });

		bookTitle.textProperty().addListener(new ChangeListener() { @Override
			public void changed(ObservableValue arg0, Object arg1, Object arg2) {
			MainController.editedBook.setTitle((bookTitle.getText() == null) ? "" : bookTitle.getText());
		} });

		bookSummary.textProperty().addListener(new ChangeListener() { @Override
			public void changed(ObservableValue arg0, Object arg1, Object arg2) {
			MainController.editedBook.setSummary((bookSummary.getText() == null) ? "" : bookSummary.getText());
		} });

		bookYear.textProperty().addListener(new ChangeListener() { @Override
			public void changed(ObservableValue arg0, Object arg1, Object arg2) {
			MainController.editedBook.setYear(Integer.parseInt(bookYear.getText().replaceAll("[^0-9]", "").equals("") ? "0" :bookYear.getText()));
		} });

		bookISBN.textProperty().addListener(new ChangeListener() { @Override
			public void changed(ObservableValue arg0, Object arg1, Object arg2) {
			MainController.editedBook.setISBN((bookISBN.getText() == null) ? "" : bookISBN.getText());
		} });

		bookPublisher.valueProperty().addListener(new ChangeListener() { @Override
			public void changed(ObservableValue arg0, Object arg1, Object arg2) {
			MainController.editedBook.setPublisher(bookPublisher.getSelectionModel().getSelectedItem().getPublisherID());
		} });

	}	//end of startListening method


	/**
	 * beautify method applies font styles and sizes
	 * to the text view fields
	 */ 
	public void beautify()
	{
		bookId.setStyle("-fx-font-size: 3ex");
		bookTitle.setStyle("-fx-font-size: 3ex");
		bookSummary.setStyle("-fx-font-size: 3ex");
		bookISBN.setStyle("-fx-font-size: 3ex");
		bookYear.setStyle("-fx-font-size: 3ex");
		bookPublisher.setStyle("-fx-font-size: 3ex");
	}

	/**
	 * addAuthorClicked method helps add author for current books
	 * @param event
	 */
	@FXML
	void addAuthorClicked(ActionEvent event) 
	{
		AuthorBook currentAuthorBook = null;
		
		Dialog<AuthorBook> dialog = new Dialog<AuthorBook>();
		dialog.setTitle("Add author");
		dialog.setHeaderText("Add author");
		ComboBox<Author> cAuthor = new ComboBox<Author>();
		
		List<Author> AuthorL = AuthorTableGateway.getInstance().getAuthors();
		ObservableList<Author> oAuthor = FXCollections.observableArrayList();
		oAuthor.addAll(AuthorL);
		cAuthor.setItems(oAuthor);
		
		TextField royalty = new TextField();
		
		GridPane grid = new GridPane();
		grid.add(new Label("Select Author :"), 1, 1);
		grid.add(cAuthor, 2, 1);
		grid.add(new Label("Royalty :"), 1, 2);
		grid.add(royalty, 2, 2);
		
		dialog.getDialogPane().setContent(grid);
		
		ButtonType ok = new ButtonType("Okay", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().add(ok);
		
		dialog.setResultConverter(new Callback<ButtonType, AuthorBook>(){

			@Override
			public AuthorBook call(ButtonType b) {
				if(b == ok && cAuthor.getSelectionModel().getSelectedItem() != null)
				{
					AuthorBook temp = new AuthorBook();
					temp.setRoyalty((int)(Double.parseDouble(royalty.getText().equals("") ? "1.0000" : royalty.getText()) * 100000));
					temp.setAuthor(cAuthor.getSelectionModel().getSelectedItem());
					temp.setBook(MainController.editedBook);
					return temp;
				}
				return null;
			}
			
		});
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.initOwner(Main.stage);
		Optional<AuthorBook> result = dialog.showAndWait();
		
		if(result.isPresent())
		{
			currentAuthorBook = result.get();
			System.out.println(currentAuthorBook);
			if(!(AuthorTableGateway.getInstance().isDuplicateRecord(currentAuthorBook)))
			{
				AuthorTableGateway.getInstance().addAuthorToBook(currentAuthorBook);
				populateAuthorTable();
			}
			else
			{
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText("Duplicate Author Entry !!");
				alert.setContentText("The author " + currentAuthorBook.getAuthor().getFirstName() + " " +
						currentAuthorBook.getAuthor().getLastName() + " already exists for the book " + 
						currentAuthorBook.getBook().getTitle());
				alert.show();
			}
		}
		
	}

	/**
	 * deleteAuthorClicked helps delete selected author from the database
	 * @param event
	 */
	@FXML
	void deleteAuthorClicked(ActionEvent event) 
	{
		int selected = authorTable.getSelectionModel().getSelectedIndex();
		if(selected >= 0 && selected < obsList.size())
		{
			AuthorTableGateway.getInstance().deleteAuthorForBook(obsList.get(selected));
			populateAuthorTable();
		}
	}
	
	/**
	 * if author is double clicked switch view for showing
	 * details and allow edits to the author
	 * @param event
	 */
	@FXML
	void handleAuthorClicked(MouseEvent event)
	{
		AuthorBook authorSelected = authorTable.getSelectionModel().getSelectedItem();
		if (MainController.userType.equals(AccessPolicy.INTERN))
			return;
		if(event.getClickCount() == 2 && authorSelected != null)
		{
			System.out.println(authorSelected);
			
			Dialog<AuthorBook> dialog = new Dialog<AuthorBook>();
			dialog.setTitle("Edit Author");
			ComboBox<Author> cAuthor = new ComboBox<Author>();
			
			List<Author> AuthorL = AuthorTableGateway.getInstance().getAuthors();
			ObservableList<Author> oAuthor = FXCollections.observableArrayList();
			oAuthor.addAll(AuthorL);
			cAuthor.setItems(oAuthor);
			cAuthor.getSelectionModel().select(authorSelected.getAuthor());			
			TextField royalty = new TextField(""+(((double)authorSelected.getRoyalty()) / 100000));
			
			GridPane grid = new GridPane();
			grid.add(new Label("Select Author :"), 1, 1);
			grid.add(cAuthor, 2, 1);
			grid.add(new Label("Royalty :"), 1, 2);
			grid.add(royalty, 2, 2);
			
			dialog.getDialogPane().setContent(grid);
			
			ButtonType ok = new ButtonType("Okay", ButtonData.OK_DONE);
			dialog.getDialogPane().getButtonTypes().add(ok);
			
			dialog.setResultConverter(new Callback<ButtonType, AuthorBook>(){

				@Override
				public AuthorBook call(ButtonType b) {
					if(b == ok && cAuthor.getSelectionModel().getSelectedItem() != null)
					{
						AuthorBook temp = new AuthorBook();
						temp.setRoyalty((int)(Double.parseDouble(royalty.getText().equals("") ? "1.0000" : royalty.getText()) * 100000));
						temp.setAuthor(cAuthor.getSelectionModel().getSelectedItem());
						temp.setBook(MainController.editedBook);
						System.out.println(temp.getRoyalty());
						return temp;
					}
					return null;
				}
				
			});
			dialog.initModality(Modality.APPLICATION_MODAL);
			dialog.initOwner(Main.stage);
			Optional<AuthorBook> result = dialog.showAndWait();
			
			if(result.isPresent())
			{
				AuthorBook newRecord = result.get();
				if(!(AuthorTableGateway.getInstance().isDuplicateRecord(newRecord)))
				{
					MainController.royaltyChanged = true;
					MainController.rab = authorSelected;
					AuthorTableGateway.getInstance().deleteAuthorForBook(authorSelected);
					AuthorTableGateway.getInstance().addAuthorToBook(newRecord);
					populateAuthorTable();
					MainController.royaltyChanged = false;
				}
				else
				{
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setHeaderText("Duplicate Author Entry !!");
					alert.setContentText("The author " + authorSelected.getAuthor().getFirstName() + " " +
							authorSelected.getAuthor().getLastName() + " already exists for the book " + 
							authorSelected.getBook().getTitle());
					alert.show();
				}
			}
		}
	}

}	//end of BookDetailViewController class
