/*package br.com.tiagoamp.aton.dao;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.biblioteca.TestHelper;
import br.com.tiagoamp.biblioteca.dao.ILivroDAO;
import br.com.tiagoamp.biblioteca.dao.LivroDaoBdLocal;
import br.com.tiagoamp.biblioteca.model.Livro;

public class LivroDAOTest {
	
	private ILivroDAO dao;
	
	@Before
	public void iniciar() throws ClassNotFoundException {
		dao = new LivroDaoBdLocal();
		((LivroDaoBdLocal)dao).setURL_DB("jdbc:sqlite:/home/tiago/proj/Biblioteca/fontes_novo/Biblioteca/database/libdatabase_testes");
		limparBaseDeDadosDeTeste();
	}
	
	@After
	public void encerrar() {
		limparBaseDeDadosDeTeste();		
		dao = null;		
	}
	
	private void limparBaseDeDadosDeTeste() {
		try {
			List<Livro> lista = dao.consultar();
			for (Iterator<Livro> iterator = lista.iterator(); iterator.hasNext();) {
				Livro livro = (Livro) iterator.next();
				dao.apagar(livro.getId());
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
			dao.inserir(livro);
			assertTrue("Nao deve lancar excecao",true);
			
			// apagando massa de dados
			List<Livro> lista = dao.consultar(livro.getTitulo(), livro.getAutor(), livro.getIsbn(), livro.getClassificacao(), livro.getPublicoAlvo());
			livro = lista.get(0);
			dao.apagar(livro.getId());			
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
			dao.inserir(livro);
			List<Livro> lista = dao.consultar(livro.getTitulo(), livro.getAutor(), livro.getIsbn(), livro.getClassificacao(), livro.getPublicoAlvo());
			livro = lista.get(0);
			livro.setAutor("Autor Alterado 2");
			
			// teste
			dao.atualizar(livro);
			assertTrue("Nao deve lancar excecao",true);
			lista = dao.consultar(livro.getTitulo(), livro.getAutor(), livro.getIsbn(), livro.getClassificacao(), livro.getPublicoAlvo());
			Livro livro2 = lista.get(0);
			assertTrue(livro2.getAutor().equals(livro.getAutor()));
			
			// apagando massa de dados
			dao.apagar(livro2.getId());
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
			dao.inserir(livro);
			List<Livro> lista = dao.consultar(livro.getTitulo(), livro.getAutor(), livro.getIsbn(), livro.getClassificacao(), livro.getPublicoAlvo());
			livro = lista.get(0);
			
			// teste
			dao.apagar(livro.getId());
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
			dao.inserir(livro);
			List<Livro> lista = dao.consultar(livro.getTitulo(), livro.getAutor(), livro.getIsbn(), livro.getClassificacao(), livro.getPublicoAlvo());
			int id = lista.get(0).getId();
			
			// teste
			Livro livro2 = dao.consultar(id);
			assertTrue(id == livro2.getId());
			
			// apagando massa de dados
			dao.apagar(livro2.getId());
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
			dao.inserir(livro);
			
			// teste
			List<Livro> lista = dao.consultar(livro.getTitulo(), livro.getAutor(), livro.getIsbn(), livro.getClassificacao(), livro.getPublicoAlvo());
			assertTrue(!lista.isEmpty());
			assertTrue(lista.size() == 1);
			
			// apagando massa de dados
			livro = lista.get(0);
			dao.apagar(livro.getId());
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
			dao.inserir(livro);
			
			// teste
			List<Livro> lista = dao.consultar();
			assertTrue(!lista.isEmpty());
			
			// apagando massa de dados
			livro = lista.get(0);
			dao.apagar(livro.getId());
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Nao deveria lancar excecao!");
		}
	}
	
}
*/