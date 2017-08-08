package br.com.tiagoamp.aton.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.com.tiagoamp.aton.model.Perfil;
import br.com.tiagoamp.aton.model.Pessoa;

public class PessoaDaoJpa implements PessoaDAO {
		
	public PessoaDaoJpa(EntityManager em) {
		this.em = em;
	}
	
	
	private EntityManager em;	
	
	
	@Override
	protected void finalize() throws Throwable {
		em.close();
	}
	
	
	@Override
	public void create(Pessoa pessoa) throws SQLException {
		em.getTransaction().begin();
		em.persist(pessoa);
		em.getTransaction().commit();				
	}

	@Override
	public void update(Pessoa pessoa) throws SQLException {
		em.getTransaction().begin();
		em.merge(pessoa);
		em.getTransaction().commit();			
	}

	@Override
	public void delete(int id) throws SQLException {
		em.getTransaction().begin();
		Pessoa pessoa = em.find(Pessoa.class, id);
		em.remove(pessoa);
		em.getTransaction().commit();		
	}

	@Override
	public Pessoa findById(int id) throws SQLException {
		return em.find(Pessoa.class, id);
	}
		
	@Override
	public Pessoa findByEmail(String email) throws SQLException {
		Query query = em.createQuery("SELECT p from Pessoa p WHERE p.email = :pEmail");
		query.setParameter("pEmail", email);		
		return (Pessoa) query.getSingleResult();
	}
		
	@Override
	public List<Pessoa> findByNomeAproximado(String nome) throws SQLException {
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Pessoa> query = criteriaBuilder.createQuery(Pessoa.class);
		
		Root<Pessoa> root = query.from(Pessoa.class);
		Path<String> nomePath = root.<String>get("nome");		
		Predicate nomeLike = criteriaBuilder.like(nomePath, nome);		
		query.where(nomeLike);
		
		TypedQuery<Pessoa> typedQuery = em.createQuery(query);				
		return typedQuery.getResultList();
	}

	@Override
	public List<Pessoa> findByFields(String nome, String telefone, Perfil perfil) throws SQLException {
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Pessoa> query = criteriaBuilder.createQuery(Pessoa.class);
		
		Root<Pessoa> root = query.from(Pessoa.class);
		Path<String> nomePath = root.<String>get("nome");
		Path<String> telefonePath = root.<String>get("telefone");
		Path<Perfil> perfilPath = root.<Perfil>get("perfil");
		
		List<Predicate> predicates = new ArrayList<>();		
		if (nome != null && !nome.isEmpty()) {
			Predicate nomeLike = criteriaBuilder.like(nomePath, nome);
			predicates.add(nomeLike);
		}
		if (telefone != null && !telefone.isEmpty()) {
			Predicate telefoneEqual = criteriaBuilder.equal(telefonePath, telefone);
			predicates.add(telefoneEqual);
		}
		if (perfil != null) {
			Predicate perfilEqual = criteriaBuilder.equal(perfilPath, perfil);
			predicates.add(perfilEqual);
		}		
		query.where((Predicate[]) predicates.toArray());
		
		TypedQuery<Pessoa> typedQuery = em.createQuery(query);				
		return typedQuery.getResultList();		
	}

	@Override
	public List<Pessoa> findAll() throws SQLException {
		return em.createQuery("from Pessoa", Pessoa.class).getResultList();		
	}
		
}
