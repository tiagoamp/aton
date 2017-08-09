package br.com.tiagoamp.aton.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name="BOOKS")
public class Book implements Comparable<Book> {
	
	public Book() {
		this.dateOfRegistration = new Date();
		this.status = Status.AVAILABLE;
		this.typeOfAcquisition = TypeOfAcquisition.DONATION;
		this.numberOfCopies = 1;
		this.numberAvailable = 1;
	}
		
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID")
	private Integer id;
	
	@NotEmpty(message = "{NotEmpty.livro.isbn}")
	@Column(name="ISBN")
	private String isbn;
	
	@NotEmpty(message = "{NotEmpty.livro.titulo}")
	@Column(name="TITLE")
    private String title;
	
	@Column(name="SUBTITLE")
    private String subtitle;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="DATE_REGISTRATION")
	private Date dateOfRegistration;
    
    @Column(name="PUBLISHING_COMPANY")
    private String publishingCompany;
    
    @Column(name="PUBLISHING_CITY")
    private String publishingCity;
    
    @Column(name="PUBLISHING_YEAR")
    private Integer publishingYear;
    
    @Column(name="PAGES")
    private Integer numberOfPages;
    
    @Column(name="GENRE")
    private String genre;
    
    @Column(name="CLASSIFICATION")
    private String classification;
    
    @Column(name="TARGET_AUDIENCE")
    private String targetAudience;
    
    @Column(name="COVER_IMG")
    private String coverImagePath;
    
    @Enumerated(EnumType.STRING)
    @Column(name="TYPE_ACQUISITION")
    private TypeOfAcquisition typeOfAcquisition;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="DATE_ACQUISITION")
    private Date dateOfAcquisition;
    
    @Column(name="DONOR_NAME")
    private String donorName;
    
    @Enumerated(EnumType.STRING)
    @Column(name="STATUS")
    private Status status;
    
    @Column(name="COMMENTS")
    private String comments;
    
    @Column(name="NUMBER_COPIES")
    private Integer numberOfCopies;
    
    @Column(name="NUMBER_AVAILABLE")
    private Integer numberAvailable;
    
    @ManyToOne
    @JoinColumn(name="REGISTERER_ID")
    private Person registerer;
    
    @Transient
    private List<String> authorsName;
    
    @Column(name="AUTHORS")
    private String authorsNameInline;
    
        
    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder(title);
    	if (authorsName !=  null) {
    		sb.append(" - ");
    		sb.append(authorsNameInline);
    	}
    	return sb.toString();
    }
    
    @Override
    public int compareTo(Book o) {
    	return this.title.compareTo(o.title) ;
    }
    
    private void synchronizeAuthorsNameInFields() {
    	if (authorsNameInline == null && authorsName != null) {
    		StringBuilder sb = new StringBuilder();
			int i;
			for (i = 0; i < (authorsName.size()-1); i++) {
				sb.append(authorsName.get(i) + " ; ");
			}
			sb.append(authorsName.get(i));  // last one without ';'
			authorsNameInline = sb.toString();
    	} else if (authorsName == null && authorsNameInline != null) {
    		authorsName = new ArrayList<>();
			String[] authorSplit = authorsNameInline.split(";");
			for (int i = 0; i < authorSplit.length; i++) {
				authorsName.add(authorSplit[i].trim());
			}
    	}
    }
    
    public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSubtitle() {
		return subtitle;
	}
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}
	public Date getDateOfRegistration() {
		return dateOfRegistration;
	}
	public void setDateOfRegistration(Date registerDate) {
		this.dateOfRegistration = registerDate;
	}
	public String getPublishingCompany() {
		return publishingCompany;
	}
	public void setPublishingCompany(String publishingCompany) {
		this.publishingCompany = publishingCompany;
	}
	public String getPublishingCity() {
		return publishingCity;
	}
	public void setPublishingCity(String publishingCity) {
		this.publishingCity = publishingCity;
	}
	public Integer getPublishingYear() {
		return publishingYear;
	}
	public void setPublishingYear(Integer publishingYear) {
		this.publishingYear = publishingYear;
	}
	public Integer getNumberOfPages() {
		return numberOfPages;
	}
	public void setNumberOfPages(Integer numberOfPages) {
		this.numberOfPages = numberOfPages;
	}
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public String getClassification() {
		return classification;
	}
	public void setClassification(String classification) {
		this.classification = classification;
	}
	public String getTargetAudience() {
		return targetAudience;
	}
	public void setTargetAudience(String targetAudience) {
		this.targetAudience = targetAudience;
	}
	public String getCoverImagePath() {
		return coverImagePath;
	}
	public void setCoverImage(String coverImagePath) {
		this.coverImagePath = coverImagePath;
	}
	public TypeOfAcquisition getTypeOfAcquisition() {
		return typeOfAcquisition;
	}
	public void setTypeOfAcquisition(TypeOfAcquisition typeOfAcquisition) {
		this.typeOfAcquisition = typeOfAcquisition;
	}
	public Date getDateOfAcquisition() {
		return dateOfAcquisition;
	}
	public void setDateOfAcquisition(Date dateOfAcquisition) {
		this.dateOfAcquisition = dateOfAcquisition;
	}
	public String getDonorName() {
		return donorName;
	}
	public void setDonorName(String donorName) {
		this.donorName = donorName;
	}
	public Person getRegisterer() {
		return registerer;
	}
	public void setRegisterer(Person registerer) {
		this.registerer = registerer;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public Integer getNumberOfCopies() {
		return numberOfCopies;
	}
	public void setNumberOfCopies(Integer numberOfCopies) {
		this.numberOfCopies = numberOfCopies;
	}
	public Integer getNumberAvailable() {
		return numberAvailable;
	}
	public void setNumberAvailable(Integer numberAvailable) {
		this.numberAvailable = numberAvailable;
	}
	
	public List<String> getAuthorsName() {
		return authorsName;
	}
	public void setAuthorsName(List<String> authorsName) {
		this.authorsName = authorsName;
		synchronizeAuthorsNameInFields();
	}
	public String getAuthorsNameInline() {
		return authorsNameInline;
    }
    public void setAuthorsNameInline(String authorsNameInline) {
		this.authorsNameInline = authorsNameInline;
		synchronizeAuthorsNameInFields();
	}
    
}
