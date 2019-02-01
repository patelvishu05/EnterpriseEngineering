package controller;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import model.Book;
import model.ViewType;

public class BookListController implements MyController
{
	@FXML private ListView<Book> booklist;
	private ArrayList<Book> bookArrayList;
	private ObservableList<Book> booksObservableList = FXCollections.observableArrayList();
	private static Logger logger = LogManager.getLogger(BookListController.class);
	
	public void initialize() 
	{
		Book book1 = new Book("Harry Potter I", "J.K. Rowling", "Fiction", "ISBN"); 
		Book book2 = new Book("Harry Potter II", "J.K. Rowling", "Fiction", "ISBN"); 
		
		bookArrayList = new ArrayList<Book>(); 
		bookArrayList.add(book1); 
		bookArrayList.add(book2);
		booksObservableList.addAll(bookArrayList);	
		booklist.setItems(booksObservableList);	
				
	 }
	
	@FXML
    public void handleBookClick(MouseEvent event) 
	{
		//Handles mouse click events perfectly and 
		//switches view to detailed view only if
		//you double click on the book
		String bookSelected = booklist.getSelectionModel().getSelectedItem().toString();
		if(event.getClickCount() == 2)
		{
			logger.info(bookSelected + " book Selected.");
			for(Book book: booksObservableList)
			{
				if (book.getTitle().equals(bookSelected))
				{
					BookDetailViewController.book = book;
					MainController.getInstance().switchView(ViewType.VIEW2);
				}
			}
		}
	}    	
}
