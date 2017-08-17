package br.com.tiagoamp.aton.service;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.easymock.TestSubject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.tiagoamp.aton.TestHelper;
import br.com.tiagoamp.aton.dao.PersonDAO;
import br.com.tiagoamp.aton.model.AtonBOException;
import br.com.tiagoamp.aton.model.Person;

public class PersonServiceTest {
	
	@Mock
	private PersonDAO daoMock;
	
	@TestSubject
	private PersonService service;
	
	private Person person;

	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);		
		service = new PersonService();		
		service.setDao(daoMock);
		person = TestHelper.getPersonForTest();
		person.setId(1);
	}

	@After
	public void tearDown() throws Exception {
	}

	
	@Test
	public void testInsert_shouldInsertEntity() throws SQLException, AtonBOException {
		when(daoMock.findByEmail(EasyMock.anyString())).thenReturn(null);
		
		service.insert(person);
		
		verify(daoMock).findByEmail(person.getEmail());
		verify(daoMock).create(person);
	}
	
	@Test(expected=AtonBOException.class)
	public void testInsert_existingEmail_shouldReturnException() throws SQLException, AtonBOException {
		when(daoMock.findByEmail(person.getEmail())).thenReturn(person);
		
		service.insert(person);
		
		verify(daoMock).findByEmail(person.getEmail());
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected = AtonBOException.class)
	public void testInsert_databaseError_shouldThrowException() throws SQLException, AtonBOException {
		when(daoMock.findByEmail(person.getEmail())).thenThrow(SQLException.class);
		service.insert(person);		
	}

	@Test
	public void testUpdate_shouldUpdateEntity() throws AtonBOException, SQLException {
		service.update(person);
		verify(daoMock).update(person);
	}
	
	@Test
	public void testDelete_shouldDeleteEntity() throws AtonBOException, SQLException {
		service.delete(person.getId());
		verify(daoMock).delete(person.getId());
	}

	@Test
	public void testFindById_shouldReturnValidOutput() throws SQLException, AtonBOException {
		int id = person.getId();
		when(daoMock.findById(id)).thenReturn(person);
		
		person = service.findById(id);
		
		assertNotNull(person);
		verify(daoMock).findById(id);
	}

	@Test
	public void testFindByEmail_shouldReturnValidOutput() throws SQLException, AtonBOException {
		String email = person.getEmail();
		when(daoMock.findByEmail(email)).thenReturn(person);
		
		person = service.findByEmail(email);
		
		assertNotNull(person);
		verify(daoMock).findByEmail(email);
	}

	@Test
	public void testFindByFields_shouldReturnValidOutput() throws SQLException, AtonBOException {
		List<Person> list = new ArrayList<>();
		when(daoMock.findByFields(person.getName(), person.getPhone(), person.getRole())).thenReturn(list);
		
		list = service.findByFields(person.getName(), person.getPhone(), person.getRole());
		
		assertNotNull(list);
		verify(daoMock).findByFields(person.getName(), person.getPhone(), person.getRole());
	}

	@Test
	public void testFindByName_shouldReturnValidOutput() throws SQLException, AtonBOException {
		String name = person.getName();
		List<Person> list = new ArrayList<>();
		when(daoMock.findByNameLike(name)).thenReturn(list);
		
		list = service.findByName(name);
		
		assertNotNull(list);
		verify(daoMock).findByNameLike(name);
	}

	@Test
	public void testGetAll_shouldReturnValidOutput() throws AtonBOException, SQLException {
		List<Person> list = new ArrayList<>();
		when(daoMock.findAll()).thenReturn(list);
		
		list = service.getAll();
		
		assertNotNull(list);
		verify(daoMock).findAll();
	}

}
