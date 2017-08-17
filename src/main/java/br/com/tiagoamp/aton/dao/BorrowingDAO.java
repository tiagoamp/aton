package br.com.tiagoamp.aton.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import br.com.tiagoamp.aton.model.Borrowing;

public interface BorrowingDAO {
	
	/**
	 * Inserts the entity in the database.
	 * 
	 * @param borrowing
	 * @throws SQLException	
	 */
	void create(Borrowing borrowing) throws SQLException;
	
	/**
	 * Updates the entity in the database.
	 * 
	 * @param borrowing
	 * @throws SQLException 
	 */
	void update(Borrowing borrowing) throws SQLException;
	
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
	 * @return borrowing 
	 * @throws SQLException
	 */
	Borrowing findById(int id) throws SQLException;
	
	/**
	 * Search the entity by given parameters in the database.
	 * 
	 * @param bookId
	 * @param personId
	 * @param dateOfBorrowing
	 * @param dateOfReturn
	 * 
	 * @throws SQLException
	 * 
	 * @return List<Borrowing> Lista de empréstimos ou null se não existir.
	 */
	List<Borrowing> findByFields(Integer bookId, Integer personId, Date dateOfBorrowing, Date dateOfReturn) throws SQLException;
	
	/**
	 * Retrieve all entities from the database.
	 * 
	 * @return List<Borrowing>
	 * @throws SQLException
	 */
	List<Borrowing> findAll() throws SQLException;
	
	/**
	 * Searchs for open (not returned / no date of return) borrowings in the database.
	 *  
	 * @return List<Borrowing>
	 * @throws SQLException
	 */
	List<Borrowing> findOpenBorrowings() throws SQLException;

}
