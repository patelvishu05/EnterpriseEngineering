package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class BookListController 
{
	@FXML private Button exitApp;
	@FXML private Button bookList;

	@FXML
	void clickedBookList(ActionEvent event) 
	{
		
	}

	@FXML
	void exitApplication(ActionEvent event) 
	{
		System.exit(0);
	}

}
