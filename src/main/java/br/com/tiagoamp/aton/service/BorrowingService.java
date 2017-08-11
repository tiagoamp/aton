package br.com.tiagoamp.aton.service;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import br.com.tiagoamp.aton.dao.BorrowingDAO;
import br.com.tiagoamp.aton.dao.BorrowingDaoJpa;
import br.com.tiagoamp.aton.dao.JPAUtil;
import br.com.tiagoamp.aton.model.AtonBOException;
import br.com.tiagoamp.aton.model.Borrowing;

/**
 * CRUD and services for 'Borrowing'.
 * 
 * @author tiagoamp
 */
public class BorrowingService {
	
	public BorrowingService() {		
		this.dao = new BorrowingDaoJpa(new JPAUtil().getMyEntityManager());		
	}
	
	private BorrowingDAO dao;
	
	
	public void insert(Borrowing borrowing) throws AtonBOException {
		try {
			dao.create(borrowing);			
		} catch (SQLException e) {
			throw new AtonBOException("Database access error!", e);
		}
	}
	
	public void update(Borrowing borrowing) throws AtonBOException {
		try {
			dao.update(borrowing);			
		} catch (SQLException e) {
			throw new AtonBOException("Database access error!", e);
		}
	}
	
	public void delete(int id) throws AtonBOException {
		try {
			dao.delete(id);
		} catch (SQLException e) {
			throw new AtonBOException("Database access error!", e);
		}
	}
	
	public Borrowing findById(int id) throws AtonBOException {
		try {
			return dao.findById(id);
		} catch (SQLException e) {
			throw new AtonBOException("Database access error!", e);
		}
	}
	
	public List<Borrowing> findByFields(Integer bookId, Integer personId, Date dateOfBorrowing, Date dateOfReturn) throws AtonBOException {
		try {
			return dao.findByFields(bookId, personId, dateOfBorrowing, dateOfReturn);			
		} catch (SQLException e) {
			throw new AtonBOException("Database access error!", e);
		}
	}
	
	public List<Borrowing> getAll() throws AtonBOException {
		try {
			return dao.findAll();
		} catch (SQLException e) {
			throw new AtonBOException("Database access error!", e);
		}
	}
	
	public List<Borrowing> getOpenBorrowings() throws AtonBOException {
		try {
			return dao.findOpenBorrowings();
		} catch (SQLException e) {
			throw new AtonBOException("Database access error!", e);
		}
	}

	
	public BorrowingDAO getDao() {
		return dao;
	}
	public void setDao(BorrowingDAO dao) {
		this.dao = dao;
	}

}
