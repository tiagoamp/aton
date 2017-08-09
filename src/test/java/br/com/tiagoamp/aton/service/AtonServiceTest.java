package br.com.tiagoamp.aton.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import br.com.tiagoamp.aton.TestHelper;
import br.com.tiagoamp.aton.dao.EmprestimoDAO;
import br.com.tiagoamp.aton.dao.BookDAO;
import br.com.tiagoamp.aton.dao.PersonDAO;
import br.com.tiagoamp.aton.model.AtonBOException;
import br.com.tiagoamp.aton.model.Emprestimo;
import br.com.tiagoamp.aton.model.Book;
import br.com.tiagoamp.aton.model.Person;

public class AtonServiceTest {
	
	@Mock
	private PersonDAO pessoaDAOMock;
	@Mock
	private BookDAO livroDAOMock;
	@Mock
	private EmprestimoDAO emprestimoDAOMock;
	
	// class under test
	private AtonService service;
	
	private Person pessoa;
	private Book livro;
	private Emprestimo emprestimo;
	
	private static int ID = 100;

	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		service = new AtonService();
		pessoa = TestHelper.getPersonForTest();
		livro = TestHelper.getBookForTest();
		emprestimo = TestHelper.getEmprestimoTeste();
		
		service.setPessoaDao(pessoaDAOMock);
		service.setLivroDao(livroDAOMock);
		service.setEmpDao(emprestimoDAOMock);
	}

	@After
	public void tearDown() throws Exception {
	}			
		

	@Test
	public void testInserirPessoa_shouldInsertPessoa() throws SQLException, AtonBOException {
		when(pessoaDAOMock.findByEmail(pessoa.getEmail())).thenReturn(null);
		when(pessoaDAOMock.create(pessoa)).thenReturn(new Integer(1));
		boolean result = service.inserirPessoa(pessoa);
		assertTrue(result);
		verify(pessoaDAOMock).findByEmail(pessoa.getEmail());
		verify(pessoaDAOMock).create(pessoa);
	}
	
	@Test(expected = AtonBOException.class)
	public void testInserirPessoa_JahExistente_shouldThrowException() throws SQLException, AtonBOException {
		when(pessoaDAOMock.findByEmail(pessoa.getEmail())).thenReturn(pessoa);
		service.inserirPessoa(pessoa);
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected = AtonBOException.class)
	public void testInserirPessoa_ErroBD_shouldThrowException() throws SQLException, AtonBOException {
		when(pessoaDAOMock.create(pessoa)).thenThrow(SQLException.class);
		service.inserirPessoa(pessoa);		
	}
	
	@Test
	public void testAtualizarPessoa_shouldUpdatePessoa() throws SQLException, AtonBOException {
		when(pessoaDAOMock.update(pessoa)).thenReturn(new Integer(1));
		boolean result = service.atualizarPessoa(pessoa);
		assertTrue(result);
		verify(pessoaDAOMock).update(pessoa);
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected = AtonBOException.class)
	public void testAtualizarPessoa_shouldThrowException() throws SQLException, AtonBOException {
		when(pessoaDAOMock.update(pessoa)).thenThrow(SQLException.class);
		service.atualizarPessoa(pessoa);		
	}

	@Test
	public void testApagarPessoa_shouldDeletePessoa() throws SQLException, AtonBOException {
		pessoa.setId(ID);
		when(pessoaDAOMock.delete(pessoa.getId())).thenReturn(new Integer(1));
		boolean result = service.apagarPessoa(pessoa.getId());
		assertTrue(result);
		verify(pessoaDAOMock).delete(pessoa.getId());
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected = AtonBOException.class)
	public void testApagarPessoa_shouldThrowException() throws SQLException, AtonBOException {
		pessoa.setId(ID);
		when(pessoaDAOMock.delete(pessoa.getId())).thenThrow(SQLException.class);
		service.apagarPessoa(pessoa.getId());		
	}
	
	@Test
	public void testConsultarPessoaPorId_shouldReturnValidOutput() throws SQLException, AtonBOException {
		when(pessoaDAOMock.findById(ID)).thenReturn(pessoa);
		pessoa = service.consultarPessoa(ID);
		assertTrue(pessoa != null);
		verify(pessoaDAOMock).findById(ID);
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected = AtonBOException.class)
	public void testConsultarPessoaPorId_shouldThrowException() throws SQLException, AtonBOException {
		when(pessoaDAOMock.findById(ID)).thenThrow(SQLException.class);
		service.consultarPessoa(ID);	
	}
	
	@Test
	public void testConsultarPessoaPorEmail_shouldReturnValidOutput() throws SQLException, AtonBOException {
		when(pessoaDAOMock.findByEmail(pessoa.getEmail())).thenReturn(pessoa);
		pessoa = service.consultarPessoaPorEmail(pessoa.getEmail());
		assertTrue(pessoa != null);
		verify(pessoaDAOMock).findByEmail(pessoa.getEmail());
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected = AtonBOException.class)
	public void testConsultarPessoaPorEmail_shouldThrowException() throws SQLException, AtonBOException {
		when(pessoaDAOMock.findByEmail(pessoa.getEmail())).thenThrow(SQLException.class);
		service.consultarPessoaPorEmail(pessoa.getEmail());	
	}

	
	@Test
	public void testConsultarPessoasParametros_shouldReturnValidOutput() throws SQLException, AtonBOException {
		List<Person> lista = new ArrayList<>();
		when(pessoaDAOMock.findByFields(pessoa.getNome(), pessoa.getTelefone(), pessoa.getPerfil())).thenReturn(lista);
		lista = service.consultarPessoas(pessoa.getNome(), pessoa.getTelefone(), pessoa.getPerfil());
		assertTrue(lista != null);
		verify(pessoaDAOMock).findByFields(pessoa.getNome(), pessoa.getTelefone(), pessoa.getPerfil());
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected = AtonBOException.class)
	public void testConsultarPessoasParametros_shouldThrowException() throws SQLException, AtonBOException {
		when(pessoaDAOMock.findByFields(pessoa.getNome(), pessoa.getTelefone(), pessoa.getPerfil())).thenThrow(SQLException.class);
		service.consultarPessoas(pessoa.getNome(), pessoa.getTelefone(), pessoa.getPerfil());	
	}
		
	@Test
	public void testConsultarPessoasPorNomeAproximado_shouldReturnValidOutput() throws SQLException, AtonBOException {
		List<Person> lista = new ArrayList<>();
		when(pessoaDAOMock.findByNameLike(pessoa.getNome())).thenReturn(lista);
		lista = service.consultarPessoasPorNomeAproximado(pessoa.getNome());
		assertTrue(lista != null);
		verify(pessoaDAOMock).findByNameLike(pessoa.getNome());
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected = AtonBOException.class)
	public void testConsultarPessoasPorNomeAproximado_shouldThrowException() throws SQLException, AtonBOException {
		when(pessoaDAOMock.findByNameLike(pessoa.getNome())).thenThrow(SQLException.class);
		service.consultarPessoasPorNomeAproximado(pessoa.getNome());	
	}
	
	@Test
	public void testConsultarPessoas_shouldReturnValidOutput() throws SQLException, AtonBOException {
		List<Person> lista = new ArrayList<>();
		when(pessoaDAOMock.findAll()).thenReturn(lista);
		lista = service.consultarPessoas();
		assertTrue(lista != null);
		verify(pessoaDAOMock).findAll();
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected = AtonBOException.class)
	public void testConsultarPessoas_shouldThrowException() throws SQLException, AtonBOException {
		when(pessoaDAOMock.findAll()).thenThrow(SQLException.class);
		service.consultarPessoas();	
	}
	
	@Test
	public void testInserirLivro_shouldInsertLivro() throws SQLException, AtonBOException {
		when(livroDAOMock.findByIsbn(livro.getIsbn())).thenReturn(null);
		when(livroDAOMock.create(livro)).thenReturn(new Integer(1));
		boolean result = service.inserirLivro(livro);
		assertTrue(result);
		verify(livroDAOMock).findByIsbn(livro.getIsbn());
		verify(livroDAOMock).create(livro);
	}
	
	@Test(expected = AtonBOException.class)
	public void testInserirLivro_JahExistente_shouldThrowException() throws SQLException, AtonBOException {
		when(livroDAOMock.findByIsbn(livro.getIsbn())).thenReturn(livro);
		service.inserirLivro(livro);
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected = AtonBOException.class)
	public void testInserirLivro_ErroBD_shouldThrowException() throws SQLException, AtonBOException {
		when(livroDAOMock.create(livro)).thenThrow(SQLException.class);
		service.inserirLivro(livro);		
	}
	
	@Test(expected = AtonBOException.class)
	public void testInserirFotoCapaLivro_invalidFileEXtension_shouldThrowExceptio() throws AtonBOException, IOException {
		File file = new File("teste.txt");
	    DiskFileItem fileItem = new DiskFileItem("file", "text/plain", false, file.getName(), (int) file.length() , file.getParentFile());
	    fileItem.getOutputStream();
		MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
	    
		service.inserirFotoCapaLivro(multipartFile, livro.getIsbn());
	}
	
	@Test
	public void testAtualizarLivro_shouldUpdateLivro() throws SQLException, AtonBOException {
		when(livroDAOMock.update(livro)).thenReturn(new Integer(1));
		boolean result = service.atualizarLivro(livro);
		assertTrue(result);
		verify(livroDAOMock).update(livro);
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected = AtonBOException.class)
	public void testAtualizarLivro_shouldThrowException() throws SQLException, AtonBOException {
		when(livroDAOMock.update(livro)).thenThrow(SQLException.class);
		service.atualizarLivro(livro);		
	}
	
	@Test
	public void testApagarLivro_shouldDeleteLivro() throws SQLException, AtonBOException {
		when(livroDAOMock.delete(ID)).thenReturn(new Integer(1));
		boolean result = service.apagarLivro(ID);
		assertTrue(result);
		verify(livroDAOMock).delete(ID);
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected = AtonBOException.class)
	public void testApagarLivro_shouldThrowException() throws SQLException, AtonBOException {
		livro.setId(ID);
		when(livroDAOMock.delete(livro.getId())).thenThrow(SQLException.class);
		service.apagarLivro(livro.getId());		
	}
	
	@Test
	public void testConsultarLivroPorId_shouldReturnValidOutput() throws SQLException, AtonBOException {
		when(livroDAOMock.findById(ID)).thenReturn(livro);
		livro = service.consultarLivro(ID);
		assertTrue(livro != null);
		verify(livroDAOMock).findById(ID);
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected = AtonBOException.class)
	public void testConsultarLivroPorId_shouldThrowException() throws SQLException, AtonBOException {
		when(livroDAOMock.findById(ID)).thenThrow(SQLException.class);
		service.consultarLivro(ID);	
	}
	
	@Test
	public void testConsultarLivroPorIsbn_shouldReturnValidOutput() throws SQLException, AtonBOException {
		when(livroDAOMock.findByIsbn(livro.getIsbn())).thenReturn(livro);
		livro = service.consultarLivroPorIsbn(livro.getIsbn());
		assertTrue(livro != null);
		verify(livroDAOMock).findByIsbn(livro.getIsbn());
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected = AtonBOException.class)
	public void testConsultarLivroPorIsbn_shouldThrowException() throws SQLException, AtonBOException {
		when(livroDAOMock.findByIsbn(livro.getIsbn())).thenThrow(SQLException.class);
		service.consultarLivroPorIsbn(livro.getIsbn());	
	}
	
	@Test
	public void testConsultarLivrosParametros_shouldReturnValidOutput() throws SQLException, AtonBOException {
		List<Book> lista = new ArrayList<>();
		when(livroDAOMock.findByFields(livro.getTitulo(), livro.getAutoresAgrupados(), livro.getIsbn(), livro.getClassificacao(), livro.getPublicoAlvo())).thenReturn(lista);
		lista = service.consultarLivros(livro.getTitulo(), livro.getAutoresAgrupados(), livro.getIsbn(), livro.getClassificacao(), livro.getPublicoAlvo());
		assertTrue(lista != null);
		verify(livroDAOMock).findByFields(livro.getTitulo(), livro.getAutoresAgrupados(), livro.getIsbn(), livro.getClassificacao(), livro.getPublicoAlvo());
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected = AtonBOException.class)
	public void testConsultarLivrosParametros_shouldThrowException() throws SQLException, AtonBOException {
		when(livroDAOMock.findByFields(livro.getTitulo(), livro.getAutoresAgrupados(), livro.getIsbn(), livro.getClassificacao(), livro.getPublicoAlvo())).thenThrow(SQLException.class);
		service.consultarLivros(livro.getTitulo(), livro.getAutoresAgrupados(), livro.getIsbn(), livro.getClassificacao(), livro.getPublicoAlvo());	
	}

	@Test
	public void testConsultarLivros_shouldReturnValidOutput() throws SQLException, AtonBOException {
		List<Book> lista = new ArrayList<>();
		when(livroDAOMock.findAll()).thenReturn(lista);
		lista = service.consultarLivros();
		assertTrue(lista != null);
		verify(livroDAOMock).findAll();
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected = AtonBOException.class)
	public void testConsultarLivros_shouldThrowException() throws SQLException, AtonBOException {
		when(livroDAOMock.findAll()).thenThrow(SQLException.class);
		service.consultarLivros();	
	}
	
	@Test
	public void testInserirEmprestimo_shouldInsertEmprestimo() throws SQLException, AtonBOException {
		when(emprestimoDAOMock.create(emprestimo)).thenReturn(new Integer(1));
		boolean result = service.inserirEmprestimo(emprestimo);
		assertTrue(result);
		verify(emprestimoDAOMock).create(emprestimo);
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected = AtonBOException.class)
	public void testInserirEmprestimo_ErroBD_shouldThrowException() throws SQLException, AtonBOException {
		when(emprestimoDAOMock.create(emprestimo)).thenThrow(SQLException.class);
		service.inserirEmprestimo(emprestimo);		
	}
	
	@Test
	public void testAtualizarEmprestimo_shouldUpdateLivro() throws SQLException, AtonBOException {
		when(emprestimoDAOMock.update(emprestimo)).thenReturn(new Integer(1));
		boolean result = service.atualizarEmprestimo(emprestimo);
		assertTrue(result);
		verify(emprestimoDAOMock).update(emprestimo);
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected = AtonBOException.class)
	public void testAtualizarEmprestimo_shouldThrowException() throws SQLException, AtonBOException {
		when(emprestimoDAOMock.update(emprestimo)).thenThrow(SQLException.class);
		service.atualizarEmprestimo(emprestimo);		
	}
	
	@Test
	public void testApagarEmprestimo_shouldDeleteLivro() throws SQLException, AtonBOException {
		when(emprestimoDAOMock.delete(ID)).thenReturn(new Integer(1));
		boolean result = service.apagarEmprestimo(ID);
		assertTrue(result);
		verify(emprestimoDAOMock).delete(ID);
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected = AtonBOException.class)
	public void testApagarEmprestimo_shouldThrowException() throws SQLException, AtonBOException {
		when(livroDAOMock.delete(ID)).thenThrow(SQLException.class);
		service.apagarLivro(ID);		
	}
	
	@Test
	public void testConsultarEmprestimoPorId_shouldReturnValidOutput() throws SQLException, AtonBOException {
		when(emprestimoDAOMock.findById(ID)).thenReturn(emprestimo);
		emprestimo = service.consultarEmprestimo(ID);
		assertTrue(emprestimo != null);
		verify(emprestimoDAOMock).findById(ID);
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected = AtonBOException.class)
	public void testConsultarEmprestimoPorId_shouldThrowException() throws SQLException, AtonBOException {
		when(emprestimoDAOMock.findById(ID)).thenThrow(SQLException.class);
		service.consultarEmprestimo(ID);	
	}
	
	@Test
	public void testConsultarEmprestimosParametros_shouldReturnValidOutput() throws SQLException, AtonBOException {
		List<Emprestimo> lista = new ArrayList<>();
		when(emprestimoDAOMock.find(ID, ID, emprestimo.getDataEmprestimo(), emprestimo.getDataDevolucao())).thenReturn(lista);
		lista = service.consultarEmprestimos(ID, ID, emprestimo.getDataEmprestimo(), emprestimo.getDataDevolucao());
		assertTrue(lista != null);
		verify(emprestimoDAOMock).find(ID, ID, emprestimo.getDataEmprestimo(), emprestimo.getDataDevolucao());
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected = AtonBOException.class)
	public void testConsultarEmprestimosParametros_shouldThrowException() throws SQLException, AtonBOException {
		when(emprestimoDAOMock.find(ID, ID, emprestimo.getDataEmprestimo(), emprestimo.getDataDevolucao())).thenThrow(SQLException.class);
		service.consultarEmprestimos(ID, ID, emprestimo.getDataEmprestimo(), emprestimo.getDataDevolucao());	
	}

	@Test
	public void testConsultarEmprestimos_shouldReturnValidOutput() throws SQLException, AtonBOException {
		List<Emprestimo> lista = new ArrayList<>();
		when(emprestimoDAOMock.findAll()).thenReturn(lista);
		lista = service.consultarEmprestimos();
		assertTrue(lista != null);
		verify(emprestimoDAOMock).findAll();
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected = AtonBOException.class)
	public void testConsultarEmprestimos_shouldThrowException() throws SQLException, AtonBOException {
		when(emprestimoDAOMock.findAll()).thenThrow(SQLException.class);
		service.consultarEmprestimos();	
	}
	
	@Test
	public void testConsultarEmprestimosEmAberto_shouldReturnValidOutput() throws SQLException, AtonBOException {
		List<Emprestimo> lista = new ArrayList<>();
		when(emprestimoDAOMock.findAllEmAberto()).thenReturn(lista);
		lista = service.consultarEmprestimosEmAberto();
		assertTrue(lista != null);
		verify(emprestimoDAOMock).findAllEmAberto();
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected = AtonBOException.class)
	public void testConsultarEmprestimosEmAberto_shouldThrowException() throws SQLException, AtonBOException {
		when(emprestimoDAOMock.findAllEmAberto()).thenThrow(SQLException.class);
		service.consultarEmprestimosEmAberto();	
	}
	
}
