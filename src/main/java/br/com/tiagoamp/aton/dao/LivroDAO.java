package br.com.tiagoamp.aton.dao;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import br.com.tiagoamp.aton.model.Livro;

public interface LivroDAO {
	
	/**
	 * Insere um livro no base de dados.
	 * 
	 * @param livro
	 * @throws SQLException
	 * @return int number of rows inserted
	 */
	int create(Livro livro) throws SQLException;
	
	/**
	 * Atualiza um livro na base de dados.
	 * 
	 * @param livro
	 * @throws SQLException
	 * @return int number of rows updated
	 */
	int update(Livro livro) throws SQLException;
	
	/**
	 * Apaga um livro na base de dados.
	 * 
	 * @param id
	 * @throws SQLException
	 * @return int number of rows deleted
	 */
	int delete(int id) throws SQLException;
	
	/**
	 * Consulta um livro pelo id.
	 * 
	 * @param id Identificador do livro.
	 * @return Livro Instância do livro ou null se não existir.
	 * @throws SQLException
	 */
	Livro findById(int id) throws SQLException;
	
	/**
	 * Consulta um livro pelo isbn.
	 * 
	 * @param isbn ISBN do livro.
	 * @return Livro Instância do livro ou null se não existir.
	 * @throws SQLException
	 */
	Livro findByIsbn(String isbn) throws SQLException;
	
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
	List<Livro> find(String titulo, String autor, String isbn, String classificacao, String publico) throws SQLException;
	
	/**
	 * Consulta todos os livros da base de dados.
	 * 
	 * @return List<Livro>
	 * @throws SQLException
	 */
	List<Livro> findAll() throws SQLException;
	
	/**
	 * Consulta livros na base de dados por nome de autor aproximado.
	 * 
	 * @param nome do autor
	 * @return List<Livro> lista de livros
	 * @throws SQLException
	 */
	List<Livro> findByAutorAproximado(String autor) throws SQLException;
	
	/**
	 * Consulta livros na base de dados por título aproximado.
	 * 
	 * @param titulo
	 * @return List<Livro> lista de livros
	 * @throws SQLException
	 */
	List<Livro> findByTituloAproximado(String titulo) throws SQLException;
	
	/**
	 * Insere figura da capa do livro no sistema de arquivos.
	 * 
	 * @param mFile Arquivo feito upload
	 * @param nomeArquivo Nome do arquivo
	 * @return Path Caminho do arquivo gravado
	 * @throws IOException
	 */
	Path createCapaLivro(MultipartFile mFile, String nomeArquivo) throws IOException; 

}
