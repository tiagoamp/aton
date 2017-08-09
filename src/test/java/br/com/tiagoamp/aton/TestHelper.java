package br.com.tiagoamp.aton;

import java.util.Arrays;
import java.util.Date;

import br.com.tiagoamp.aton.model.Book;
import br.com.tiagoamp.aton.model.Emprestimo;
import br.com.tiagoamp.aton.model.Perfil;
import br.com.tiagoamp.aton.model.Person;
import br.com.tiagoamp.aton.model.Status;
import br.com.tiagoamp.aton.model.TypeOfAcquisition;

public class TestHelper {
	
	public static Person getPersonForTest() {
		Person person = new Person("TEST@TESTEMAIL.COM", "NAME FOR TESTS", "11-1111-1111", Perfil.ADMINISTRATOR);
		person.setPassword("1234");
		return person;
	}
		
	public static Book getBookForTest() {
		Book book = new Book();
		book.setAuthorsName(Arrays.asList("Author Name 01", "Author Name 02"));
		book.setClassification("Internal classification for tests");
		book.setComments("Comments...");
		book.setCoverImage("path/to/img.jpg");
		book.setDateOfAcquisition(new Date());
		book.setDateOfRegistration(new Date());
		book.setDonorName("Donor Name");
		book.setGenre("Horror");
		book.setIsbn("ISBN-1234-567");
		book.setNumberAvailable(2);
		book.setNumberOfCopies(3);
		book.setNumberOfPages(200);
		book.setPublishingCity("São Paulo");
		book.setPublishingCompany("Editors Test");
		book.setPublishingYear(2000);
		book.setStatus(Status.AVAILABLE);
		book.setSubtitle("Subtitle of Book for Tests");
		book.setTargetAudience("Adults");
		book.setTitle("Title of Book For Tests");
		book.setTypeOfAcquisition(TypeOfAcquisition.DONATION);
		book.setRegisterer(getPersonForTest());
		return book;
	}
	
	public static Emprestimo getBorrowingForTest() {
		Emprestimo emp = new Emprestimo();
		Book livro = TestHelper.getBookForTest();
		livro.setId(1);
		emp.setLivro(livro);
		Person pessoa = TestHelper.getPersonForTest();
		pessoa.setId(1);
		emp.setPessoa(pessoa);
		emp.setDataEmprestimo(new Date());
		return emp;
	}

}
