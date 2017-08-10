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
		this.borrowDao = new BorrowingDaoJpa(new JPAUtil().getMyEntityManager());		
	}
	
	private BorrowingDAO borrowDao;
	
	
	public void insert(Borrowing borrowing) throws AtonBOException {
		try {
			borrowDao.create(borrowing);			
		} catch (SQLException e) {
			throw new AtonBOException("Database access error!", e);
		}
	}
	
	public void update(Borrowing borrowing) throws AtonBOException {
		try {
			borrowDao.update(borrowing);			
		} catch (SQLException e) {
			throw new AtonBOException("Database access error!", e);
		}
	}
	
	public void delete(int id) throws AtonBOException {
		try {
			borrowDao.delete(id);
		} catch (SQLException e) {
			throw new AtonBOException("Database access error!", e);
		}
	}
	
	public Borrowing findById(int id) throws AtonBOException {
		try {
			return borrowDao.findById(id);
		} catch (SQLException e) {
			throw new AtonBOException("Database access error!", e);
		}
	}
	
	public List<Borrowing> findByFields(Integer bookId, Integer personId, Date dateOfBorrowing, Date dateOfReturn) throws AtonBOException {
		try {
			return borrowDao.findByFields(bookId, personId, dateOfBorrowing, dateOfReturn);			
		} catch (SQLException e) {
			throw new AtonBOException("Database access error!", e);
		}
	}
	
	public List<Borrowing> getAll() throws AtonBOException {
		try {
			return borrowDao.findAll();
		} catch (SQLException e) {
			throw new AtonBOException("Database access error!", e);
		}
	}
	
	public List<Borrowing> getOpenBorrowings() throws AtonBOException {
		try {
			return borrowDao.findOpenBorrowings();
		} catch (SQLException e) {
			throw new AtonBOException("Database access error!", e);
		}
	}

	
	public BorrowingDAO getBorrowDao() {
		return borrowDao;
	}
	public void setBorrowDao(BorrowingDAO borrowDao) {
		this.borrowDao = borrowDao;
	}

}
