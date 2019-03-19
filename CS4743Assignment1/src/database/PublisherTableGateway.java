package database;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.*;

import model.Book;
import model.Publisher;

public class PublisherTableGateway 
{
	private static PublisherTableGateway instance = null;
	private static Logger logger = LogManager.getLogger(PublisherTableGateway.class);
	private Connection connection;
	
	private PublisherTableGateway() {
		
	}
	
	public static PublisherTableGateway getInstance() {
		if(instance == null)
			instance = new PublisherTableGateway();
		return instance;
	}
	
	public List<Publisher> fetchPublishers()
	{
		List<Publisher> publisherList = new ArrayList<Publisher>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		ResultSet rs = null;
		Statement statement = null;
		Publisher publisher = null;
		try
		{
			statement = this.connection.createStatement();
			rs = statement.executeQuery("SELECT * FROM BookPublisher");
			while(rs.next())
			{
				publisher = new Publisher();
				publisher.setPublisherID(Integer.parseInt(rs.getString("id")));
				publisher.setPublisherName(rs.getString("publisher_name"));
				publisher.setDateAdded(LocalDateTime.parse(rs.getString("date_added"),formatter));
				publisherList.add(publisher);
			}
			rs.close();
			statement.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(rs != null)
				rs = null;
			if(statement != null)
				statement = null;
		}
		
		
		return publisherList;
	}
	

}
