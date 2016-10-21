package br.com.tiagoamp.aton.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Emprestimo implements Comparable<Emprestimo> {
	
	public Emprestimo() {
	}
	
	public Emprestimo(Livro livro, Pessoa pessoa, Date dataEmprestimo, Date dataDevolucaoProgramada, Date dataDevolucao) {
		this.livro = livro;
		this.pessoa = pessoa;
		this.dataEmprestimo = dataEmprestimo;
		this.dataDevolucaoProgramada = dataDevolucaoProgramada;
		this.dataDevolucao = dataDevolucao;
	}
	
	private Integer id;
    private Livro livro;
    private Pessoa pessoa;
    
    private Date dataEmprestimo;
    private String dataEmprestimoFormatada;
    
    private Date dataDevolucaoProgramada;
    private String dataDevolucaoProgramadaFormatada;

    private Date dataDevolucao;
    private String dataDevolucaoFormatada;
   
    private boolean atrasado;
    
    @Override
    public String toString() {
    	String str = "Emprestimo > Livro: ";
    	
    	if (livro != null) {
    		str += "livro.getTitulo() ";
    	} else {
    		str += "[livro apagado] ";
    	}
    	    	
    	str += "> Leitor: ";
    	if (pessoa != null) {
    		str += pessoa.getNome();
    	} else {
    		str += "[pessoa apagada]";
    	}
    	return str;
    }
    
    @Override
    public int compareTo(Emprestimo o) {
    	// ordem decrescente de data de emprestimo
    	return o.getDataEmprestimo().compareTo(this.getDataEmprestimo());
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
	public Date getDataDevolucaoProgramada() {
		return dataDevolucaoProgramada;
	}
	public void setDataDevolucaoProgramada(Date dataDevolucao) {
		this.dataDevolucaoProgramada = dataDevolucao;
	}
	public String getDataEmprestimoFormatada() {
		if (dataEmprestimo != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			dataEmprestimoFormatada = sdf.format(dataEmprestimo);
		}
		return dataEmprestimoFormatada;
	}
	public void setDataEmprestimoFormatada(String dataEmprestimoFormatada) {
		this.dataEmprestimoFormatada = dataEmprestimoFormatada;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			this.dataEmprestimo = sdf.parse(dataEmprestimoFormatada);
		} catch (ParseException e) {
			e.printStackTrace(); // validado no controller
		}
	}
	public String getDataDevolucaoProgramadaFormatada() {
		if (dataDevolucaoProgramada != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			dataDevolucaoProgramadaFormatada = sdf.format(dataDevolucaoProgramada);
		}
		return dataDevolucaoProgramadaFormatada;
	}
	public void setDataDevolucaoProgramadaFormatada(String dataDevolucaoProgramadaFormatada) {
		this.dataDevolucaoProgramadaFormatada = dataDevolucaoProgramadaFormatada;		
	}
	public Date getDataDevolucao() {
		return dataDevolucao;
	}
	public void setDataDevolucao(Date dataDevolucao) {
		this.dataDevolucao = dataDevolucao;
	}
	public String getDataDevolucaoFormatada() {
		if (dataDevolucao != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			dataDevolucaoFormatada = sdf.format(dataDevolucao);
		}
		return dataDevolucaoFormatada;
	}
	public void setDataDevolucaoFormatada(String dataDevolucaoFormatada) {
		this.dataDevolucaoFormatada = dataDevolucaoFormatada;
	}
	public void setAtrasado(boolean atrasado) {
		this.atrasado = atrasado;
	}
	public boolean getAtrasado() {
		this.atrasado = false;
    	if (dataDevolucao != null) { // jah devolveu
    		int i = dataDevolucao.compareTo(dataDevolucaoProgramada);
    		if (i > 0) atrasado = true;
    	} else { // ainda nao devolveu
    		Date hoje = new Date();
    		int i = hoje.compareTo(dataDevolucaoProgramada);
    		if (i > 0) atrasado = true;
    	}    	
    	return atrasado;
	}

}
