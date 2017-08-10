package br.com.tiagoamp.aton.dao;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.tiagoamp.aton.TestHelper;
import br.com.tiagoamp.aton.model.Book;
import br.com.tiagoamp.aton.model.Borrowing;
import br.com.tiagoamp.aton.model.Person;

public class BorrowingDAOTest {

	private BookDAO bookDao;
	private PersonDAO personDao;
	private BorrowingDAO borrowDao;
	
	
	@Before
	public void setup() throws ClassNotFoundException {
		instanciateDaoForTests("jpa");
		cleanDatabaseDataForTests();
	}
	
	@After
	public void teardown() {		
		bookDao = null;
		personDao = null;
		borrowDao = null;
	}
	
	
	@Test
	public void testCreate_shouldInsertEntity() throws SQLException {
		Borrowing borrow = TestHelper.getBorrowingForTest();
		Person person = this.insertPersonToBorrowBook();
		Book book = insertBookToBeBorrowed();
		borrow.setPerson(person);
		borrow.setBook(book);
		
		borrowDao.create(borrow);
		
		List<Borrowing> list = borrowDao.findByFields(book.getId(), person.getId(), borrow.getDateOfBorrowing(), borrow.getDateOfReturn());
		Borrowing borrowingRetrieved = list.get(0); 
		assertEquals("Must retrieve inserted entity.", borrow, borrowingRetrieved);		
	}
	
	@Test(expected=Exception.class)
	public void testCreate_existingId_shouldThrowsException() throws SQLException {
		Borrowing borrowRetrieved = insertBorrowingInDataBaseForTests();
		borrowDao = null;
		borrowDao = new BorrowingDaoJpa(new JPAUtil().getMyTestsEntityManager());  // new dao with new entity manager
				
		borrowDao.create(borrowRetrieved); // same id should throw exception		
	}

	@Test
	public void testUpdate_shouldUpdateEntity() throws SQLException {
		Borrowing borrowRetrieved = insertBorrowingInDataBaseForTests();  // transient --> managed --> detached
		Date newDate = new Date();
		borrowRetrieved.setDateOfBorrowing(newDate);
		
		borrowDao.update(borrowRetrieved);
		Borrowing borrowingAfterUpdate = borrowDao.findById(borrowRetrieved.getId());
		assertNotNull(borrowingAfterUpdate);
		assertEquals("'Borrowing date' should be updated.", newDate, borrowingAfterUpdate.getDateOfBorrowing());
	}
	
	@Test
	public void testDelete_shouldDeleteEntity() throws SQLException {
		Borrowing borrowRetrieved = insertBorrowingInDataBaseForTests();  // transient --> managed --> detached
		int id = borrowRetrieved.getId();
		borrowDao.delete(id);
		
		borrowRetrieved = borrowDao.findById(id);
		assertNull("Entity must be deleted.", borrowRetrieved);
	}
	
	@Test
	public void testFindById_shouldReturnValidOutput() throws SQLException {
		Borrowing borrowingInserted = insertBorrowingInDataBaseForTests();
		
		Borrowing borrowingRetrievedById = borrowDao.findById(borrowingInserted.getId());
		assertNotNull("Must retrieve the entity by Id.", borrowingRetrievedById);
		assertEquals("Must retrieve the entity with same 'Id'.", borrowingInserted.getId(), borrowingRetrievedById.getId());		
	}

	@Test
	public void testFindByFields_allFieldsMatches_shouldReturnValidOutput() throws SQLException {
		Borrowing borrow = insertBorrowingInDataBaseForTests();
		
		List<Borrowing> list = borrowDao.findByFields(borrow.getBook().getId(), borrow.getPerson().getId(), borrow.getDateOfBorrowing(), borrow.getDateOfReturn());
		assertTrue(!list.isEmpty());
		assertTrue(list.size() == 1);
	}
	
	@Test
	public void testFindByFields_byBookId_shouldReturnValidOutput() throws SQLException {
		Borrowing borrow = insertBorrowingInDataBaseForTests();
		
		List<Borrowing> list = borrowDao.findByFields(borrow.getBook().getId(), null, null, null);
		assertTrue(!list.isEmpty());
		assertTrue(list.size() == 1);
	}
	
	@Test
	public void testFindByFields_byPersonId_shouldReturnValidOutput() throws SQLException {
		Borrowing borrow = insertBorrowingInDataBaseForTests();
		
		List<Borrowing> list = borrowDao.findByFields(null, borrow.getPerson().getId(), null, null);
		assertTrue(!list.isEmpty());
		assertTrue(list.size() == 1);
	}
	
	@Test
	public void testFindByFields_byBorrowingDate_shouldReturnValidOutput() throws SQLException {
		Borrowing borrow = insertBorrowingInDataBaseForTests();
		
		List<Borrowing> list = borrowDao.findByFields(null, null, borrow.getDateOfBorrowing(), null);
		assertTrue(!list.isEmpty());
		assertTrue(list.size() == 1);
	}
	
	@Test
	public void testFindByFields_byReturningDate_shouldReturnValidOutput() throws SQLException {
		Borrowing borrow = insertBorrowingInDataBaseForTests();
		
		List<Borrowing> list = borrowDao.findByFields(null, null, null, borrow.getDateOfReturn());
		assertTrue(!list.isEmpty());
		assertTrue(list.size() == 1);
	}

	@Test
	public void testFindAll_emptyDataBase_shouldReturnEmptyList() throws SQLException {
		List<Borrowing> list = borrowDao.findAll();
		assertTrue("Must not retrieve entities.", list.isEmpty());
	}
	
	@Test
	public void testFindAll_shouldReturnValidOutput() throws SQLException {
		insertBorrowingsListInDataBaseForTests();
		
		List<Borrowing> list = borrowDao.findAll();
		assertNotNull("Must return entities previously inserted.", list);
		assertEquals("Should return 3 entities.", 3, list.size());
	}

	@Test
	public void testFindOpenBorrowings() throws SQLException {
		insertBorrowingsListInDataBaseForTests();
		
		List<Borrowing> list = borrowDao.findOpenBorrowings();
		assertNotNull("Must return entities previously inserted.", list);
		assertEquals("Should return 2 entities with open borrowing.", 2, list.size());
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
			borrowDao = new BorrowingDaoJpa(new JPAUtil().getMyTestsEntityManager());
		}
	}

	private void cleanDatabaseDataForTests() {
		try {
			List<Book> bookList = bookDao.findAll();
			List<Person> personList = personDao.findAll();
			List<Borrowing> borrowList = borrowDao.findAll();
			for (Person person : personList) {
				personDao.delete(person.getId());
			}
			for (Book book : bookList) {
				bookDao.delete(book.getId());
			}			
			for (Borrowing borrow : borrowList) {
				borrowDao.delete(borrow.getId());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private Borrowing insertBorrowingInDataBaseForTests() throws SQLException {
		Person person = this.insertPersonToBorrowBook();
		Book book = this.insertBookToBeBorrowed();
		Borrowing borrow = TestHelper.getBorrowingForTest();
		borrow.setPerson(person);
		borrow.setBook(book);
		borrowDao.create(borrow);
		List<Borrowing> list = borrowDao.findByFields(book.getId(), person.getId(), borrow.getDateOfBorrowing(), borrow.getDateOfReturn()); // getting db id
		borrow = list.get(0); 
		return borrow;
	}

	private List<Borrowing> insertBorrowingsListInDataBaseForTests() throws SQLException {
		Book book1 = TestHelper.getBookForTest();
		Book book2 = TestHelper.getBookForTest();
		Book book3 = TestHelper.getBookForTest();

		Person person = insertPersonToBorrowBook();

		// setting differents 'isbn' for each book
		book1.setIsbn(book1.getIsbn() + "-book1");
		book1.setRegisterer(person);
		book2.setIsbn(book2.getIsbn() + "-book2");
		book2.setRegisterer(person);
		book3.setIsbn(book3.getIsbn() + "-book3");
		book3.setRegisterer(person);

		bookDao.create(book1);
		bookDao.create(book2);
		bookDao.create(book3);

		Borrowing borrow1 = TestHelper.getBorrowingForTest();
		Borrowing borrow2 = TestHelper.getBorrowingForTest();
		Borrowing borrow3 = TestHelper.getBorrowingForTest();
		
		borrow1.setBook(book1);
		borrow1.setPerson(person);
		borrow2.setBook(book2);
		borrow2.setPerson(person);
		borrow3.setBook(book3);
		borrow3.setPerson(person);
		borrow3.setDateOfReturn(new Date()); // borrowing 3 was returned
		
		borrowDao.create(borrow1);
		borrowDao.create(borrow2);
		borrowDao.create(borrow3);
		
		return borrowDao.findAll();
	}

	private Person insertPersonToBorrowBook() throws SQLException {
		Person person = TestHelper.getPersonForTest();
		personDao.create(person);
		return personDao.findByEmail(person.getEmail());
	}
	
	private Book insertBookToBeBorrowed() throws SQLException {
		Book book = TestHelper.getBookForTest();
		book.setRegisterer(null);  // no need to use registerer person in these tests
		bookDao.create(book);
		return bookDao.findByIsbn(book.getIsbn());
	}

}
