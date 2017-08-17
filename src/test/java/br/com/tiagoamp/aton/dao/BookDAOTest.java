package br.com.tiagoamp.aton.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.tiagoamp.aton.TestHelper;
import br.com.tiagoamp.aton.model.Book;
import br.com.tiagoamp.aton.model.Person;

public class BookDAOTest {
	
	private BookDAO bookDao;
	private PersonDAO personDao;
	
	
	@Before
	public void setup() throws ClassNotFoundException {
		instanciateDaoForTests("jpa");
		cleanDatabaseDataForTests();
	}
	
	@After
	public void teardown() {		
		bookDao = null;
		personDao = null;
	}
		
	
	@Test
	public void testCreate_shouldInsertEntity() throws SQLException {
		Book book = TestHelper.getBookForTest();
		Person person = this.insertPersonToRegisterBook();
		book.setRegisterer(person);
		
		bookDao.create(book);
		
		Book bookRetrieved = bookDao.findByIsbn(book.getIsbn());
		assertEquals("Must retrieve inserted entity.", book, bookRetrieved);		
	}
	
	@Test(expected=Exception.class)
	public void testCreate_existingId_shouldThrowsException() throws SQLException {
		Book bookRetrieved = insertBookInDataBaseForTests();
		bookDao = null;
		bookDao = new BookDaoJpa(new JPAUtil().getMyTestsEntityManager());  // new dao with new entity manager
				
		bookDao.create(bookRetrieved); // same id should throw exception		
	}
	
	@Test
	public void testUpdate_shouldUpdateEntity() throws SQLException {
		Book bookRetrieved = insertBookInDataBaseForTests();  // transient --> managed --> detached
		String newTitle = "Title Updated 2".toUpperCase();
		bookRetrieved.setTitle(newTitle);
		
		bookDao.update(bookRetrieved);
		Book bookAfterUpdate = bookDao.findById(bookRetrieved.getId());
		assertNotNull(bookAfterUpdate);
		assertEquals("'Title' should be updated.", newTitle, bookAfterUpdate.getTitle());
	}
	
	@Test
	public void testDelete_shouldDeleteEntity() throws SQLException {
		Book bookRetrieved = insertBookInDataBaseForTests();  // transient --> managed --> detached
		int id = bookRetrieved.getId();
		bookDao.delete(id);
		
		bookRetrieved = bookDao.findById(id);
		assertNull("Entity must be deleted.", bookRetrieved);
	}
	
	@Test
	public void testFindById_shouldReturnValidOutput() throws SQLException {
		Book bookInserted = insertBookInDataBaseForTests();
		
		Book bookRetrievedById = bookDao.findById(bookInserted.getId());
		assertNotNull("Must retrieve the entity by Id.", bookRetrievedById);
		assertEquals("Must retrieve the entity with same 'Id'.", bookInserted.getId(), bookRetrievedById.getId());		
	}
	
	@Test
	public void testFindAll_emptyDataBase_shouldReturnEmptyList() throws SQLException {
		List<Book> list = bookDao.findAll();
		assertTrue("Must not retrieve entities.", list.isEmpty());
	}
	
	@Test
	public void testFindAll_shouldReturnValidOutput() throws SQLException {
		insertBooksListInDataBaseForTests();
		
		List<Book> list = bookDao.findAll();
		assertNotNull("Must return entities previously inserted.", list);
		assertEquals("Should return 3 entities.", 3, list.size());
	}
	
	@Test
	public void testFindByFields_allFieldsMatches_shouldReturnValidOutput() throws SQLException {
		Book book = insertBookInDataBaseForTests();
		
		List<Book> list = bookDao.findByFields(book.getTitle(), book.getAuthors().get(0).getName(), book.getIsbn(), book.getClassification(), book.getTargetAudience());
		assertTrue(!list.isEmpty());
		assertTrue(list.size() == 1);
	}
	
	@Test
	public void testFindByFields_byTitle_shouldReturnValidOutput() throws SQLException {
		Book book = insertBookInDataBaseForTests();
		
		List<Book> list = bookDao.findByFields(book.getTitle(), null, null, null, null);
		assertTrue(!list.isEmpty());
		assertTrue(list.size() == 1);
	}
	
	@Test
	public void testFindByFields_byAuthorName_shouldReturnValidOutput() throws SQLException {
		Book book = insertBookInDataBaseForTests();
		
		List<Book> list = bookDao.findByFields(null, book.getAuthors().get(0).getName(), null, null, null);
		assertTrue(!list.isEmpty());
		assertTrue(list.size() == 1);
	}
	
	@Test
	public void testFindByFields_byIsbn_shouldReturnValidOutput() throws SQLException {
		Book book = insertBookInDataBaseForTests();
		
		List<Book> list = bookDao.findByFields(null, null, book.getIsbn(), null, null);
		assertTrue(!list.isEmpty());
		assertTrue(list.size() == 1);
	}
	
	@Test
	public void testFindByFields_byClassification_shouldReturnValidOutput() throws SQLException {
		Book book = insertBookInDataBaseForTests();
		
		List<Book> list = bookDao.findByFields(null, null, null, book.getClassification(), null);
		assertTrue(!list.isEmpty());
		assertTrue(list.size() == 1);
	}
	
	@Test
	public void testFindByFields_byTargetAudience_shouldReturnValidOutput() throws SQLException {
		Book book = insertBookInDataBaseForTests();
		
		List<Book> list = bookDao.findByFields(null, null, null, null, book.getTargetAudience());
		assertTrue(!list.isEmpty());
		assertTrue(list.size() == 1);
	}

	@Test
	public void testFindByIsbn_shouldReturnValidOutput() throws SQLException {
		Book book = insertBookInDataBaseForTests();
		String isbn = book.getIsbn();
		
		Book bookRetrievedByIsbn = bookDao.findByIsbn(isbn);
		assertNotNull("Must return searched entity.", bookRetrievedByIsbn);
		assertEquals("Must have same 'isbn' value." , isbn, bookRetrievedByIsbn.getIsbn());
	}
	
	@Test
	public void testFindByAuthorNameLike_shouldReturnValidOutput() throws SQLException {
		Book book = insertBookInDataBaseForTests();
		String partialAuthorName = book.getAuthors().get(0).getName().substring(0,6);
		
		List<Book> list = bookDao.findByAuthorNameLike(partialAuthorName);
		assertNotNull("Must return entity by partial 'author name'." , list);
		assertTrue("Must contain entity by partial 'author name'." ,list.contains(book));
	}

	@Test
	public void testFindByTitleLike_shouldReturnValidOutput() throws SQLException {
		Book book = insertBookInDataBaseForTests();
		String partialTitle = book.getTitle().substring(4);
		
		List<Book> list = bookDao.findByTitleLike(partialTitle);
		assertNotNull("Must return entity by partial 'title'." , list);
		assertTrue("Must contain entity by partial 'title'." ,list.contains(book));
	}

	@Test
	public void testCreateCapaLivro() {
		assertTrue("Awaiting DAO implementation.", true);
	}
	
	
	// HELPER METHODS
	
	private void instanciateDaoForTests(String type) {
		if (type == null)
			throw new IllegalArgumentException("JDBC or JPA should be informed as argument!");
		if (type.equals("jdbc")) {
			bookDao = new BookDaoJdbc();
			((BookDaoJdbc) bookDao).setURL_DB("jdbc:sqlite:" + ((BookDaoJdbc) bookDao).getPATH_DB() + "atondbtests");
		} else if (type.equals("jpa")) {
			bookDao = new BookDaoJpa(new JPAUtil().getMyTestsEntityManager());
			personDao = new PersonDaoJpa(new JPAUtil().getMyTestsEntityManager());
		}
	}

	private void cleanDatabaseDataForTests() {
		try {
			List<Book> bookList = bookDao.findAll();
			List<Person> personList = personDao.findAll();
			for (Book book: bookList) {
				bookDao.delete(book.getId());				
			}
			for (Person person : personList) {
				personDao.delete(person.getId());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private Book insertBookInDataBaseForTests() throws SQLException {
		Book book = TestHelper.getBookForTest();
		Person person = this.insertPersonToRegisterBook();
		book.setRegisterer(person);
		bookDao.create(book);
		book = bookDao.findByIsbn(book.getIsbn());  // getting db id
		return book;
	}

	private List<Book> insertBooksListInDataBaseForTests() throws SQLException {
		Book book1 = TestHelper.getBookForTest();
		Book book2 = TestHelper.getBookForTest();
		Book book3 = TestHelper.getBookForTest();
		
		Person person = insertPersonToRegisterBook();
		
		// setting differents 'isbn' for each book
		book1.setIsbn(book1.getIsbn()+ "-book1");
		book1.setRegisterer(person);
		book2.setIsbn(book2.getIsbn()+ "-book2");
		book2.setRegisterer(person);
		book3.setIsbn(book3.getIsbn()+ "-book3");
		book3.setRegisterer(person);
		
		bookDao.create(book1);
		bookDao.create(book2);
		bookDao.create(book3);

		return bookDao.findAll();
	}
	
	private Person insertPersonToRegisterBook() throws SQLException {
		Person person = TestHelper.getPersonForTest();
		personDao.create(person);
		return personDao.findByEmail(person.getEmail());
	}

}
