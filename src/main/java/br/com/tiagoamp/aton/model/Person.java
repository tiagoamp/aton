package br.com.tiagoamp.aton.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name="PEOPLE")
public class Person implements Comparable<Person> {
	
	public Person() {
	}
	
	public Person(String email, String name, String phone, Perfil role) {
		this.email = email;
		this.name = name;
		this.phone = phone;
		this.role = role;
	}
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID")
	private Integer id;
	
	@NotEmpty(message = "{NotEmpty.pessoa.email}")
	@Email
	@Column(name="EMAIL")
	private String email;
	
	@NotEmpty(message = "{NotEmpty.pessoa.nome}")
	@Column(name="NAME")
	private String name;
	
	@Column(name="PHONE")
	private String phone;
	
	@NotNull(message = "{NotNull.pessoa.perfil}")
	@Enumerated(EnumType.STRING)
	@Column(name="ROLE")
	private Perfil role;
	
	@Column(name="PASSWORD")
	private String password;
    
	
    @Override
    public String toString() {
    	StringBuilder r = new StringBuilder(name + " - " + email + " - " + role); 
    	return r.toString();
    }    
    @Override
    public boolean equals(Object obj) {
    	return obj instanceof Person && ((Person)obj).email.equals(email);
    }    
    @Override
	public int compareTo(Person p) {
		return this.name.compareTo(p.name); // ordem alfabetica
	}
    
    
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Perfil getRole() {
		return role;
	}
	public void setRole(Perfil role) {
		this.role = role;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}   
	
}
