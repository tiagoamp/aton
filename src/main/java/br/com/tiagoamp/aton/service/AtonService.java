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

import br.com.tiagoamp.aton.dao.EmprestimoDAO;
import br.com.tiagoamp.aton.dao.EmprestimoDaoBdLocal;
import br.com.tiagoamp.aton.dao.BookDAO;
import br.com.tiagoamp.aton.dao.BookDaoJdbc;
import br.com.tiagoamp.aton.dao.PersonDAO;
import br.com.tiagoamp.aton.dao.PessoaDaoJdbc;
import br.com.tiagoamp.aton.model.AtonBOException;
import br.com.tiagoamp.aton.model.Emprestimo;
import br.com.tiagoamp.aton.model.Book;
import br.com.tiagoamp.aton.model.Perfil;
import br.com.tiagoamp.aton.model.Person;

/**
 * Fornece servicos de CRUD para a UI.
 * 
 * @author tiagoamp
 */
public class AtonService {
	
	Logger logger = Logger.getLogger(AtonService.class);
	
	public AtonService() {		
		this.pessoaDao = new PessoaDaoJdbc();
		this.livroDao = new BookDaoJdbc();
		this.empDao = new EmprestimoDaoBdLocal();		
	}
	
	private PersonDAO pessoaDao;
	private BookDAO livroDao;
	private EmprestimoDAO empDao;
	
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
	
	public List<Person> consultarPessoas(String nome, String telefone, Perfil perfil) throws AtonBOException {
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
	
	public boolean inserirEmprestimo(Emprestimo emprestimo) throws AtonBOException {
		try {
			int result = empDao.create(emprestimo);
			return result == 1;
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new AtonBOException("Erro durante acesso no banco de dados!", e);
		}
	}
	
	public boolean atualizarEmprestimo(Emprestimo emprestimo) throws AtonBOException {
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
	
	private Emprestimo popularEmprestimo(Emprestimo emp) throws AtonBOException {
		emp.setLivro(this.consultarLivro(emp.getLivro().getId()));
		emp.setPessoa(this.consultarPessoa(emp.getPessoa().getId()));
		return emp;
	}
	
	private List<Emprestimo> popularEmprestimos(List<Emprestimo> lista) throws AtonBOException {
		List<Emprestimo> result = new ArrayList<>();
		if (!lista.isEmpty()) {
			for (Emprestimo emp : lista) {
				emp = popularEmprestimo(emp);
				result.add(emp);
			}
		}
		return result;
	}
	
	public Emprestimo consultarEmprestimo(int id) throws AtonBOException {
		try {
			Emprestimo emprestimo = empDao.findById(id);
			return popularEmprestimo(emprestimo);
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new AtonBOException("Erro durante acesso no banco de dados!", e);
		}
	}
	
	public List<Emprestimo> consultarEmprestimos(Integer idLivro, Integer idPessoa, Date dataEmprestimo, Date dataDevolucao) throws AtonBOException {
		try {
			List<Emprestimo> lista = empDao.find(idLivro, idPessoa, dataEmprestimo, dataDevolucao);
			return popularEmprestimos(lista);
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new AtonBOException("Erro durante acesso no banco de dados!", e);
		}
	}
	
	public List<Emprestimo> consultarEmprestimos() throws AtonBOException {
		try {
			List<Emprestimo> lista = empDao.findAll();
			return popularEmprestimos(lista);
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new AtonBOException("Erro durante acesso no banco de dados!", e);
		}
	}
	
	public List<Emprestimo> consultarEmprestimosEmAberto() throws AtonBOException {
		try {
			List<Emprestimo> lista = empDao.findAllEmAberto();
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
	public EmprestimoDAO getEmpDao() {
		return empDao;
	}
	public void setEmpDao(EmprestimoDAO empDao) {
		this.empDao = empDao;
	}	

}
