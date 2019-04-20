package database;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.sql.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.Author;
import model.AuthorBook;
import model.Book;

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
			ps.setString(2, ab.getAuthor().getFirstName() + " " + ab.getAuthor().getLastName() + " Author Added !");
			System.out.println(ps.toString());
			ps.executeUpdate();
			rs = null;


		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs != null)
				rs = null;
			if(ps != null)
				ps = null;
		}
	}

	public boolean isDuplicateRecord(AuthorBook ab) 
	{
		PreparedStatement st = null;
		ResultSet rs = null;
		boolean flag = false;
		try 
		{
			String dbQuery = "SELECT * FROM `author_book` WHERE `author_id` = ? AND`book_id` = ?;";
			st = this.connection.prepareStatement(dbQuery);
			st.setInt(1, ab.getAuthor().getId());
			st.setInt(2, ab.getBook().getId());
			rs = st.executeQuery();

			if(rs.next())
				flag = true;
		}
		catch(SQLException e) 
		{
			e.printStackTrace();
//			Alert alert = new Alert(AlertType.INFORMATION);
//			alert.setHeaderText("Duplicate Author Entry !!");
//			alert.setContentText("The author " + ab.getAuthor().getFirstName() + " " +
//					ab.getAuthor().getLastName() + " already exists for the book " + 
//					ab.getBook().getTitle());
//			alert.show();
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

	//--------------ACCESSORS--------------//

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

}
