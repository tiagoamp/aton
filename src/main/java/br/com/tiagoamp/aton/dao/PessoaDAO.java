package br.com.tiagoamp.aton.dao;

import java.sql.SQLException;
import java.util.List;

import br.com.tiagoamp.aton.model.Perfil;
import br.com.tiagoamp.aton.model.Pessoa;

public interface PessoaDAO {
	
	/**
	 * Insere uma pessoa no base de dados.
	 * 
	 * @param pessoa
	 * @throws SQLException 
	 * @return int number of rows inserted
	 */
	int create(Pessoa pessoa) throws SQLException;
	
	/**
	 * Atualiza uma pessoa na base de dados.
	 * 
	 * @param pessoa
	 * @throws SQLException
	 * @return int number of rows updated 
	 */
	int update(Pessoa pessoa) throws SQLException;
	
	/**
	 * Apaga uma pessoa na base de dados.
	 * 
	 * @param id 
	 * @throws SQLException 
	 * @return int number of rows deleted
	 */
	int delete(int id) throws SQLException;
	
	/**
	 * Consulta uma pessoa pelo id.
	 * 
	 * @param id Identificador da pessoa.
	 * @return Pessoa Instância da pessoa ou null se não existir.
	 * @throws SQLException 
	 */
	Pessoa findById(int id) throws SQLException;
	
	/**
	 * Consulta uma pessoa pelo e-mail.
	 * 
	 * @param email
	 * @return Pessoa Instância da pessoa ou null se não existir.
	 * @throws SQLException
	 */
	Pessoa findByEmail(String email) throws SQLException;
	
	/**
	 * Consulta pessoas na base de dados por nome aproximado.
	 * 
	 * @param nome
	 * @return List<Pessoa> lista de pessoas
	 * @throws SQLException
	 */
	List<Pessoa> findByNomeAproximado(String nome) throws SQLException;
	
	/**
	 * Consulta pessoas na base de dados conforme parâmetros informados.
	 * 
	 * @param nome
	 * @param telefone
	 * @param perfil
	 * @return List<Pessoa> lista de pessoas
	 * @throws SQLException
	 */
	List<Pessoa> find(String nome, String telefone, Perfil perfil) throws SQLException;
	
	/**
	 * Consulta e retorna todas as 'pessoas' na base de dados
	 * @return List<Pessoa> Lista de pessoas
	 * @throws SQLException
	 */
	List<Pessoa> findAll() throws SQLException;

}
