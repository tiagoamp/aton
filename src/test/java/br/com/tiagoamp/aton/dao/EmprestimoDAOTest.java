package br.com.tiagoamp.aton.dao;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.tiagoamp.aton.TestHelper;
import br.com.tiagoamp.aton.model.Emprestimo;

public class EmprestimoDAOTest {
	
private IEmprestimoDAO dao;
	
	@Before
	public void iniciar() throws ClassNotFoundException {
		dao = new EmprestimoDaoBdLocal();
		((EmprestimoDaoBdLocal)dao).setURL_DB("jdbc:sqlite:/home/tiago/proj/Biblioteca/fontes_novo/Biblioteca/database/libdatabase_testes");
		limparBaseDeDadosDeTeste();
	}
	
	@After
	public void encerrar() {
		limparBaseDeDadosDeTeste();
		dao = null;		
	}
	
	private void limparBaseDeDadosDeTeste() {
		try {
			List<Emprestimo> lista = dao.consultar();
			for (Iterator<Emprestimo> iterator = lista.iterator(); iterator.hasNext();) {
				Emprestimo emp = (Emprestimo) iterator.next();
				dao.apagar(emp.getId());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testInserir() {
		try {
			Emprestimo emp = TestHelper.getEmprestimoTeste();
			
			// teste
			dao.inserir(emp);
			assertTrue("Nao deve lancar excecao",true);
			
			// apagando massa de dados
			List<Emprestimo> lista = dao.consultar(emp.getLivro().getId(), emp.getPessoa().getId(), emp.getDataEmprestimo(), emp.getDataDevolucao());
			emp = lista.get(0);
			dao.apagar(emp.getId());			
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Nao deveria lancar excecao!");
		}
	}

	@Test
	public void testAtualizar() {
		try {
			// criando massa de dados
			Emprestimo emp = TestHelper.getEmprestimoTeste();
			emp.setDataDevolucao(null);
			dao.inserir(emp); // insert sem data de devolucao
			List<Emprestimo> lista = dao.consultar(emp.getLivro().getId(),emp.getPessoa().getId(),null,null);
			emp = lista.get(0);
			emp.setDataDevolucao(new Date()); 
			
			// teste
			dao.atualizar(emp); // atualiza com data de devolucao
			assertTrue("Nao deve lancar excecao",true);
			lista = dao.consultar(emp.getLivro().getId(),emp.getPessoa().getId(),null,null);
			Emprestimo emp2 = lista.get(0);
			assertTrue(emp2.getDataDevolucao() != null);
			
			// apagando massa de dados
			dao.apagar(emp2.getId());
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Nao deveria lancar excecao!");
		}
	}

	@Test
	public void testApagar() {
		try {
			// criando massa de dados
			Emprestimo emp = TestHelper.getEmprestimoTeste();
			dao.inserir(emp);
			List<Emprestimo> lista = dao.consultar(emp.getLivro().getId(),emp.getPessoa().getId(),null,null);
			emp = lista.get(0);
			
			// teste
			dao.apagar(emp.getId());
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
			Emprestimo emp = TestHelper.getEmprestimoTeste();
			dao.inserir(emp);
			List<Emprestimo> lista = dao.consultar(emp.getLivro().getId(),emp.getPessoa().getId(),null,null);
			int id = lista.get(0).getId();
			
			// teste
			Emprestimo emp2 = dao.consultar(id);
			assertTrue(id == emp2.getId());
			
			// apagando massa de dados
			dao.apagar(emp2.getId());
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Nao deveria lancar excecao!");
		}
	}

	@Test
	public void testConsultarIntegerIntegerDateDate() {
		try {
			// criando massa de dados
			Emprestimo emp = TestHelper.getEmprestimoTeste();
			dao.inserir(emp);
			
			// teste
			List<Emprestimo> lista = dao.consultar(emp.getLivro().getId(),emp.getPessoa().getId(),null,null);
			assertTrue(!lista.isEmpty());
			assertTrue(lista.size() == 1);
			
			// apagando massa de dados
			emp = lista.get(0);
			dao.apagar(emp.getId());
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Nao deveria lancar excecao!");
		}
	}

	@Test
	public void testConsultar() {
		try {
			// criando massa de dados
			Emprestimo emp = TestHelper.getEmprestimoTeste();
			dao.inserir(emp);
			
			// teste
			List<Emprestimo> lista = dao.consultar();
			assertTrue(!lista.isEmpty());
			
			// apagando massa de dados
			emp = lista.get(0);
			dao.apagar(emp.getId());
		} catch (SQLException e) {
			e.printStackTrace();
			fail("Nao deveria lancar excecao!");
		}
	}

}