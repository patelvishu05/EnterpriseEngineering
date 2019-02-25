package application;
import java.io.FileInputStream;
import java.net.URL;
import java.sql.Connection;
import java.util.Properties;

import com.mysql.cj.jdbc.MysqlDataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import controller.BookDetailViewController;
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
	private static Logger logger = LogManager.getLogger(Main.class);
	
	@Override
	public void start(Stage primaryStage) throws Exception 
	{
		stage = primaryStage;
		URL url = getClass().getResource("../view/MainView.fxml");
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
		logger.info("Creating Connection to the database....");
		Properties properties = new Properties();
		FileInputStream fst = new FileInputStream("./src/database.properties");
		properties.load(fst);
		fst.close();
		
		//creating connection to the database
		MysqlDataSource ds = new MysqlDataSource();
		ds.setURL(properties.getProperty("MYSQL_DB_URL"));
		ds.setUser(properties.getProperty("MYSQL_DB_USERNAME"));
		ds.setPassword(properties.getProperty("MYSQL_DB_PASSWORD"));
		Connection connection = ds.getConnection();
		BookGateway.getInstance().setConnection(connection);
		logger.info("Database Connection created !!");
	}	//end of init method

	@Override
	public void stop() throws Exception 
	{
		super.stop();
		
		//close connection to the database
		logger.info("Closing database connection....."); 
		BookGateway.getInstance().getConnection().close();
		logger.info("Database Connection closed !!");
	}	//end of stop method
	
}	//end of Main class
