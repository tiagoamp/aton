package br.com.tiagoamp.aton.service;

import java.sql.SQLException;
import java.util.List;

import br.com.tiagoamp.aton.dao.BookDAO;
import br.com.tiagoamp.aton.dao.BookDaoJpa;
import br.com.tiagoamp.aton.dao.JPAUtil;
import br.com.tiagoamp.aton.model.AtonBOException;
import br.com.tiagoamp.aton.model.Book;

/**
 * CRUD and services for 'Book'.
 * 
 * @author tiagoamp
 */
public class BookService {
	
	public BookService() {		
		this.bookDao = new BookDaoJpa(new JPAUtil().getMyEntityManager());		
	}
	
	private BookDAO bookDao;
	
	
	public void insert(Book book) throws AtonBOException {
		try {
			Book bookRetrieved = bookDao.findByIsbn(book.getIsbn());
			if (bookRetrieved != null) throw new AtonBOException("'ISBN' already registered");			
			bookDao.create(book);
		} catch (SQLException e) {
			throw new AtonBOException("Database access error!", e);
		}				
	}
	
	public void update(Book book) throws AtonBOException {
		try {
			bookDao.update(book);
		} catch (SQLException e) {
			throw new AtonBOException("Database access error!", e);
		}
	}
	
	public void delete(int id) throws AtonBOException {
		try {
			bookDao.delete(id);
		} catch (SQLException e) {
			throw new AtonBOException("Database access error!", e);
		}
	}
	
	public Book findById(int id) throws AtonBOException {
		try {
			return bookDao.findById(id);
		} catch (SQLException e) {
			throw new AtonBOException("Database access error!", e);
		}
	}
	
	public Book findByIsbn(String isbn) throws AtonBOException {
		try {
			return bookDao.findByIsbn(isbn);
		} catch (SQLException e) {
			throw new AtonBOException("Database access error!", e);
		}
	}
	
	public List<Book> findByFields(String title, String authorsName, String isbn, String classification, String targetAudience) throws AtonBOException {
		try {
			return bookDao.findByFields(title, authorsName, isbn, classification, targetAudience);
		} catch (SQLException e) {
			throw new AtonBOException("Database access error!", e);
		}
	}
	
	public List<Book> getAll() throws AtonBOException {
		try {
			return bookDao.findAll();
		} catch (SQLException e) {
			throw new AtonBOException("Database access error!", e);
		}
	}
	
	public List<Book> findByAuthorName(String authorName) throws AtonBOException {
		try {
			return bookDao.findByAuthorNameLike(authorName);			
		} catch (SQLException e) {
			throw new AtonBOException("Database access error!", e);
		}
	}
	
	public List<Book> findByTitle(String title) throws AtonBOException {
		try {
			return bookDao.findByTitleLike(title);			
		} catch (SQLException e) {
			throw new AtonBOException("Database access error!", e);
		}
	}
	
	
	public BookDAO getBookDao() {
		return bookDao;
	}
	public void setBookDao(BookDAO bookDao) {
		this.bookDao = bookDao;
	}

}
