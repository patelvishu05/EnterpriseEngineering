package controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import application.Main;
import database.AuthorTableGateway;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import model.Author;
import model.Book;
import model.ViewType;

/**
 * AuthorDetailController controls events that
 * occurs on the AuthorDetailView and handles events
 * accordingly.
 * 
 * @author Vishalkumar Patel
 * @author Juan-Diaz Sada
 *
 */
public class AuthorDetailController implements Initializable, MyController 
{
	@FXML private Button ok;
	@FXML private Button cancel;
	@FXML private TextArea authorGender;
	@FXML private TextArea authorWebsite;
	@FXML private TextArea authorLastName;
	@FXML private TextArea authorFirstName;
	@FXML private TextArea authorId;
	@FXML private TextArea authorBday;

	private Author author;

	public AuthorDetailController(Author a)
	{
		this.author = a;
	}
	
	/**
	 * clickedOk method helps save author to the database
	 * @param event
	 */
	@FXML
	void clickedOk(ActionEvent event) 
	{
		MainController.previousAuthor = this.author;
		System.out.println("~~~~~~~~~~~~~>Here");
		System.out.println(MainController.previousAuthor + "-->\t" + MainController.editedAuthor);
		author.save(MainController.previousAuthor, MainController.editedAuthor);
		switchToHomeView();
	}

	@FXML
	void clickedCancel(ActionEvent event) 
	{
		MainController.getInstance().switchView(ViewType.VIEW5, new Book());
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

	/**
	 * initialize method helps initialize gui fields
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{
		startListening();
		authorId.setText(""+author.getId());
		authorGender.setText(author.getGender());
		authorFirstName.setText(author.getFirstName());
		authorLastName.setText(author.getLastName());
		authorWebsite.setText(author.getWebsite());
		authorBday.setText(author.getDateOfBirth() == LocalDate.MIN ? "" + (("" + author.getDateOfBirth()).replaceAll(".*", "")) : "" + author.getDateOfBirth() );
	}	//end of initalize method
	
	/**
	 * startListening() method keeps listening 
	 * the event changes on gui components. If fields
	 * change
	 */
	@SuppressWarnings(value = { "unchecked","rawtypes" })
	public void startListening()
	{
		MainController.editedAuthor = new Author();
		
		authorId.textProperty().addListener(new ChangeListener() { 	@Override
			public void changed(ObservableValue arg0, Object arg1, Object arg2) {
				MainController.editedAuthor.setId(Integer.parseInt(authorId.getText().replaceAll("[^0-9]", "").equals("") ? "0" : authorId.getText()));
		} });
		
		authorFirstName.textProperty().addListener(new ChangeListener() { @Override
			public void changed(ObservableValue arg0, Object arg1, Object arg2) {
			MainController.editedAuthor.setFirstName(authorFirstName.getText() == null ? "" : authorFirstName.getText());
		} });
		
		authorLastName.textProperty().addListener(new ChangeListener() { @Override
			public void changed(ObservableValue arg0, Object arg1, Object arg2) {
			MainController.editedAuthor.setLastName(authorLastName.getText() == null ? "" : authorLastName.getText());
		} });
		
		authorGender.textProperty().addListener(new ChangeListener() { @Override
			public void changed(ObservableValue arg0, Object arg1, Object arg2) {
			MainController.editedAuthor.setGender(authorGender.getText() == null ? "" : authorGender.getText());
		} });
		
		authorWebsite.textProperty().addListener(new ChangeListener() { @Override
			public void changed(ObservableValue arg0, Object arg1, Object arg2) {
			MainController.editedAuthor.setWebsite(authorWebsite.getText() == null ? "" : authorWebsite.getText());
		} });
		
		authorBday.textProperty().addListener(new ChangeListener() { @Override
			public void changed(ObservableValue arg0, Object arg1, Object arg2) {
			if(authorBday.getText().length() == 10)
			MainController.editedAuthor.setDateOfBirth(authorBday.getText() == null ? LocalDate.MIN : LocalDate.parse(authorBday.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		} });
		
	}	//end of startListening method

}	//end of AuthorDetailController class
