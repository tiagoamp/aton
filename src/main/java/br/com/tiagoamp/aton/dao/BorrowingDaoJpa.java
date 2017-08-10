package br.com.tiagoamp.aton.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import br.com.tiagoamp.aton.model.Borrowing;

public class BorrowingDaoJpa implements BorrowingDAO {
	
	public BorrowingDaoJpa(EntityManager em) {
		this.em = em;
	}
	
	
	private EntityManager em;	
	
	
	@Override
	protected void finalize() throws Throwable {
		em.close();
	}
	

	@Override
	public void create(Borrowing borrowing) throws SQLException {
		em.getTransaction().begin();
		em.persist(borrowing);
		em.getTransaction().commit();

	}

	@Override
	public void update(Borrowing borrowing) throws SQLException {
		em.getTransaction().begin();
		em.merge(borrowing);
		em.getTransaction().commit();
	}

	@Override
	public void delete(int id) throws SQLException {
		em.getTransaction().begin();
		Borrowing borrow = em.find(Borrowing.class, id);
		em.remove(borrow);
		em.getTransaction().commit();
	}

	@Override
	public Borrowing findById(int id) throws SQLException {
		return em.find(Borrowing.class, id);
	}

	@Override
	public List<Borrowing> findByFields(Integer bookId, Integer personId, Date dateOfBorrowing, Date dateOfReturn) throws SQLException {
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Borrowing> query = criteriaBuilder.createQuery(Borrowing.class);
		
		ParameterExpression<java.util.Date> parameter = criteriaBuilder.parameter(java.util.Date.class);
		
		Root<Borrowing> root = query.from(Borrowing.class);
		Path<Integer> bookIdPath = root.<String>get("book").<Integer>get("id");
		Path<Integer> personIdPath = root.<String>get("person").<Integer>get("id");
		Path<Date> dateOfBorrowingPath = root.<Date>get("dateOfBorrowing");
		Path<Date> dateOfReturnPath = root.<Date>get("dateOfReturn");
		
		List<Predicate> predicates = new ArrayList<>();		
		if (bookId != null) {
			Predicate bookEqual = criteriaBuilder.equal(bookIdPath, bookId);
			predicates.add(bookEqual);
		}
		if (personId != null) {
			Predicate personEqual = criteriaBuilder.equal(personIdPath, personId);
			predicates.add(personEqual);
		}
		if (dateOfBorrowing != null) {
			Predicate dateOfBorrowingPredicate = criteriaBuilder.equal(dateOfBorrowingPath.as(java.sql.Date.class), parameter);
			predicates.add(dateOfBorrowingPredicate);			
		}
		if (dateOfReturn != null) {
			Predicate dateOfReturnPredicate = criteriaBuilder.equal(dateOfReturnPath.as(java.sql.Date.class), parameter);
			predicates.add(dateOfReturnPredicate);			
		}
		query.where((Predicate[]) predicates.toArray(new Predicate[0]));
		
		TypedQuery<Borrowing> typedQuery = em.createQuery(query);
		if (dateOfBorrowing != null) {
			typedQuery.setParameter(parameter, dateOfBorrowing, TemporalType.DATE);
		}
		if (dateOfReturn != null) {
			typedQuery.setParameter(parameter, dateOfReturn, TemporalType.DATE);
		}
		
		return typedQuery.getResultList();
	}

	@Override
	public List<Borrowing> findAll() throws SQLException {
		return em.createQuery("from Borrowing", Borrowing.class).getResultList();
	}

	@Override
	public List<Borrowing> findOpenBorrowings() throws SQLException {
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Borrowing> query = criteriaBuilder.createQuery(Borrowing.class);
		
		Root<Borrowing> root = query.from(Borrowing.class);
		Path<Date> dateOfReturnPath = root.<Date>get("dateOfReturn");
		
		Predicate dateOfReturnNull = criteriaBuilder.isNull(dateOfReturnPath);
		query.where(dateOfReturnNull);
		
		TypedQuery<Borrowing> typedQuery = em.createQuery(query);				
		return typedQuery.getResultList();
	}

}
