package br.com.tiagoamp.aton.dao;

import java.sql.SQLException;
import java.util.List;

import br.com.tiagoamp.aton.model.Perfil;
import br.com.tiagoamp.aton.model.Person;

public interface PersonDAO {
	
	/**
	 * Inserts the entity in the database.
	 * 
	 * @param person
	 * @throws SQLException 
	 */
	void create(Person person) throws SQLException;
	
	/**
	 * Updates the entity in the database.
	 * 
	 * @param person
	 * @throws SQLException 
	 */
	void update(Person person) throws SQLException;
	
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
	 * @return Person 
	 * @throws SQLException 
	 */
	Person findById(int id) throws SQLException;
	
	/**
	 * Search the entity by 'e-mail' in the database.
	 * 
	 * @param email
	 * @return Person
	 * @throws SQLException
	 */
	Person findByEmail(String email) throws SQLException;
	
	/**
	 * Search the entity by 'name-like' in the database.
	 * 
	 * @param name
	 * @return List<Pessoa> List of entity
	 * @throws SQLException
	 */
	List<Person> findByNameLike(String name) throws SQLException;
	
	/**
	 * Search the entity by given parameters in the database.
	 * 
	 * @param name Name of the person (Uses 'like' for partial name search)
	 * @param phone Phone number literal representation
	 * @param role Role
	 * @return List<Person> 
	 * @throws SQLException
	 */
	List<Person> findByFields(String name, String phone, Perfil role) throws SQLException;
	
	/**
	 * Retrieve all entities from the database.
	 * 
	 * @return List<Person> List of entity
	 * @throws SQLException
	 */
	List<Person> findAll() throws SQLException;

}
