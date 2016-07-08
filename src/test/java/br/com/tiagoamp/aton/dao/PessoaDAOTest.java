package br.com.tiagoamp.aton.dao;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.tiagoamp.aton.TestHelper;
import br.com.tiagoamp.aton.model.Pessoa;

public class PessoaDAOTest {
	
	private IPessoaDAO dao;
	
	@Before
	public void iniciar() throws ClassNotFoundException {
		dao = new PessoaDaoBdLocal();
		((PessoaDaoBdLocal)dao).setURL_DB("jdbc:sqlite:database/atondbtests");
		limparBaseDeDadosDeTeste();
	}
	
	@After
	public void encerrar() {
		limparBaseDeDadosDeTeste();
		dao = null;		
	}
	
	private void limparBaseDeDadosDeTeste() {
		try {
			List<Pessoa> lista = dao.consultar();
			for (Iterator<Pessoa> iterator = lista.iterator(); iterator.hasNext();) {
				Pessoa pessoa = (Pessoa) iterator.next();
				dao.apagar(pessoa.getId());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testInserir() {
		try {
			Pessoa pessoa = TestHelper.getPessoaTeste();
			
			// teste
			dao.inserir(pessoa);
			assertTrue("Nao deve lancar excecao",true);
			
			// apagando massa de dados
			List<Pessoa> lista = dao.consultar(pessoa.getNome(), pessoa.getTelefone(), pessoa.getPerfil());
			pessoa = lista.get(0);
			dao.apagar(pessoa.getId());			
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Nao deveria lancar excecao!");
		}		
	}

	@Test
	public void testAtualizar() {
		try {
			// criando massa de dados
			Pessoa pessoa = TestHelper.getPessoaTeste();
			dao.inserir(pessoa);
			List<Pessoa> lista = dao.consultar(pessoa.getNome(), pessoa.getTelefone(), pessoa.getPerfil());
			pessoa = lista.get(0);
			pessoa.setNome("Nome alterado 2");
			
			// teste
			dao.atualizar(pessoa);
			assertTrue("Nao deve lancar excecao",true);
			lista = dao.consultar(pessoa.getNome(), pessoa.getTelefone(), pessoa.getPerfil());
			Pessoa pessoa2 = lista.get(0);
			assertTrue(pessoa2.getNome().equals(pessoa.getNome()));
			
			// apagando massa de dados
			dao.apagar(pessoa2.getId());
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Nao deveria lancar excecao!");
		}
	}

	@Test
	public void testConsultarPorId() {
		try {
			// criando massa de dados
			Pessoa pessoa1 = TestHelper.getPessoaTeste();
			dao.inserir(pessoa1);
			List<Pessoa> lista = dao.consultar(pessoa1.getNome(), pessoa1.getTelefone(), pessoa1.getPerfil());
			int id = lista.get(0).getId();
			
			// teste
			Pessoa pessoa2 = dao.consultarPorId(id);
			assertTrue(id == pessoa2.getId());
			
			// apagando massa de dados
			dao.apagar(pessoa2.getId());
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Nao deveria lancar excecao!");
		}
	}
	
	@Test
	public void testConsultarPorEmail() {
		try {
			// criando massa de dados
			Pessoa pessoa1 = TestHelper.getPessoaTeste();
			dao.inserir(pessoa1);
			List<Pessoa> lista = dao.consultar(pessoa1.getNome(), pessoa1.getTelefone(), pessoa1.getPerfil());
			String email = lista.get(0).getEmail();
			
			// teste
			Pessoa pessoa2 = dao.consultarPorEmail(email);
			assertTrue(email.equals(pessoa2.getEmail()));
			
			// apagando massa de dados
			dao.apagar(pessoa2.getId());
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Nao deveria lancar excecao!");
		}
	}
	
	//TESTE QUEBRADO, APARENTEMENTE É NO DAO, NA PARTE DE SETSTRING NO PARAMETER
	
	@Test
	public void testConsultarPorNomeAproximado() {
		try {
			// criando massa de dados
			Pessoa pessoa1 = TestHelper.getPessoaTeste();
			dao.inserir(pessoa1);
			List<Pessoa> lista = dao.consultar(pessoa1.getNome(), pessoa1.getTelefone(), pessoa1.getPerfil());
			String nome = lista.get(0).getNome();
			
			// teste
			List<Pessoa> lista2 = dao.consultarPorNomeAproximado(nome);
			Pessoa pessoa2 = lista2.get(0);
			assertTrue(nome.equals(pessoa2.getNome()));
			
			// apagando massa de dados
			dao.apagar(pessoa2.getId());
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Nao deveria lancar excecao!");
		}
	}

	@Test
	public void testConsultarStringStringString() {
		try {
			// criando massa de dados
			Pessoa pessoa = TestHelper.getPessoaTeste();
			dao.inserir(pessoa);
			
			// teste
			List<Pessoa> lista = dao.consultar(pessoa.getNome(), pessoa.getTelefone(), pessoa.getPerfil());
			assertTrue(!lista.isEmpty());
			assertTrue(lista.size() == 1);
			
			// apagando massa de dados
			pessoa = lista.get(0);
			dao.apagar(pessoa.getId());
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Nao deveria lancar excecao!");
		}
	}
	
	@Test
	public void testConsultar() {
		try {
			// criando massa de dados
			Pessoa pessoa = TestHelper.getPessoaTeste();
			dao.inserir(pessoa);
			
			// teste
			List<Pessoa> lista = dao.consultar();
			assertTrue(!lista.isEmpty());
			
			// apagando massa de dados
			pessoa = lista.get(0);
			dao.apagar(pessoa.getId());
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Nao deveria lancar excecao!");
		}
	}
	
	@Test
	public void testApagar() {
		try {
			// criando massa de dados
			Pessoa pessoa = TestHelper.getPessoaTeste();
			dao.inserir(pessoa);
			List<Pessoa> lista = dao.consultar(pessoa.getNome(), pessoa.getTelefone(), pessoa.getPerfil());
			pessoa = lista.get(0);
			
			// teste
			dao.apagar(pessoa.getId());
			assertTrue("Nao deve lancar excecao",true);			
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Nao deveria lancar excecao!");
		}
	}

}
