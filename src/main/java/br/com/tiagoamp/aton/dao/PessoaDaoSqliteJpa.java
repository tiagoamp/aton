package br.com.tiagoamp.aton.dao;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.tiagoamp.aton.model.Perfil;
import br.com.tiagoamp.aton.model.Pessoa;

public class PessoaDaoSqliteJpa implements PessoaDAO {
		
	private EntityManager em;	
	

	private void instanciateEntityManager() {
		em = new JPAUtil().getMyEntityManager();		
	}
	
	private void closeEntityManager() {
		em.close();		
	}
	
	
	@Override
	public void create(Pessoa pessoa) throws SQLException {
		instanciateEntityManager();
		
		em.getTransaction().begin();
		em.persist(pessoa);
		em.getTransaction().commit();
		
		closeEntityManager();		
	}

	@Override
	public void update(Pessoa pessoa) throws SQLException {
		instanciateEntityManager();
		
		em.getTransaction().begin();
		em.merge(pessoa);
		em.getTransaction().commit();
		
		closeEntityManager();	
	}

	@Override
	public void delete(int id) throws SQLException {
		instanciateEntityManager();
		
		em.getTransaction().begin();
		Pessoa pessoa = em.find(Pessoa.class, id);
		em.remove(pessoa);
		em.getTransaction().commit();
		
		closeEntityManager();
	}

	@Override
	public Pessoa findById(int id) throws SQLException {
		instanciateEntityManager();
		Pessoa pessoa = em.find(Pessoa.class, id);
		closeEntityManager();
		return pessoa;
	}
		
	@Override
	public Pessoa findByEmail(String email) throws SQLException {
		instanciateEntityManager();
		
		Query query = em.createQuery("SELECT p from Pessoa p WHERE p.email = :pEmail");
		query.setParameter("pEmail", email);		
		Pessoa pessoa = (Pessoa) query.getSingleResult();
		
		closeEntityManager();
		return pessoa;
	}
		
	@SuppressWarnings("unchecked")
	@Override
	public List<Pessoa> findByNomeAproximado(String nome) throws SQLException {
		instanciateEntityManager();
		
		Query query = em.createQuery("SELECT p from Pessoa p WHERE p.nome like :pNome");
		query.setParameter("pNome", nome);		
		List<Pessoa> list = query.getResultList();
		
		closeEntityManager();
		return list;
	}

	@Override
	public List<Pessoa> find(String nome, String telefone, Perfil perfil) throws SQLException {
		instanciateEntityManager();
		
		 // TO DO: IMPLEMENTAR VERIFICANDO SE PARAMETROS SÃO IGUAIS A NULL
		
		return null;
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Pessoa> findAll() throws SQLException {
		instanciateEntityManager();
		
		List<Pessoa> list = (List<Pessoa>) em.createQuery("from Pessoa").getResultList();
		
		closeEntityManager();
		return list;
	}
	
	
	public EntityManager getEntityManager() {
		return em;
	}
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

}
