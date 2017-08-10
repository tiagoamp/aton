package br.com.tiagoamp.aton.dao;

import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.tiagoamp.aton.TestHelper;
import br.com.tiagoamp.aton.model.Borrowing;

public class EmprestimoDAOTest {
	
	private BorrowingDAO dao;
	private Borrowing emp;
	
	@Before
	public void setup() throws ClassNotFoundException {
		dao = new BorrowingDaoJdbc();
		((BorrowingDaoJdbc)dao).setURL_DB("jdbc:sqlite:" + ((BorrowingDaoJdbc)dao).getPATH_DB() + "atondbtests");
		emp = TestHelper.getEmprestimoTeste();
	}
	
	@After
	public void teardown() {
		limparBaseDeDadosDeTeste();
		dao = null;		
	}
	
	private void limparBaseDeDadosDeTeste() {
		try {
			List<Borrowing> lista = dao.findAll();
			for (Iterator<Borrowing> iterator = lista.iterator(); iterator.hasNext();) {
				Borrowing emp = iterator.next();
				dao.delete(emp.getId());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testCreate() throws SQLException {
		int result = dao.create(emp);
		assertTrue(result == 1);
	}

	@Test
	public void testUpdate() throws SQLException {
		// criando massa de dados
		emp.setDataDevolucao(null);
		dao.create(emp); // insert sem data de devolucao
		List<Borrowing> lista = dao.findByFields(emp.getLivro().getId(),emp.getPessoa().getId(),null,null);
		emp = lista.get(0);
		emp.setDataDevolucao(new Date()); 
		// teste
		int result = dao.update(emp); // atualiza com data de devolucao
		assertTrue(result == 1);
		lista = dao.findByFields(emp.getLivro().getId(),emp.getPessoa().getId(),null,null);
		emp = lista.get(0);
		assertTrue(emp.getDataDevolucao() != null);
	}

	@Test
	public void testDelete() throws SQLException {
		// criando massa de dados
		dao.create(emp);
		List<Borrowing> lista = dao.findByFields(emp.getLivro().getId(),emp.getPessoa().getId(),null,null);
		emp = lista.get(0);
		// teste
		int result = dao.delete(emp.getId());
		assertTrue(result == 1);
	}

	@Test
	public void testFindById() throws SQLException {
		// criando massa de dados
		dao.create(emp);
		List<Borrowing> lista = dao.findByFields(emp.getLivro().getId(),emp.getPessoa().getId(),null,null);
		int id = lista.get(0).getId();
		// teste
		Borrowing emp2 = dao.findById(id);
		assertTrue(id == emp2.getId());
	}

	@Test
	public void testFindIntegerIntegerDateDate() throws SQLException {
		// criando massa de dados
		dao.create(emp);
		// teste
		List<Borrowing> lista = dao.findByFields(emp.getLivro().getId(),emp.getPessoa().getId(),null,null);
		assertTrue(!lista.isEmpty());
		assertTrue(lista.size() == 1);		
	}

	@Test
	public void testFindAll() throws SQLException {
		// criando massa de dados
		dao.create(emp);
		// teste
		List<Borrowing> lista = dao.findAll();
		assertTrue(!lista.isEmpty());
	}
	
	@Test
	public void testFindAllEmAberto() throws SQLException {
		// criando massa de dados
		emp.setDataDevolucao(null);
		dao.create(emp);
		// teste
		List<Borrowing> lista = dao.findOpenBorrowings();
		assertTrue(!lista.isEmpty());
	}

}