package br.com.tiagoamp.aton.dao;

import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.tiagoamp.aton.model.Livro;
import br.com.tiagoamp.aton.model.Pessoa;
import br.com.tiagoamp.aton.model.Situacao;
import br.com.tiagoamp.aton.model.TipoAquisicao;

public class LivroDaoBdLocal implements ILivroDAO {
	
	Logger logger = Logger.getLogger(LivroDaoBdLocal.class);
	
	public LivroDaoBdLocal(){
	}
	
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
	
	private Connection conn;
	private PreparedStatement pstmt;
	private String URL_DB = "jdbc:sqlite:/home/tiago/proj/Biblioteca/fontes_novo/Biblioteca/database/libdatabase";
	
	public void setURL_DB(String url) {
		this.URL_DB = url;
	}

	@Override
	public void inserir(Livro livro) throws SQLException {
		logger.debug("Inserindo 'livro': " + livro);
		try {			
			conn = DriverManager.getConnection(URL_DB);
			String sql = "INSERT INTO LIVROS(TITULO, SUBTITULO, DT_CADASTRO, ISBN,"
					+ "EDITORA, LOCAL_PUBLICACAO, ANO_PUBLICACAO, NRO_PAGINAS, GENERO,"
					+ "CLASSIFICACAO, PUBLICO_ALVO, PATH_FOTO_CAPA, DT_AQUISICAO, TIPO_AQUISICAO,"
					+ "NM_DOADOR, ID_PESSOA_CADASTRADORA, SITUACAO, AUTOR) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);			
			pstmt.setString(1, livro.getTitulo());
			pstmt.setString(2, livro.getSubtitulo());
			pstmt.setDate(3, new java.sql.Date(livro.getDataCadastro().getTime()) );
			pstmt.setString(4, livro.getIsbn());
			pstmt.setString(5, livro.getEditora());
			pstmt.setString(6, livro.getLocalPublicacao());
			pstmt.setObject(7, livro.getAnoPublicacao());
			pstmt.setObject(8, livro.getNroPaginas());
			pstmt.setString(9, livro.getGenero());
			pstmt.setString(10, livro.getClassificacao());
			pstmt.setString(11, livro.getPublicoAlvo());
			pstmt.setObject(12, livro.getPathFotoCapa() != null ? livro.getPathFotoCapa().toString() : null);
			pstmt.setDate(13, new java.sql.Date(livro.getDataAquisicao().getTime()));
			pstmt.setObject(14, livro.getTipoAquisicao() != null ? livro.getTipoAquisicao().toString() : null);
			pstmt.setString(15, livro.getNomeDoador());
			pstmt.setObject(16, livro.getPessoaCadastradora() != null ? livro.getPessoaCadastradora().getId() : null);
			pstmt.setObject(17, livro.getSituacao() != null ? livro.getSituacao().toString() : null );
			pstmt.setString(18, livro.getAutoresAgrupados());
			pstmt.executeUpdate();
			logger.debug("Inserção concluída!");
		} catch (SQLException e) {
			logger.error(e);
			throw e;			
		} finally {
			if (!pstmt.isClosed()) pstmt.close();
			if (!conn.isClosed()) conn.close();			
		}
	}

	@Override
	public void atualizar(Livro livro) throws SQLException {
		logger.debug("Atualizando: " + livro);
		try {			
			conn = DriverManager.getConnection(URL_DB);
			String sql = "UPDATE LIVROS SET "
					+ "TITULO = ?, SUBTITULO = ?, DT_CADASTRO = ?, ISBN = ?,"
					+ "EDITORA = ?, LOCAL_PUBLICACAO = ?, ANO_PUBLICACAO = ?, NRO_PAGINAS = ?, GENERO = ?,"
					+ "CLASSIFICACAO = ?, PUBLICO_ALVO = ?, PATH_FOTO_CAPA = ?, DT_AQUISICAO = ?, TIPO_AQUISICAO = ?,"
					+ "NM_DOADOR = ?, ID_PESSOA_CADASTRADORA = ?, SITUACAO = ?, AUTOR = ?";
			pstmt = conn.prepareStatement(sql);			
			pstmt.setString(1, livro.getTitulo());
			pstmt.setString(2, livro.getSubtitulo());
			pstmt.setDate(3, new java.sql.Date(livro.getDataCadastro().getTime()) );
			pstmt.setString(4, livro.getIsbn());
			pstmt.setString(5, livro.getEditora());
			pstmt.setString(6, livro.getLocalPublicacao());
			pstmt.setInt(7, livro.getAnoPublicacao());
			pstmt.setInt(8, livro.getNroPaginas());
			pstmt.setString(9, livro.getGenero());
			pstmt.setString(10, livro.getClassificacao());
			pstmt.setString(11, livro.getPublicoAlvo());
			pstmt.setString(12, livro.getPathFotoCapa().toString());
			pstmt.setDate(13, new java.sql.Date(livro.getDataAquisicao().getTime()));
			pstmt.setString(14, livro.getTipoAquisicao().toString());
			pstmt.setString(15, livro.getNomeDoador());
			pstmt.setInt(16, livro.getPessoaCadastradora().getId());
			pstmt.setString(17, livro.getSituacao().toString());
			pstmt.setString(18, livro.getAutoresAgrupados());
			pstmt.executeUpdate();
			logger.debug("Atualização concluída!");
		} catch (SQLException e) {
			logger.error(e);
			throw e;			
		} finally {
			if (!pstmt.isClosed()) pstmt.close();
			if (!conn.isClosed()) conn.close();
		}
	}

	@Override
	public void apagar(int id) throws SQLException {
		logger.debug("Apagando id: " + id);
		try {			
			conn = DriverManager.getConnection(URL_DB);
			String sql = "DELETE FROM LIVROS WHERE ID = ?";
			pstmt = conn.prepareStatement(sql);			
			pstmt.setInt(1, id);
			pstmt.executeUpdate();
			logger.debug("Delete concluído!");
		} catch (SQLException e) {
			logger.error(e);
			throw e;			
		} finally {
			if (!pstmt.isClosed()) pstmt.close();
			if (!conn.isClosed()) conn.close();
		}

	}

	@Override
	public Livro consultar(int id) throws SQLException {
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
	public List<Livro> consultar(String titulo, String autor, String isbn, String classificacao, String publico) throws SQLException {
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
			if (titulo != null) pstmt.setString(qtParams++, titulo);
			if (autor != null) pstmt.setString(qtParams++, autor);
			if (isbn != null) pstmt.setString(qtParams++, isbn);
			if (classificacao != null) pstmt.setString(qtParams++, classificacao);
			if (publico != null) pstmt.setString(qtParams++, publico);
						
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
	public List<Livro> consultar() throws SQLException {
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
	public Livro consultar(String isbn) throws SQLException {
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

}
