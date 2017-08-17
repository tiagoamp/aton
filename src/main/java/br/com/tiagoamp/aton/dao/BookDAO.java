package br.com.tiagoamp.aton.dao;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import br.com.tiagoamp.aton.model.Book;

public interface BookDAO {
	
	/**
	 * Inserts the entity in the database.
	 * 
	 * @param book
	 * @throws SQLException
	 */
	void create(Book book) throws SQLException;
	
	/**
	 * Updates the entity in the database.
	 * 
	 * @param book
	 * @throws SQLException
	 */
	void update(Book book) throws SQLException;
	
	/**
	 * Deletes the entity in the database.
	 * 
	 * @param id
	 * @throws SQLException
	 */
	void delete(int id) throws SQLException;
	
	/**
	 * Search the entity by 'id' in the database.
	 * 
	 * @param id Entity id.
	 * @return Book
	 * @throws SQLException
	 */
	Book findById(int id) throws SQLException;
	
	/**
	 * Search the entity by 'isbn' in the database.
	 * 
	 * @param isbn 
	 * @return Book
	 * @throws SQLException
	 */
	Book findByIsbn(String isbn) throws SQLException;
	
	/**
	 * Search the entity by given parameters in the database.
	 * 
	 * @param title Title of the book (Uses 'like' for partial name search)
	 * @param author Author name of the book (Uses 'like' for partial name search) 
	 * @param isbn 
	 * @param classification 
	 * @param targetAudience
	 * @return List<Book> 
	 */
	List<Book> findByFields(String title, String authorsNameInline, String isbn, String classification, String targetAudience) throws SQLException;
	
	/**
	 * Retrieve all entities from the database.
	 * 
	 * @return List<Book>
	 * @throws SQLException
	 */
	List<Book> findAll() throws SQLException;
	
	/**
	 * Search the entity by 'author name-like' in the database.
	 * 
	 * @param authorName 
	 * @return List<Book> 
	 * @throws SQLException
	 */
	List<Book> findByAuthorNameLike(String authorName) throws SQLException;
	
	/**
	 * Search the entity by 'title-like' in the database.
	 * 
	 * @param title
	 * @return List<Book> 
	 * @throws SQLException
	 */
	List<Book> findByTitleLike(String title) throws SQLException;
	
	/**
	 * Inserts the book cover image in the database.
	 * 
	 * @param mFile Uploaded file.
	 * @param fileName File name.
	 * @return Path 
	 * @throws IOException
	 */
	Path createCapaLivro(MultipartFile mFile, String fileName) throws IOException; 

}
