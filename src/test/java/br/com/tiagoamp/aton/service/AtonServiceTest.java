package br.com.tiagoamp.aton.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.tiagoamp.aton.TestHelper;
import br.com.tiagoamp.aton.dao.PessoaDAO;
import br.com.tiagoamp.aton.model.BibException;
import br.com.tiagoamp.aton.model.Pessoa;

public class AtonServiceTest {
	
	@Mock
	private PessoaDAO pessoaDAOMock;
	
	// class under test
	private AtonService service;
	
	private Pessoa pessoa;

	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		service = new AtonService();
		pessoa = TestHelper.getPessoaTeste();
		
		service.setPessoaDao(pessoaDAOMock);
	}

	@After
	public void tearDown() throws Exception {
	}			
		

	@Test
	public void testInserirPessoa_shouldInsertPessoa() throws SQLException, BibException {
		when(pessoaDAOMock.create(pessoa)).thenReturn(new Integer(1));
		boolean result = service.inserirPessoa(pessoa);
		assertTrue(result);
		verify(pessoaDAOMock).create(pessoa);
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected = BibException.class)
	public void testInserirPessoa_shouldThrowException() throws SQLException, BibException {
		when(pessoaDAOMock.create(pessoa)).thenThrow(SQLException.class);
		service.inserirPessoa(pessoa);		
	}
	
	@Test
	public void testAtualizarPessoa_shouldUpdatePessoa() throws SQLException, BibException {
		when(pessoaDAOMock.update(pessoa)).thenReturn(new Integer(1));
		boolean result = service.atualizarPessoa(pessoa);
		assertTrue(result);
		verify(pessoaDAOMock).update(pessoa);
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected = BibException.class)
	public void testAtualizarPessoa_shouldThrowException() throws SQLException, BibException {
		when(pessoaDAOMock.update(pessoa)).thenThrow(SQLException.class);
		service.atualizarPessoa(pessoa);		
	}

	@Test
	public void testApagarPessoa_shouldDeletePessoa() throws SQLException, BibException {
		pessoa.setId(100);
		when(pessoaDAOMock.delete(pessoa.getId())).thenReturn(new Integer(1));
		boolean result = service.apagarPessoa(pessoa.getId());
		assertTrue(result);
		verify(pessoaDAOMock).delete(pessoa.getId());
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected = BibException.class)
	public void testApagarPessoa_shouldThrowException() throws SQLException, BibException {
		pessoa.setId(100);
		when(pessoaDAOMock.delete(pessoa.getId())).thenThrow(SQLException.class);
		service.apagarPessoa(pessoa.getId());		
	}
	
	@Test
	public void testConsultarPessoaPorId_shouldReturnValidOutput() throws SQLException, BibException {
		int id = 100;
		when(pessoaDAOMock.findById(id)).thenReturn(pessoa);
		Pessoa p = service.consultarPessoa(id);
		assertTrue(p != null);
		verify(pessoaDAOMock).findById(id);
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected = BibException.class)
	public void testConsultarPessoaPorId_shouldThrowException() throws SQLException, BibException {
		int id = 100;
		when(pessoaDAOMock.findById(id)).thenThrow(SQLException.class);
		service.consultarPessoa(id);	
	}
	
	@Test
	public void testConsultarPessoaPorEmail_shouldReturnValidOutput() throws SQLException, BibException {
		when(pessoaDAOMock.findByEmail(pessoa.getEmail())).thenReturn(pessoa);
		Pessoa p = service.consultarPessoaPorEmail(pessoa.getEmail());
		assertTrue(p != null);
		verify(pessoaDAOMock).findByEmail(pessoa.getEmail());
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected = BibException.class)
	public void testConsultarPessoaPorEmail_shouldThrowException() throws SQLException, BibException {
		when(pessoaDAOMock.findByEmail(pessoa.getEmail())).thenThrow(SQLException.class);
		service.consultarPessoaPorEmail(pessoa.getEmail());	
	}

	
	@Test
	public void testConsultarPessoasParametros_shouldReturnValidOutput() throws SQLException, BibException {
		List<Pessoa> lista = new ArrayList<>();
		when(pessoaDAOMock.find(pessoa.getNome(), pessoa.getTelefone(), pessoa.getPerfil())).thenReturn(lista);
		lista = service.consultarPessoas(pessoa.getNome(), pessoa.getTelefone(), pessoa.getPerfil());
		assertTrue(lista != null);
		verify(pessoaDAOMock).find(pessoa.getNome(), pessoa.getTelefone(), pessoa.getPerfil());
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected = BibException.class)
	public void testConsultarPessoasParametros_shouldThrowException() throws SQLException, BibException {
		when(pessoaDAOMock.find(pessoa.getNome(), pessoa.getTelefone(), pessoa.getPerfil())).thenThrow(SQLException.class);
		service.consultarPessoas(pessoa.getNome(), pessoa.getTelefone(), pessoa.getPerfil());	
	}
		
	@Test
	public void testConsultarPessoasPorNomeAproximado_shouldReturnValidOutput() throws SQLException, BibException {
		List<Pessoa> lista = new ArrayList<>();
		when(pessoaDAOMock.findByNomeAproximado(pessoa.getNome())).thenReturn(lista);
		lista = service.consultarPessoasPorNomeAproximado(pessoa.getNome());
		assertTrue(lista != null);
		verify(pessoaDAOMock).findByNomeAproximado(pessoa.getNome());
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected = BibException.class)
	public void testConsultarPessoasPorNomeAproximado_shouldThrowException() throws SQLException, BibException {
		when(pessoaDAOMock.findByNomeAproximado(pessoa.getNome())).thenThrow(SQLException.class);
		service.consultarPessoasPorNomeAproximado(pessoa.getNome());	
	}
	
	@Test
	public void testConsultarPessoas_shouldReturnValidOutput() throws SQLException, BibException {
		List<Pessoa> lista = new ArrayList<>();
		when(pessoaDAOMock.findAll()).thenReturn(lista);
		lista = service.consultarPessoas();
		assertTrue(lista != null);
		verify(pessoaDAOMock).findAll();
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected = BibException.class)
	public void testConsultarPessoas_shouldThrowException() throws SQLException, BibException {
		when(pessoaDAOMock.findAll()).thenThrow(SQLException.class);
		service.consultarPessoas();	
	}
	

	/*@Test
	public void testInserirLivro() {
		try {
			Livro l = TestHelper.getLivroTeste();
			daoLivroMock.inserir((Livro)EasyMock.anyObject());
			EasyMock.replay(daoLivroMock);		
			EasyMock.verify();
			
			service.inserirLivro(l);
			
			assertTrue("Não deve lançar exceção!", true);
		} catch (SQLException | BibException e) {
			e.printStackTrace();
			fail("Não deve lançar exceção!");
		}
	}

	@Test
	public void testAtualizarLivro() {
		try {
			Livro l = TestHelper.getLivroTeste();
			daoLivroMock.atualizar((Livro)EasyMock.anyObject());
			EasyMock.replay(daoLivroMock);	
			EasyMock.verify();
			
			service.atualizarLivro(l);
			
			assertTrue("Não deve lançar exceção!", true);
		} catch (SQLException | BibException e) {
			e.printStackTrace();
			fail("Não deve lançar exceção!");
		}
	}

	@Test
	public void testApagarLivro() {
		try {
			daoLivroMock.apagar(EasyMock.anyInt());
			EasyMock.replay(daoLivroMock);	
			EasyMock.verify();
			
			service.apagarLivro(100);
			
			assertTrue("Não deve lançar exceção!", true);
		} catch (SQLException | BibException e) {
			e.printStackTrace();
			fail("Não deve lançar exceção!");
		}
	}

	@Test
	public void testConsultarLivroInt() {
		try {
			Livro l = TestHelper.getLivroTeste();
			EasyMock.expect(daoLivroMock.consultar(EasyMock.anyInt())).andReturn(l);
			EasyMock.replay(daoLivroMock);	
			EasyMock.verify();
						
			Livro l2 = service.consultarLivro(100);
			
			assertTrue(l.getAutor().equals(l2.getAutor()));
		} catch (SQLException | BibException e) {
			e.printStackTrace();
			fail("Não deve lançar exceção!");
		}
	}

	@Test
	public void testConsultarLivroStringStringStringStringString() {
		try {
			Livro l = TestHelper.getLivroTeste();
			List<Livro> lista = new ArrayList<>();
			lista.add(l);
			EasyMock.expect(daoLivroMock.consultar(EasyMock.anyString(),EasyMock.anyString(),EasyMock.anyString(), EasyMock.anyString(), EasyMock.anyString())).andReturn(lista);
			EasyMock.replay(daoLivroMock);	
			EasyMock.verify();
						
			Livro l2 = service.consultarLivro(l.getTitulo(), l.getAutor(),l.getIsbn(),l.getClassificacao(),l.getPublicoAlvo()).get(0);
			
			assertTrue(l.getAutor().equals(l2.getAutor()));
		} catch (SQLException | BibException e) {
			e.printStackTrace();
			fail("Não deve lançar exceção!");
		}
	}

	@Test
	public void testConsultarLivros() {
		try {
			Livro l = TestHelper.getLivroTeste();
			List<Livro> lista = new ArrayList<>();
			lista.add(l);
			EasyMock.expect(daoLivroMock.consultar()).andReturn(lista);
			EasyMock.replay(daoLivroMock);	
			EasyMock.verify();
						
			Livro l2 = service.consultarLivros().get(0);
			
			assertTrue(l.getAutor().equals(l2.getAutor()));
		} catch (SQLException | BibException e) {
			e.printStackTrace();
			fail("Não deve lançar exceção!");
		}
	}

	@Test
	public void testInserirEmprestimo() {
		try {
			Emprestimo e = TestHelper.getEmprestimoTeste();
			daoEmprestimoMock.inserir((Emprestimo)EasyMock.anyObject());
			EasyMock.replay(daoEmprestimoMock);		
			EasyMock.verify();
			
			service.inserirEmprestimo(e);
			
			assertTrue("Não deve lançar exceção!", true);
		} catch (SQLException | BibException e) {
			e.printStackTrace();
			fail("Não deve lançar exceção!");
		}
	}

	@Test
	public void testAtualizarEmprestimo() {
		try {
			Emprestimo e = TestHelper.getEmprestimoTeste();
			daoEmprestimoMock.atualizar((Emprestimo)EasyMock.anyObject());
			EasyMock.replay(daoEmprestimoMock);	
			EasyMock.verify();
			
			service.atualizarEmprestimo(e);
			
			assertTrue("Não deve lançar exceção!", true);
		} catch (SQLException | BibException e) {
			e.printStackTrace();
			fail("Não deve lançar exceção!");
		}
	}

	@Test
	public void testApagarEmprestimo() {
		try {
			daoEmprestimoMock.apagar(EasyMock.anyInt());
			EasyMock.replay(daoEmprestimoMock);	
			EasyMock.verify();
			
			service.apagarEmprestimo(100);
			
			assertTrue("Não deve lançar exceção!", true);
		} catch (SQLException | BibException e) {
			e.printStackTrace();
			fail("Não deve lançar exceção!");
		}
	}

	@Test
	public void testConsultarEmprestimoInt() {
		try {
			Emprestimo e = TestHelper.getEmprestimoTeste();
			EasyMock.expect(daoEmprestimoMock.consultar(EasyMock.anyInt())).andReturn(e);
			EasyMock.replay(daoEmprestimoMock);	
			EasyMock.verify();
						
			Emprestimo e2 = service.consultarEmprestimo(100);
			
			assertTrue( e.getPessoa().getNome().equals(e2.getPessoa().getNome()) );
		} catch (SQLException | BibException e) {
			e.printStackTrace();
			fail("Não deve lançar exceção!");
		}
	}

	@Test
	public void testConsultarEmprestimoIntIntDateDate() {
		try {
			Emprestimo e = TestHelper.getEmprestimoTeste();
			List<Emprestimo> lista = new ArrayList<>();
			lista.add(e);
			EasyMock.expect(daoEmprestimoMock.consultar(EasyMock.anyInt(),EasyMock.anyInt(),(Date)EasyMock.anyObject(),(Date)EasyMock.anyObject())).andReturn(lista);
			EasyMock.replay(daoEmprestimoMock);	
			EasyMock.verify();
						
			Emprestimo e2 = service.consultarEmprestimo(100, 100, new Date(), new Date()).get(0);
			
			assertTrue( e.getPessoa().getNome().equals(e2.getPessoa().getNome()) );
		} catch (SQLException | BibException e) {
			e.printStackTrace();
			fail("Não deve lançar exceção!");
		}
	}
	
	public void testConsultarEmprestimos() {
		try {
			Emprestimo e = TestHelper.getEmprestimoTeste();
			List<Emprestimo> lista = new ArrayList<>();
			lista.add(e);
			EasyMock.expect(daoEmprestimoMock.consultar()).andReturn(lista);
			EasyMock.replay(daoEmprestimoMock);	
			EasyMock.verify();
						
			Emprestimo e2 = service.consultarEmprestimos().get(0);
			
			assertTrue( e.getPessoa().getNome().equals(e2.getPessoa().getNome()) );
		} catch (SQLException | BibException e) {
			e.printStackTrace();
			fail("Não deve lançar exceção!");
		}
	}*/

}
