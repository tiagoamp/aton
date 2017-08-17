package br.com.tiagoamp.aton.service;

import java.sql.SQLException;
import java.util.List;

import br.com.tiagoamp.aton.dao.JPAUtil;
import br.com.tiagoamp.aton.dao.PersonDAO;
import br.com.tiagoamp.aton.dao.PersonDaoJpa;
import br.com.tiagoamp.aton.model.AtonBOException;
import br.com.tiagoamp.aton.model.Person;
import br.com.tiagoamp.aton.model.Role;

/**
 * CRUD and services for 'Person'.
 * 
 * @author tiagoamp
 */
public class PersonService {
	
	public PersonService() {		
		this.dao = new PersonDaoJpa(new JPAUtil().getMyEntityManager());		
	}
	
	private PersonDAO dao;
	
	public void insert(Person person) throws AtonBOException {
		try {
			Person personRetrieved = dao.findByEmail(person.getEmail());
			if (personRetrieved != null) throw new AtonBOException("'E-mail' já cadastrado!");
			dao.create(person);
		} catch (SQLException e) {
			throw new AtonBOException("Erro no acesso ao banco de dados!", e);
		}
	}
	
	public void update(Person person) throws AtonBOException {
		try {
			dao.update(person);
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
	
	public Person findById(int id) throws AtonBOException {
		try {
			return dao.findById(id);
		} catch (SQLException e) {
			throw new AtonBOException("Erro no acesso ao banco de dados!", e);
		}
	}
	
	public Person findByEmail(String email) throws AtonBOException {
		try {
			return dao.findByEmail(email);
		} catch (SQLException e) {
			throw new AtonBOException("Erro no acesso ao banco de dados!", e);
		}
	}
	
	public List<Person> findByFields(String name, String phone, Role role) throws AtonBOException {
		try {
			return dao.findByFields(name, phone, role);			
		} catch (SQLException e) {
			throw new AtonBOException("Erro no acesso ao banco de dados!", e);
		}
	}
	
	public List<Person> findByName(String name) throws AtonBOException {
		try {
			return dao.findByNameLike(name);			
		} catch (SQLException e) {
			throw new AtonBOException("Erro no acesso ao banco de dados!", e);
		}
	}
	
	public List<Person> getAll() throws AtonBOException {
		try {
			return dao.findAll();
		} catch (SQLException e) {
			throw new AtonBOException("Erro no acesso ao banco de dados!", e);
		}
	}
	
		
	public PersonDAO getDao() {
		return dao;
	}
	public void setDao(PersonDAO dao) {
		this.dao = dao;
	}

}
