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
	
	@FXML
	void clickedOk(ActionEvent event) 
	{
		MainController.previousAuthor = this.author;
		System.out.println("~~~~~~~~~~~~~>Here");
		System.out.println(MainController.previousAuthor + "-->\t" + MainController.editedAuthor);
		author.save(MainController.previousAuthor, MainController.editedAuthor);
//		String g = authorGender.getText() == null ? "" : authorGender.getText();
//		String w = authorWebsite.getText() == null ? "" : authorWebsite.getText();
//		String ln = authorLastName.getText() == null ? "" : authorLastName.getText();
//		String fn = authorFirstName.getText() == null ? "" : authorFirstName.getText();
//		int id = authorId.getText() == null ? 0 : Integer.parseInt(authorId.getText());
//		LocalDate bd = authorBday.getText() == null ? LocalDate.MIN : LocalDate.parse(authorBday.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//		
//		Author createdAuthor = new Author(id, fn, ln, bd, g, w);
//		AuthorTableGateway.getInstance().insertAuthor(createdAuthor);
//		System.out.println(createdAuthor);
//		Alert alert = new Alert(AlertType.INFORMATION);
//		alert.setHeaderText("Success !!");
//		alert.setContentText("Author " + createdAuthor.getFirstName() + " " + createdAuthor.getLastName() +
//				" successfully added to the Database !!");
//		alert.showAndWait();
//		switchToHomeView();
	}

	@FXML
	void clickedCancel(ActionEvent event) 
	{
		MainController.getInstance().switchView(ViewType.VIEW5, new Book());
	}
	
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
	public void initialize(URL arg0, ResourceBundle arg1) 
	{
		startListening();
		authorId.setText(""+author.getId());
		authorGender.setText(author.getGender());
		authorFirstName.setText(author.getFirstName());
		authorLastName.setText(author.getLastName());
		authorWebsite.setText(author.getWebsite());
		authorBday.setText(author.getDateOfBirth() == LocalDate.MIN ? "" + (("" + author.getDateOfBirth()).replaceAll(".*", "")) : "" + author.getDateOfBirth() );
//		if(this.author == new Author())
//			authorBday.setText(("" + author.getDateOfBirth()).replaceAll(".*", ""));
//		else
//			authorBday.setText("" + author.getDateOfBirth());
	}
	
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
		
	}

}
