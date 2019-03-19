package database;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import controller.BookDetailViewController;
import controller.MainController;

import javafx.scene.control.Alert;

import model.Book;

/**
 * BookGateway handles all tasks pertaining to the
 * database and perform tasks accordingly.
 * Tasks include create, read, user and update 
 * 
 * @author Vishalkumar Patel
 * @author Juan-Diaz Sada
 *
 */
public class BookGateway 
{
	private static BookGateway instance = null;
	private static Logger logger = LogManager.getLogger(BookGateway.class);
	private Connection connection;

	private BookGateway() {		
	}

	public static BookGateway getInstance()
	{
		if(instance == null)
			instance = new BookGateway();
		return instance;
	}

	/**
	 * getBooks is the read part of CRUD of our application 
	 * that reads in all books from the database and
	 * saves it to a list and sends it to the callee.
	 * @return books- list of books
	 */
	public List<Book> getBooks()
	{
		List<Book> books = new ArrayList<Book>();
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
				book.setLastModified(rs.getTimestamp("last_modified").toLocalDateTime());
				System.out.println("Test: " + book.getTitle() + " time in book " + book.getLastModified() );
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
		return books;
	}	//end of getBooks method

	/**
	 * delete is the delete part of CRUD of our application that
	 * takes in a book as parameter and deletes it from the 
	 * database.
	 * 
	 * @param book
	 */
	public void deleteBook(Book book)
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
		logger.info("Book Deleted: id=" + book.getId() + "\ttitle= " + book.getTitle());
	}	//end of delete method

	/**
	 * insert method receives a book parameter and
	 * creates a new entry for that book in to the
	 * book table in the database
	 * 
	 * @param book- Book
	 */
	public void insertBook(Book book)
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
		logger.info("Book Created: id=" + book.getId() + "\ttitle= " + book.getTitle());
	}	//end of insert method

	
	public LocalDateTime getBookLastModifiedById(int id) throws SQLException 
	{
		LocalDateTime date = null;
		PreparedStatement st = null;
		try 
		{
			st = connection.prepareStatement("select * from BookDatabase where id = ?");
			st.setInt(1, id);
			ResultSet rs = st.executeQuery();
			rs.next();
			Timestamp ts = rs.getTimestamp("last_modified");
			date = ts.toLocalDateTime();
			
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			throw new SQLException(e);
		} 
		finally 
		{
			try 
			{
				if(st != null)
					st.close();
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
				throw new SQLException(e);
			}
		}

		return date;
	}
	
	
	
	
	
	/**
	 * update method receives a book  in paramater and make
	 * updates necessary components of the book in the book
	 * table database, so that any changes made will be reflected
	 * throughout the application
	 * 
	 * @param book
	 */
	public void updateBook(Book book) throws SQLException
	{
		//First check whether time stamps match 
		LocalDateTime currentTimestamp = getBookLastModifiedById(book.getId()); 
		
		if(!currentTimestamp.equals(book.getLastModified())) 
		{
			System.out.println("currentTimestamp:"+ currentTimestamp + "bookTimestamp:\n" + book.getLastModified() + "\n" );
			BookDetailViewController.displaySaveErrorAlert(); 
			return;
		}
		
		
		String dbQuery = "UPDATE BookDatabase SET "
				+ "`title` = ?, "
				+ "`summary` = ?, "
				+ "`year_published` = ?, "
				+ "`publisher_id` = ?, "
				+ "`isbn` = ? "
				+ "WHERE (`id` = ?)";

		PreparedStatement ps = null;
		ps = connection.prepareStatement(dbQuery);
		ps.setString(1, book.getTitle());
		ps.setString(2, book.getSummary());
		ps.setInt(3, book.getYear());
		ps.setInt(4, book.getPublisher());
		ps.setString(5, book.getISBN());
		ps.setInt(6, book.getId());
//		System.out.println(ps.toString());
		ps.executeUpdate();


		logger.info("Book updated");
	}	//end of update method

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
