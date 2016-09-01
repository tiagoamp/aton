package br.com.tiagoamp.aton.dao;

import java.sql.SQLException;
import java.util.List;

import br.com.tiagoamp.aton.model.Livro;

public interface LivroDAO {
	
	/**
	 * Insere um livro no base de dados.
	 * 
	 * @param livro
	 */
	void inserir(Livro livro) throws SQLException;
	
	/**
	 * Atualiza um livro na base de dados.
	 * 
	 * @param livro
	 */
	void atualizar(Livro livro) throws SQLException;
	
	/**
	 * Apaga um livro na base de dados.
	 * 
	 * @param id
	 */
	void apagar(int id) throws SQLException;
	
	/**
	 * Consulta um livro pelo id.
	 * 
	 * @param id Identificador do livro.
	 * @return Livro Instância do livro ou null se não existir.
	 */
	Livro consultar(int id) throws SQLException;
	
	/**
	 * Consulta um livro pelo isbn.
	 * 
	 * @param isbn ISBN do livro.
	 * @return Livro Instância do livro ou null se não existir.
	 */
	Livro consultar(String isbn) throws SQLException;
	
	/**
	 * Consulta livros na base de dados baseado nos parâmetros informados.
	 * 
	 * @param titulo Título do livro.
	 * @param autor Autor do livro.
	 * @param isbn ISBN do livro.
	 * @param classificacao Classificação do livro.
	 * @param publico Público alvo do livro.
	 * 
	 * @return List<Livro> Lista de livros ou null se não existir.
	 */
	List<Livro> consultar(String titulo, String autor, String isbn, String classificacao, String publico) throws SQLException;
	
	/**
	 * Consulta todos os livros da base de dados.
	 * @return List<Livro>
	 * @throws SQLException
	 */
	List<Livro> consultar() throws SQLException;

}
