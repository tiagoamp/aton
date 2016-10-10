package br.com.tiagoamp.aton.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import br.com.tiagoamp.aton.model.Emprestimo;
import br.com.tiagoamp.aton.model.Livro;
import br.com.tiagoamp.aton.model.Pessoa;

public class EmprestimoDaoBdLocal implements EmprestimoDAO {
	
	Logger logger = Logger.getLogger(EmprestimoDaoBdLocal.class);
	
	public EmprestimoDaoBdLocal() {
		InputStream istream = this.getClass().getResourceAsStream("config.properties");
		Properties prop = new Properties();
		try {
			prop.load(istream);
			Class.forName("org.sqlite.JDBC");
		} catch (IOException | ClassNotFoundException e) {
			logger.error(e);
		}
		PATH_DB = prop.getProperty("bd_path");
		NAME_DB = prop.getProperty("bd_name");
		URL_DB = "jdbc:sqlite:" + PATH_DB + NAME_DB;
	}
	
	private Connection conn;
	private PreparedStatement pstmt;
	private String URL_DB;
	private String PATH_DB;
	private String NAME_DB;
	
		
	private Emprestimo carregarObjeto(ResultSet rs) throws SQLException {
		Emprestimo emp = new Emprestimo();
		emp.setId(rs.getInt("ID"));
		Pessoa pessoa = new Pessoa();
		pessoa.setId(rs.getInt("ID_PESSOA"));
		emp.setPessoa(pessoa);
		Livro livro = new Livro();
		livro.setId(rs.getInt("ID_LIVRO"));
		emp.setLivro(livro);
		emp.setDataEmprestimo(rs.getDate("DT_EMPRESTIMO"));
		emp.setDataDevolucao(rs.getDate("DT_DEVOLUCAO"));
		emp.setDataDevolucaoProgramada(rs.getDate("DT_DEVOLUCAO_PROGRAMADA"));
		return emp;
	}

	@Override
	public int create(Emprestimo emprestimo) throws SQLException {
		logger.debug("Inserindo : " + emprestimo);
		int result;
		try {			
			conn = DriverManager.getConnection(URL_DB);
			String sql = "INSERT INTO EMPRESTIMOS(ID_PESSOA, ID_LIVRO, DT_EMPRESTIMO, DT_DEVOLUCAO, DT_DEVOLUCAO_PROGRAMADA) VALUES (?, ?, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);			
			pstmt.setInt(1, emprestimo.getPessoa().getId());
			pstmt.setInt(2, emprestimo.getLivro().getId());
			pstmt.setDate(3, new java.sql.Date(emprestimo.getDataEmprestimo().getTime()));
			if (emprestimo.getDataDevolucao() != null) {
				pstmt.setDate(4, new java.sql.Date(emprestimo.getDataDevolucao().getTime()));
			}			
			if (emprestimo.getDataDevolucaoProgramada() != null) {
				pstmt.setDate(5, new java.sql.Date(emprestimo.getDataDevolucaoProgramada().getTime()));
			}
			result = pstmt.executeUpdate();
			logger.debug("Inserção concluída!");
			return result;
		} catch (SQLException e) {
			logger.error(e);
			throw e;			
		} finally {
			if (!pstmt.isClosed()) pstmt.close();
			if (!conn.isClosed()) conn.close();			
		}
	}

	@Override
	public int update(Emprestimo emprestimo) throws SQLException {
		logger.debug("Atualizando: " + emprestimo);
		int result;
		try {			
			conn = DriverManager.getConnection(URL_DB);
			String sql = "UPDATE EMPRESTIMOS SET ID_PESSOA = ?, ID_LIVRO = ?, DT_EMPRESTIMO = ?, "
					+ "DT_DEVOLUCAO = ?, DT_DEVOLUCAO_PROGRAMADA = ? WHERE ID = ?";
			pstmt = conn.prepareStatement(sql);			
			pstmt.setInt(1, emprestimo.getPessoa().getId());
			pstmt.setInt(2, emprestimo.getLivro().getId());
			pstmt.setDate(3, new java.sql.Date(emprestimo.getDataEmprestimo().getTime()));
			pstmt.setDate(4, emprestimo.getDataDevolucao() != null ? new java.sql.Date(emprestimo.getDataDevolucao().getTime()) : null);
			pstmt.setDate(5, emprestimo.getDataDevolucaoProgramada() != null ? new java.sql.Date(emprestimo.getDataDevolucaoProgramada().getTime()) : null);
			pstmt.setInt(6, emprestimo.getId());
			result = pstmt.executeUpdate();
			logger.debug("Atualização concluída!");
			return result;
		} catch (SQLException e) {
			logger.error(e);
			throw e;			
		} finally {
			if (!pstmt.isClosed()) pstmt.close();
			if (!conn.isClosed()) conn.close();
		}

	}

	@Override
	public int delete(int id) throws SQLException {
		logger.debug("Apagando id: " + id);
		int result;
		try {			
			conn = DriverManager.getConnection(URL_DB);
			String sql = "DELETE FROM EMPRESTIMOS WHERE ID = ?";
			pstmt = conn.prepareStatement(sql);			
			pstmt.setInt(1, id);
			result = pstmt.executeUpdate();
			logger.debug("Delete concluído!");
			return result;
		} catch (SQLException e) {
			logger.error(e);
			throw e;			
		} finally {
			if (!pstmt.isClosed()) pstmt.close();
			if (!conn.isClosed()) conn.close();
		}
	}

	@Override
	public Emprestimo findById(int id) throws SQLException {
		logger.debug("Consultar com id: " + id);
		Emprestimo emp = null;
		try {			
			conn = DriverManager.getConnection(URL_DB);
			String sql = "SELECT * FROM EMPRESTIMOS WHERE ID = ?";
			pstmt = conn.prepareStatement(sql);			
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				emp = carregarObjeto(rs);
			}
		    rs.close();
			logger.debug("Consulta concluída!");
			return emp;
		} catch (SQLException e) {
			logger.error(e);
			throw e;			
		} finally {
			if (!pstmt.isClosed()) pstmt.close();
			if (!conn.isClosed()) conn.close();
		}
	}

	@Override
	public List<Emprestimo> find(Integer idLivro, Integer idPessoa, Date dataEmprestimo, Date dataDevolucao) throws SQLException {
		logger.debug("Consultando com parametros: " + idLivro + " , " + idPessoa + ", " + dataEmprestimo + " , " + dataDevolucao);
		List<Emprestimo> lista = new ArrayList<>();
		try {			
			conn = DriverManager.getConnection(URL_DB);
			StringBuilder sql = new StringBuilder("SELECT * FROM EMPRESTIMOS");
			if (idLivro != null || idPessoa != null || dataEmprestimo != null || dataDevolucao != null) {
				sql.append(" WHERE ID IS NOT NULL ");
			}
			
			if (idLivro != null) sql.append("AND ID_LIVRO = ? ");
			if (idPessoa != null) sql.append("AND ID_PESSOA = ? ");
			if (dataEmprestimo != null) sql.append("AND DT_EMPRESTIMO = ? ");
			if (dataDevolucao != null) sql.append("AND DT_DEVOLUCAO = ? ");
			
			pstmt = conn.prepareStatement(sql.toString());
			int qtParams = 1;
			if (idLivro != null) pstmt.setInt(qtParams++, idLivro);
			if (idPessoa != null) pstmt.setInt(qtParams++, idPessoa);
			if (dataEmprestimo != null) pstmt.setDate(qtParams++, new java.sql.Date(dataEmprestimo.getTime()));
			if (dataDevolucao != null) pstmt.setDate(qtParams++, new java.sql.Date(dataDevolucao.getTime()));				
									
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Emprestimo emp = carregarObjeto(rs);
				lista.add(emp);
			}
		    rs.close();
			logger.debug("Consulta concluída!");
			return lista;
		} catch (SQLException e) {
			logger.error(e);
			throw e;			
		} finally {
			if (!pstmt.isClosed()) pstmt.close();
			if (!conn.isClosed()) conn.close();
		}
	}

	@Override
	public List<Emprestimo> findAll() throws SQLException {
		logger.debug("Consultando todos 'emprestimos'");
		List<Emprestimo> lista = new ArrayList<>();
		try {			
			conn = DriverManager.getConnection(URL_DB);
			StringBuilder sql = new StringBuilder("SELECT * FROM EMPRESTIMOS");
			pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Emprestimo emp = carregarObjeto(rs);
				lista.add(emp);
			}
		    rs.close();
			logger.debug("Consulta concluída!");
			return lista;
		} catch (SQLException e) {
			logger.error(e);
			throw e;			
		} finally {
			if (!pstmt.isClosed()) pstmt.close();
			if (!conn.isClosed()) conn.close();
		}
	}
	
	@Override
	public List<Emprestimo> findAllEmAberto() throws SQLException {
		logger.debug("Consultando todos 'emprestimos' em aberto");
		List<Emprestimo> lista = new ArrayList<>();
		try {			
			conn = DriverManager.getConnection(URL_DB);
			StringBuilder sql = new StringBuilder("SELECT * FROM EMPRESTIMOS WHERE DT_DEVOLUCAO IS NULL");
			pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Emprestimo emp = carregarObjeto(rs);
				lista.add(emp);
			}
		    rs.close();
			logger.debug("Consulta concluída!");
			return lista;
		} catch (SQLException e) {
			logger.error(e);
			throw e;			
		} finally {
			if (!pstmt.isClosed()) pstmt.close();
			if (!conn.isClosed()) conn.close();
		}
	}
	
	public String getPATH_DB() {
		return PATH_DB;
	}
	public void setPATH_DB(String strPathDB) {
		PATH_DB = strPathDB;
	}
	public String getNAME_DB() {
		return NAME_DB;
	}
	public void setNAME_DB(String strNameDB) {
		NAME_DB = strNameDB;
	}
	public String getURL_DB() {
		return URL_DB;
	}
	public void setURL_DB(String url) {
		this.URL_DB = url;
	}

}
