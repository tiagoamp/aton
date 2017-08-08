package br.com.tiagoamp.aton.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.tiagoamp.aton.TestHelper;
import br.com.tiagoamp.aton.model.Perfil;
import br.com.tiagoamp.aton.model.Pessoa;

public class PessoaDAOTest {
	
	private PessoaDAO dao;
	
		
	@Before
	public void setup() throws ClassNotFoundException {
		instanciateDaoForTests("jpa");
		limparBaseDeDadosDeTeste();
	}
	
	@After
	public void teardown() {		
		dao = null;		
	}
	
	
	@Test
	public void testCreate_shouldInsertEntity() throws SQLException {
		Pessoa pessoa = TestHelper.getPessoaTeste(); 
		dao.create(pessoa);
		
		Pessoa pessoaRetrieved = dao.findByEmail(pessoa.getEmail());
		assertEquals("Must retrieve inserted entity.", pessoa.getEmail(), pessoaRetrieved.getEmail());		
	}
	
	@Test(expected=Exception.class)
	public void testCreate_existingId_shouldThrowsException() throws SQLException {
		Pessoa pessoaRetrieved = insertPessoaInDataBaseForTests();
		dao = null;
		dao = new PessoaDaoJpa(new JPAUtil().getMyTestsEntityManager());  // new dao with new entity manager
				
		dao.create(pessoaRetrieved); // same id should throw exception		
	}
	
	@Test
	public void testUpdate_shouldUpdateEntity() throws SQLException {
		Pessoa pessoaRetrieved = insertPessoaInDataBaseForTests();  // transient --> managed --> detached
		String newNome = "Nome Alterado 2".toUpperCase();
		pessoaRetrieved.setNome(newNome);
		
		dao.update(pessoaRetrieved);
		Pessoa pessoaAfterUpdate = dao.findById(pessoaRetrieved.getId());
		assertNotNull(pessoaAfterUpdate);
		assertEquals("'Nome' should be updated.", newNome, pessoaAfterUpdate.getNome());
	}
	
	@Test
	public void testDelete_shouldDeleteEntity() throws SQLException {
		Pessoa pessoaRetrieved = insertPessoaInDataBaseForTests();  // transient --> managed --> detached
		int id = pessoaRetrieved.getId();
		dao.delete(id);
		
		pessoaRetrieved = dao.findById(id);
		assertNull("Entity must be deleted.", pessoaRetrieved);
	}
	
	@Test
	public void testFindById_shouldReturnValidOutput() throws SQLException {
		Pessoa pessoaInserted = insertPessoaInDataBaseForTests();
		
		Pessoa pessoaRetrievedById = dao.findById(pessoaInserted.getId());
		assertEquals("Must retrieve the entity by Id.", pessoaInserted.getId(), pessoaRetrievedById.getId());		
	}
	
	@Test
	public void testFindAll_emptyDataBase_shouldReturnEmptyList() throws SQLException {
		List<Pessoa> list = dao.findAll();
		assertTrue("Must not retrieve entities.", list.isEmpty());
	}
	
	@Test
	public void testFindAll_shouldReturnValidOutput() throws SQLException {
		insertPessoaListInDataBaseForTestes();
		
		List<Pessoa> list = dao.findAll();
		assertNotNull("Must return entities previously inserted.", list);
		assertEquals("Should return 3 entities.", 3, list.size());
	}
	
	@Test
	public void testFindByEmail_shouldReturnValidOutput() throws SQLException {
		Pessoa pessoa = insertPessoaInDataBaseForTests();
		String email = pessoa.getEmail();
		
		Pessoa pessoaRetrievedByEmail = dao.findByEmail(email);
		assertNotNull("Must return searched entity.", pessoaRetrievedByEmail);
		assertEquals("Must have same e-mail value." , email, pessoaRetrievedByEmail.getEmail());		
	}
	
	@Test
	public void testFindByNomeAproximado_shouldReturnValidOutput() throws SQLException {
		Pessoa pessoa = insertPessoaInDataBaseForTests();
		String partialNome = pessoa.getNome().substring(4);
		
		List<Pessoa> list = dao.findByNomeAproximado(partialNome);
		assertNotNull("Must return entity by partial 'nome'." , list);
		assertTrue("Must contain entity by partial 'nome'." ,list.contains(pessoa));				
	}
	
	@Test
	public void testFindByFields_allFieldsMatch_shouldReturnValidOutput() throws SQLException {
		Pessoa pessoa = insertPessoaInDataBaseForTests();
		
		List<Pessoa> list = dao.findByFields(pessoa.getNome(), pessoa.getTelefone(), pessoa.getPerfil());
		assertTrue(!list.isEmpty());
		assertTrue(list.size() == 1);
	}
	
	@Test
	public void testFindByFields_byNome_shouldReturnValidOutput() throws SQLException {
		Pessoa pessoa = insertPessoaInDataBaseForTests();
		
		List<Pessoa> list = dao.findByFields(pessoa.getNome(), null, null);
		assertTrue(!list.isEmpty());
		assertTrue(list.size() == 1);
	}
	
	@Test
	public void testFindByFields_byTelefone_shouldReturnValidOutput() throws SQLException {
		Pessoa pessoa = insertPessoaInDataBaseForTests();
		
		List<Pessoa> list = dao.findByFields(null, pessoa.getTelefone(), null);
		assertTrue(!list.isEmpty());
		assertTrue(list.size() == 1);
	}
	
	@Test
	public void testFindByFields_byPerfil_shouldReturnValidOutput() throws SQLException {
		Pessoa pessoa = insertPessoaInDataBaseForTests();
		
		List<Pessoa> list = dao.findByFields(null, null, pessoa.getPerfil());
		assertTrue(!list.isEmpty());
		assertTrue(list.size() == 1);
	}
		
		
	// HELPER METHODS
	private void instanciateDaoForTests(String type) {
		if (type == null) throw new IllegalArgumentException("JDBC or JPA should be informed as argument!");
		if (type.equals("jdbc")) {
			dao = new PessoaDaoJdbc();
			((PessoaDaoJdbc)dao).setURL_DB("jdbc:sqlite:" + ((PessoaDaoJdbc)dao).getPATH_DB() + "atondbtests");			
		} else if(type.equals("jpa")) {    
			dao = new PessoaDaoJpa(new JPAUtil().getMyTestsEntityManager());
		}	
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
	
	private Pessoa insertPessoaInDataBaseForTests() throws SQLException {
		Pessoa pessoa = TestHelper.getPessoaTeste();
		dao.create(pessoa);
		pessoa = dao.findByEmail(pessoa.getEmail());
		return pessoa;
	}
	
	private List<Pessoa> insertPessoaListInDataBaseForTestes() throws SQLException {
		Pessoa pessoa1 = new Pessoa("email1@email.com", "Nome 01", "111-222", Perfil.ADMINISTRADOR);
		Pessoa pessoa2 = new Pessoa("email2@email.com", "Nome 02", "111-222", Perfil.ADMINISTRADOR);
		Pessoa pessoa3 = new Pessoa("email3@email.com", "Nome 03", "111-222", Perfil.ADMINISTRADOR);
		
		dao.create(pessoa1);
		dao.create(pessoa2);
		dao.create(pessoa3);
		
		return dao.findAll();
	}
	
}
