package application;
import java.io.FileInputStream;
import java.net.URL;
import java.sql.Connection;
import java.util.Properties;

import com.mysql.cj.jdbc.MysqlDataSource;
import com.sun.media.jfxmedia.logging.Logger;

import controller.MainController;
import database.BookGateway;
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
	
	@Override
	public void init() throws Exception 
	{
		super.init();
		Properties properties = new Properties();
		FileInputStream fst = new FileInputStream("./src/database.properties");
		properties.load(fst);
		fst.close();
		
		//TODO: add logger log logic 
		MysqlDataSource ds = new MysqlDataSource();
		ds.setURL(properties.getProperty("MYSQL_DB_URL"));
		ds.setUser(properties.getProperty("MYSQL_DB_USERNAME"));
		ds.setPassword(properties.getProperty("MYSQL_DB_PASSWORD"));
		Connection connection = ds.getConnection();
		BookGateway.getInstance().setConnection(connection);
	}

	@Override
	public void stop() throws Exception {
		super.stop();
		//TODO: add logger log logic 
		BookGateway.getInstance().getConnection().close();
		
	}
	
}	//end of Main class
