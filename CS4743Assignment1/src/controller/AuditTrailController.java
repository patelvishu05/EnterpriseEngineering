package controller;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.AuditTrailEntry;
import model.Book;
import model.ViewType;

public class AuditTrailController implements MyController, Initializable
{
	@FXML private Label bookTitleLabel;
	@FXML private Button back;
	@FXML private TableView<AuditTrailEntry> auditTrailTable;
	@FXML private TableColumn<AuditTrailEntry,String> message;
	@FXML private TableColumn<AuditTrailEntry,LocalDateTime> timeStamp;
		
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{
		
	}
	
	@FXML
	void clickedBack(ActionEvent event)
	{
		//TODO: Remember to go back to the book that was already open
		MainController.getInstance().switchView(ViewType.VIEW2, new Book());
	}
	
}
