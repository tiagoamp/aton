package br.com.tiagoamp.aton.dao;

import java.io.IOException;
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

import org.springframework.web.multipart.MultipartFile;

import br.com.tiagoamp.aton.model.Book;

public class BookDaoJpa implements BookDAO {
	
	public BookDaoJpa(EntityManager em) {
		this.em = em;
	}
	
	
	private EntityManager em;	
	
	
	@Override
	protected void finalize() throws Throwable {
		em.close();
	}
	

	@Override
	public void create(Book book) throws SQLException {
		em.getTransaction().begin();
		em.persist(book);
		em.getTransaction().commit();
	}

	@Override
	public void update(Book book) throws SQLException {
		em.getTransaction().begin();
		em.merge(book);
		em.getTransaction().commit();
	}

	@Override
	public void delete(int id) throws SQLException {
		em.getTransaction().begin();
		Book book = em.find(Book.class, id);
		em.remove(book);
		em.getTransaction().commit();
	}

	@Override
	public Book findById(int id) throws SQLException {
		return em.find(Book.class, id);
	}

	@Override
	public Book findByIsbn(String isbn) throws SQLException {
		Query query = em.createQuery("SELECT b from Book b WHERE b.isbn = :pIsbn");
		query.setParameter("pIsbn", isbn);		
		return (Book) query.getSingleResult();
	}

	@Override
	public List<Book> findByFields(String title, String authorsNameInline, String isbn, String classification,	String targetAudience) throws SQLException {
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Book> query = criteriaBuilder.createQuery(Book.class);
		
		Root<Book> root = query.from(Book.class);
		Path<String> titlePath = root.<String>get("title");
		
		Path<String> authorPath = root.<String>get("authorsNameInline");		
		Path<String> isbnPath = root.<String>get("isbn");
		Path<String> classificationPath = root.<String>get("classification");
		Path<String> targetPath = root.<String>get("targetAudience");
		
		List<Predicate> predicates = new ArrayList<>();		
		if (title != null && !title.isEmpty()) {
			Predicate titleLike = criteriaBuilder.like(titlePath, "%" + title + "%");
			predicates.add(titleLike);
		}
		if (authorsNameInline != null && !authorsNameInline.isEmpty()) {
			Predicate authorLike = criteriaBuilder.like(authorPath, "%" + authorsNameInline + "%");
			predicates.add(authorLike);
		}
		if (isbn != null && !isbn.isEmpty()) {
			Predicate isbnEqual = criteriaBuilder.equal(isbnPath, isbn);
			predicates.add(isbnEqual);
		}		
		if (classification != null && !classification.isEmpty()) {
			Predicate classificationEqual = criteriaBuilder.equal(classificationPath, classification);
			predicates.add(classificationEqual);
		}
		if (targetAudience != null && !targetAudience.isEmpty()) {
			Predicate targetEqual = criteriaBuilder.equal(targetPath, targetAudience);
			predicates.add(targetEqual);
		}
		query.where((Predicate[]) predicates.toArray(new Predicate[0]));
		
		TypedQuery<Book> typedQuery = em.createQuery(query);				
		return typedQuery.getResultList();
	}

	@Override
	public List<Book> findAll() throws SQLException {
		return em.createQuery("from Book", Book.class).getResultList();
	}

	@Override
	public List<Book> findByAuthorNameLike(String authorName) throws SQLException {
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Book> query = criteriaBuilder.createQuery(Book.class);
		
		Root<Book> root = query.from(Book.class);
		Path<String> authorPath = root.<String>get("authorsNameInline");	
		Predicate authorLike = criteriaBuilder.like(authorPath, "%" + authorName + "%");		
		query.where(authorLike);
		
		TypedQuery<Book> typedQuery = em.createQuery(query);				
		return typedQuery.getResultList();
	}

	@Override
	public List<Book> findByTitleLike(String title) throws SQLException {
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Book> query = criteriaBuilder.createQuery(Book.class);
		
		Root<Book> root = query.from(Book.class);
		Path<String> titlePath = root.<String>get("title");	
		Predicate titleLike = criteriaBuilder.like(titlePath, "%" + title + "%");		
		query.where(titleLike);
		
		TypedQuery<Book> typedQuery = em.createQuery(query);				
		return typedQuery.getResultList();
	}

	@Override
	public java.nio.file.Path createCapaLivro(MultipartFile mFile, String fileName) throws IOException {
		throw new RuntimeException("Not implemented yet!!!");
	}

}
