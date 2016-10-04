package br.com.tiagoamp.aton.model;

import java.util.Date;

public class Emprestimo {
	
	public Emprestimo() {
	}
	
	public Emprestimo(Livro livro, Pessoa pessoa, Date dataEmprestimo, Date dataDevolucao) {
		this.livro = livro;
		this.pessoa = pessoa;
		this.dataEmprestimo = dataEmprestimo;
		this.dataDevolucao = dataDevolucao;
	}
	
	private Integer id;
    private Livro livro;
    private Pessoa pessoa;
    private Date dataEmprestimo;
    private Date dataDevolucao;

    
    @Override
    public String toString() {
    	if (pessoa != null) {
    		return "Emprestimo > Livro: " + livro.getTitulo() + " para: " + pessoa.getNome();
    	}
    	return "Emprestimo > Livro: " + livro.getTitulo();
    }
    
    
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Livro getLivro() {
		return livro;
	}
	public void setLivro(Livro livro) {
		this.livro = livro;
	}
	public Pessoa getPessoa() {
		return pessoa;
	}
	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
	public Date getDataEmprestimo() {
		return dataEmprestimo;
	}
	public void setDataEmprestimo(Date dataEmprestimo) {
		this.dataEmprestimo = dataEmprestimo;
	}
	public Date getDataDevolucao() {
		return dataDevolucao;
	}
	public void setDataDevolucao(Date dataDevolucao) {
		this.dataDevolucao = dataDevolucao;
	}

}
