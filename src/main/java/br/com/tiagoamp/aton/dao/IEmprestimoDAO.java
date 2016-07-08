package br.com.tiagoamp.aton.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import br.com.tiagoamp.aton.model.Emprestimo;

public interface IEmprestimoDAO {
	
	/**
	 * Insere um empréstimo no base de dados.
	 * 
	 * @param emprestimo
	 */
	void inserir(Emprestimo emprestimo) throws SQLException;
	
	/**
	 * Atualiza um empréstimo na base de dados.
	 * 
	 * @param emprestimo
	 * @throws SQLException
	 */
	void atualizar(Emprestimo emprestimo) throws SQLException;
	
	/**
	 * Apaga um empréstimo na base de dados.
	 * 
	 * @param id
	 * @throws SQLException
	 */
	void apagar(int id) throws SQLException;
	
	/**
	 * Consulta um empréstimo pelo id.
	 * 
	 * @param id Identificador do empréstimo.
	 * @return Emprestimo Instância do empréstimo ou null se não existir.
	 * @throws SQLException
	 */
	Emprestimo consultar(int id) throws SQLException;
	
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
	List<Emprestimo> consultar(Integer idLivro, Integer idPessoa, Date dataEmprestimo, Date dataDevolucao) throws SQLException;
	
	/**
	 * Consulta todos os emprestimos da base de dados.
	 * @return List<Emprestimo>
	 * @throws SQLException
	 */
	List<Emprestimo> consultar() throws SQLException;

}
