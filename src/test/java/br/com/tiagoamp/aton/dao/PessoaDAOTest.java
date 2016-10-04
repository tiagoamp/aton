package br.com.tiagoamp.aton.dao;

import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.tiagoamp.aton.TestHelper;
import br.com.tiagoamp.aton.model.Pessoa;

public class PessoaDAOTest {
	
	private PessoaDAO dao;
	private Pessoa pessoa;
	
	@Before
	public void setup() throws ClassNotFoundException {
		dao = new PessoaDaoBdLocal();
		((PessoaDaoBdLocal)dao).setURL_DB("jdbc:sqlite:" + ((PessoaDaoBdLocal)dao).getPATH_DB() + "atondbtests");
		pessoa = TestHelper.getPessoaTeste();
	}
	
	@After
	public void teardown() {
		limparBaseDeDadosDeTeste();
		dao = null;		
	}
	
	private void limparBaseDeDadosDeTeste() {
		try {
			List<Pessoa> lista = dao.findAll();
			for (Iterator<Pessoa> iterator = lista.iterator(); iterator.hasNext();) {
				Pessoa pessoa = (Pessoa) iterator.next();
				dao.delete(pessoa.getId());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testCreate() throws SQLException {
		int result = dao.create(pessoa);
		assertTrue(result == 1);	
	}

	@Test
	public void testUpdate() throws SQLException {
		// criando massa de dados
		dao.create(pessoa);
		List<Pessoa> lista = dao.find(pessoa.getNome(), pessoa.getTelefone(), pessoa.getPerfil());
		pessoa = lista.get(0);
		String nmAlterado = "Nome Alterado 2";
		pessoa.setNome(nmAlterado);
		// teste
		int result = dao.update(pessoa);
		assertTrue(result == 1);
		lista = dao.find(pessoa.getNome(), pessoa.getTelefone(), pessoa.getPerfil());
		pessoa = lista.get(0);
		assertTrue(nmAlterado.toUpperCase().equals(pessoa.getNome()));
	}

	@Test
	public void testFindById() throws SQLException{
		// criando massa de dados
		dao.create(pessoa);
		List<Pessoa> lista = dao.find(pessoa.getNome(), pessoa.getTelefone(), pessoa.getPerfil());
		int id = lista.get(0).getId();
		// teste
		Pessoa pessoa2 = dao.findById(id);
		assertTrue(id == pessoa2.getId());		
	}
	
	@Test
	public void testFindByEmail() throws SQLException {
		// criando massa de dados
		dao.create(pessoa);
		List<Pessoa> lista = dao.find(pessoa.getNome(), pessoa.getTelefone(), pessoa.getPerfil());
		String email = lista.get(0).getEmail();
		// teste
		Pessoa pessoa2 = dao.findByEmail(email);
		assertTrue(email.equals(pessoa2.getEmail()));		
	}
		
	@Test
	public void testFindByNomeAproximado() throws SQLException {
		// criando massa de dados
		dao.create(pessoa);
		List<Pessoa> lista = dao.find(pessoa.getNome(), pessoa.getTelefone(), pessoa.getPerfil());
		String nome = lista.get(0).getNome();
		// teste
		List<Pessoa> lista2 = dao.findByNomeAproximado(nome);
		Pessoa pessoa2 = lista2.get(0);
		assertTrue(nome.equals(pessoa2.getNome()));
	}

	@Test
	public void testFindStringStringString() throws SQLException {
		// criando massa de dados
		dao.create(pessoa);
		// teste
		List<Pessoa> lista = dao.find(pessoa.getNome(), pessoa.getTelefone(), pessoa.getPerfil());
		assertTrue(!lista.isEmpty());
		assertTrue(lista.size() == 1);
	}
	
	@Test
	public void testFindAll() throws SQLException {
		// criando massa de dados
		dao.create(pessoa);
		// teste
		List<Pessoa> lista = dao.findAll();
		assertTrue(!lista.isEmpty());
	}
	
	@Test
	public void testDelete() throws SQLException {
		// criando massa de dados
		dao.create(pessoa);
		List<Pessoa> lista = dao.find(pessoa.getNome(), pessoa.getTelefone(), pessoa.getPerfil());
		pessoa = lista.get(0);
		// teste
		int result = dao.delete(pessoa.getId());
		assertTrue(result == 1);			
		
	}

}
