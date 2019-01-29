package controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class MainViewController 
{
	@FXML private Button exitApp;
	@FXML private Button bookList;
	@FXML private AnchorPane mainView; 

	@FXML
	void clickedBookList(ActionEvent event) 
	{
		System.out.println("Clicked list view!" );
	 	try {
            

            FXMLLoader loader = new FXMLLoader(); 
            loader.setLocation(getClass().getResource("/BookListView.fxml"));
           // loader.setController(this);
            //mainView.setCenter(loader.load());
           mainView.getChildren().clear();
           mainView.getChildren().add(loader.load()); 
    
		
        } 
        catch (IOException e) {
            e.printStackTrace();
        }   
		
	}
	public void initialize() {
try {
            

            FXMLLoader loader = new FXMLLoader(); 
            loader.setLocation(getClass().getResource("/BookListView.fxml"));
           // loader.setController(this);
            //mainView.setCenter(loader.load());
           mainView.getChildren().clear();
           mainView.getChildren().add(loader.load()); 
    
		
        } 
        catch (IOException e) {
            e.printStackTrace();
        }   
		
		
	}

	@FXML
	void exitApplication(ActionEvent event) 
	{
		System.exit(0);
	}

}
