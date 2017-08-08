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
@Table(name="PESSOAS")
public class Pessoa implements Comparable<Pessoa> {
	
	public Pessoa() {
	}
	
	public Pessoa(String email, String nome, String telefone, Perfil perfil) {
		this.email = email;
		this.nome = nome;
		this.telefone = telefone;
		this.perfil = perfil;
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
	@Column(name="NOME")
	private String nome;
	
	@Column(name="TELEFONE")
	private String telefone;
	
	@NotNull(message = "{NotNull.pessoa.perfil}")
	@Enumerated(EnumType.STRING)
	@Column(name="PERFIL")
	private Perfil perfil;
	
	@Column(name="SENHA")
	private String senha;
    
	
    @Override
    public String toString() {
    	StringBuilder r = new StringBuilder(nome + " - " + email + " - " + perfil); 
    	return r.toString();
    }
    
    @Override
    public boolean equals(Object obj) {
    	return obj instanceof Pessoa && ((Pessoa)obj).email.equals(email);
    }
    
    @Override
	public int compareTo(Pessoa p) {
		return this.nome.compareTo(p.nome); // ordem alfabetica
	}
    
    
    public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Perfil getPerfil() {
		return perfil;
	}
	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	
}
