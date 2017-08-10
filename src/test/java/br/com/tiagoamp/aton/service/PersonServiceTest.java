package br.com.tiagoamp.aton.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.SQLException;

import org.easymock.EasyMock;
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
	
	private PersonService service;

	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);		
		service = new PersonService();		
		service.setDao(daoMock);		
	}

	@After
	public void tearDown() throws Exception {
	}

	
	@Test
	public void testInsert_shouldReturnValidOutput() throws SQLException, AtonBOException {
		Person person = TestHelper.getPersonForTest();
		when(daoMock.findByEmail(EasyMock.anyString())).thenReturn(null);
		
		service.insert(person);
		
		verify(daoMock).findByEmail(person.getEmail());
		verify(daoMock).create(person);
	}
	
	@Test(expected=AtonBOException.class)
	public void testInsert_eistingEmail_shouldReturnException() {
		
	}

	@Test
	public void testUpdate() {
		fail("Not yet implemented");
	}

	@Test
	public void testDelete() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindById() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindByEmail() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindByFields() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindByName() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAll() {
		fail("Not yet implemented");
	}

}
