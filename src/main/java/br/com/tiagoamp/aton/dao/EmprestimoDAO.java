package br.com.tiagoamp.aton.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import br.com.tiagoamp.aton.model.Emprestimo;

public interface EmprestimoDAO {
	
	/**
	 * Insere um empréstimo no base de dados.
	 * 
	 * @param emprestimo
	 * @throws SQLException	 * 
	 * @return int number of rows inserted
	 */
	int create(Emprestimo emprestimo) throws SQLException;
	
	/**
	 * Atualiza um empréstimo na base de dados.
	 * 
	 * @param emprestimo
	 * @throws SQLException 
	 * @return int number of rows updated
	 */
	int update(Emprestimo emprestimo) throws SQLException;
	
	/**
	 * Apaga um empréstimo na base de dados.
	 * 
	 * @param id
	 * @throws SQLException
	 * @return int number of rows deleted
	 */
	int delete(int id) throws SQLException;
	
	/**
	 * Consulta um empréstimo pelo id.
	 * 
	 * @param id Identificador do empréstimo.
	 * @return Emprestimo Instância do empréstimo ou null se não existir.
	 * @throws SQLException
	 */
	Emprestimo findById(int id) throws SQLException;
	
	/**
	 * Consulta emprestimos na base de dados baseado nos parâmetros informados.
	 * 
	 * @param idLivro
	 * @param idPessoa
	 * @param dataEmprestimo
	 * @param dataDevolucao
	 * 
	 * @throws SQLException
	 * 
	 * @return List<Emprestimo> Lista de empréstimos ou null se não existir.
	 */
	List<Emprestimo> find(Integer idLivro, Integer idPessoa, Date dataEmprestimo, Date dataDevolucao) throws SQLException;
	
	/**
	 * Consulta todos os emprestimos da base de dados.
	 * @return List<Emprestimo>
	 * @throws SQLException
	 */
	List<Emprestimo> findAll() throws SQLException;

}
