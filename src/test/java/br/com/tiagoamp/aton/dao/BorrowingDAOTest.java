package br.com.tiagoamp.aton.dao;

import static org.junit.Assert.*;

import java.sql.SQLException;
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
	public void testFindByFields() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindAll() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindOpenBorrowings() {
		fail("Not yet implemented");
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
			for (Book book : bookList) {
				bookDao.delete(book.getId());
			}
			for (Person person : personList) {
				personDao.delete(person.getId());
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

		Borrowing borrow1 = new TestHelper().getBorrowingForTest();
		Borrowing borrow2 = new TestHelper().getBorrowingForTest();
		Borrowing borrow3 = new TestHelper().getBorrowingForTest();
		
		borrow1.setBook(book1);
		borrow1.setPerson(person);
		borrow2.setBook(book2);
		borrow2.setPerson(person);
		borrow3.setBook(book3);
		borrow3.setPerson(person);
		
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
