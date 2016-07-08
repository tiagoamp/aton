package br.com.tiagoamp.aton.model;

import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

public class Livro {
	
	public Livro() {
		this.dataCadastro = new Date();
		this.situacao = Situacao.DISPONIVEL;
	}
		
	private Integer id;
	
	@NotEmpty(message = "{NotEmpty.livro.isbn}")
	private String isbn;
	
	@NotEmpty(message = "{NotEmpty.livro.titulo}")
    private String titulo;
    private String subtitulo;
	private Date dataCadastro;
    private String editora;
    private String localPublicacao;
    private Integer anoPublicacao;
    private Integer nroPaginas;
    private String genero;
    private String classificacao;
    private String publicoAlvo;
    private Path pathFotoCapa;
    private TipoAquisicao tipoAquisicao;
    private String nomeDoador;
    private Pessoa pessoaCadastradora;
    private Situacao situacao;
    private String observacoes;
    private List<String> autores;
    private String autoresAgrupados;
    private Date dataAquisicao;
    private String dataAquisicaoFormatada;
    
    @Override
    public String toString() {
    	return titulo + " - " + autoresAgrupados;
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
		
}
