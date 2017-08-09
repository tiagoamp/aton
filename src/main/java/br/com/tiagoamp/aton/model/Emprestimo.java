package br.com.tiagoamp.aton.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

//@Entity
@Table(name="EMPRESTIMOS")
public class Emprestimo implements Comparable<Emprestimo> {
	
	public Emprestimo() {
	}
	
	public Emprestimo(Livro livro, Person pessoa, Date dataEmprestimo, Date dataDevolucaoProgramada, Date dataDevolucao) {
		this.livro = livro;
		this.pessoa = pessoa;
		this.dataEmprestimo = dataEmprestimo;
		this.dataDevolucaoProgramada = dataDevolucaoProgramada;
		this.dataDevolucao = dataDevolucao;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID")
	private Integer id;
	
	@ManyToOne
	@Column(name="ID_LIVRO")
    private Livro livro;
    
	@ManyToOne
	@Column(name="ID_PESSOA")
    private Person pessoa;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="DATA_EMPRESTIMO")
    private Date dataEmprestimo;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="DATA_DEVOLUCAO_PROGRAMADA")
    private Date dataDevolucaoProgramada;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="DATA_DEVOLUCAO")
    private Date dataDevolucao;
    
     @Transient
    private boolean atrasado;
     
     
     //private String dataDevolucaoProgramadaFormatada;
     //private String dataEmprestimoFormatada;
     //private String dataDevolucaoFormatada;
    
    
    @Override
    public String toString() {
    	String str = "Emprestimo > Livro: ";
    	
    	if (livro != null) {
    		str += livro.getTitulo();
    	} else {
    		str += " [livro apagado] ";
    	}
    	    	
    	str += " > Leitor: ";
    	if (pessoa != null) {
    		str += pessoa.getNome();
    	} else {
    		str += " [pessoa apagada] ";
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
	public Person getPessoa() {
		return pessoa;
	}
	public void setPessoa(Person pessoa) {
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
	/*public String getDataEmprestimoFormatada() {
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
	}*/
	public Date getDataDevolucao() {
		return dataDevolucao;
	}
	public void setDataDevolucao(Date dataDevolucao) {
		this.dataDevolucao = dataDevolucao;
	}
	/*public String getDataDevolucaoFormatada() {
		if (dataDevolucao != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			dataDevolucaoFormatada = sdf.format(dataDevolucao);
		}
		return dataDevolucaoFormatada;
	}
	public void setDataDevolucaoFormatada(String dataDevolucaoFormatada) {
		this.dataDevolucaoFormatada = dataDevolucaoFormatada;
	}*/
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
