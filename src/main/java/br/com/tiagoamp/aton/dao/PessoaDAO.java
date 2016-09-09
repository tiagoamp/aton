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
	 */
	void inserir(Pessoa pessoa) throws SQLException;
	
	/**
	 * Atualiza uma pessoa na base de dados.
	 * 
	 * @param pessoa
	 * @throws SQLException 
	 */
	void atualizar(Pessoa pessoa) throws SQLException;
	
	/**
	 * Apaga uma pessoa na base de dados.
	 * 
	 * @param id 
	 * @throws SQLException 
	 */
	void apagar(int id) throws SQLException;
	
	/**
	 * Consulta uma pessoa pelo id.
	 * 
	 * @param id Identificador da pessoa.
	 * @return Pessoa Instância da pessoa ou null se não existir.
	 * @throws SQLException 
	 */
	Pessoa consultarPorId(int id) throws SQLException;
	
	/**
	 * Consulta uma pessoa pelo e-mail.
	 * 
	 * @param email
	 * @return Pessoa Instância da pessoa ou null se não existir.
	 * @throws SQLException
	 */
	Pessoa consultarPorEmail(String email) throws SQLException;
	
	/**
	 * Consulta pessoas na base de dados por nome aproximado.
	 * 
	 * @param nome
	 * @return
	 * @throws SQLException
	 */
	List<Pessoa> consultarPorNomeAproximado(String nome) throws SQLException;
	
	/**
	 * Consulta pessoas na base de dados conforme parâmetros informados.
	 * 
	 * @param nome
	 * @param telefone
	 * @param perfil
	 * @return
	 * @throws SQLException
	 */
	List<Pessoa> consultar(String nome, String telefone, Perfil perfil) throws SQLException;
	
	/**
	 * Consulta e retorna todas as 'pessoas' na base de dados
	 * @return List<Pessoa> Lista de pessoas
	 * @throws SQLException
	 */
	List<Pessoa> consultar() throws SQLException;

}
