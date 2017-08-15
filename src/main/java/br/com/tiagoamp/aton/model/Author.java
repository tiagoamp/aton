package br.com.tiagoamp.aton.model;

import javax.persistence.Embeddable;
import javax.persistence.Table;

@Embeddable
@Table(name="AUTHORS")
public class Author {
	
	public Author() {
	}

	public Author(String name) {
		this.name = name.toUpperCase();
	}
	
	
	private String name;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name.toUpperCase();
	}
	
}
