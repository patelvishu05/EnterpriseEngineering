package application;
import java.net.URL;
import controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

/**
 * The Main Launcher class that launches the
 * application
 * @author Vishalkumar Patel
 * @author Juan-Diaz Sada
 *
 */
public class Main extends Application 
{

	//Used for changing the stageTile everytime 
	//a view is changed
	public static Stage stage;
	
	@Override
	public void start(Stage primaryStage) throws Exception 
	{
		stage = primaryStage;
		URL url = getClass().getResource("../MainView.fxml");
		FXMLLoader loader = new FXMLLoader(url);
		MainController controller = MainController.getInstance();
		loader.setController(controller);
		Parent rootNode = loader.load();
		controller.setBorderPane((BorderPane) rootNode);
		stage.setScene(new Scene(rootNode));
		stage.setTitle("Book");
		stage.show();
	}	//end of start method

	public static void main(String[] args) {
		launch(args);
	}
	
//	@Override
//	public void init() throws Exception {
//		// TODO Auto-generated method stub
//		super.init();
//	}
//
//	@Override
//	public void stop() throws Exception {
//		// TODO Auto-generated method stub
//		super.stop();
//	}
	
}	//end of Main class
