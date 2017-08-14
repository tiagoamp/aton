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
		this.dao = new BookDaoJpa(new JPAUtil().getMyEntityManager());		
	}
	
	private BookDAO dao;
	
	
	public void insert(Book book) throws AtonBOException {
		try {
			Book bookRetrieved = dao.findByIsbn(book.getIsbn());
			if (bookRetrieved != null) throw new AtonBOException("'ISBN' já cadastrado!");			
			dao.create(book);
		} catch (SQLException e) {
			throw new AtonBOException("Erro no acesso ao banco de dados!", e);
		}				
	}
	
	public void update(Book book) throws AtonBOException {
		try {
			dao.update(book);
		} catch (SQLException e) {
			throw new AtonBOException("Erro no acesso ao banco de dados!", e);
		}
	}
	
	public void delete(int id) throws AtonBOException {
		try {
			dao.delete(id);
		} catch (SQLException e) {
			throw new AtonBOException("Erro no acesso ao banco de dados!", e);
		}
	}
	
	public Book findById(int id) throws AtonBOException {
		try {
			return dao.findById(id);
		} catch (SQLException e) {
			throw new AtonBOException("Erro no acesso ao banco de dados!", e);
		}
	}
	
	public Book findByIsbn(String isbn) throws AtonBOException {
		try {
			return dao.findByIsbn(isbn);
		} catch (SQLException e) {
			throw new AtonBOException("Erro no acesso ao banco de dados!", e);
		}
	}
	
	public List<Book> findByFields(String title, String authorsName, String isbn, String classification, String targetAudience) throws AtonBOException {
		try {
			return dao.findByFields(title, authorsName, isbn, classification, targetAudience);
		} catch (SQLException e) {
			throw new AtonBOException("Erro no acesso ao banco de dados!", e);
		}
	}
	
	public List<Book> getAll() throws AtonBOException {
		try {
			return dao.findAll();
		} catch (SQLException e) {
			throw new AtonBOException("Erro no acesso ao banco de dados!", e);
		}
	}
	
	public List<Book> findByAuthorName(String authorName) throws AtonBOException {
		try {
			return dao.findByAuthorNameLike(authorName);			
		} catch (SQLException e) {
			throw new AtonBOException("Erro no acesso ao banco de dados!", e);
		}
	}
	
	public List<Book> findByTitle(String title) throws AtonBOException {
		try {
			return dao.findByTitleLike(title);			
		} catch (SQLException e) {
			throw new AtonBOException("Erro no acesso ao banco de dados!", e);
		}
	}
	
	
	public BookDAO getDao() {
		return dao;
	}
	public void setDao(BookDAO dao) {
		this.dao = dao;
	}

}
