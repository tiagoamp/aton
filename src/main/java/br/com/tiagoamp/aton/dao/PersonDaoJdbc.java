package br.com.tiagoamp.aton.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import br.com.tiagoamp.aton.model.Role;
import br.com.tiagoamp.aton.model.Person;

@Deprecated
public class PersonDaoJdbc implements PersonDAO {
	
	Logger logger = Logger.getLogger(PersonDaoJdbc.class);
	
	public PersonDaoJdbc() {
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
	
	private Person carregarObjeto(ResultSet rs) throws SQLException {
		Person p = new Person();
		p.setId(rs.getInt("ID"));
		p.setEmail(rs.getString("EMAIL"));
		p.setName(rs.getString("NOME"));
		p.setPhone(rs.getString("TELEFONE"));
		p.setRole(Role.valueOf(rs.getString("PERFIL")));
		p.setPassword(rs.getString("SENHA"));
		return p;
	}
	
	@Override
	public void create(Person pessoa) throws SQLException {
		logger.debug("Inserindo : " + pessoa);
		try {			
			conn = DriverManager.getConnection(URL_DB);
			String sql = "INSERT INTO PESSOAS(EMAIL, NOME, TELEFONE, PERFIL, SENHA) VALUES (?, ?, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, pessoa.getEmail().toUpperCase());
			pstmt.setString(2, pessoa.getName().toUpperCase());
			pstmt.setString(3, pessoa.getPhone());
			pstmt.setString(4, pessoa.getRole().toString());
			pstmt.setString(5, pessoa.getPassword() != null ? pessoa.getPassword().toUpperCase() : null);
			pstmt.executeUpdate();
			logger.debug("Inserção executada!");
		} catch (SQLException e) {
			logger.error(e);
			throw e;			
		} finally {
			if (!pstmt.isClosed()) pstmt.close();
			if (!conn.isClosed()) conn.close();			
		}
	}

	@Override
	public void update(Person pessoa) throws SQLException {
		logger.debug("Atualizando: " + pessoa);
		try {			
			conn = DriverManager.getConnection(URL_DB);
			String sql = "UPDATE PESSOAS SET EMAIL = ?, NOME = ?, TELEFONE = ?, PERFIL = ?, SENHA = ? WHERE ID = ?";
			pstmt = conn.prepareStatement(sql);	
			pstmt.setString(1, pessoa.getEmail().toUpperCase());
			pstmt.setString(2, pessoa.getName().toUpperCase());
			pstmt.setString(3, pessoa.getPhone());
			pstmt.setString(4, pessoa.getRole().toString());
			pstmt.setString(5, pessoa.getPassword() != null ? pessoa.getPassword().toUpperCase() : null);
			pstmt.setInt(6, pessoa.getId());
			pstmt.executeUpdate();
			logger.debug("Atualização executada!");			
		} catch (SQLException e) {
			logger.error(e);
			throw e;			
		} finally {
			if (!pstmt.isClosed()) pstmt.close();
			if (!conn.isClosed()) conn.close();
		}
	}

	@Override
	public void delete(int id) throws SQLException {
		logger.debug("Apagando id: " + id);
		try {			
			conn = DriverManager.getConnection(URL_DB);
			String sql = "DELETE FROM PESSOAS WHERE ID = ?";
			pstmt = conn.prepareStatement(sql);			
			pstmt.setInt(1, id);
			pstmt.executeUpdate();
			logger.debug("Exclusão concluída!");			
		} catch (SQLException e) {
			logger.error(e);
			throw e;			
		} finally {
			if (pstmt != null && !pstmt.isClosed()) pstmt.close();
			if (conn != null && !conn.isClosed()) conn.close();
		}
	}

	@Override
	public Person findById(int id) throws SQLException {
		logger.debug("Consultar com id: " + id);
		Person p = null;
		try {			
			conn = DriverManager.getConnection(URL_DB);
			String sql = "SELECT * FROM PESSOAS WHERE ID = ?";
			pstmt = conn.prepareStatement(sql);			
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				p = carregarObjeto(rs);
			}
		    rs.close();
			logger.debug("Consulta concluída!");
			return p;
		} catch (SQLException e) {
			logger.error(e);
			throw e;			
		} finally {
			if (pstmt != null && !pstmt.isClosed()) pstmt.close();
			if (conn != null && !conn.isClosed()) conn.close();
		}
	}
	
	@Override
	public List<Person> findByFields(String nome, String telefone, Role perfil) throws SQLException {
		logger.debug("Consultando com parametros: " + nome +","+ telefone +","+ perfil);
		List<Person> lista = new ArrayList<>();
		try {
			conn = DriverManager.getConnection(URL_DB);
			StringBuilder sql = new StringBuilder("SELECT * FROM PESSOAS");
			if (nome != null || telefone != null || perfil != null) {
				sql.append(" WHERE ID IS NOT NULL ");
			}
			
			if (nome != null) sql.append("AND NOME = ? ");
			if (telefone != null) sql.append("AND TELEFONE = ? ");
			if (perfil != null) sql.append("AND PERFIL = ? ");
			
			pstmt = conn.prepareStatement(sql.toString());
			int qtParams = 1;
			if (nome != null) pstmt.setString(qtParams++, nome.toUpperCase());
			if (telefone != null) pstmt.setString(qtParams++, telefone.toUpperCase());
			if (perfil != null) pstmt.setString(qtParams++, perfil.toString());				
									
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Person p = carregarObjeto(rs);
				lista.add(p);
			}
		    rs.close();
			logger.debug("Consulta concluída!");
			return lista;
		} catch (SQLException e) {
			logger.error(e);
			throw e;			
		} finally {
			if (pstmt != null && !pstmt.isClosed()) pstmt.close();
			if (conn != null && !conn.isClosed()) conn.close();
		}
	}

	@Override
	public List<Person> findAll() throws SQLException {
		logger.debug("Consultando todas 'pessoas'");
		List<Person> lista = new ArrayList<>();
		try {			
			conn = DriverManager.getConnection(URL_DB);
			StringBuilder sql = new StringBuilder("SELECT * FROM PESSOAS");
			pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Person p = carregarObjeto(rs);
				lista.add(p);
			}
		    rs.close();
			logger.debug("Consulta concluída!");
			return lista;
		} catch (SQLException e) {
			logger.error(e);
			throw e;			
		} finally {
			if (pstmt != null && !pstmt.isClosed()) pstmt.close();
			if (conn != null && !conn.isClosed()) conn.close();
		}
	}

	@Override
	public Person findByEmail(String email) throws SQLException {
		logger.debug("Consultar com e-mail: " + email);
		Person p = null;
		try {			
			conn = DriverManager.getConnection(URL_DB);
			String sql = "SELECT * FROM PESSOAS WHERE EMAIL = ?";
			pstmt = conn.prepareStatement(sql);			
			pstmt.setString(1, email.toUpperCase());
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				p = carregarObjeto(rs);
			}
		    rs.close();
			logger.debug("Consulta concluída!");
			return p;
		} catch (SQLException e) {
			logger.error(e);
			throw e;			
		} finally {
			if (pstmt != null && !pstmt.isClosed()) pstmt.close();
			if (conn != null && !conn.isClosed()) conn.close();
		}
	}

	@Override
	public List<Person> findByNameLike(String nome) throws SQLException {
		logger.debug("Consultando por nome: " + nome);
		List<Person> lista = new ArrayList<>();
		try {
			conn = DriverManager.getConnection(URL_DB);
			String sql = "SELECT * FROM PESSOAS WHERE NOME LIKE ?";
			pstmt = conn.prepareStatement(sql);			
			pstmt.setString(1, "%" + nome.toUpperCase() + "%");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Person p = carregarObjeto(rs);
				lista.add(p);
			}
		    rs.close();
			logger.debug("Consulta concluída!");
			return lista;
		} catch (SQLException e) {
			logger.error(e);
			throw e;			
		} finally {
			if (pstmt != null && !pstmt.isClosed()) pstmt.close();
			if (conn != null && !conn.isClosed()) conn.close();
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