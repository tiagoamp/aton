package br.com.tiagoamp.aton.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import br.com.tiagoamp.aton.dao.EmprestimoDAO;
import br.com.tiagoamp.aton.dao.EmprestimoDaoBdLocal;
import br.com.tiagoamp.aton.dao.LivroDAO;
import br.com.tiagoamp.aton.dao.LivroDaoBdLocal;
import br.com.tiagoamp.aton.dao.PessoaDAO;
import br.com.tiagoamp.aton.dao.PessoaDaoBdLocal;
import br.com.tiagoamp.aton.model.BibException;
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
	
	private final String fotoDirPath;
	
	public AtonService() {		
		this.pessoaDao = new PessoaDaoBdLocal();
		this.livroDao = new LivroDaoBdLocal();
		this.empDao = new EmprestimoDaoBdLocal();
		
		InputStream istream = this.getClass().getResourceAsStream("config.properties");
		Properties prop = new Properties();
		try {
			prop.load(istream);			
		} catch (IOException e) {
			logger.error(e);
		}
		fotoDirPath = prop.getProperty("foto_path");
	}
	
	private PessoaDAO pessoaDao;
	private LivroDAO livroDao;
	private EmprestimoDAO empDao;
	
	public boolean inserirPessoa(Pessoa pessoa) throws BibException {
		try {
			Pessoa p = pessoaDao.findByEmail(pessoa.getEmail());
			if (p != null) throw new BibException("E-mail já cadastrado!");
			int result = pessoaDao.create(pessoa);
			return result == 1;
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new BibException("Erro durante acesso no banco de dados!", e);
		}
	}
	
	public boolean atualizarPessoa(Pessoa pessoa) throws BibException {
		try {
			int result = pessoaDao.update(pessoa);
			return result == 1;
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new BibException("Erro durante acesso no banco de dados!", e);
		}
	}
	
	public boolean apagarPessoa(int id) throws BibException {
		try {
			int result = pessoaDao.delete(id);
			return result == 1;
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new BibException("Erro durante acesso no banco de dados!", e);
		}
	}
	
	public Pessoa consultarPessoa(int id) throws BibException {
		try {
			return pessoaDao.findById(id);
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new BibException("Erro durante acesso no banco de dados!", e);
		}
	}
	
	public Pessoa consultarPessoaPorEmail(String email) throws BibException {
		try {
			return pessoaDao.findByEmail(email);
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new BibException("Erro durante acesso no banco de dados!", e);
		}
	}
	
	public List<Pessoa> consultarPessoas(String nome, String telefone, Perfil perfil) throws BibException {
		try {
			return pessoaDao.find(nome, telefone, perfil);			
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new BibException("Erro durante acesso no banco de dados!", e);
		}
	}
	
	public List<Pessoa> consultarPessoasPorNomeAproximado(String nome) throws BibException {
		try {
			return pessoaDao.findByNomeAproximado(nome);			
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new BibException("Erro durante acesso no banco de dados!", e);
		}
	}
	
	public List<Pessoa> consultarPessoas() throws BibException {
		try {
			return pessoaDao.findAll();
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new BibException("Erro durante acesso no banco de dados!", e);
		}
	}
	
	public void inserirLivro(Livro livro) throws BibException {
		try {
			Livro l = livroDao.findByIsbn(livro.getIsbn());
			if (l != null) throw new BibException("ISBN já cadastrado!");			
			livroDao.create(livro);			
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new BibException("Erro durante acesso no banco de dados!", e);
		}				
	}
	
	public Path inserirFotoCapaLivro(MultipartFile mFile, String isbn) throws BibException {
		Path path = null;
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
			if (!isValidaExtension) throw new BibException("Foto do livro: arquivo com extensão inválida!");
			String[] split = originalName.split("\\.");
			String extension = split[split.length - 1];
			path = Paths.get(fotoDirPath, isbn + "." + extension);
			Files.copy(mFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);			
		} catch (IOException e) {
			logger.error("Erro: " + e);
			throw new BibException("Erro ao gravar foto da capa do livro!");
		}
		return path;
	}
	
	public void atualizarLivro(Livro livro) throws BibException {
		try {
			livroDao.update(livro);
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new BibException("Erro durante acesso no banco de dados!", e);
		}
	}
	
	public void apagarLivro(int id) throws BibException {
		try {
			livroDao.delete(id);
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new BibException("Erro durante acesso no banco de dados!", e);
		}
	}
	
	public Livro consultarLivro(int id) throws BibException {
		try {
			return livroDao.findById(id);
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new BibException("Erro durante acesso no banco de dados!", e);
		}
	}
	
	public Livro consultarLivro(String isbn) throws BibException {
		try {
			return livroDao.findByIsbn(isbn);
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new BibException("Erro durante acesso no banco de dados!", e);
		}
	}
	
	public List<Livro> consultarLivro(String titulo, String autor, String isbn, String classificacao, String publico) throws BibException {
		try {
			return livroDao.find(titulo, autor, isbn, classificacao, publico);
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new BibException("Erro durante acesso no banco de dados!", e);
		}
	}
	
	public List<Livro> consultarLivros() throws BibException {
		try {
			return livroDao.findAll();
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new BibException("Erro durante acesso no banco de dados!", e);
		}
	}
	/*
	public void inserirEmprestimo(Emprestimo emprestimo) throws BibException {
		try {
			empDao.inserir(emprestimo);
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new BibException("Erro durante acesso no banco de dados!", e);
		}
	}
	
	public void atualizarEmprestimo(Emprestimo emprestimo) throws BibException {
		try {
			empDao.atualizar(emprestimo);
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new BibException("Erro durante acesso no banco de dados!", e);
		}
	}
	
	public void apagarEmprestimo(int id) throws BibException {
		try {
			empDao.apagar(id);
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new BibException("Erro durante acesso no banco de dados!", e);
		}
	}
	
	public Emprestimo consultarEmprestimo(int id) throws BibException {
		try {
			return empDao.consultar(id);
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new BibException("Erro durante acesso no banco de dados!", e);
		}
	}
	
	public List<Emprestimo> consultarEmprestimo(int idLivro, int idPessoa, Date dataEmprestimo, Date dataDevolucao) throws BibException {
		try {
			return empDao.consultar(idLivro, idPessoa, dataEmprestimo, dataDevolucao);
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new BibException("Erro durante acesso no banco de dados!", e);
		}
	}
	
	public List<Emprestimo> consultarEmprestimos() throws BibException {
		try {
			return empDao.consultar();
		} catch (SQLException e) {
			logger.error("Erro durante acesso no banco de dados! " + e);
			throw new BibException("Erro durante acesso no banco de dados!", e);
		}
	}*/

	
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
