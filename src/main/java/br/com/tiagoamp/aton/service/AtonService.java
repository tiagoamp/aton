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
import br.com.tiagoamp.aton.dao.LivroDAO;
import br.com.tiagoamp.aton.dao.LivroDaoBdLocal;
import br.com.tiagoamp.aton.dao.PessoaDAO;
import br.com.tiagoamp.aton.dao.PessoaDaoBdLocal;
import br.com.tiagoamp.aton.model.AtonBOException;
import br.com.tiagoamp.aton.model.Emprestimo;
import br.com.tiagoamp.aton.model.Livro;
import br.com.tiagoamp.aton.model.Perfil;
import br.com.tiagoamp.aton.model.Pessoa;

/**
 * Fornece servicos de CRUD para a UI.
 * 
 * @author tiagoamp
 */
public class AtonService {
	
	Logger logger = Logger.getLogger(AtonService.class);
	
	public AtonService() {		
		this.pessoaDao = new PessoaDaoBdLocal();
		this.livroDao = new LivroDaoBdLocal();
		this.empDao = new EmprestimoDaoBdLocal();		
	}
	
	private PessoaDAO pessoaDao;
	private LivroDAO livroDao;
	private EmprestimoDAO empDao;
	
	public boolean inserirPessoa(Pessoa pessoa) throws AtonBOException {
		try {
			Pessoa p = pessoaDao.findByEmail(pessoa.getEmail());
			if (p != null) throw new AtonBOException("E-mail já cadastrado!");
			int result = pessoaDao.create(pessoa);
			return result == 1;
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new AtonBOException("Erro durante acesso no banco de dados!", e);
		}
	}
	
	public boolean atualizarPessoa(Pessoa pessoa) throws AtonBOException {
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
	
	public Pessoa consultarPessoa(int id) throws AtonBOException {
		try {
			return pessoaDao.findById(id);
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new AtonBOException("Erro durante acesso no banco de dados!", e);
		}
	}
	
	public Pessoa consultarPessoaPorEmail(String email) throws AtonBOException {
		try {
			return pessoaDao.findByEmail(email);
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new AtonBOException("Erro durante acesso no banco de dados!", e);
		}
	}
	
	public List<Pessoa> consultarPessoas(String nome, String telefone, Perfil perfil) throws AtonBOException {
		try {
			return pessoaDao.find(nome, telefone, perfil);			
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new AtonBOException("Erro durante acesso no banco de dados!", e);
		}
	}
	
	public List<Pessoa> consultarPessoasPorNomeAproximado(String nome) throws AtonBOException {
		try {
			return pessoaDao.findByNomeAproximado(nome);			
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new AtonBOException("Erro durante acesso no banco de dados!", e);
		}
	}
	
	public List<Pessoa> consultarPessoas() throws AtonBOException {
		try {
			return pessoaDao.findAll();
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new AtonBOException("Erro durante acesso no banco de dados!", e);
		}
	}
	
	public boolean inserirLivro(Livro livro) throws AtonBOException {
		try {
			Livro l = livroDao.findByIsbn(livro.getIsbn());
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
	
	public boolean atualizarLivro(Livro livro) throws AtonBOException {
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
	
	public Livro consultarLivro(int id) throws AtonBOException {
		try {
			return livroDao.findById(id);
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new AtonBOException("Erro durante acesso no banco de dados!", e);
		}
	}
	
	public Livro consultarLivroPorIsbn(String isbn) throws AtonBOException {
		try {
			return livroDao.findByIsbn(isbn);
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new AtonBOException("Erro durante acesso no banco de dados!", e);
		}
	}
	
	public List<Livro> consultarLivros(String titulo, String autor, String isbn, String classificacao, String publico) throws AtonBOException {
		try {
			return livroDao.find(titulo, autor, isbn, classificacao, publico);
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new AtonBOException("Erro durante acesso no banco de dados!", e);
		}
	}
	
	public List<Livro> consultarLivros() throws AtonBOException {
		try {
			return livroDao.findAll();
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new AtonBOException("Erro durante acesso no banco de dados!", e);
		}
	}
	
	public List<Livro> consultarLivrosPorAutorAproximado(String autor) throws AtonBOException {
		try {
			return livroDao.findByAutorAproximado(autor);			
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new AtonBOException("Erro durante acesso no banco de dados!", e);
		}
	}
	
	public List<Livro> consultarLivrosPorTituloAproximado(String titulo) throws AtonBOException {
		try {
			return livroDao.findByTituloAproximado(titulo);			
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

	
	public PessoaDAO getPessoaDao() {
		return pessoaDao;
	}
	public void setPessoaDao(PessoaDAO pessoaDao) {
		this.pessoaDao = pessoaDao;
	}
	public LivroDAO getLivroDao() {
		return livroDao;
	}
	public void setLivroDao(LivroDAO livroDao) {
		this.livroDao = livroDao;
	}
	public EmprestimoDAO getEmpDao() {
		return empDao;
	}
	public void setEmpDao(EmprestimoDAO empDao) {
		this.empDao = empDao;
	}	

}
