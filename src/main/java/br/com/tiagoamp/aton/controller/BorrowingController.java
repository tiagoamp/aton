package br.com.tiagoamp.aton.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.tiagoamp.aton.model.AtonBOException;
import br.com.tiagoamp.aton.model.Book;
import br.com.tiagoamp.aton.model.Borrowing;
import br.com.tiagoamp.aton.model.MessaType;
import br.com.tiagoamp.aton.model.Person;
import br.com.tiagoamp.aton.model.Role;
import br.com.tiagoamp.aton.model.to.MessageTO;
import br.com.tiagoamp.aton.service.BookService;
import br.com.tiagoamp.aton.service.BorrowingService;
import br.com.tiagoamp.aton.service.PersonService;

@Controller
@RequestMapping("emprestimos")
public class BorrowingController {
	
	public BorrowingController() {
		personService = new PersonService();
		bookService = new BookService();
		borrowService = new BorrowingService();
	}
	
	Logger logger = Logger.getLogger(BorrowingController.class);
	
	private PersonService personService;
	private BookService bookService;
	private BorrowingService borrowService;
	
	
	@RequestMapping(method = RequestMethod.POST)
	public String pesquisarEmprestimo(HttpServletRequest request,  
	        @RequestParam(value="tBook", required=false) String pBook, 
	        @RequestParam(value="tPerson", required=false) String pPerson,
	        @RequestParam(value="fAbertos", required=false) String pAbertos,
	        @RequestParam(value="identificador", required=false) String pId,
	        Model model){
		
		if (pId != null && !pId.isEmpty()) {  // return of borrowing
			Borrowing borrowing = null;
			try {		
				borrowing = borrowService.findById(Integer.parseInt(pId));
				borrowService.returnBorrowedBook(borrowing);
				model.addAttribute("mensagem",new MessageTO("Devolução com sucesso: " + borrowing.toString(), MessaType.SUCESSO));
				logger.info("Emprestimo devolvido: " + borrowing);
			} catch (AtonBOException e) {
				logger.error("Erro: " + e);
				model.addAttribute("mensagem",new MessageTO(e.getBusinessMessage(), MessaType.ERRO));				
			}
			return "emprestimos/emprestimos";
		}
		
		List<Borrowing> list = new ArrayList<>();
		
		try {			
			if (pBook != null && !pBook.isEmpty()) {
				pBook = pBook.trim().toUpperCase();
				List<Book> books = new ArrayList<>();
				books.addAll(bookService.findByTitle(pBook));
				books.addAll(bookService.findByAuthorName(pBook));
				for (Book b : books) {
					list = borrowService.findByFields(b.getId(), null, null, null);
				}
			} else if (pPerson != null && !pPerson.isEmpty()) {
				pPerson = pPerson.trim().toUpperCase();
				List<Person> people = new ArrayList<>();
				people.addAll(personService.findByName(pPerson));
				for (Person p: people) {
					list = borrowService.findByFields(null, p.getId(), null, null);
				}
			} else if (pBook == null && pPerson == null) {
				if (pAbertos == null) {
					list = borrowService.getAll();
				} else {
					list = borrowService.getOpenBorrowings();
				}				
			}
			
			if (list.isEmpty()) {
				model.addAttribute("mensagem",new MessageTO("Consulta sem resultados!", MessaType.ERRO));				
			}
			
			Collections.sort(list);
			model.addAttribute("listofborrowing", list);			
		} catch (AtonBOException e) {
			logger.error("Erro: " + e);
			model.addAttribute("mensagem",new MessageTO(e.getBusinessMessage(), MessaType.ERRO));
		}
		
		return "emprestimos/emprestimos";
						
	}
	
	@RequestMapping(value = "consultapessoa", method = RequestMethod.POST)
	public String consultarPessoaParaEmprestimo(HttpServletRequest request,  
	        @RequestParam(value="tEmail", required=false) String pEmail, 
	        @RequestParam(value="tFields", required=false) String pFields,
	        @RequestParam(value="tIdBook", required=false) String pBookId,
	        Model model){
		List<Person> list = new ArrayList<>();
		Book book = null;
		try {
			book = bookService.findById(Integer.parseInt(pBookId)); // retrieving book
			list = this.pesquisarPessoasPorParametros(pEmail, pFields); 
			if (list.isEmpty()) {
				model.addAttribute("mensagem",new MessageTO("Consulta sem resultados!", MessaType.ERRO));
			}
			model.addAttribute("listofpeople", list);
		} catch (AtonBOException e) {
			logger.error("Erro: " + e);
			model.addAttribute("mensagem",new MessageTO(e.getBusinessMessage(), MessaType.ERRO));
		}
		Borrowing borrowing = new Borrowing(book, new Person(), new Date(), getSuggestedReturnDate(new Date()), null);
		model.addAttribute("borrowing", borrowing);
		return "emprestimos/emprestimolivro";
	}
	
	@RequestMapping("selecionarpessoa")
	public String selecionarPessoaParaEmprestimo(HttpServletRequest request,  
	        @RequestParam(value="acao", required=false) String pAcao, 
	        @RequestParam(value="identificador", required=false) String pPersonId,
	        @RequestParam(value="book", required=false) String pBookId,
	        Model model){
		Book book = null;
		Person person = null;
		try {
			book = bookService.findById(Integer.parseInt(pBookId)); 
			person = personService.findById(Integer.parseInt(pPersonId)); 			
		} catch (AtonBOException e) {
			logger.error("Erro: " + e);
			model.addAttribute("mensagem",new MessageTO(e.getBusinessMessage(), MessaType.ERRO));
		}
		Borrowing borrowing = new Borrowing(book, person, new Date(), getSuggestedReturnDate(new Date()), null);
		model.addAttribute("borrowing", borrowing);
		return "emprestimos/emprestimolivro";	
	}
	
	@RequestMapping(value="livroemprestado", method = RequestMethod.POST)
	public String emprestarLivro(@Valid Borrowing borrowing, BindingResult result, Model model, HttpServletRequest request) {
		boolean hasErrors = false;
		if(result.hasErrors()) {
			hasErrors = true;
		}
		try { // retrieving book and person
			borrowing.setBook(bookService.findById(borrowing.getBook().getId()));
			if (borrowing.getPerson() == null || borrowing.getPerson().getId() == null) {
				model.addAttribute("mensagem",new MessageTO("Pessoa não selecionada para empréstimo do livro!", MessaType.ERRO));
				model.addAttribute("borrowing", borrowing);
				return "emprestimos/emprestimolivro";
			}
			borrowing.setPerson(personService.findById(borrowing.getPerson().getId()));			
		} catch (AtonBOException e) {
			logger.error("Erro: " + e);
			model.addAttribute("mensagem",new MessageTO(e.getBusinessMessage(), MessaType.ERRO));
			model.addAttribute("borrowing", borrowing);
			return "emprestimos/emprestimolivro";
		}
		if (borrowing.getDateOfBorrowing().toString().isEmpty()) {
			result.reject("dateOfBorrowing", "Campo obrigatório não preenchido: Data de Empréstimo.");
			hasErrors = true;
		} else if (borrowing.getDateOfScheduledReturn().toString().isEmpty()) {
			result.reject("dateOfScheduledReturn", "Campo obrigatório não preenchido: Data de Devolução.");
			hasErrors = true;
		}
		if (hasErrors) {
			model.addAttribute("borrowing", borrowing);
			return "emprestimos/emprestimolivro";
		}
		
		try {			
			borrowService.insert(borrowing);
			model.addAttribute("mensagem",new MessageTO("Empréstimo realizado com sucesso: " + borrowing.toString(), MessaType.SUCESSO));
			model.addAttribute("mode", "consulta");
			logger.info("Emprestimo cadastrado: " + borrowing);
		} catch (AtonBOException e) {
			logger.error("Erro: " + e);
			model.addAttribute("borrowing", borrowing);
			model.addAttribute("mensagem",new MessageTO(e.getBusinessMessage(), MessaType.ERRO));
			return "emprestimos/emprestimolivro";
		}
		return "emprestimos/emprestimolivro";
	}
		
	//@RequestMapping(method=RequestMethod.GET)
	/*public String devolverLivro(HttpServletRequest request,  
	        @RequestParam(value="acao", required=false) String pAcao, 
	        @RequestParam(value="identificador", required=false) String pId, 
	        Model model) {		
		Borrowing borrowing = null;				
		if (pId != null && !pId.isEmpty()) {
			try {		
				borrowing = borrowService.findById(Integer.parseInt(pId));
				borrowService.returnBorrowedBook(borrowing);
				model.addAttribute("mensagem",new MessageTO("Devolução com sucesso: " + borrowing.toString(), MessaType.SUCESSO));
				logger.info("Emprestimo devolvido: " + borrowing);
			} catch (AtonBOException e) {
				logger.error("Erro: " + e);
				model.addAttribute("mensagem",new MessageTO(e.getBusinessMessage(), MessaType.ERRO));
				return "emprestimos";
			}
			//model.addAttribute("acao",pAcao);
		}		
		model.addAttribute("borrowing", borrowing);
		return "emprestimos/emprestimos";
	}*/
	
	private List<Person> pesquisarPessoasPorParametros(String email, String param) throws AtonBOException {
		List<Person> list = new ArrayList<>();
		if (email != null && !email.isEmpty()) { // E-MAIL
			Person p = personService.findByEmail(email.trim().toUpperCase());
			if (p != null) list.add(p);
		} else {  // FIELDS
			if (param != null && !param.isEmpty()) {
				param = param.trim().toUpperCase();
				for (Role role : Role.values()) {
					if (role.toString().equals(param))
						list = personService.findByFields(null, null, role);
				}
				if (list.isEmpty()) {
					list = personService.findByName(param);
				}
			}
		}
		return list;
	}
	
	private Date getSuggestedReturnDate(Date initialDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, 10);
		return new Date(calendar.getTimeInMillis());
	}
			
}
