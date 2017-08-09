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
import br.com.tiagoamp.aton.model.Person;

public class PersonDaoJpa implements PersonDAO {
		
	public PersonDaoJpa(EntityManager em) {
		this.em = em;
	}
	
	
	private EntityManager em;	
	
	
	@Override
	protected void finalize() throws Throwable {
		em.close();
	}
	
	
	@Override
	public void create(Person person) throws SQLException {
		em.getTransaction().begin();
		em.persist(person);
		em.getTransaction().commit();				
	}

	@Override
	public void update(Person person) throws SQLException {
		em.getTransaction().begin();
		em.merge(person);
		em.getTransaction().commit();			
	}

	@Override
	public void delete(int id) throws SQLException {
		em.getTransaction().begin();
		Person person = em.find(Person.class, id);
		em.remove(person);
		em.getTransaction().commit();		
	}

	@Override
	public Person findById(int id) throws SQLException {
		return em.find(Person.class, id);
	}
		
	@Override
	public Person findByEmail(String email) throws SQLException {
		Query query = em.createQuery("SELECT p from Person p WHERE p.email = :pEmail");
		query.setParameter("pEmail", email);		
		return (Person) query.getSingleResult();
	}
		
	@Override
	public List<Person> findByNameLike(String name) throws SQLException {
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Person> query = criteriaBuilder.createQuery(Person.class);
		
		Root<Person> root = query.from(Person.class);
		Path<String> namePath = root.<String>get("name");		
		Predicate nameLike = criteriaBuilder.like(namePath, "%" + name + "%");		
		query.where(nameLike);
		
		TypedQuery<Person> typedQuery = em.createQuery(query);				
		return typedQuery.getResultList();
	}

	@Override
	public List<Person> findByFields(String name, String phone, Perfil role) throws SQLException {
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Person> query = criteriaBuilder.createQuery(Person.class);
		
		Root<Person> root = query.from(Person.class);
		Path<String> namePath = root.<String>get("name");
		Path<String> phonePath = root.<String>get("phone");
		Path<Perfil> rolePath = root.<Perfil>get("role");
		
		List<Predicate> predicates = new ArrayList<>();		
		if (name != null && !name.isEmpty()) {
			Predicate nameLike = criteriaBuilder.like(namePath, "%" + name + "%");
			predicates.add(nameLike);
		}
		if (phone != null && !phone.isEmpty()) {
			Predicate phoneEqual = criteriaBuilder.equal(phonePath, phone);
			predicates.add(phoneEqual);
		}
		if (role != null) {
			Predicate roleEqual = criteriaBuilder.equal(rolePath, role);
			predicates.add(roleEqual);
		}		
		query.where((Predicate[]) predicates.toArray(new Predicate[0]));
		
		TypedQuery<Person> typedQuery = em.createQuery(query);				
		return typedQuery.getResultList();		
	}

	@Override
	public List<Person> findAll() throws SQLException {
		return em.createQuery("from Person", Person.class).getResultList();		
	}
		
}
