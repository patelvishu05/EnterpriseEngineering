package database;

import java.sql.*;
import java.util.ArrayList;
import model.Book;

public class BookGateway 
{
	private static BookGateway instance = null;
	private Connection connection;
	
	private BookGateway() 
	{		
	}
	
	public static BookGateway getInstance()
	{
		if(instance == null)
			instance = new BookGateway();
		return instance;
	}
	
	public ArrayList<Book> getBooks()
	{
		ArrayList<Book> books = new ArrayList<Book>();
		ResultSet rs = null;
		Statement statement = null;
		Book book = null;
		try
		{
			statement = this.connection.createStatement();
            rs = statement.executeQuery("SELECT * FROM BookDatabase");
            while(rs.next())
            {
            	book = new Book();
              	book.setId(Integer.parseInt(rs.getString("id")));
            	book.setTitle(rs.getString("title"));
            	book.setSummary(rs.getString("summary"));
            	book.setYear(Integer.parseInt(rs.getString("year_published")));
            	book.setPublisher(Integer.parseInt(rs.getString("publisher_id")));
            	book.setISBN(rs.getString("isbn"));
            	books.add(book);
            }
            rs.close();
            statement.close();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(rs!=null)
				rs = null;
			if(statement!=null)
				statement = null;
		}
				
		
		//TODO: do something here something cool
		return books;
	}
	
	public void delete(Book book)
	{
		String dbQuery = "DELETE FROM BookDatabase WHERE (`id` = ?);";
		PreparedStatement ps = null;
		
		try
		{
			ps = connection.prepareStatement(dbQuery);
			ps.setInt(1, book.getId());
			ps.executeUpdate();
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(ps != null)
				ps = null;
		}
	}
	
	public void insert(Book book)
	{
		//TODO: insert book into databases
		String dbQuery = "INSERT INTO BookDatabase (`id`, `title`, `summary`, `year_published`, `publisher_id`, `isbn`) "
				+ "VALUES (?,?,?,?,?,?);";
		PreparedStatement ps = null;
		try
		{
			ps = connection.prepareStatement(dbQuery);
			ps.setInt(1, book.getId());
			ps.setString(2, book.getTitle());
			ps.setString(3, book.getSummary());
			ps.setInt(4, book.getYear());
			ps.setInt(5, book.getPublisher());
			ps.setString(6, book.getISBN());
			ps.executeUpdate();
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(ps!=null)
				ps = null;
		}
		
		
	}
	
	//--------------ACCESSORS--------------//
	public Connection getConnection()
	{
		return this.connection;
	}
	
	public void setConnection(Connection connection)
	{
		this.connection = connection;
	}

}
