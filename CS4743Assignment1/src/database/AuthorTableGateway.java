package database;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import controller.MainController;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.Author;
import model.AuthorBook;
import model.Book;

/**
 * AuthorTableGateway handles all tasks pertaining to the
 * Authordatabase and perform tasks accordingly.
 * Tasks include create, read, user and update 
 * 
 * @author Vishalkumar Patel
 * @author Juan-Diaz Sada
 *
 */
public class AuthorTableGateway 
{
	private static AuthorTableGateway instance;	
	private static Logger logger = LogManager.getLogger(AuthorTableGateway.class);
	private Connection connection;

	private AuthorTableGateway() {
	}

	public static AuthorTableGateway getInstance() {
		if(instance == null)
			instance = new AuthorTableGateway();
		return instance;
	}

	/**
	 * getAuthors is the read part of CRUD of our application 
	 * that reads in all authors from the database and
	 * saves it to a list and sends it to the callee.
	 * @return authors- list of authors
	 */
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
			if(st!=null)
				st = null;
		}
		return authorList;
	}

	/**
	 * addAuthorToBook helps associate author to existing books
	 * @param ab
	 */
	public void addAuthorToBook(AuthorBook ab) {
		PreparedStatement ps = null;
		ResultSet rs= null;
		try {
			String dbQuery = "INSERT INTO `author_book` (`author_id`, `book_id`, `royalty`) VALUES (?, ?, ?);";
			String dbQuery2 = "INSERT INTO book_audit_trail (`book_id` ,`entry_msg`) VALUES (?,?);";

			ps = connection.prepareStatement(dbQuery,Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, ab.getAuthor().getId());
			ps.setInt(2, ab.getBook().getId());
			ps.setBigDecimal(3, new BigDecimal(((double)ab.getRoyalty()) / 100000));		
			ps.executeUpdate();

			rs = ps.getGeneratedKeys();
			rs.next();
			ps = connection.prepareStatement(dbQuery2);
			ps.setInt(1,ab.getBook().getId());
			if(MainController.royaltyChanged)
				ps.setString(2, MainController.rab.getAuthor().getFirstName() + " " + 
								MainController.rab.getAuthor().getLastName() + "'s royalty " +
								"changed from " + (((double)MainController.rab.getRoyalty()) / 100000) + "%" + 
								" to " + (((double)ab.getRoyalty()) / 100000) + "%");
			else
				ps.setString(2, ab.getAuthor().getFirstName() + " " + ab.getAuthor().getLastName() + " Author Added !");
			System.out.println(ps.toString());
			ps.executeUpdate();
			rs.close();
			ps.close();

		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null)
				rs = null;
			if(ps != null)
				ps = null;
		}
	}
	
	/**
	 * deleteAuthorForBook helps dissociate author from the selected book
	 * @param book
	 */
	public void deleteAuthorForBook(AuthorBook book)
	{
		String dbQuery = "DELETE FROM `author_book` WHERE `author_id` = ? AND `book_id` = ?;";
		String dbQuery2 = "INSERT INTO book_audit_trail (`book_id` ,`entry_msg`) VALUES (?,?);";
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			ps = connection.prepareStatement(dbQuery,Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, book.getAuthor().getId());
			ps.setInt(2, book.getBook().getId());
			ps.executeUpdate();
			
			rs = ps.getGeneratedKeys();
			rs.next();
			ps = connection.prepareStatement(dbQuery2);
			ps.setInt(1,book.getBook().getId());
			if(MainController.royaltyChanged == false)
				ps.setString(2, book.getAuthor().getFirstName() + " " + book.getAuthor().getLastName() + " Author Deleted !");
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
			if(ps != null)
				ps = null;
			if(rs != null)
				rs = null;
		}
		logger.info("Author Deleted for Book=" + book.getBook().getTitle());
	}

	/**
	 * isDuplicateRecord method helps check if duplicate records 
	 * exist in database
	 * @param ab
	 * @return
	 */
	public boolean isDuplicateRecord(AuthorBook ab) 
	{
		PreparedStatement st = null;
		ResultSet rs = null;
		boolean flag = false;
		AuthorBook temp = null;
		try 
		{
			String dbQuery = "SELECT * FROM `author_book` WHERE `author_id` = ? AND`book_id` = ?;";
			st = this.connection.prepareStatement(dbQuery);
			st.setInt(1, ab.getAuthor().getId());
			st.setInt(2, ab.getBook().getId());
			rs = st.executeQuery();

			if(rs.next())
			{
				temp = new AuthorBook();
				double royalty = Double.parseDouble(rs.getString("royalty")) * 100000;
				temp.setRoyalty((int) royalty );
				if(temp.getRoyalty() == ab.getRoyalty())
					flag = true;
			}
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
			if(st!=null)
				st = null;
		}
		return flag;
	}

	/**
	 * insertAuthor helps insert authors to author database
	 * @param a
	 */
	public void insertAuthor(Author a)
	{
		String dbQuery = "INSERT INTO `AuthorDatabase` (`author_id`, `first_name`, `last_name`, `dob`, `gender`, `web_site`) VALUES (?,?,?,?,?,?)";		
		PreparedStatement ps = null;
		try
		{
			ps = connection.prepareStatement(dbQuery,Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, a.getId());
			ps.setString(2, a.getFirstName());
			ps.setString(3, a.getLastName());
			ps.setDate(4, Date.valueOf(a.getDateOfBirth()));
			ps.setString(5, a.getGender());
			ps.setString(6, a.getWebsite());
			ps.executeUpdate();
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
		logger.info("Book Created: id=" + a.getId() + "\tName= " + a.getFirstName() + " " + a.getLastName());
	}
	
	/**
	 * deleteAuthor helps delete author from author database
	 * @param a
	 */
	public void deleteAuthor(Author a)
	{
		PreparedStatement ps = null;
		try
		{
			String dbQuery = "DELETE from `AuthorDatabase` WHERE author_id = ?;";
			
			ps = this.connection.prepareStatement(dbQuery);
			ps.setInt(1, a.getId());
			ps.executeUpdate();
			ps.close();
		}
		catch(SQLException e)
		{
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Delete Error !!");
			alert.setContentText("Cannot delete this author because he is " + 
					"associated with atleast 1 book");
			alert.showAndWait();
		}
		finally
		{
			if(ps!=null)
				ps = null;
		}
	}
	
	/**
	 * updateAuthor helps mak changes to author in the author database
	 * for the selected author
	 * @param a
	 */
	public void updateAuthor(Author a)
	{
		PreparedStatement ps = null;
		try
		{
			String dbQuery = "UPDATE `AuthorDatabase` SET"
					+ "`first_name` = ?, "
					+ "`last_name` = ?, "
					+ "`dob` = ?, "
					+ "`gender` = ?, "
					+ "`web_site` = ?"
					+ " WHERE (`author_id` = ?);";
			ps = this.connection.prepareStatement(dbQuery);
			ps.setString(1, a.getFirstName());
			ps.setString(2, a.getLastName());
			ps.setDate(3, Date.valueOf(a.getDateOfBirth()));
			ps.setString(4, a.getGender());
			ps.setString(5, a.getWebsite());
			ps.setInt(6, a.getId());
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
	}

	//--------------ACCESSORS--------------//

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

}
