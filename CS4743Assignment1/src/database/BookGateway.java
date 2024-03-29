package database;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import controller.BookDetailViewController;
import controller.MainController;

import javafx.scene.control.Alert;
import model.AuditTrailEntry;
import model.Author;
import model.AuthorBook;
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
	public List<Book> getBooks(int upperLimit, int endLimit)
	{
		List<Book> books = new ArrayList<Book>();
		ResultSet rs = null;
		PreparedStatement statement = null;
		Book book = null;
		try
		{
			String dbQuery = "SELECT * FROM BookDatabase LIMIT ?,?";
			statement = this.connection.prepareStatement(dbQuery);
			statement.setInt(1, upperLimit);
			statement.setInt(2, endLimit);
			rs = statement.executeQuery();
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
				//				System.out.println("Test: " + book.getTitle() + " time in book " + book.getLastModified() );
				books.add(book);
			}
			
			String dbQuery2 = "SELECT COUNT(*) FROM BookDatabase;";
			statement = this.connection.prepareStatement(dbQuery2);
			rs = statement.executeQuery();
			
			rs.next();
			MainController.allSize = rs.getInt(1);
			
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
	 * getAuthorsForBook method helps get AuthorBook from
	 * author_book database
	 * 
	 * @param book
	 * @return
	 */
	public List<AuthorBook> getAuthorsForBook(Book book)
	{
		List<AuthorBook> bookAuthors = new ArrayList<AuthorBook>();

		Book b = null;
		Author a = null;
		AuthorBook ab = null;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		PreparedStatement ps = null;
		ResultSet rs = null;
		String dbQuery = "SELECT * from author_book " + 
				"    INNER JOIN AuthorDatabase AD on author_book.author_id = AD.author_id " + 
				"    INNER JOIN BookDatabase BD on author_book.book_id = BD.id "     +  
				"    WHERE book_id=?;";
		try
		{
			ps = this.connection.prepareStatement(dbQuery);
			ps.setString(1, ""+book.getId());
			rs = ps.executeQuery();

			while(rs.next())
			{
				b = new Book();
				b.setId(Integer.parseInt(rs.getString("id")));
				b.setTitle(rs.getString("title"));
				b.setSummary(rs.getString("summary"));
				b.setYear(Integer.parseInt(rs.getString("year_published")));
				b.setPublisher(Integer.parseInt(rs.getString("publisher_id")));
				b.setISBN(rs.getString("isbn"));
				b.setLastModified(rs.getTimestamp("last_modified").toLocalDateTime());

				a = new Author();
				a.setId(Integer.parseInt(rs.getString("author_id")));
				a.setFirstName(rs.getString("first_name"));
				a.setLastName(rs.getString("last_name"));
				a.setDateOfBirth(LocalDate.parse(rs.getString("dob"), formatter));
				a.setGender(rs.getString("gender"));
				a.setWebsite(rs.getString("web_site"));

				ab = new AuthorBook();
				ab.setAuthor(a);
				ab.setBook(b);
				System.out.println(rs.getString("royalty"));
				double royalty = Double.parseDouble(rs.getString("royalty")) * 100000;
				ab.setRoyalty((int) royalty );

				bookAuthors.add(ab);
			}
			rs.close();
			ps.close();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(rs!=null)
				rs = null;
			if(ps!=null)
				ps = null;
		}

		return bookAuthors;
	}

	/**
	 * getAudit helps get audit trails for book
	 * 
	 * @param book
	 * @return
	 */
	public List<AuditTrailEntry> getAudit(Book book)
	{
		List<AuditTrailEntry> bookAudits = new ArrayList<AuditTrailEntry>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		ResultSet rs = null;
		PreparedStatement ps = null;
		AuditTrailEntry audit = null;
		try
		{
			String dbQuery = "SELECT * FROM book_audit_trail where book_id=? ORDER BY date_added ASC";
			ps = this.connection.prepareStatement(dbQuery);
			ps.setString(1, ""+book.getId());
			rs = ps.executeQuery();
			while(rs.next())
			{
				audit = new AuditTrailEntry();
				audit.setId(Integer.parseInt(rs.getString("id")));
				audit.setMessage(rs.getString("entry_msg"));
				audit.setDateAdded(LocalDateTime.parse(rs.getString("date_added"),formatter));
				bookAudits.add(audit);
			}
			rs.close();
			ps.close();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(rs!=null)
				rs = null;
			if(ps!=null)
				ps = null;
		}
		return bookAudits;
	}


	/**
	 * delete is the delete part of CRUD of our application that
	 * takes in a book as parameter and deletes it from the 
	 * database.
	 * 
	 * @param book
	 */
	public void deleteBook(Book book)
	{
		String dbQuery = "DELETE FROM author_book WHERE book_id = ?;";
		String dbQuery2 = "DELETE FROM BookDatabase WHERE (`id` = ?);";
		PreparedStatement ps = null;
		try
		{
			ps = connection.prepareStatement(dbQuery);
			ps.setInt(1, book.getId());
			ps.executeUpdate();
			
			ps = connection.prepareStatement(dbQuery2);
			ps.setInt(1, book.getId());
			ps.executeUpdate();
			ps.close();
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
				+ "VALUES (?,?,?,?,?,?); ";
		String dbQuery2 = "INSERT INTO book_audit_trail (`book_id` ,`entry_msg`) VALUES (?,?);";
		PreparedStatement ps = null;
		try
		{
			ps = connection.prepareStatement(dbQuery,Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, book.getId());
			ps.setString(2, book.getTitle());
			ps.setString(3, book.getSummary());
			ps.setInt(4, book.getYear());
			ps.setInt(5, book.getPublisher());
			ps.setString(6, book.getISBN());
			ps.executeUpdate();

			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			ps = connection.prepareStatement(dbQuery2);
			ps.setInt(1,rs.getInt(1));
			ps.setString(2, "Book Added !");
			System.out.println(ps.toString());
			ps.executeUpdate();
			rs.close();
			ps.close();
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
			st.close();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			throw new SQLException(e);
		} 
		finally 
		{
			if(st != null)
				st= null;
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

		String dbQuery2= "INSERT INTO book_audit_trail (`book_id` ,`entry_msg`) VALUES (?,?);";

		if(!(MainController.auditChange.equals("")))
		{
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
			ps = connection.prepareStatement(dbQuery2);
			ps.setInt(1,book.getId());
			ps.setString(2,MainController.auditChange);
			ps.executeUpdate();
			ps.close();
		}

		logger.info("Book updated");
	}	//end of update method
	
	/**
	 * searchBooks helps us search for user searched books from
	 * the database and return it to the user
	 * @param searchWord
	 * @return List<Book>
	 */
	public List<Book> searchBooks(String searchWord)
	{
		List<Book> books = new ArrayList<Book>();
		Book book = null;
		ResultSet rs = null;
		PreparedStatement st = null;
		
		try
		{
//			String dbQuery = "SELECT * FROM BookDatabase WHERE title REGEXP ?;";
			String dbQuery = "SELECT * FROM BookDatabase WHERE title LIKE ?;";
			st = this.connection.prepareStatement(dbQuery);
			st.setString(1, searchWord);

			System.out.println(st.toString());		
			rs = st.executeQuery();
				
					
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
				books.add(book);
			}
			
			String dbQuery2 = "SELECT COUNT(*) FROM BookDatabase WHERE title LIKE ?;";
			st = this.connection.prepareStatement(dbQuery2);
			st.setString(1, searchWord);
			rs = st.executeQuery();
			
			rs.next();
			MainController.searchSize = rs.getInt(1);
			System.out.println("*****************" + MainController.searchSize);
			rs.close();
			st.close();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(rs!=null)
				rs = null;
			if(st !=null)
				st = null;
		}
		return books;
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
