package controller;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.AuditTrailEntry;
import model.Book;
import model.ViewType;

/**
 * AuditTrailController class controls actions that occurs
 * on AuditTrailView and handles events accordingly
 * 
 * @author Vishalkumar Patel
 * @author Juan-Diaz Sada
 *
 */
public class AuditTrailController implements MyController, Initializable
{
	@FXML private Label bookTitleLabel;
	@FXML private Button back;
	public static Book book;
	private List<AuditTrailEntry> audits;
	@FXML private TableView<AuditTrailEntry> auditTrailTable;
	@FXML private TableColumn<AuditTrailEntry,Integer> id;
	@FXML private TableColumn<AuditTrailEntry,String> message;
	@FXML private TableColumn<AuditTrailEntry,LocalDateTime> timeStamp;
	private ObservableList auditObsList;
	private ArrayList<AuditTrailEntry> auditArrayList;
	
	public AuditTrailController(List<AuditTrailEntry> a)
	{
		this.audits = a;
	}
	
	/**
	 * initialize method initialized the view with the provided details
	 * and sets them to be viewed by the user
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) 
	{
		this.bookTitleLabel.setText(book.getTitle());
		
		auditObsList= FXCollections.observableArrayList();
		auditArrayList = new ArrayList<AuditTrailEntry>();
		id.setCellValueFactory(new PropertyValueFactory("id"));
		message.setCellValueFactory(new PropertyValueFactory("message"));
		timeStamp.setCellValueFactory(new PropertyValueFactory("dateAdded"));
		
		auditArrayList.addAll(this.audits);
		auditObsList.addAll(auditArrayList);
		auditTrailTable.setItems(auditObsList);
	}	//end of initialize method
	
	/**
	 * If the user clicks back button go back to the the
	 * book detailview for the book that was open
	 * @param event
	 */
	@FXML
	void clickedBack(ActionEvent event)
	{
		MainController.getInstance().switchView(ViewType.VIEW2, book);
	}	//end of clickedBack method
	
}	//end of AuditTrailController class
