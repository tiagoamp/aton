package br.com.tiagoamp.aton.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="BORROWING")
public class Borrowing implements Comparable<Borrowing> {
	
	public Borrowing() {
		this.dateOfBorrowing = new Date();
	}
	
	public Borrowing(Book book, Person person, Date dateOfBorrowing, Date dateOfScheduledReturn, Date dateOfReturn) {
		this.book = book;
		this.person = person;
		this.dateOfBorrowing = dateOfBorrowing;
		this.dateOfScheduledReturn = dateOfScheduledReturn;
		this.dateOfReturn = dateOfReturn;
	}
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="ID_BOOK")
    private Book book;
    
	@ManyToOne
	@JoinColumn(name="ID_PERSON")
    private Person person;
    
	@DateTimeFormat(pattern="dd/MM/yyyy")
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name="DATE_BORROWING")
    private Date dateOfBorrowing;
    
	@DateTimeFormat(pattern="dd/MM/yyyy")
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name="DATE_SCHEDULED_RETURN")
    private Date dateOfScheduledReturn;
    
	@DateTimeFormat(pattern="dd/MM/yyyy")
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name="DATE_RETURN")
    private Date dateOfReturn;
    
     @Transient
    private boolean overdue;
     
     
    @Override
    public String toString() {
    	StringBuffer sb = new StringBuffer("Emprestimo ");
    	
    	sb.append(" > Livro: ");    	
    	if (book != null) sb.append(book.getTitle()); 
    	else sb.append(" [livro apagado] ");
    	    	    	
    	sb.append(" > Leitor: ");
    	if (person != null) sb.append(person.getName());
    	else sb.append(" [pessoa apagada] ");
    	return sb.toString();
    }
    
    @Override
    public int compareTo(Borrowing o) {
    	return o.getDateOfBorrowing().compareTo(this.getDateOfBorrowing()); // decrescent order of borrowing date
    }
    
    @Override
    public boolean equals(Object obj) {
    	return obj instanceof Borrowing && 
    			dateOfBorrowing.equals( ((Borrowing)obj).getDateOfBorrowing() ) &&
    			book.equals( ((Borrowing)obj).getBook() ) &&
    			person.equals( ((Borrowing)obj).getPerson() );
    }
        
    public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	public Date getDateOfBorrowing() {
		return dateOfBorrowing;
	}
	public void setDateOfBorrowing(Date dateOfBorrowing) {
		this.dateOfBorrowing = dateOfBorrowing;
	}
	public Date getDateOfScheduledReturn() {
		return dateOfScheduledReturn;
	}
	public void setDateOfScheduledReturn(Date dateOfScheduledReturn) {
		this.dateOfScheduledReturn = dateOfScheduledReturn;
	}
	public Date getDateOfReturn() {
		return dateOfReturn;
	}
	public void setDateOfReturn(Date dateOfReturn) {
		this.dateOfReturn = dateOfReturn;
	}
	public void setOverdue(boolean overdue) {
		this.overdue = overdue;
	}
	public boolean isOverdue() {
		this.overdue = false;
    	if (dateOfReturn != null) { // returned
    		int i = dateOfReturn.compareTo(dateOfScheduledReturn);
    		if (i > 0) overdue = true;
    	} else { // not yet returned
    		Date hoje = new Date();
    		int i = hoje.compareTo(dateOfScheduledReturn);
    		if (i > 0) overdue = true;
    	}    	
    	return overdue;
	}

}
