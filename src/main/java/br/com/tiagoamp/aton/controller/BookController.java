package br.com.tiagoamp.aton.controller;

import java.util.ArrayList;
import java.util.Collections;
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
import br.com.tiagoamp.aton.model.MessaType;
import br.com.tiagoamp.aton.model.TypeOfAcquisition;
import br.com.tiagoamp.aton.model.to.MessageTO;
import br.com.tiagoamp.aton.service.BookService;

@Controller
@RequestMapping("livros")
public class BookController {
	
	public BookController() {
		bookService = new BookService();		
	}
	
	Logger logger = Logger.getLogger(BookController.class);
	
	private BookService bookService;
	
	
	@RequestMapping(value="cadastro", method=RequestMethod.GET)
	public String cadastrarLivro(HttpServletRequest request,  
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
		
		if (pAcao != null && pAcao.equals("excluir")) {
			model.addAttribute("mensagem",new MessageTO("Confirma exclusão com os dados abaixo?", MessaType.ALERTA));
		}
				
		model.addAttribute("book", book);
	    return "livros/cadastro";
	}
	
	@RequestMapping(value="cadastro", method=RequestMethod.POST)
	public String salvarLivro(@Valid Book book, BindingResult result, Model model, HttpServletRequest request, @RequestParam(value="exclusao", required=false) String pExclusao) {
		boolean hasErrors = false;
		
		boolean isDelete = pExclusao != null && !pExclusao.isEmpty();
		
		if (!isDelete) {
			if(result.hasErrors()) {
				hasErrors = true;
			}
			if (book.getDateOfAcquisition().toString().isEmpty()) {				
				result.reject("dateOfAcquisition", "Campo obrigatório não preenchido: Data de Aquisição.");
				hasErrors = true;
			}
			if (book.getTypeOfAcquisition() == TypeOfAcquisition.COMPRA && !book.getDonorName().isEmpty()) {
				result.reject("typeOfAcquisition", "Tipo de aquisição 'COMPRA' não permite cadastro de doador.");
				hasErrors = true;
			}
			if (book.getNumberAvailable() > book.getNumberOfCopies()) {
				result.reject("numberOfCopies", "Quantidade de exemplares disponíveis deve ser menor que quantidade total.");
				model.addAttribute("acao","alterar");
				hasErrors = true;
			}
		}
		
		if (hasErrors) {
			book.setTypeOfAcquisition(null);
			model.addAttribute("book", book);
		}
		
		try {						
			if (book.getId() == null) {
				book.setNumberAvailable(book.getNumberOfCopies());
				bookService.insert(book); 			
			} else {
				if (isDelete) {
					bookService.delete(book.getId());
				} else {
					bookService.update(book); 
				}				
			}
			model.addAttribute("mensagem",new MessageTO("Operação realizada com sucesso: " + book.toString(), MessaType.SUCESSO));
			model.addAttribute("acao", "consultar");
			logger.info("Livro cadastrado/alterado/deletado: " + book);
		} catch (AtonBOException e) {
			logger.error("Erro: " + e);
			model.addAttribute("book", book);
			model.addAttribute("mensagem",new MessageTO(e.getBusinessMessage(), MessaType.ERRO));
		}		
		
		return "livros/cadastro";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String pesquisarLivro(HttpServletRequest request,  
	        @RequestParam(value="tISBN", required=false) String pIsbn, 
	        @RequestParam(value="tFields", required=false) String pFields, 
	        @RequestParam(value="tList", required=false) String pList,
	        Model model){
		List<Book> list = new ArrayList<>();
		try {
			if (pIsbn != null && !pIsbn.isEmpty()) { 
				Book b = searchByIsbn(pIsbn);
				if (b != null) list.add(b);
			} else if (pFields != null && !pFields.isEmpty()) {
				list = searchByTitleOrAuthor(pFields);
			} else if (pIsbn == null && pFields == null) {
				list = bookService.getAll();
			}
			
			if (list.isEmpty()) {
				model.addAttribute("mensagem",new MessageTO("Consulta sem resultados!", MessaType.ERRO));
			}
			
			Collections.sort(list);
			model.addAttribute("listofbooks", list);
		} catch (AtonBOException e) {
			logger.error("Erro: " + e);
			model.addAttribute("mensagem",new MessageTO(e.getBusinessMessage(), MessaType.ERRO));
		}
		
		return "livros/livros";		
	}
	
	
	private Book searchByIsbn(String isbn) throws AtonBOException {
		return bookService.findByIsbn(isbn.trim().toUpperCase());
	}
	
	private List<Book> searchByTitleOrAuthor(String param) throws AtonBOException {
		List<Book> list = new ArrayList<>();
		param = param.trim().toUpperCase();
		
		list = bookService.findByTitle(param);
		
		if (list.isEmpty()) {
			list = bookService.findByAuthorName(param);						
		}	
		return list;
	}		
				
}
