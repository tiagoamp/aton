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
	private Livro livro;
	
	@Before
	public void setup() throws ClassNotFoundException {
		dao = new LivroDaoBdLocal();
		((LivroDaoBdLocal)dao).setURL_DB("jdbc:sqlite:database/atondbtests");
		limparBaseDeDadosDeTeste();
		
		livro = TestHelper.getLivroTeste();
	}
	
	@After
	public void teardown() {
		limparBaseDeDadosDeTeste();		
		dao = null;		
	}
	
	private void limparBaseDeDadosDeTeste() {
		try {
			List<Livro> lista = dao.findAll();
			for (Iterator<Livro> iterator = lista.iterator(); iterator.hasNext();) {
				Livro livro = iterator.next();
				dao.delete(livro.getId());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testCreate() throws SQLException {
		int result = dao.create(livro);
		assertTrue(result == 1);	
	}

	@Test
	public void testUpdate() throws SQLException {
		// criando massa de dados
		dao.create(livro);
		List<Livro> lista = dao.find(livro.getTitulo(), livro.getAutoresAgrupados(), livro.getIsbn(), livro.getClassificacao(), livro.getPublicoAlvo());
		livro = lista.get(0);
		String nmAutoresAlterados = "Autor Alterado 2"; 
		livro.setAutoresAgrupados(nmAutoresAlterados);			
		// teste
		int result = dao.update(livro);
		assertTrue(result == 1);
		lista = dao.find(livro.getTitulo(), livro.getAutoresAgrupados(), livro.getIsbn(), livro.getClassificacao(), livro.getPublicoAlvo());
		livro = lista.get(0);
		assertTrue(nmAutoresAlterados.toUpperCase().equals(livro.getAutoresAgrupados()));		
	}

	@Test
	public void testDelete() throws SQLException {
		// criando massa de dados
		dao.create(livro);
		List<Livro> lista = dao.find(livro.getTitulo(), livro.getAutoresAgrupados(), livro.getIsbn(), livro.getClassificacao(), livro.getPublicoAlvo());
		livro = lista.get(0);			
		// teste
		int result = dao.delete(livro.getId());
		assertTrue(result == 1);		
	}

	@Test
	public void testFindById() throws SQLException {
		// criando massa de dados
		dao.create(livro);
		List<Livro> lista = dao.find(livro.getTitulo(), livro.getAutoresAgrupados(), livro.getIsbn(), livro.getClassificacao(), livro.getPublicoAlvo());
		int id = lista.get(0).getId();
		// teste
		Livro livro2 = dao.findById(id);
		assertTrue(id == livro2.getId());
	}

	@Test
	public void testFindStringStringStringStringString() throws SQLException {
		// criando massa de dados
		dao.create(livro);
		// teste
		List<Livro> lista = dao.find(livro.getTitulo(), livro.getAutoresAgrupados(), livro.getIsbn(), livro.getClassificacao(), livro.getPublicoAlvo());
		assertTrue(!lista.isEmpty());
		assertTrue(lista.size() == 1);		
	}

	@Test
	public void testFindAll() throws SQLException {
		// criando massa de dados
		dao.create(livro);
		// teste
		List<Livro> lista = dao.findAll();
		assertTrue(!lista.isEmpty());		
	}
	
}