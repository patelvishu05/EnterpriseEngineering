package controller;

import java.util.ArrayList;
import java.util.Collections;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.ListView;
import model.Book;

public class BookDetailViewController implements MyController
{
	
	
	
	public void initialize() {
		
				
	 }

	@FXML
	void exitApplication(ActionEvent event) 
	{
		System.exit(0);
	}

}
