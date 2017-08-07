package br.com.tiagoamp.aton.dao;

import java.sql.SQLException;
import java.util.List;

import br.com.tiagoamp.aton.model.Perfil;
import br.com.tiagoamp.aton.model.Pessoa;

public interface PessoaDAO {
	
	/**
	 * Inserts the entity in the database.
	 * 
	 * @param pessoa
	 * @throws SQLException 
	 */
	void create(Pessoa pessoa) throws SQLException;
	
	/**
	 * Updates the entity in the database.
	 * 
	 * @param pessoa
	 * @throws SQLException 
	 */
	void update(Pessoa pessoa) throws SQLException;
	
	/**
	 * Deletes the entity in the database.
	 * 
	 * @param id 
	 * @throws SQLException
	 */
	void delete(int id) throws SQLException;
	
	/**
	 * Search the entity by 'id' in the database.
	 * 
	 * @param id Identificador da pessoa.
	 * @return Pessoa Instância da pessoa ou null se não existir.
	 * @throws SQLException 
	 */
	Pessoa findById(int id) throws SQLException;
	
	/**
	 * Search the entity by 'e-mail' in the database.
	 * 
	 * @param email
	 * @return Pessoa Instância da pessoa ou null se não existir.
	 * @throws SQLException
	 */
	Pessoa findByEmail(String email) throws SQLException;
	
	/**
	 * Search the entity by 'name-like' in the database.
	 * 
	 * @param nome
	 * @return List<Pessoa> List of entity
	 * @throws SQLException
	 */
	List<Pessoa> findByNomeAproximado(String nome) throws SQLException;
	
	/**
	 * Search the entity by given parameters in the database.
	 * 
	 * @param nome
	 * @param telefone
	 * @param perfil
	 * @return List<Pessoa> List of entity
	 * @throws SQLException
	 */
	List<Pessoa> find(String nome, String telefone, Perfil perfil) throws SQLException;
	
	/**
	 * Consulta e retorna todas as 'pessoas' na base de dados
	 * @return List<Pessoa> List of entity
	 * @throws SQLException
	 */
	List<Pessoa> findAll() throws SQLException;

}
