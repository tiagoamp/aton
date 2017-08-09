package br.com.tiagoamp.aton.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.tiagoamp.aton.TestHelper;
import br.com.tiagoamp.aton.model.Perfil;
import br.com.tiagoamp.aton.model.Person;

public class PersonDAOTest {
	
	private PersonDAO dao;
	
		
	@Before
	public void setup() throws ClassNotFoundException {
		instanciateDaoForTests("jpa");
		cleanDatabaseDataForTests();
	}
	
	@After
	public void teardown() {		
		dao = null;		
	}
	
	
	@Test
	public void testCreate_shouldInsertEntity() throws SQLException {
		Person person = TestHelper.getPersonForTest(); 
		dao.create(person);
		
		Person personRetrieved = dao.findByEmail(person.getEmail());
		assertEquals("Must retrieve inserted entity.", person, personRetrieved);		
	}
	
	@Test(expected=Exception.class)
	public void testCreate_existingId_shouldThrowsException() throws SQLException {
		Person personRetrieved = insertPersonInDataBaseForTests();
		dao = null;
		dao = new PersonDaoJpa(new JPAUtil().getMyTestsEntityManager());  // new dao with new entity manager
				
		dao.create(personRetrieved); // same id should throw exception		
	}
	
	@Test
	public void testUpdate_shouldUpdateEntity() throws SQLException {
		Person personRetrieved = insertPersonInDataBaseForTests();  // transient --> managed --> detached
		String newName = "Name Updated 2".toUpperCase();
		personRetrieved.setName(newName);
		
		dao.update(personRetrieved);
		Person personAfterUpdate = dao.findById(personRetrieved.getId());
		assertNotNull(personAfterUpdate);
		assertEquals("'Name' should be updated.", newName, personAfterUpdate.getName());
	}
	
	@Test
	public void testDelete_shouldDeleteEntity() throws SQLException {
		Person personRetrieved = insertPersonInDataBaseForTests();  // transient --> managed --> detached
		int id = personRetrieved.getId();
		dao.delete(id);
		
		personRetrieved = dao.findById(id);
		assertNull("Entity must be deleted.", personRetrieved);
	}
	
	@Test
	public void testFindById_shouldReturnValidOutput() throws SQLException {
		Person personInserted = insertPersonInDataBaseForTests();
		
		Person personRetrievedById = dao.findById(personInserted.getId());
		assertNotNull("Must retrieve the entity by Id.", personRetrievedById);
		assertEquals("Must retrieve the entity with same 'Id'.", personInserted.getId(), personRetrievedById.getId());		
	}
	
	@Test
	public void testFindAll_emptyDataBase_shouldReturnEmptyList() throws SQLException {
		List<Person> list = dao.findAll();
		assertTrue("Must not retrieve entities.", list.isEmpty());
	}
	
	@Test
	public void testFindAll_shouldReturnValidOutput() throws SQLException {
		insertPeopleListInDataBaseForTests();
		
		List<Person> list = dao.findAll();
		assertNotNull("Must return entities previously inserted.", list);
		assertEquals("Should return 3 entities.", 3, list.size());
	}
	
	@Test
	public void testFindByEmail_shouldReturnValidOutput() throws SQLException {
		Person person = insertPersonInDataBaseForTests();
		String email = person.getEmail();
		
		Person personRetrievedByEmail = dao.findByEmail(email);
		assertNotNull("Must return searched entity.", personRetrievedByEmail);
		assertEquals("Must have same e-mail value." , email, personRetrievedByEmail.getEmail());		
	}
	
	@Test
	public void testFindByNameLike_shouldReturnValidOutput() throws SQLException {
		Person person = insertPersonInDataBaseForTests();
		String partialName = person.getName().substring(4);
		
		List<Person> list = dao.findByNameLike(partialName);
		assertNotNull("Must return entity by partial 'name'." , list);
		assertTrue("Must contain entity by partial 'name'." ,list.contains(person));				
	}
	
	@Test
	public void testFindByFields_allFieldsMatches_shouldReturnValidOutput() throws SQLException {
		Person person = insertPersonInDataBaseForTests();
		
		List<Person> list = dao.findByFields(person.getName(), person.getPhone(), person.getRole());
		assertTrue(!list.isEmpty());
		assertTrue(list.size() == 1);
	}
	
	@Test
	public void testFindByFields_byName_shouldReturnValidOutput() throws SQLException {
		Person person = insertPersonInDataBaseForTests();
		
		List<Person> list = dao.findByFields(person.getName(), null, null);
		assertTrue(!list.isEmpty());
		assertTrue(list.size() == 1);
	}
	
	@Test
	public void testFindByFields_byPhone_shouldReturnValidOutput() throws SQLException {
		Person person = insertPersonInDataBaseForTests();
		
		List<Person> list = dao.findByFields(null, person.getPhone(), null);
		assertTrue(!list.isEmpty());
		assertTrue(list.size() == 1);
	}
	
	@Test
	public void testFindByFields_byRole_shouldReturnValidOutput() throws SQLException {
		Person person = insertPersonInDataBaseForTests();
		
		List<Person> list = dao.findByFields(null, null, person.getRole());
		assertTrue(!list.isEmpty());
		assertTrue(list.size() == 1);
	}
		
		
	// HELPER METHODS
	
	private void instanciateDaoForTests(String type) {
		if (type == null) throw new IllegalArgumentException("JDBC or JPA should be informed as argument!");
		if (type.equals("jdbc")) {
			dao = new PessoaDaoJdbc();
			((PessoaDaoJdbc)dao).setURL_DB("jdbc:sqlite:" + ((PessoaDaoJdbc)dao).getPATH_DB() + "atondbtests");			
		} else if(type.equals("jpa")) {    
			dao = new PersonDaoJpa(new JPAUtil().getMyTestsEntityManager());
		}	
	}
	
	private void cleanDatabaseDataForTests() {
		try {
			List<Person> list = dao.findAll();
			for (Iterator<Person> iterator = list.iterator(); iterator.hasNext();) {
				Person person = (Person) iterator.next();
				dao.delete(person.getId());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private Person insertPersonInDataBaseForTests() throws SQLException {
		Person person = TestHelper.getPersonForTest();
		dao.create(person);
		person = dao.findByEmail(person.getEmail());
		return person;
	}
	
	private List<Person> insertPeopleListInDataBaseForTests() throws SQLException {
		Person person1 = new Person("email1@email.com", "Name 01", "111-222", Perfil.ADMINISTRATOR);
		Person person2 = new Person("email2@email.com", "Name 02", "111-222", Perfil.ADMINISTRATOR);
		Person person3 = new Person("email3@email.com", "Name 03", "111-222", Perfil.ADMINISTRATOR);
		
		dao.create(person1);
		dao.create(person2);
		dao.create(person3);
		
		return dao.findAll();
	}
	
}
