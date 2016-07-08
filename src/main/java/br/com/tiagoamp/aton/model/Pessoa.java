package br.com.tiagoamp.aton.model;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

public class Pessoa implements Comparable<Pessoa> {
	
	public Pessoa() {
	}
	
	public Pessoa(String email, String nome, String telefone, Perfil perfil) {
		this.email = email;
		this.nome = nome;
		this.telefone = telefone;
		this.perfil = perfil;
	}
	
	
	private Integer id;
	
	@NotEmpty(message = "{NotEmpty.pessoa.email}")
	@Email
	private String email;
	
	@NotEmpty(message = "{NotEmpty.pessoa.nome}")
	private String nome;
	
	private String telefone;
	
	@NotNull(message = "{NotNull.pessoa.perfil}")
	private Perfil perfil;
	
	private String senha;
    
	
    @Override
    public String toString() {
    	StringBuilder r = new StringBuilder(nome + " - " + email + " - " + perfil); 
    	return r.toString();
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
