package br.com.tiagoamp.aton.dao;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import br.com.tiagoamp.aton.model.Livro;
import br.com.tiagoamp.aton.model.Pessoa;
import br.com.tiagoamp.aton.model.Situacao;
import br.com.tiagoamp.aton.model.TipoAquisicao;

public class LivroDaoBdLocal implements LivroDAO {
	
	Logger logger = Logger.getLogger(LivroDaoBdLocal.class);
	
	public LivroDaoBdLocal(){
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
	
	private Livro carregarObjeto(ResultSet rs) throws SQLException {
		Livro l = new Livro();
		l.setId(rs.getInt("ID"));
		l.setTitulo(rs.getString("TITULO"));
		l.setSubtitulo(rs.getString("SUBTITULO"));
		l.setDataCadastro(rs.getDate("DT_CADASTRO"));
		l.setIsbn(rs.getString("ISBN"));
		l.setEditora(rs.getString("EDITORA"));
		l.setLocalPublicacao(rs.getString("LOCAL_PUBLICACAO"));
		l.setAnoPublicacao(rs.getInt("ANO_PUBLICACAO"));
		l.setNroPaginas(rs.getInt("NRO_PAGINAS"));
		l.setGenero(rs.getString("GENERO"));
		l.setClassificacao(rs.getString("CLASSIFICACAO"));
		l.setPublicoAlvo(rs.getString("PUBLICO_ALVO"));
		l.setPathFotoCapa(Paths.get(rs.getString("PATH_FOTO_CAPA")));
		l.setDataAquisicao(rs.getDate("DT_AQUISICAO"));
		l.setTipoAquisicao(TipoAquisicao.valueOf(rs.getString("TIPO_AQUISICAO")));
		l.setNomeDoador(rs.getString("NM_DOADOR"));
		Pessoa pessoa = new Pessoa();
		pessoa.setId(rs.getInt("ID_PESSOA_CADASTRADORA"));
		l.setPessoaCadastradora(pessoa);
		l.setSituacao(Situacao.valueOf(rs.getString("SITUACAO")));
		l.setAutoresAgrupados(rs.getString("AUTOR"));
		return l;
	}
	
	@Override
	public int create(Livro livro) throws SQLException {
		logger.debug("Inserindo: " + livro);
		int result;
		try {			
			conn = DriverManager.getConnection(URL_DB);
			String sql = "INSERT INTO LIVROS(TITULO, SUBTITULO, DT_CADASTRO, ISBN,"
					+ "EDITORA, LOCAL_PUBLICACAO, ANO_PUBLICACAO, NRO_PAGINAS, GENERO,"
					+ "CLASSIFICACAO, PUBLICO_ALVO, PATH_FOTO_CAPA, DT_AQUISICAO, TIPO_AQUISICAO,"
					+ "NM_DOADOR, ID_PESSOA_CADASTRADORA, SITUACAO, AUTOR) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);			
			pstmt.setString(1, livro.getTitulo().toUpperCase());
			pstmt.setString(2, livro.getSubtitulo().toUpperCase());
			pstmt.setDate(3, new java.sql.Date(livro.getDataCadastro().getTime()) );
			pstmt.setString(4, livro.getIsbn().toUpperCase());
			pstmt.setString(5, livro.getEditora().toUpperCase());
			pstmt.setString(6, livro.getLocalPublicacao().toUpperCase());
			pstmt.setObject(7, livro.getAnoPublicacao());
			pstmt.setObject(8, livro.getNroPaginas());
			pstmt.setString(9, livro.getGenero());
			pstmt.setString(10, livro.getClassificacao().toUpperCase());
			pstmt.setString(11, livro.getPublicoAlvo().toUpperCase());
			pstmt.setObject(12, livro.getPathFotoCapa() != null ? livro.getPathFotoCapa().toString() : null);
			pstmt.setDate(13, new java.sql.Date(livro.getDataAquisicao().getTime()));
			pstmt.setObject(14, livro.getTipoAquisicao() != null ? livro.getTipoAquisicao().toString() : null);
			pstmt.setString(15, livro.getNomeDoador().toUpperCase());
			pstmt.setObject(16, livro.getPessoaCadastradora() != null ? livro.getPessoaCadastradora().getId() : null);
			pstmt.setObject(17, livro.getSituacao() != null ? livro.getSituacao().toString() : null );
			pstmt.setString(18, livro.getAutoresAgrupados().toUpperCase());
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
	public int update(Livro livro) throws SQLException {
		logger.debug("Atualizando: " + livro);
		int result;
		try {			
			conn = DriverManager.getConnection(URL_DB);
			String sql = "UPDATE LIVROS SET "
					+ "TITULO = ?, SUBTITULO = ?, DT_CADASTRO = ?, ISBN = ?,"
					+ "EDITORA = ?, LOCAL_PUBLICACAO = ?, ANO_PUBLICACAO = ?, NRO_PAGINAS = ?, GENERO = ?,"
					+ "CLASSIFICACAO = ?, PUBLICO_ALVO = ?, PATH_FOTO_CAPA = ?, DT_AQUISICAO = ?, TIPO_AQUISICAO = ?,"
					+ "NM_DOADOR = ?, ID_PESSOA_CADASTRADORA = ?, SITUACAO = ?, AUTOR = ? "
					+ "	WHERE ID = ?";
			pstmt = conn.prepareStatement(sql);			
			pstmt.setString(1, livro.getTitulo().toUpperCase());
			pstmt.setString(2, livro.getSubtitulo().toUpperCase());
			pstmt.setDate(3, new java.sql.Date(livro.getDataCadastro().getTime()) );
			pstmt.setString(4, livro.getIsbn().toUpperCase());
			pstmt.setString(5, livro.getEditora().toUpperCase());
			pstmt.setString(6, livro.getLocalPublicacao().toUpperCase());
			pstmt.setInt(7, livro.getAnoPublicacao());
			pstmt.setInt(8, livro.getNroPaginas());
			pstmt.setString(9, livro.getGenero().toUpperCase());
			pstmt.setString(10, livro.getClassificacao().toUpperCase());
			pstmt.setString(11, livro.getPublicoAlvo().toUpperCase());
			pstmt.setString(12, livro.getPathFotoCapa().toString());
			pstmt.setDate(13, new java.sql.Date(livro.getDataAquisicao().getTime()));
			pstmt.setString(14, livro.getTipoAquisicao().toString());
			pstmt.setString(15, livro.getNomeDoador().toUpperCase());
			pstmt.setInt(16, livro.getPessoaCadastradora() != null ? livro.getPessoaCadastradora().getId() : 0);
			pstmt.setString(17, livro.getSituacao().toString());
			pstmt.setString(18, livro.getAutoresAgrupados().toUpperCase());
			pstmt.setInt(19, livro.getId());
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
			String sql = "DELETE FROM LIVROS WHERE ID = ?";
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
	public Livro findById(int id) throws SQLException {
		logger.debug("Consultar com id: " + id);
		Livro p = null;
		try {			
			conn = DriverManager.getConnection(URL_DB);
			String sql = "SELECT * FROM LIVROS WHERE ID = ?";
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
			if (!pstmt.isClosed()) pstmt.close();
			if (!conn.isClosed()) conn.close();
		}
	}

	@Override
	public List<Livro> find(String titulo, String autor, String isbn, String classificacao, String publico) throws SQLException {
		logger.debug("Consultando com parametros: " + titulo + " , " + autor + ", " + isbn + " , " + classificacao + " , " + publico);
		List<Livro> lista = new ArrayList<>();
		try {			
			conn = DriverManager.getConnection(URL_DB);
			StringBuilder sql = new StringBuilder("SELECT * FROM LIVROS");
			if (titulo != null || autor != null || isbn != null || classificacao != null || publico != null) {
				sql.append(" WHERE ID IS NOT NULL ");
			}
			
			if (titulo != null) sql.append("AND TITULO = ? ");
			if (autor != null) sql.append("AND AUTOR = ? ");
			if (isbn != null) sql.append("AND ISBN = ? ");
			if (classificacao != null) sql.append("AND CLASSIFICACAO = ? ");
			if (publico != null) sql.append("AND PUBLICO_ALVO = ? ");
			
			pstmt = conn.prepareStatement(sql.toString());
			int qtParams = 1;
			if (titulo != null) pstmt.setString(qtParams++, titulo.toUpperCase());
			if (autor != null) pstmt.setString(qtParams++, autor.toUpperCase());
			if (isbn != null) pstmt.setString(qtParams++, isbn);
			if (classificacao != null) pstmt.setString(qtParams++, classificacao.toUpperCase());
			if (publico != null) pstmt.setString(qtParams++, publico.toUpperCase());
						
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Livro l = carregarObjeto(rs);
				lista.add(l);
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
	public List<Livro> findAll() throws SQLException {
		logger.debug("Consultando todos 'livros'");
		List<Livro> lista = new ArrayList<>();
		try {			
			conn = DriverManager.getConnection(URL_DB);
			StringBuilder sql = new StringBuilder("SELECT * FROM LIVROS");
			pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Livro l = carregarObjeto(rs);
				lista.add(l);
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
	public Livro findByIsbn(String isbn) throws SQLException {
		logger.debug("Consultar com isbn: " + isbn);
		Livro p = null;
		try {			
			conn = DriverManager.getConnection(URL_DB);
			String sql = "SELECT * FROM LIVROS WHERE ISBN = ?";
			pstmt = conn.prepareStatement(sql);			
			pstmt.setString(1, isbn);
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
			if (!pstmt.isClosed()) pstmt.close();
			if (!conn.isClosed()) conn.close();
		}
	}

	@Override
	public Path createCapaLivro(MultipartFile mFile, String nomeArquivo) throws IOException {
		Path path = Paths.get(PATH_DB,"pics",nomeArquivo);
		Files.copy(mFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
		return path;
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

	@Override
	public List<Livro> findByAutorAproximado(String autor) throws SQLException {
		logger.debug("Consultando por autor aproximado: " + autor);
		List<Livro> lista = new ArrayList<>();
		try {
			conn = DriverManager.getConnection(URL_DB);
			String sql = "SELECT * FROM LIVROS WHERE AUTOR LIKE ?";
			pstmt = conn.prepareStatement(sql);			
			pstmt.setString(1, "%" + autor.toUpperCase() + "%");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Livro l = carregarObjeto(rs);
				lista.add(l);
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
	public List<Livro> findByTituloAproximado(String titulo) throws SQLException {
		logger.debug("Consultando por titulo aproximado: " + titulo);
		List<Livro> lista = new ArrayList<>();
		try {
			conn = DriverManager.getConnection(URL_DB);
			String sql = "SELECT * FROM LIVROS WHERE TITULO LIKE ?";
			pstmt = conn.prepareStatement(sql);			
			pstmt.setString(1, "%" + titulo.toUpperCase() + "%");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Livro l = carregarObjeto(rs);
				lista.add(l);
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
	
}
