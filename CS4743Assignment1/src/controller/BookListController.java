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
		for(Book book : bookArrayList) {
			booksObservableList.add(book); 
		}
		
		booklist.setItems(booksObservableList);						// populate the ObservableList<Transaction>
				
	 }
	
	@FXML
    public void handleBookClick(MouseEvent event) {
        System.out.println(booklist.getSelectionModel().getSelectedItem()); // null in every case
/*try {
            

            FXMLLoader loader = new FXMLLoader(); 
            loader.setLocation(getClass().getResource("/BookListView.fxml"));
           // loader.setController(this);
            //mainView.setCenter(loader.load());
           MainViewController.mainView.getChildren().clear();
           MainViewController.mainView.getChildren().add(loader.load()); 
    
		
        } 
        catch (IOException e) {
            e.printStackTrace();
        }  */ 
		
		
	}
    

	@FXML
	void exitApplication(ActionEvent event) 
	{
		Platform.exit();
	}

}
