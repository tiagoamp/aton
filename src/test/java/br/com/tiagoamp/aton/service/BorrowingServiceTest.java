package br.com.tiagoamp.aton.service;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.easymock.TestSubject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.tiagoamp.aton.TestHelper;
import br.com.tiagoamp.aton.dao.BorrowingDAO;
import br.com.tiagoamp.aton.model.AtonBOException;
import br.com.tiagoamp.aton.model.Borrowing;

public class BorrowingServiceTest {

	@Mock
	private BorrowingDAO daoMock;
	
	@TestSubject
	private BorrowingService service;
	
	private Borrowing borrow;

	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);		
		service = new BorrowingService();		
		service.setDao(daoMock);
		borrow = TestHelper.getBorrowingForTest();
		borrow.setId(1);
	}

	@After
	public void tearDown() throws Exception {
	}
	

	@Test
	public void testInsert_shouldInsertEntity() throws SQLException, AtonBOException {
		service.insert(borrow);
		verify(daoMock).create(borrow);
	}
	
	@Test
	public void testUpdate_shouldUpdateEntity() throws AtonBOException, SQLException {
		service.update(borrow);
		verify(daoMock).update(borrow);
	}
	
	@Test
	public void testDelete_shouldDeleteEntity() throws AtonBOException, SQLException {
		service.delete(borrow.getId());
		verify(daoMock).delete(borrow.getId());
	}

	@Test
	public void testFindById_shouldReturnValidOutput() throws SQLException, AtonBOException {
		int id = borrow.getId();
		when(daoMock.findById(id)).thenReturn(borrow);
		
		borrow = service.findById(id);
		
		assertNotNull(borrow);
		verify(daoMock).findById(id);
	}

	@Test
	public void testFindByFields_shouldReturnValidOutput() throws SQLException, AtonBOException {
		List<Borrowing> list = new ArrayList<>();
		when(daoMock.findByFields(borrow.getBook().getId(), borrow.getPerson().getId(), borrow.getDateOfBorrowing(), borrow.getDateOfReturn())).thenReturn(list);
		
		list = service.findByFields(borrow.getBook().getId(), borrow.getPerson().getId(), borrow.getDateOfBorrowing(), borrow.getDateOfReturn());
		
		assertNotNull(list);
		verify(daoMock).findByFields(borrow.getBook().getId(), borrow.getPerson().getId(), borrow.getDateOfBorrowing(), borrow.getDateOfReturn());
	}

	@Test
	public void testGetAll_shouldReturnValidOutput() throws AtonBOException, SQLException {
		List<Borrowing> list = new ArrayList<>();
		when(daoMock.findAll()).thenReturn(list);
		
		list = service.getAll();
		
		assertNotNull(list);
		verify(daoMock).findAll();
	}

	@Test
	public void testGetOpenBorrowing_shouldReturnValidOutput() throws SQLException, AtonBOException {
		List<Borrowing> list = new ArrayList<>();
		when(daoMock.findOpenBorrowings()).thenReturn(list);
		
		list = service.getOpenBorrowings();
		
		assertNotNull(list);
		verify(daoMock).findOpenBorrowings();
	}

}
