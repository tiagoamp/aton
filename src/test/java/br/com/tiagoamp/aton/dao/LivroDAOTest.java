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
import br.com.tiagoamp.aton.model.Livro;

public class LivroDAOTest {
	
	private LivroDAO dao;
	
	@Before
	public void iniciar() throws ClassNotFoundException {
		dao = new LivroDaoBdLocal();
		((LivroDaoBdLocal)dao).setURL_DB("jdbc:sqlite:database/atondbtests");
		limparBaseDeDadosDeTeste();
	}
	
	@After
	public void encerrar() {
		limparBaseDeDadosDeTeste();		
		dao = null;		
	}
	
	private void limparBaseDeDadosDeTeste() {
		try {
			List<Livro> lista = dao.findAll();
			for (Iterator<Livro> iterator = lista.iterator(); iterator.hasNext();) {
				Livro livro = (Livro) iterator.next();
				dao.delete(livro.getId());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testInserir() {
		try {
			Livro livro = TestHelper.getLivroTeste();
			
			// teste
			dao.create(livro);
			assertTrue("Nao deve lancar excecao",true);
			
			// apagando massa de dados
			List<Livro> lista = dao.find(livro.getTitulo(), livro.getAutoresAgrupados(), livro.getIsbn(), livro.getClassificacao(), livro.getPublicoAlvo());
			livro = lista.get(0);
			dao.delete(livro.getId());			
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Nao deveria lancar excecao!");
		}
	}

	@Test
	public void testAtualizar() {
		try {
			// criando massa de dados
			Livro livro = TestHelper.getLivroTeste();
			dao.create(livro);
			List<Livro> lista = dao.find(livro.getTitulo(), livro.getAutoresAgrupados(), livro.getIsbn(), livro.getClassificacao(), livro.getPublicoAlvo());
			livro = lista.get(0);
			livro.setAutoresAgrupados("Autor Alterado 2".toUpperCase());
			
			// teste
			dao.update(livro);
			assertTrue("Nao deve lancar excecao",true);
			lista = dao.find(livro.getTitulo(), livro.getAutoresAgrupados(), livro.getIsbn(), livro.getClassificacao(), livro.getPublicoAlvo());
			Livro livro2 = lista.get(0);
			assertTrue(livro2.getAutoresAgrupados().equals(livro.getAutoresAgrupados()));
			
			// apagando massa de dados
			dao.delete(livro2.getId());
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Nao deveria lancar excecao!");
		}
	}

	@Test
	public void testApagar() {
		try {
			// criando massa de dados
			Livro livro = TestHelper.getLivroTeste();
			dao.create(livro);
			List<Livro> lista = dao.find(livro.getTitulo(), livro.getAutoresAgrupados(), livro.getIsbn(), livro.getClassificacao(), livro.getPublicoAlvo());
			livro = lista.get(0);
			
			// teste
			dao.delete(livro.getId());
			assertTrue("Nao deve lancar excecao",true);			
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Nao deveria lancar excecao!");
		}
	}

	@Test
	public void testConsultarInt() {
		try {
			// criando massa de dados
			Livro livro = TestHelper.getLivroTeste();
			dao.create(livro);
			List<Livro> lista = dao.find(livro.getTitulo(), livro.getAutoresAgrupados(), livro.getIsbn(), livro.getClassificacao(), livro.getPublicoAlvo());
			int id = lista.get(0).getId();
			
			// teste
			Livro livro2 = dao.findById(id);
			assertTrue(id == livro2.getId());
			
			// apagando massa de dados
			dao.delete(livro2.getId());
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Nao deveria lancar excecao!");
		}
	}

	@Test
	public void testConsultarStringStringStringStringString() {
		try {
			// criando massa de dados
			Livro livro = TestHelper.getLivroTeste();
			dao.create(livro);
			
			// teste
			List<Livro> lista = dao.find(livro.getTitulo(), livro.getAutoresAgrupados(), livro.getIsbn(), livro.getClassificacao(), livro.getPublicoAlvo());
			assertTrue(!lista.isEmpty());
			assertTrue(lista.size() == 1);
			
			// apagando massa de dados
			livro = lista.get(0);
			dao.delete(livro.getId());
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Nao deveria lancar excecao!");
		}
	}

	@Test
	public void testConsultar() {
		try {
			// criando massa de dados
			Livro livro = TestHelper.getLivroTeste();
			dao.create(livro);
			
			// teste
			List<Livro> lista = dao.findAll();
			assertTrue(!lista.isEmpty());
			
			// apagando massa de dados
			livro = lista.get(0);
			dao.delete(livro.getId());
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Nao deveria lancar excecao!");
		}
	}
	
}