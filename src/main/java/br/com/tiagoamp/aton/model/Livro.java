package br.com.tiagoamp.aton.model;

import java.nio.file.Path;
import java.text.SimpleDateFormat;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name="LIVROS")
public class Livro implements Comparable<Livro> {
	
	public Livro() {
		this.dataCadastro = new Date();
		this.situacao = Situacao.DISPONIVEL;
		this.tipoAquisicao = TipoAquisicao.DOACAO;
		this.qtdExemplares = 1;
		this.qtdDisponiveis = 1;
	}
		
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID")
	private Integer id;
	
	@NotEmpty(message = "{NotEmpty.livro.isbn}")
	@Column(name="ISBN")
	private String isbn;
	
	@NotEmpty(message = "{NotEmpty.livro.titulo}")
	@Column(name="TITULO")
    private String titulo;
	
	@Column(name="SUBTITULO")
    private String subtitulo;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="DATA_CADASTRO")
	private Date dataCadastro;
    
    @Column(name="EDITORA")
    private String editora;
    
    @Column(name="LOCAL_PUBLICACAO")
    private String localPublicacao;
    
    @Column(name="ANO_PUBLICACAO")
    private Integer anoPublicacao;
    
    @Column(name="NU_PAGINAS")
    private Integer nroPaginas;
    
    @Column(name="GENERO")
    private String genero;
    
    @Column(name="CLASSIFICACAO")
    private String classificacao;
    
    @Column(name="PUBLICO_ALVO")
    private String publicoAlvo;
    
    @Column(name="IMG_CAPA")
    private Path pathFotoCapa;
    
    @Enumerated(EnumType.STRING)
    @Column(name="TIPO_AQUISICAO")
    private TipoAquisicao tipoAquisicao;
    
    @Column(name="NOME_DOADOR")
    private String nomeDoador;
    
    @Column(name="CADASTRADOR")
    private Pessoa pessoaCadastradora;
    
    @Enumerated(EnumType.STRING)
    @Column(name="SITUACAO")
    private Situacao situacao;
    
    @Column(name="OBSERVACOES")
    private String observacoes;
    
    @Column(name="AUTORES")
    private List<String> autores;
    
    //private String autoresAgrupados;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="DATA_AQUISICAO")
    private Date dataAquisicao;
    
    @Column(name="QTD_EXEMPLARES")
    private Integer qtdExemplares;
    
    @Column(name="QTD_DISPONIVEIS")
    private Integer qtdDisponiveis;
    
    
    //private String dataAquisicaoFormatada;
    
    
    @Override
    public String toString() {
    	return titulo + " - " + autoresAgrupados;
    }
    
    @Override
    public int compareTo(Livro o) {
    	return this.getTitulo().compareTo(o.getTitulo());
    }
    
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getSubtitulo() {
		return subtitulo;
	}
	public void setSubtitulo(String subtitulo) {
		this.subtitulo = subtitulo;
	}
	public Date getDataCadastro() {
		return dataCadastro;
	}
	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public String getEditora() {
		return editora;
	}
	public void setEditora(String editora) {
		this.editora = editora;
	}
	public String getLocalPublicacao() {
		return localPublicacao;
	}
	public void setLocalPublicacao(String localPublicacao) {
		this.localPublicacao = localPublicacao;
	}
	public Integer getAnoPublicacao() {
		return anoPublicacao;
	}
	public void setAnoPublicacao(Integer anoPublicacao) {
		this.anoPublicacao = anoPublicacao;
	}
	public Integer getNroPaginas() {
		return nroPaginas;
	}
	public void setNroPaginas(Integer nroPaginas) {
		this.nroPaginas = nroPaginas;
	}
	public String getGenero() {
		return genero;
	}
	public void setGenero(String genero) {
		this.genero = genero;
	}
	public String getClassificacao() {
		return classificacao;
	}
	public void setClassificacao(String classificacao) {
		this.classificacao = classificacao;
	}
	public String getPublicoAlvo() {
		return publicoAlvo;
	}
	public void setPublicoAlvo(String publicoAlvo) {
		this.publicoAlvo = publicoAlvo;
	}
	public Date getDataAquisicao() {
		return dataAquisicao;
	}
	public void setDataAquisicao(Date dataAquisicao) {
		this.dataAquisicao = dataAquisicao;
	}
	public TipoAquisicao getTipoAquisicao() {
		return tipoAquisicao;
	}
	public void setTipoAquisicao(TipoAquisicao tipoAquisicao) {
		this.tipoAquisicao = tipoAquisicao;
	}
	public String getNomeDoador() {
		return nomeDoador;
	}
	public void setNomeDoador(String nomeDoador) {
		this.nomeDoador = nomeDoador;
	}
	public Pessoa getPessoaCadastradora() {
		return pessoaCadastradora;
	}
	public void setPessoaCadastradora(Pessoa pessoaCadastradora) {
		this.pessoaCadastradora = pessoaCadastradora;
	}
	public Situacao getSituacao() {
		return situacao;
	}
	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}
	public String getObservacoes() {
		return observacoes;
	}
	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}
	public List<String> getAutores() {
		if (autoresAgrupados != null && !autoresAgrupados.isEmpty()) {
			autores = new ArrayList<>();
			String[] autoresSplit = autoresAgrupados.split(";");
			for (int i = 0; i < autoresSplit.length; i++) {
				String autor = autoresSplit[i];
				autores.add(autor);
			}
		}
		return autores;
	}
	public void setAutores(List<String> autores) {
		this.autores = autores;
	}
	public String getAutoresAgrupados() {
		return autoresAgrupados;
	}
	public void setAutoresAgrupados(String autoresAgrupados) {
		this.autoresAgrupados = autoresAgrupados;
	}
	public Path getPathFotoCapa() {
		return pathFotoCapa;
	}
	public void setPathFotoCapa(Path pathFotoCapa) {
		this.pathFotoCapa = pathFotoCapa;
	}
	public String getDataAquisicaoFormatada() {
		if (dataAquisicao != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			dataAquisicaoFormatada = sdf.format(dataAquisicao);
		}
		return dataAquisicaoFormatada;
	}
	public void setDataAquisicaoFormatada(String dataAquisicaoFormatada) {
		this.dataAquisicaoFormatada = dataAquisicaoFormatada;		
	}
	public Integer getQtdExemplares() {
		return qtdExemplares;
	}
	public void setQtdExemplares(Integer qtdExemplares) {
		this.qtdExemplares = qtdExemplares;
	}
	public Integer getQtdDisponiveis() {
		return qtdDisponiveis;
	}
	public void setQtdDisponiveis(Integer qtdDisponiveis) {
		this.qtdDisponiveis = qtdDisponiveis;
		if (this.qtdDisponiveis == 0) {
			this.situacao = Situacao.EMPRESTADO; 
		} else {
			this.situacao = Situacao.DISPONIVEL;
		}
	}
		
}
