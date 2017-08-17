package br.com.tiagoamp.aton.service;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import br.com.tiagoamp.aton.dao.BorrowingDAO;
import br.com.tiagoamp.aton.dao.BorrowingDaoJpa;
import br.com.tiagoamp.aton.dao.JPAUtil;
import br.com.tiagoamp.aton.model.AtonBOException;
import br.com.tiagoamp.aton.model.Book;
import br.com.tiagoamp.aton.model.Borrowing;
import br.com.tiagoamp.aton.model.Status;

/**
 * CRUD and services for 'Borrowing'.
 * 
 * @author tiagoamp
 */
public class BorrowingService {
	
	public BorrowingService() {		
		this.dao = new BorrowingDaoJpa(new JPAUtil().getMyEntityManager());
		this.bookService = new BookService();
	}
	
	private BorrowingDAO dao;
	private BookService bookService;
	
	
	public void insert(Borrowing borrowing) throws AtonBOException {
		try {
			dao.create(borrowing);
			Book book = borrowing.getBook();
			book.setNumberAvailable(book.getNumberAvailable() - 1);
			if (book.getNumberAvailable() == 0) book.setStatus(Status.EMPRESTADO);
			bookService.update(book);			
		} catch (SQLException e) {
			throw new AtonBOException("Erro no acesso ao banco de dados!", e);
		}
	}
	
	public void update(Borrowing borrowing) throws AtonBOException {
		try {
			dao.update(borrowing);			
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
	
	public Borrowing findById(int id) throws AtonBOException {
		try {
			return dao.findById(id);
		} catch (SQLException e) {
			throw new AtonBOException("Erro no acesso ao banco de dados!", e);
		}
	}
	
	public List<Borrowing> findByFields(Integer bookId, Integer personId, Date dateOfBorrowing, Date dateOfReturn) throws AtonBOException {
		try {
			return dao.findByFields(bookId, personId, dateOfBorrowing, dateOfReturn);			
		} catch (SQLException e) {
			throw new AtonBOException("Erro no acesso ao banco de dados!", e);
		}
	}
	
	public List<Borrowing> getAll() throws AtonBOException {
		try {
			return dao.findAll();
		} catch (SQLException e) {
			throw new AtonBOException("Erro no acesso ao banco de dados!", e);
		}
	}
	
	public List<Borrowing> getOpenBorrowings() throws AtonBOException {
		try {
			return dao.findOpenBorrowings();
		} catch (SQLException e) {
			throw new AtonBOException("Erro no acesso ao banco de dados!", e);
		}
	}

	
	public BorrowingDAO getDao() {
		return dao;
	}
	public void setDao(BorrowingDAO dao) {
		this.dao = dao;
	}
	public BookService getBookService() {
		return bookService;
	}
	public void setBookService(BookService bookService) {
		this.bookService = bookService;
	}

}
