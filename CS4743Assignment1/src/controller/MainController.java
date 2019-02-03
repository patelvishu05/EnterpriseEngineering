package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import application.Main;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import model.ViewType;

/**
 * MainController handles all events occuring
 * on the Main view
 * 
 * @author Vishalkumar Patel
 * @author Juan-Diaz Sada
 *
 */
public class MainController implements Initializable
{
	@FXML private BorderPane borderPane;
    @FXML private Button exitApp;
    @FXML private Button bookList;
    @FXML private Label displayLabel;
    private static Logger logger = LogManager.getLogger(MainController.class);
	private static MainController instance = null;
	
	//private constructor
	private MainController() {
		
	}
	
	//public getInstance method to return 
	//the instance object, if it does not
	//exist it creates a new instance
	public static MainController getInstance()
	{
		if(instance == null)
			instance = new MainController();
		return instance;
	}
	
	//switchView provided a view 
	//switches to that view accordingly
	public void switchView(ViewType view)
	{
		String viewString="";
		MyController controller = null;
		switch (view)
		{
			case VIEW1: viewString = "../BookListView.fxml";
						setDisplayLabelText("Book List");
						controller = new BookListController();
						break;
						
			case VIEW2: viewString = "../BookDetailView.fxml";
						setDisplayLabelText("Book Detail View");
						controller = new BookDetailViewController();
						break;			
		}
		try
		{
			URL url = this.getClass().getResource(viewString);
			FXMLLoader loader = new FXMLLoader(url);
			loader.setController(controller);
			Parent viewNode = loader.load();
			borderPane.setCenter(viewNode);
		}
		catch (IOException e)
		{
			logger.error("Error occured in Loading View in switchView()");
			e.printStackTrace();
		}
	}
	
	//whenever a view is changed the Title of the
	//application is changed automatically
	public void setDisplayLabelText(String name)
	{
		displayLabel.setText(name);
		Main.stage.setTitle(name);
		displayLabel.setStyle("-fx-font-size: 5ex");
	}
	
    @FXML
    void clickedBookList(ActionEvent event) 
    {
    	switchView(ViewType.VIEW1);
    }

    @FXML
    void exitApplication(ActionEvent event) 
    {
    	Platform.exit();
    }
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}

	//-------------ACCESSORS-------------//
	public BorderPane getBorderPane() {
		return borderPane;
	}

	public void setBorderPane(BorderPane borderPane) {
		this.borderPane = borderPane;
	}
	
	
}	//end of class MainController