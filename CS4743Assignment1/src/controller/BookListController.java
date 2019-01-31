package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import model.Book;
import model.ViewType;

public class BookListController implements MyController
{
	@FXML private ListView<Book> booklist; 
	private ObservableList<Book> booksObservableList = FXCollections.observableArrayList();
	
	public void initialize() {
		Book book1 = new Book("Harry Potter I", "J.K. Rowling", "Fiction", "ISBN"); 
		Book book2 = new Book("Harry Potter II", "J.K. Rowling", "Fiction", "ISBN"); 
		
		ArrayList<Book> bookArrayList = new ArrayList<Book>(); 
		bookArrayList.add(book1); 
		bookArrayList.add(book2);
		booksObservableList.addAll(bookArrayList);	
		booklist.setItems(booksObservableList);						// populate the ObservableList<Transaction>
				
	 }
	
	@FXML
    public void handleBookClick(MouseEvent event) 
	{
		//Handles mouse click events perfectly and 
		//switches view to detailed view only if
		//you double click on the book
		String bookSelected = (String) booklist.getSelectionModel().getSelectedItem().toString();
		if(event.getClickCount() == 2)
		{
			MainController.getInstance().switchView(ViewType.VIEW2);
		}
	}    	
}
