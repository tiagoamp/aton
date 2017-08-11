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
import br.com.tiagoamp.aton.dao.BookDAO;
import br.com.tiagoamp.aton.model.AtonBOException;
import br.com.tiagoamp.aton.model.Book;

public class BookServiceTest {

	@Mock
	private BookDAO daoMock;
	
	@TestSubject
	private BookService service;
	
	private Book book;

	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);		
		service = new BookService();		
		service.setDao(daoMock);
		book = TestHelper.getBookForTest();
		book.setId(1);
	}

	@After
	public void tearDown() throws Exception {
	}
	

	@Test
	public void testInsert_shouldInsertEntity() throws SQLException, AtonBOException {
		when(daoMock.findByIsbn(EasyMock.anyString())).thenReturn(null);
		
		service.insert(book);
		
		verify(daoMock).findByIsbn(book.getIsbn());
		verify(daoMock).create(book);
	}
	
	@Test(expected=AtonBOException.class)
	public void testInsert_existingEmail_shouldReturnException() throws SQLException, AtonBOException {
		when(daoMock.findByIsbn(book.getIsbn())).thenReturn(book);
		
		service.insert(book);
		
		verify(daoMock).findByIsbn(book.getIsbn());
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected = AtonBOException.class)
	public void testInsert_databaseError_shouldThrowException() throws SQLException, AtonBOException {
		when(daoMock.findByIsbn(book.getIsbn())).thenThrow(SQLException.class);
		service.insert(book);		
	}

	@Test
	public void testUpdate_shouldUpdateEntity() throws AtonBOException, SQLException {
		service.update(book);
		verify(daoMock).update(book);
	}
	
	@Test
	public void testDelete_shouldDeleteEntity() throws AtonBOException, SQLException {
		service.delete(book.getId());
		verify(daoMock).delete(book.getId());
	}
	

	@Test
	public void testFindById_shouldReturnValidOutput() throws SQLException, AtonBOException {
		int id = book.getId();
		when(daoMock.findById(id)).thenReturn(book);
		
		book = service.findById(id);
		
		assertNotNull(book);
		verify(daoMock).findById(id);
	}

	@Test
	public void testFindByIsbn_shouldReturnValidOutput() throws SQLException, AtonBOException {
		String isbn = book.getIsbn();
		when(daoMock.findByIsbn(isbn)).thenReturn(book);
		
		book = service.findByIsbn(isbn);
		
		assertNotNull(book);
		verify(daoMock).findByIsbn(isbn);
	}

	@Test
	public void testFindByFields_shouldReturnValidOutput() throws SQLException, AtonBOException {
		List<Book> list = new ArrayList<>();
		when(daoMock.findByFields(book.getTitle(), book.getAuthorsNameInline(), book.getIsbn(), book.getClassification(), book.getTargetAudience())).thenReturn(list);
		
		list = service.findByFields(book.getTitle(), book.getAuthorsNameInline(), book.getIsbn(), book.getClassification(), book.getTargetAudience());
		
		assertNotNull(list);
		verify(daoMock).findByFields(book.getTitle(), book.getAuthorsNameInline(), book.getIsbn(), book.getClassification(), book.getTargetAudience());
	}

	@Test
	public void testGetAll_shouldReturnValidOutput() throws AtonBOException, SQLException {
		List<Book> list = new ArrayList<>();
		when(daoMock.findAll()).thenReturn(list);
		
		list = service.getAll();
		
		assertNotNull(list);
		verify(daoMock).findAll();
	}

	@Test
	public void testFindByAuthorName_shouldReturnValidOutput() throws SQLException, AtonBOException {
		String author = book.getAuthorsNameInline();
		List<Book> list = new ArrayList<>();
		when(daoMock.findByAuthorNameLike(author)).thenReturn(list);
		
		list = service.findByAuthorName(author);
		
		assertNotNull(list);
		verify(daoMock).findByAuthorNameLike(author);
	}

	@Test
	public void testFindByTitle_shouldReturnValidOutput() throws SQLException, AtonBOException {
		String title = book.getTitle();
		List<Book> list = new ArrayList<>();
		when(daoMock.findByTitleLike(title)).thenReturn(list);
		
		list = service.findByTitle(title);
		
		assertNotNull(list);
		verify(daoMock).findByTitleLike(title);
	}

}
