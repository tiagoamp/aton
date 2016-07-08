package br.com.tiagoamp.aton.service;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.tiagoamp.aton.TestHelper;
import br.com.tiagoamp.aton.dao.EmprestimoDaoBdLocal;
import br.com.tiagoamp.aton.dao.IEmprestimoDAO;
import br.com.tiagoamp.aton.dao.ILivroDAO;
import br.com.tiagoamp.aton.dao.IPessoaDAO;
import br.com.tiagoamp.aton.dao.LivroDaoBdLocal;
import br.com.tiagoamp.aton.dao.PessoaDaoBdLocal;
import br.com.tiagoamp.aton.model.BibException;
import br.com.tiagoamp.aton.model.Perfil;
import br.com.tiagoamp.aton.model.Pessoa;

public class AtonServiceTest {
	
	// Classe alvo
	private AtonService service;

	// mocks
	private IPessoaDAO daoPessoaMock;
	private ILivroDAO daoLivroMock;
	private IEmprestimoDAO daoEmprestimoMock;

	@Before
	public void setUp() throws Exception {
		//iniciacao da classe alvo
		service = new AtonService();		
				
		//iniciacao dos mocks
		daoPessoaMock = EasyMock.createMock(PessoaDaoBdLocal.class);
		daoLivroMock = EasyMock.createMock(LivroDaoBdLocal.class);
		daoEmprestimoMock = EasyMock.createMock(EmprestimoDaoBdLocal.class);
				
		// setando mocks na classe alvo
		service.inicializarDaosBdLocal(daoPessoaMock, daoLivroMock, daoEmprestimoMock);
		
	}

	@After
	public void tearDown() throws Exception {
	}			
		

	@Test
	public void testInserirPessoa() {
		try {
			Pessoa p = TestHelper.getPessoaTeste();
			EasyMock.expect(daoPessoaMock.consultarPorEmail(EasyMock.anyString())).andReturn(null);
			daoPessoaMock.inserir((Pessoa)EasyMock.anyObject());
			EasyMock.replay(daoPessoaMock);		
			EasyMock.verify();
			
			service.inserirPessoa(p);
			
			assertTrue("Não deve lançar exceção!", true);
		} catch (SQLException | BibException e) {
			e.printStackTrace();
			fail("Não deve lançar exceção!");
		}
	}

	@Test
	public void testAtualizarPessoa() {
		try {
			Pessoa p = TestHelper.getPessoaTeste();
			daoPessoaMock.atualizar((Pessoa)EasyMock.anyObject());
			EasyMock.replay(daoPessoaMock);	
			EasyMock.verify();
			
			service.atualizarPessoa(p);
			
			assertTrue("Não deve lançar exceção!", true);
		} catch (SQLException | BibException e) {
			e.printStackTrace();
			fail("Não deve lançar exceção!");
		}
	}

	@Test
	public void testApagarPessoa() {
		try {
			daoPessoaMock.apagar(EasyMock.anyInt());
			EasyMock.replay(daoPessoaMock);	
			EasyMock.verify();
			
			service.apagarPessoa(100);
			
			assertTrue("Não deve lançar exceção!", true);
		} catch (SQLException | BibException e) {
			e.printStackTrace();
			fail("Não deve lançar exceção!");
		}
	}

	@Test
	public void testConsultarPessoaInt() {
		try {
			Pessoa p = TestHelper.getPessoaTeste();
			EasyMock.expect(daoPessoaMock.consultarPorId(EasyMock.anyInt())).andReturn(p);
			EasyMock.replay(daoPessoaMock);	
			EasyMock.verify();
						
			Pessoa p2 = service.consultarPessoa(100);
			
			assertTrue(p.getNome().equals(p2.getNome()));
		} catch (SQLException | BibException e) {
			e.printStackTrace();
			fail("Não deve lançar exceção!");
		}
	}

	@Test
	public void testConsultarPessoaStringStringString() {
		try {
			Pessoa p = TestHelper.getPessoaTeste();
			List<Pessoa> lista = new ArrayList<>();
			lista.add(p);
			EasyMock.expect(daoPessoaMock.consultar(EasyMock.anyString(),EasyMock.anyString(),(Perfil)EasyMock.anyObject())).andReturn(lista);
			EasyMock.replay(daoPessoaMock);	
			EasyMock.verify();
						
			Pessoa p2 = service.consultarPessoas(p.getNome(), p.getTelefone(), p.getPerfil()).get(0);
			
			assertTrue(p.getNome().equals(p2.getNome()));
		} catch (SQLException | BibException e) {
			e.printStackTrace();
			fail("Não deve lançar exceção!");
		}
	}

	@Test
	public void testConsultarPessoas() {
		try {
			Pessoa p = TestHelper.getPessoaTeste();
			List<Pessoa> lista = new ArrayList<>();
			lista.add(p);
			EasyMock.expect(daoPessoaMock.consultar()).andReturn(lista);
			EasyMock.replay(daoPessoaMock);	
			EasyMock.verify();
						
			Pessoa p2 = service.consultarPessoas().get(0);
			
			assertTrue(p.getNome().equals(p2.getNome()));
		} catch (SQLException | BibException e) {
			e.printStackTrace();
			fail("Não deve lançar exceção!");
		}
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
