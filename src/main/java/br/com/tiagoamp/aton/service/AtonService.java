package br.com.tiagoamp.aton.service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import br.com.tiagoamp.aton.dao.BorrowingDAO;
import br.com.tiagoamp.aton.dao.BorrowingDaoJdbc;
import br.com.tiagoamp.aton.dao.BookDAO;
import br.com.tiagoamp.aton.dao.BookDaoJdbc;
import br.com.tiagoamp.aton.dao.PersonDAO;
import br.com.tiagoamp.aton.dao.PessoaDaoJdbc;
import br.com.tiagoamp.aton.model.AtonBOException;
import br.com.tiagoamp.aton.model.Borrowing;
import br.com.tiagoamp.aton.model.Book;
import br.com.tiagoamp.aton.model.Role;
import br.com.tiagoamp.aton.model.Person;

/**
 * CRUD Services.
 * 
 * @author tiagoamp
 */
public class AtonService {
	
	Logger logger = Logger.getLogger(AtonService.class);
	
	public AtonService() {		
		this.pessoaDao = new PessoaDaoJdbc();
		this.livroDao = new BookDaoJdbc();
		this.empDao = new BorrowingDaoJdbc();		
	}
	
	private PersonDAO pessoaDao;
	private BookDAO livroDao;
	private BorrowingDAO empDao;
	
	public boolean inserirPessoa(Person pessoa) throws AtonBOException {
		try {
			Person p = pessoaDao.findByEmail(pessoa.getEmail());
			if (p != null) throw new AtonBOException("E-mail já cadastrado!");
			int result = pessoaDao.create(pessoa);
			return result == 1;
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new AtonBOException("Erro durante acesso no banco de dados!", e);
		}
	}
	
	public boolean atualizarPessoa(Person pessoa) throws AtonBOException {
		try {
			int result = pessoaDao.update(pessoa);
			return result == 1;
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new AtonBOException("Erro durante acesso no banco de dados!", e);
		}
	}
	
	public boolean apagarPessoa(int id) throws AtonBOException {
		try {
			int result = pessoaDao.delete(id);
			return result == 1;
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new AtonBOException("Erro durante acesso no banco de dados!", e);
		}
	}
	
	public Person consultarPessoa(int id) throws AtonBOException {
		try {
			return pessoaDao.findById(id);
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new AtonBOException("Erro durante acesso no banco de dados!", e);
		}
	}
	
	public Person consultarPessoaPorEmail(String email) throws AtonBOException {
		try {
			return pessoaDao.findByEmail(email);
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new AtonBOException("Erro durante acesso no banco de dados!", e);
		}
	}
	
	public List<Person> consultarPessoas(String nome, String telefone, Role perfil) throws AtonBOException {
		try {
			return pessoaDao.findByFields(nome, telefone, perfil);			
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new AtonBOException("Erro durante acesso no banco de dados!", e);
		}
	}
	
	public List<Person> consultarPessoasPorNomeAproximado(String nome) throws AtonBOException {
		try {
			return pessoaDao.findByNameLike(nome);			
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new AtonBOException("Erro durante acesso no banco de dados!", e);
		}
	}
	
	public List<Person> consultarPessoas() throws AtonBOException {
		try {
			return pessoaDao.findAll();
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new AtonBOException("Erro durante acesso no banco de dados!", e);
		}
	}
	
	public boolean inserirLivro(Book livro) throws AtonBOException {
		try {
			Book l = livroDao.findByIsbn(livro.getIsbn());
			if (l != null) throw new AtonBOException("ISBN já cadastrado!");			
			int result = livroDao.create(livro);
			return result == 1;
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new AtonBOException("Erro durante acesso no banco de dados!", e);
		}				
	}
	
	public Path inserirFotoCapaLivro(MultipartFile mFile, String isbn) throws AtonBOException {
		Path path = null;
		String nomeArquivo = null;
		try {
			String originalName = mFile.getOriginalFilename().toLowerCase();
			String[] validExtensions = {"jpg", "gif", "png"};
			boolean isValidaExtension = false;
			for (int i = 0; i < validExtensions.length; i++) {
				if (originalName.endsWith(validExtensions[i])) {
					isValidaExtension = true;
					break;
				}
			}
			if (!isValidaExtension) throw new AtonBOException("Foto do livro: arquivo com extensão inválida!");
			String[] split = originalName.split("\\.");
			String extension = split[split.length - 1];
			nomeArquivo = isbn + "." + extension;
			path = livroDao.createCapaLivro(mFile, nomeArquivo);
		} catch (IOException e) {
			logger.error("Erro: " + e);
			throw new AtonBOException("Erro ao gravar foto da capa do livro!");
		}
		return path != null ? Paths.get("database", "pics", nomeArquivo) : null;
	}
	
	public boolean atualizarLivro(Book livro) throws AtonBOException {
		try {
			int result = livroDao.update(livro);
			return result == 1;
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new AtonBOException("Erro durante acesso no banco de dados!", e);
		}
	}
	
	public boolean apagarLivro(int id) throws AtonBOException {
		try {
			int result = livroDao.delete(id);
			return result == 1;
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new AtonBOException("Erro durante acesso no banco de dados!", e);
		}
	}
	
	public Book consultarLivro(int id) throws AtonBOException {
		try {
			return livroDao.findById(id);
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new AtonBOException("Erro durante acesso no banco de dados!", e);
		}
	}
	
	public Book consultarLivroPorIsbn(String isbn) throws AtonBOException {
		try {
			return livroDao.findByIsbn(isbn);
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new AtonBOException("Erro durante acesso no banco de dados!", e);
		}
	}
	
	public List<Book> consultarLivros(String titulo, String autor, String isbn, String classificacao, String publico) throws AtonBOException {
		try {
			return livroDao.findByFields(titulo, autor, isbn, classificacao, publico);
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new AtonBOException("Erro durante acesso no banco de dados!", e);
		}
	}
	
	public List<Book> consultarLivros() throws AtonBOException {
		try {
			return livroDao.findAll();
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new AtonBOException("Erro durante acesso no banco de dados!", e);
		}
	}
	
	public List<Book> consultarLivrosPorAutorAproximado(String autor) throws AtonBOException {
		try {
			return livroDao.findByAuthorNameLike(autor);			
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new AtonBOException("Erro durante acesso no banco de dados!", e);
		}
	}
	
	public List<Book> consultarLivrosPorTituloAproximado(String titulo) throws AtonBOException {
		try {
			return livroDao.findByTitleLike(titulo);			
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new AtonBOException("Erro durante acesso no banco de dados!", e);
		}
	}
	
	public boolean inserirEmprestimo(Borrowing emprestimo) throws AtonBOException {
		try {
			int result = empDao.create(emprestimo);
			return result == 1;
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new AtonBOException("Erro durante acesso no banco de dados!", e);
		}
	}
	
	public boolean atualizarEmprestimo(Borrowing emprestimo) throws AtonBOException {
		try {
			int result = empDao.update(emprestimo);
			return result == 1;
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new AtonBOException("Erro durante acesso no banco de dados!", e);
		}
	}
	
	public boolean apagarEmprestimo(int id) throws AtonBOException {
		try {
			int result = empDao.delete(id);
			return result == 1;
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new AtonBOException("Erro durante acesso no banco de dados!", e);
		}
	}
	
	private Borrowing popularEmprestimo(Borrowing emp) throws AtonBOException {
		emp.setLivro(this.findById(emp.getLivro().getId()));
		emp.setPessoa(this.findById(emp.getPessoa().getId()));
		return emp;
	}
	
	private List<Borrowing> popularEmprestimos(List<Borrowing> lista) throws AtonBOException {
		List<Borrowing> result = new ArrayList<>();
		if (!lista.isEmpty()) {
			for (Borrowing emp : lista) {
				emp = popularEmprestimo(emp);
				result.add(emp);
			}
		}
		return result;
	}
	
	public Borrowing consultarEmprestimo(int id) throws AtonBOException {
		try {
			Borrowing emprestimo = empDao.findById(id);
			return popularEmprestimo(emprestimo);
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new AtonBOException("Erro durante acesso no banco de dados!", e);
		}
	}
	
	public List<Borrowing> consultarEmprestimos(Integer idLivro, Integer idPessoa, Date dataEmprestimo, Date dataDevolucao) throws AtonBOException {
		try {
			List<Borrowing> lista = empDao.findByFields(idLivro, idPessoa, dataEmprestimo, dataDevolucao);
			return popularEmprestimos(lista);
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new AtonBOException("Erro durante acesso no banco de dados!", e);
		}
	}
	
	public List<Borrowing> consultarEmprestimos() throws AtonBOException {
		try {
			List<Borrowing> lista = empDao.findAll();
			return popularEmprestimos(lista);
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new AtonBOException("Erro durante acesso no banco de dados!", e);
		}
	}
	
	public List<Borrowing> consultarEmprestimosEmAberto() throws AtonBOException {
		try {
			List<Borrowing> lista = empDao.findOpenBorrowings();
			return popularEmprestimos(lista);
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new AtonBOException("Erro durante acesso no banco de dados!", e);
		}
	}

	
	public PersonDAO getPessoaDao() {
		return pessoaDao;
	}
	public void setPessoaDao(PersonDAO pessoaDao) {
		this.pessoaDao = pessoaDao;
	}
	public BookDAO getLivroDao() {
		return livroDao;
	}
	public void setLivroDao(BookDAO livroDao) {
		this.livroDao = livroDao;
	}
	public BorrowingDAO getEmpDao() {
		return empDao;
	}
	public void setEmpDao(BorrowingDAO empDao) {
		this.empDao = empDao;
	}	

}
