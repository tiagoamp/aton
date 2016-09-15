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
import br.com.tiagoamp.aton.dao.LivroDAO;
import br.com.tiagoamp.aton.dao.PessoaDAO;
import br.com.tiagoamp.aton.model.AtonBOException;
import br.com.tiagoamp.aton.model.Livro;
import br.com.tiagoamp.aton.model.Pessoa;

public class AtonServiceTest {
	
	@Mock
	private PessoaDAO pessoaDAOMock;
	@Mock
	private LivroDAO livroDAOMock;
	
	// class under test
	private AtonService service;
	
	private Pessoa pessoa;
	private Livro livro;
	
	private static int ID = 100;

	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		service = new AtonService();
		pessoa = TestHelper.getPessoaTeste();
		livro = TestHelper.getLivroTeste();
		
		service.setPessoaDao(pessoaDAOMock);
		service.setLivroDao(livroDAOMock);
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
		List<Pessoa> lista = new ArrayList<>();
		when(pessoaDAOMock.find(pessoa.getNome(), pessoa.getTelefone(), pessoa.getPerfil())).thenReturn(lista);
		lista = service.consultarPessoas(pessoa.getNome(), pessoa.getTelefone(), pessoa.getPerfil());
		assertTrue(lista != null);
		verify(pessoaDAOMock).find(pessoa.getNome(), pessoa.getTelefone(), pessoa.getPerfil());
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected = AtonBOException.class)
	public void testConsultarPessoasParametros_shouldThrowException() throws SQLException, AtonBOException {
		when(pessoaDAOMock.find(pessoa.getNome(), pessoa.getTelefone(), pessoa.getPerfil())).thenThrow(SQLException.class);
		service.consultarPessoas(pessoa.getNome(), pessoa.getTelefone(), pessoa.getPerfil());	
	}
		
	@Test
	public void testConsultarPessoasPorNomeAproximado_shouldReturnValidOutput() throws SQLException, AtonBOException {
		List<Pessoa> lista = new ArrayList<>();
		when(pessoaDAOMock.findByNomeAproximado(pessoa.getNome())).thenReturn(lista);
		lista = service.consultarPessoasPorNomeAproximado(pessoa.getNome());
		assertTrue(lista != null);
		verify(pessoaDAOMock).findByNomeAproximado(pessoa.getNome());
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected = AtonBOException.class)
	public void testConsultarPessoasPorNomeAproximado_shouldThrowException() throws SQLException, AtonBOException {
		when(pessoaDAOMock.findByNomeAproximado(pessoa.getNome())).thenThrow(SQLException.class);
		service.consultarPessoasPorNomeAproximado(pessoa.getNome());	
	}
	
	@Test
	public void testConsultarPessoas_shouldReturnValidOutput() throws SQLException, AtonBOException {
		List<Pessoa> lista = new ArrayList<>();
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
		livro.setId(ID);
		when(livroDAOMock.delete(livro.getId())).thenReturn(new Integer(1));
		boolean result = service.apagarLivro(livro.getId());
		assertTrue(result);
		verify(livroDAOMock).delete(livro.getId());
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
	
}
