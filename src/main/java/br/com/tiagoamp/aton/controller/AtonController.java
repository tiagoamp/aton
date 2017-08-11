package br.com.tiagoamp.aton.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.codec.digest.DigestUtils;
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
import br.com.tiagoamp.aton.model.Person;
import br.com.tiagoamp.aton.model.Role;
import br.com.tiagoamp.aton.model.MessaType;
import br.com.tiagoamp.aton.model.TypeOfAcquisition;
import br.com.tiagoamp.aton.model.to.MessageTO;
import br.com.tiagoamp.aton.service.BookService;
import br.com.tiagoamp.aton.service.BorrowingService;
import br.com.tiagoamp.aton.service.PersonService;

@Controller
public class AtonController {
	
	public AtonController() {
		personService = new PersonService();
		bookService = new BookService();
		borrowService = new BorrowingService();
	}
	
	Logger logger = Logger.getLogger(AtonController.class);
	
	private PersonService personService;
	private BookService bookService;
	private BorrowingService borrowService;
	
	@RequestMapping("/aton")
	public String pageInicial() {
		return "aton";
	}
	
	@RequestMapping("initializeAndDeleteThisEntry")
	public String pageInitialize() {
		//TODO: Delete this method after initializing the application
		
		try {
			List<Person> list = personService.getAll();
			if (list.isEmpty()) {
				Person person = new Person("admin@email.com", "System Administrator", null, Role.ADMINISTRATOR);
				person.setPassword(DigestUtils.sha1Hex("admin"));
				personService.insert(person);
			}				
		} catch (AtonBOException e) {
			e.printStackTrace();
		}
	    return "aton";
	}
	
	@RequestMapping("pessoas")
	public String pagePessoas() {
	    return "pessoas";
	}
	
	@RequestMapping("cadastropessoa")
	public String cadastrarPessoa(HttpServletRequest request,  
	        @RequestParam(value="acao", required=false) String pAcao, 
	        @RequestParam(value="identificador", required=false) String pId, 
	        Model model) {
		
		Person pessoa = new Person();
		
		if (pId != null && !pId.isEmpty()) {
			try {		
				pessoa = personService.findById(Integer.parseInt(pId));				 				
			} catch (AtonBOException e) {
				logger.error("Erro: " + e);
				model.addAttribute("mensagem",new MessageTO(e.getBusinessMessage(), MessaType.ERRO));
				return "pessoas";
			}
			model.addAttribute("acao",pAcao);
		}
		
		if (pAcao != null && pAcao.equals("excluir")) {
			model.addAttribute("mensagem",new MessageTO("Confirma exclusão com os dados abaixo?", MessaType.ALERTA));
		}
				
		model.addAttribute("pessoa", pessoa);
	    return "pessoas/cadastro";
	}
	
	@RequestMapping("listapessoas")
	public String listarPessoas(HttpServletRequest request, Model model) {
		List<Person> lista = new ArrayList<>();
		
		try {		
			lista = personService.getAll();
			if (lista.isEmpty()) {
				throw new AtonBOException("Consulta sem resultados!");
			}
			Set<Person> listaOrdenada = new TreeSet<>();
			listaOrdenada.addAll(lista);
			model.addAttribute("listapessoas", listaOrdenada);
		} catch (AtonBOException e) {
			logger.error("Erro: " + e);
			model.addAttribute("mensagem",new MessageTO(e.getBusinessMessage(), MessaType.ERRO));
			return "pessoas";
		}
		
	    return "pessoas";
	}
	
	@RequestMapping("exclusaopessoa")
	public String excluirPessoa(HttpServletRequest request,  
	        @RequestParam(value="acao", required=false) String pAcao, 
	        @RequestParam(value="identificador", required=false) String pId, 
	        Model model) {		
		Person pessoa;
		try {		
			pessoa = personService.findById(Integer.parseInt(pId));
			if (pessoa == null) {
				throw new AtonBOException("Erro na exclusão de pessoa: Identificador inválido!");
			}
			personService.delete(pessoa.getId());
			model.addAttribute("mensagem",new MessageTO("Exclusão com sucesso: " + pessoa.toString(), MessaType.SUCESSO));
			logger.info("Pessoa excluída: " + pessoa);
		} catch (AtonBOException e) {
			logger.error("Erro: " + e);
			model.addAttribute("mensagem",new MessageTO(e.getBusinessMessage(), MessaType.ERRO));			
		}		
		return "pessoas";
	}
	
	@RequestMapping(value="pessoacadastrada", method = RequestMethod.POST)
	public String salvarPessoa(@Valid Person person, BindingResult result, Model model) {
		boolean hasErrors = false;
		if(result.hasErrors()) {
			hasErrors = true;
		}
		if (person.getRole() != null && person.getRole() != Role.READER && person.getRole().equals("")) {
			result.reject("senha", "Senha deve ser preenchida para perfil 'Administrador' ou 'Bibliotecário'.");
			hasErrors = true;
		}
		if (hasErrors) {
			person.setRole(null);
			model.addAttribute("pessoa", person);
			return "pessoas/cadastro";
		}
		
		try {
			// digest da senha
			if (person.getPassword() != null && !person.getPassword().isEmpty()) {
				person.setPassword(DigestUtils.sha1Hex(person.getPassword().toUpperCase()));
			}
			// gravando pessoas
			if (person.getId() == null) {
				personService.insert(person); // insert
			} else {
				personService.update(person); // update
			}
			model.addAttribute("mensagem",new MessageTO("Gravação com sucesso: " + person.toString(), MessaType.SUCESSO));
			logger.info("Pessoa cadastrada: " + person);
		} catch (AtonBOException e) {
			logger.error("Erro: " + e);
			model.addAttribute("pessoa", person);
			model.addAttribute("mensagem",new MessageTO(e.getBusinessMessage(), MessaType.ERRO));
			return "pessoas/cadastro";
		}			
		
		return "pessoas";
	}
		
	@RequestMapping(value = "consultapessoa", method = RequestMethod.POST)
	public String consultarPessoa(HttpServletRequest request,  
	        @RequestParam(value="tEmail", required=false) String pEmail, 
	        @RequestParam(value="tDados", required=false) String pDados, 
	        Model model){
		List<Person> lista = new ArrayList<>();
		try {
			lista = this.pesquisarPessoasPorParametros(pEmail, pDados);
			if (lista.isEmpty()) {
				throw new AtonBOException("Consulta sem resultados!");
			}
			model.addAttribute("listapessoas", lista);
		} catch (AtonBOException e) {
			logger.error("Erro: " + e);
			model.addAttribute("mensagem",new MessageTO(e.getBusinessMessage(), MessaType.ERRO));
		}
		return "pessoas";		
	}
		
	@RequestMapping("livros")
	public String pageLivros() {
	    return "livros";
	}
	
	@RequestMapping("cadastrolivro")
	public String cadastrarLivro(HttpServletRequest request,  
	        @RequestParam(value="acao", required=false) String pAcao, 
	        @RequestParam(value="identificador", required=false) String pId, 
	        Model model) {
		Book livro = new Book();
		if (pId != null && !pId.isEmpty()) {
			try {		
				livro = bookService.findById(Integer.parseInt(pId));				 				
			} catch (AtonBOException e) {
				logger.error("Erro: " + e);
				model.addAttribute("mensagem",new MessageTO(e.getBusinessMessage(), MessaType.ERRO));
				return "livros";
			}
			model.addAttribute("acao",pAcao);
		}
		
		if (pAcao != null && pAcao.equals("excluir")) {
			model.addAttribute("mensagem",new MessageTO("Confirma exclusão com os dados abaixo?", MessaType.ALERTA));
		}
				
		model.addAttribute("livro", livro);
	    return "livros/cadastro";
	}
	
	@RequestMapping(value = "consultalivro", method = RequestMethod.POST)
	public String consultarLivro(HttpServletRequest request,  
	        @RequestParam(value="tISBN", required=false) String pISBN, 
	        @RequestParam(value="tDados", required=false) String pDados, 
	        Model model){
		List<Book> list = new ArrayList<>();
		try {
			if (pISBN != null && !pISBN.isEmpty()) { // CAMPO DE PESQ ISBN PREENCHIDO
				// pesquisa por isbn
				Book l = bookService.findByIsbn(pISBN.trim().toUpperCase());
				if (l != null)
					list.add(l);
			} else { // CAMPO DE PESQ DADOS PREENCHIDO
				if (pDados != null && !pDados.isEmpty()) {
					pDados = pDados.trim().toUpperCase();
					// pesquisa por titulo aproximado
					list = bookService.findByTitle(pDados);
					if (list.isEmpty()) {
						// pesquisa por autor aproximado
						list = bookService.findByAuthorName(pDados);						
					}
				}
			}
			if (list.isEmpty()) {
				throw new AtonBOException("Consulta sem resultados!");
			}
			model.addAttribute("listalivros", list);
		} catch (AtonBOException e) {
			logger.error("Erro: " + e);
			model.addAttribute("mensagem",new MessageTO(e.getBusinessMessage(), MessaType.ERRO));
		}
		return "livros";		
	}
		
	@RequestMapping(value="livrocadastrado", method = RequestMethod.POST)
	public String salvarLivro(@Valid Book book, BindingResult result, Model model, HttpServletRequest request
			/*@, RequestParam(value="file", required=false) MultipartFile pFile*/) {
		boolean hasErrors = false;
		if(result.hasErrors()) {
			hasErrors = true;
		}
		if (!book.getDateOfAcquisition().toString().isEmpty()) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			try {
				book.setDateOfAcquisition(sdf.parse(book.getDateOfAcquisition().toString()));
			} catch (ParseException e) {
				result.reject("dataAquisicao", "Data de Aquisição em formato inválido.");
				hasErrors = true;
			}
		} else {
			result.reject("dataAquisicaoFormatada", "Campo obrigatório não preenchido: Data de Aquisição.");
			hasErrors = true;
		}
		if (book.getTypeOfAcquisition() == TypeOfAcquisition.PURCHASE && !book.getDonorName().isEmpty()) {
			result.reject("tipoAquisicao", "Tipo de aquisição 'COMPRA' não permite cadastro de doador.");
			hasErrors = true;
		}
		if (book.getNumberAvailable() > book.getNumberOfCopies()) {
			result.reject("qtdExemplares", "Quantidade de exemplares disponíveis deve ser menor que quantidade total.");
			model.addAttribute("acao","alterar");
			hasErrors = true;
		}
		if (hasErrors) {
			book.setTypeOfAcquisition(null);
			model.addAttribute("livro", book);
			return "livros/cadastro";
		}
		
		//MultipartFile mFile = pFile;
		try {
				//FIXME : Implementar e refatorar gravação de figura de capa do livro (  shame on me  :-(   )
			/*if (!mFile.isEmpty()) {  // capa do livro
				Path path = service.inserirFotoCapaLivro(mFile, livro.getIsbn());
				livro.setPathFotoCapa(path);
			}*/			
			if (book.getId() == null) {
				book.setNumberAvailable(book.getNumberOfCopies());
				bookService.insert(book); // insert				
			} else {
				/*if (livro.getPathFotoCapa() == null) {
					Livro l = service.consultarLivro(livro.getId());
					if (l.getPathFotoCapa() != null) livro.setPathFotoCapa(l.getPathFotoCapa());	
				}*/				
					//FIXME Implementar e refatorar gravação de figura de capa do livro
				bookService.update(book); // update
			}
			model.addAttribute("mensagem",new MessageTO("Gravação com sucesso: " + book.toString(), MessaType.SUCESSO));
			logger.info("Livro cadastrado: " + book);
		} catch (AtonBOException e) {
			logger.error("Erro: " + e);
			model.addAttribute("livro", book);
			model.addAttribute("mensagem",new MessageTO(e.getBusinessMessage(), MessaType.ERRO));
			return "livros/cadastro";
		}		
		return "livros";
	}
	
	@RequestMapping("listalivros")
	public String listarLivros(HttpServletRequest request, Model model) {
		List<Book> lista = new ArrayList<>();
		try {		
			lista = bookService.getAll();
			if (lista.isEmpty()) {
				throw new AtonBOException("Consulta sem resultados!");
			}
			Collections.sort(lista);
			model.addAttribute("listalivros", lista);
		} catch (AtonBOException e) {
			logger.error("Erro: " + e);
			model.addAttribute("mensagem",new MessageTO(e.getBusinessMessage(), MessaType.ERRO));
			return "livros";
		}		
	    return "livros";
	}
	
	@RequestMapping("exclusaolivro")
	public String excluirLivro(HttpServletRequest request,  
	        @RequestParam(value="acao", required=false) String pAcao, 
	        @RequestParam(value="identificador", required=false) String pId, 
	        Model model) {		
		Book book;
		try {		
			book = bookService.findById(Integer.parseInt(pId));
			if (book == null) {
				throw new AtonBOException("Erro na exclusão de pessoa: Identificador inválido!");
			}
			borrowService.delete(book.getId());
			model.addAttribute("mensagem",new MessageTO("Exclusão com sucesso: " + book.toString(), MessaType.SUCESSO));
			logger.info("Livro excluído: " + book);
		} catch (AtonBOException e) {
			logger.error("Erro: " + e);
			model.addAttribute("mensagem",new MessageTO(e.getBusinessMessage(), MessaType.ERRO));			
		}		
		return "livros";
	}
	
	@RequestMapping("emprestimos")
	public String pageEmprestimos() {
	    return "emprestimos";
	}

	@RequestMapping("emprestimolivro")
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
		Borrowing emprestimo = new Borrowing(book, new Person(), new Date(), null, null);
		model.addAttribute("emprestimo", emprestimo);
		return "emprestimos/emprestimolivro";
	}
	
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
	
	@RequestMapping(value = "consultaemprestimo", method = RequestMethod.POST)
	public String consultarEmprestimo(HttpServletRequest request,  
	        @RequestParam(value="tLivro", required=false) String pLivro, 
	        @RequestParam(value="tPessoa", required=false) String pPessoa, 
	        Model model){
		List<Borrowing> lista = new ArrayList<>();
		try {
			if (pLivro != null && !pLivro.isEmpty()) { // CAMPO DE PESQ LIVRO PREENCHIDO
				pLivro = pLivro.trim().toUpperCase();
				List<Book> books = new ArrayList<>();
				books.addAll(bookService.findByTitle(pLivro));
				books.addAll(bookService.findByAuthorName(pLivro));
				for (int i = 0; i < books.size(); i++) {
					Book livro = books.get(i);
					lista = borrowService.findByFields(livro.getId(), null, null, null);
				}
			} else { // CAMPO DE LEITOR(PESSOA) PREENCHIDO
				if (pPessoa != null && !pPessoa.isEmpty()) {
					pPessoa = pPessoa.trim().toUpperCase();
					List<Person> people = new ArrayList<>();
					people.addAll(personService.findByName(pPessoa));
					for (int i = 0; i < people.size(); i++) {
						Person pessoa = people.get(i);
						lista = borrowService.findByFields(null, pessoa.getId(), null, null);
					}
				}
			}
			if (lista.isEmpty()) {
				throw new AtonBOException("Consulta sem resultados!");
			}
			Collections.sort(lista);
			model.addAttribute("listaemprestimos", lista);
		} catch (AtonBOException e) {
			logger.error("Erro: " + e);
			model.addAttribute("mensagem",new MessageTO(e.getBusinessMessage(), MessaType.ERRO));
		}
		return "emprestimos";		
	}
	
	@RequestMapping("listaemprestimos")
	public String listarEmprestimos(HttpServletRequest request, Model model) {
		List<Borrowing> lista = new ArrayList<>();
		try {		
			lista = borrowService.getAll();
			if (lista.isEmpty()) {
				throw new AtonBOException("Consulta sem resultados!");
			}
			Collections.sort(lista);			
			model.addAttribute("listaemprestimos", lista);
		} catch (AtonBOException e) {
			logger.error("Erro: " + e);
			model.addAttribute("mensagem",new MessageTO(e.getBusinessMessage(), MessaType.ERRO));			
		}		
	    return "emprestimos";
	}
	
	@RequestMapping("listaemprestimosabertos")
	public String listarEmprestimosEmAberto(HttpServletRequest request, Model model) {
		List<Borrowing> lista = new ArrayList<>();
		try {		
			lista = borrowService.getOpenBorrowings();
			if (lista.isEmpty()) {
				throw new AtonBOException("Consulta sem resultados!");
			}
			Collections.sort(lista);			
			model.addAttribute("listaemprestimos", lista);
		} catch (AtonBOException e) {
			logger.error("Erro: " + e);
			model.addAttribute("mensagem",new MessageTO(e.getBusinessMessage(), MessaType.ERRO));			
		}		
	    return "emprestimos";
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
	
	@RequestMapping("autorizacao")
	public String pageAutorizacao() {
	    return "autorizacao";
	}
	
	@RequestMapping("sobre")
	public String pageSobre() {
		return "sobre";
	}
	
	/*@RequestMapping("login")
	public String pageLogin(HttpServletRequest request, Model model) {
		Person person = new Person();
		model.addAttribute("person", person);
	    return "login";
	}
	
	@RequestMapping("efetuarlogin")
	public String efetuarLogin(Person pessoa, BindingResult result, Model model, HttpSession session) {
		if (pessoa.getEmail().isEmpty() || pessoa.getPassword().isEmpty()) {
			model.addAttribute("mensagem",new MensagemTO("Campos não preenchidos!", TipoMensagem.ERRO));
			return "login";
		}
		Person pessoaBD = null;
		try {
			pessoaBD = personService.findByEmail(pessoa.getEmail());
			if (pessoaBD == null) {
				model.addAttribute("mensagem",new MensagemTO("Usuário inexistente!", TipoMensagem.ERRO));
				return "login";
			} else if (pessoaBD.getPassword().isEmpty()) {
				model.addAttribute("mensagem",new MensagemTO("Usuário sem acesso cadastrado.", TipoMensagem.ERRO));
				return "login";
			}
			// verificando credenciais
			pessoa.setPassword(DigestUtils.sha1Hex(pessoa.getPassword()));
			if (pessoaBD.getPassword().equals(pessoa.getPassword()) && pessoaBD.getRole() != Role.READER) {
				pessoaBD.setPassword(null); // null por seguranca, pra setar obj na sessao
				session.setAttribute("usuario", pessoaBD);
				logger.info("Usuario autenticado: " + pessoaBD);
			} else {
				model.addAttribute("mensagem",new MensagemTO("Credenciais inválidas!", TipoMensagem.ERRO));
				return "login";
			}
		} catch (AtonBOException e) {
			logger.error("Erro: " + e);
			model.addAttribute("mensagem",new MensagemTO(e.getBusinessMessage(), TipoMensagem.ERRO));
			return "login";
		}
		model.addAttribute("mensagem",new MensagemTO("Usuário autenticado!", TipoMensagem.SUCESSO));
		return "aton";
	}
	
	@RequestMapping("logout")
	public String logout(HttpSession session, HttpServletRequest request, Model model) {
		session.invalidate();
		model.addAttribute("mensagem",new MensagemTO("Logout concluído!", TipoMensagem.SUCESSO));
	    return "aton";
	}*/
		
}
