package br.com.tiagoamp.aton.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
	        Model model){
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
		
	/*@RequestMapping(value="emprestimolivro", method=RequestMethod.POST)
	public String carregarEmprestimoLivro(HttpServletRequest request,  
	        @RequestParam(value="acao", required=false) String pAcao, 
	        @RequestParam(value="identificador", required=false) String pId, 
	        Model model) {		
		Book book = new Book();				
		if (pId != null && !pId.isEmpty()) {
			try {		
				book = bookService.findById(Integer.parseInt(pId));				 				
			} catch (AtonBOException e) {
				logger.error("Erro: " + e);
				model.addAttribute("mensagem",new MessageTO(e.getBusinessMessage(), MessaType.ERRO));
				return "livros";
			}
			model.addAttribute("acao",pAcao);
		}		
		Borrowing borrowing = new Borrowing(book, new Person(), new Date(), null, null);
		model.addAttribute("borrowing", borrowing);
		return "emprestimos/emprestimolivro";
	}*/
	
	
	
	@RequestMapping(value = "consultapessoaemprestimo", method = RequestMethod.POST)
	public String consultarPessoaParaEmprestimo(HttpServletRequest request,  
	        @RequestParam(value="tEmail", required=false) String pEmail, 
	        @RequestParam(value="tDados", required=false) String pDados,
	        @RequestParam(value="tIdLivro", required=false) String pIdLivro,
	        Model model){
		List<Person> lista = new ArrayList<>();
		Book livro = null;
		try {
			livro = bookService.findById(Integer.parseInt(pIdLivro)); // recarregando livro
			lista = this.pesquisarPessoasPorParametros(pEmail, pDados); 
			if (lista.isEmpty()) {
				throw new AtonBOException("Consulta sem resultados!");
			}
			model.addAttribute("listapessoas", lista);
		} catch (AtonBOException e) {
			logger.error("Erro: " + e);
			model.addAttribute("mensagem",new MessageTO(e.getBusinessMessage(), MessaType.ERRO));
		}				
		Borrowing emprestimo = new Borrowing(livro, new Person(), new Date(), null, null);
		model.addAttribute("emprestimo", emprestimo);
		return "emprestimos/emprestimolivro";
	}
	
	@RequestMapping(value = "emprestimoselecionarpessoa", method = RequestMethod.POST)
	public String selecionarPessoaParaEmprestimo(HttpServletRequest request,  
	        @RequestParam(value="acao", required=false) String pAcao, 
	        @RequestParam(value="identificador", required=false) String pId,
	        @RequestParam(value="idLivro", required=false) String pIdLivro,
	        Model model){
		Book livro = null;
		Person pessoa = null;
		try {
			livro = bookService.findById(Integer.parseInt(pIdLivro)); 
			pessoa = personService.findById(Integer.parseInt(pId)); 			
		} catch (AtonBOException e) {
			logger.error("Erro: " + e);
			model.addAttribute("mensagem",new MessageTO(e.getBusinessMessage(), MessaType.ERRO));
		}
		// Regra da data sugerida de devolução ==> D + 10
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, 10);
		
		Borrowing emprestimo = new Borrowing(livro, pessoa, new Date(), new Date(calendar.getTimeInMillis()), null);
		model.addAttribute("emprestimo", emprestimo);
		return "emprestimos/emprestimolivro";	
	}
	
	private List<Person> pesquisarPessoasPorParametros(String email, String param) throws AtonBOException {
		List<Person> lista = new ArrayList<>();
		if (email != null && !email.isEmpty()) { // CAMPO DE PESQ EMAIL PREENCHIDO
			// pesquisa por e-mail
			Person p = personService.findByEmail(email.trim().toUpperCase());
			if (p != null)
				lista.add(p);
		} else { // CAMPO DE PESQ DADOS PREENCHIDO
			if (param != null && !param.isEmpty()) {
				param = param.trim().toUpperCase();
				// pesquisa por perfil
				for (Role perfil : Role.values()) {
					if (perfil.toString().equals(param))
						lista = personService.findByFields(null, null, Role.valueOf(param));
				}
				if (lista.isEmpty()) {
					// pesquisa por telefone
					lista = personService.findByFields(null, param, null);
					if (lista.isEmpty()) {
						// pesquisa por nome aproximado
						lista = personService.findByName(param);
					}
				}
			}
		}
		return lista;
	}
	
	@RequestMapping(value="livroemprestado", method = RequestMethod.POST)
	public String emprestarLivro(@Valid Borrowing borrowing, BindingResult result, Model model, HttpServletRequest request) {
		boolean hasErrors = false;
		if(result.hasErrors()) {
			hasErrors = true;
		}
		try { // recuperando livro e pessoa
			borrowing.setBook(bookService.findById(borrowing.getBook().getId()));
			if (borrowing.getPerson() == null || borrowing.getPerson().getId() == null) throw new AtonBOException("Pessoa não selecionada para empréstimo do livro!");
			borrowing.setPerson(personService.findById(borrowing.getPerson().getId()));			
		} catch (AtonBOException e) {
			logger.error("Erro: " + e);
			model.addAttribute("mensagem",new MessageTO(e.getBusinessMessage(), MessaType.ERRO));
			model.addAttribute("emprestimo", borrowing);
			return "emprestimos/emprestimolivro";
		}
		if (borrowing.getDateOfBorrowing().toString().isEmpty()) {
			result.reject("dataEmprestimoFormatada", "Campo obrigatório não preenchido: Data de Empréstimo.");
			hasErrors = true;
		} else if (borrowing.getDateOfScheduledReturn().toString().isEmpty()) {
			result.reject("dataDevolucaoProgramadaFormatada", "Campo obrigatório não preenchido: Data de Devolução.");
			hasErrors = true;
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			try {
				borrowing.setDateOfBorrowing(sdf.parse(borrowing.getDateOfBorrowing().toString()));
				borrowing.setDateOfScheduledReturn(sdf.parse(borrowing.getDateOfScheduledReturn().toString()));
			} catch (ParseException e) {
				result.reject("dataEmprestimoFormatada", "Data de Empréstimo ou Devolução em formato inválido.");
				hasErrors = true;
			}
		} 
		if (hasErrors) {
			model.addAttribute("emprestimo", borrowing);
			return "emprestimos/emprestimolivro";
		}
		
		try {			
			borrowService.insert(borrowing);
			Book book = borrowing.getBook();
			book.setNumberAvailable(book.getNumberAvailable() - 1);
			bookService.update(book);			
			model.addAttribute("mensagem",new MessageTO("Gravação com sucesso: " + borrowing.toString(), MessaType.SUCESSO));
			logger.info("Emprestimo cadastrado: " + borrowing);
		} catch (AtonBOException e) {
			logger.error("Erro: " + e);
			model.addAttribute("emprestimo", borrowing);
			model.addAttribute("mensagem",new MessageTO(e.getBusinessMessage(), MessaType.ERRO));
			return "emprestimos/emprestimolivro";
		}	
		return "livros";
	}
	
	@RequestMapping("devolucaolivro")
	public String DevolverLivro(HttpServletRequest request,  
	        @RequestParam(value="acao", required=false) String pAcao, 
	        @RequestParam(value="identificador", required=false) String pId, 
	        Model model) {		
		Borrowing emprestimo = null;				
		if (pId != null && !pId.isEmpty()) {
			try {		
				emprestimo = borrowService.findById(Integer.parseInt(pId));
				emprestimo.setDateOfReturn(new Date());
				borrowService.update(emprestimo);
				Book livro = emprestimo.getBook();
				livro.setNumberAvailable(livro.getNumberAvailable() + 1);
				bookService.update(livro);
				model.addAttribute("mensagem",new MessageTO("Devolução com sucesso: " + emprestimo.toString(), MessaType.SUCESSO));
				logger.info("Emprestimo devolvido: " + emprestimo);
			} catch (AtonBOException e) {
				logger.error("Erro: " + e);
				model.addAttribute("mensagem",new MessageTO(e.getBusinessMessage(), MessaType.ERRO));
				return "emprestimos";
			}
			model.addAttribute("acao",pAcao);
		}		
		model.addAttribute("emprestimo", emprestimo);
		return "emprestimos";
	}
		
}
