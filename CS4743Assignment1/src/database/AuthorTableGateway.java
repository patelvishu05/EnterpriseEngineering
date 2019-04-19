package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import model.Author;

public class AuthorTableGateway 
{
	private static AuthorTableGateway instance;	
	private static Logger logger = LogManager.getLogger(AuthorTableGateway.class);
	private Connection connection;
	
	private AuthorTableGateway(){
		
	}
	
	public static AuthorTableGateway getInstance() {
		if(instance == null)
			instance = new AuthorTableGateway();
		return instance;
	}
	
	public List<Author> getAuthors() 
	{
		List<Author> authorList = new ArrayList<Author>();
		Statement st = null;
		ResultSet rs = null;
		Author author = null;
		try 
		{
			String dbQuery = "SELECT * from AuthorDatabase";
			st = this.connection.createStatement();
			rs = st.executeQuery(dbQuery);
			
			while(rs.next())
			{
				author = new Author();
				author.setFirstName(rs.getString("first_name"));
				author.setLastName(rs.getString("last_name"));
				author.setDateOfBirth(LocalDate.parse(rs.getString("dob"), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
				author.setGender(rs.getString("gender"));
				author.setId(Integer.parseInt(rs.getString("author_id")));
				author.setWebsite(rs.getString("web_site"));
				authorList.add(author);
			}
		}
		catch(SQLException e) 
		{
			e.printStackTrace();
		}
		finally
		{
			if(rs!=null)
				rs = null;
			if(st!=null)
				st = null;
		}
		return authorList;
	}

	//--------------ACCESSORS--------------//
	
	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

}
