package controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import authenticator.AccessPolicy;
import database.AuthorTableGateway;
import database.BookGateway;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import model.Author;
import model.Book;
import model.ViewType;
/**
 * AuthorListController is a controller class and listens / reacts
 * to events happening on AuthorListView and depending on the
 * action carries out various tasks like adding / deleting
 * 
 * @author Vishalkumar Patel
 * @author Juan-Diaz Sada
 */
public class AuthorListController implements Initializable, MyController
{
	@FXML private ListView<Author> authorList;
	@FXML private Button delete;
	ObservableList<Author> oAuthor;
	
	@FXML void handleBookClick(ActionEvent event) {

	}

	/**
	 * deleteAuthor method deletes and author from the database
	 * @param event
	 */
	@FXML
	void deleteAuthor(ActionEvent event) {
		int bookSelected = authorList.getSelectionModel().getSelectedIndex();
		if(bookSelected >= 0 && bookSelected < oAuthor.size())
		{
			AuthorTableGateway.getInstance().deleteAuthor(oAuthor.get(bookSelected));
			helper();
		}
	}
	
	/**
	 * authorListClicked method checks to see if an item inside it
	 * was double clicked if so it allows them to modify the underlying 
	 * author details
	 * 
	 * @param event
	 */
	@FXML
	void authorListClicked(MouseEvent event)
	{
		Author author = authorList.getSelectionModel().getSelectedItem();
		System.out.println(author);
		if(event.getClickCount() == 2 && author != null)
		{
			MainController.selectedAuthor = author;
			MainController.getInstance().switchView(ViewType.VIEW4, new Book());
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		helper();
		if(!MainController.userType.equals(AccessPolicy.ADMIN))
		{
			delete.setDisable(true);
		}
	}
	
	public void helper() {
		List<Author> AuthorL = AuthorTableGateway.getInstance().getAuthors();
		oAuthor = FXCollections.observableArrayList();
		oAuthor.addAll(AuthorL);
		authorList.setItems(oAuthor);
	}

}	//end of class AuthorListController
