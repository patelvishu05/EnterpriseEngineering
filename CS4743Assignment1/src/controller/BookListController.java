package controller;

import java.util.ArrayList;
import java.util.Collections;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.ListView;
import model.Book;

public class BookListController 
{
	@FXML private ListView<Book> booklist; 
	private ObservableList<Book> booksObservableList = FXCollections.observableArrayList();
	
	public void initialize() {
		Book book1 = new Book("Harry Potter I", "J.K. Rowling", "genre", "ISBN"); 
		Book book2 = new Book("Harry Potter II", "J.K. Rowling", "genre", "ISBN"); 
		
		ArrayList<Book> bookArrayList = new ArrayList<Book>(); 
		bookArrayList.add(book1); 
		bookArrayList.add(book2);
		for(Book book : bookArrayList) {
			booksObservableList.add(book); 
		
		}
		
		booklist.setItems(booksObservableList);						// populate the ObservableList<Transaction>
				
	 }

	@FXML
	void exitApplication(ActionEvent event) 
	{
		System.exit(0);
	}

}
