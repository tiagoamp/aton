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

import br.com.tiagoamp.aton.model.Book;
import br.com.tiagoamp.aton.model.Person;
import br.com.tiagoamp.aton.model.Status;
import br.com.tiagoamp.aton.model.TypeOfAcquisition;

@Deprecated
public class BookDaoJdbc implements BookDAO {
	
	private Logger logger = Logger.getLogger(BookDaoJdbc.class);
	
	public BookDaoJdbc(){
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
	
	private Book carregarObjeto(ResultSet rs) throws SQLException {
		Book book = new Book();
		book.setId(rs.getInt("ID"));
		book.setTitle(rs.getString("TITULO"));
		book.setSubtitle(rs.getString("SUBTITULO"));
		book.setDateOfRegistration(rs.getDate("DT_CADASTRO"));
		book.setIsbn(rs.getString("ISBN"));
		book.setPublishingCompany(rs.getString("EDITORA"));
		book.setPublishingCity(rs.getString("LOCAL_PUBLICACAO"));
		book.setPublishingYear(rs.getInt("ANO_PUBLICACAO"));
		book.setNumberOfPages(rs.getInt("NRO_PAGINAS"));
		book.setGenre(rs.getString("GENERO"));
		book.setClassification(rs.getString("CLASSIFICACAO"));
		book.setTargetAudience(rs.getString("PUBLICO_ALVO"));
		book.setDateOfAcquisition(rs.getDate("DT_AQUISICAO"));
		book.setTypeOfAcquisition(TypeOfAcquisition.valueOf(rs.getString("TIPO_AQUISICAO")));
		book.setDonorName(rs.getString("NM_DOADOR"));
		Person person = new Person();
		person.setId(rs.getInt("ID_PESSOA_CADASTRADORA"));
		book.setRegisterer(person);
		book.setStatus(Status.valueOf(rs.getString("SITUACAO")));
		book.setAuthorsNameInline(rs.getString("AUTOR"));
		book.setNumberOfCopies(rs.getInt("QTD_EXEMPLARES"));
		book.setNumberAvailable(rs.getInt("QTD_DISPONIVEIS"));
		return book;
	}
	
	@Override
	public void create(Book book) throws SQLException {
		logger.debug("Inserindo: " + book);
		try {			
			conn = DriverManager.getConnection(URL_DB);
			String sql = "INSERT INTO LIVROS(TITULO, SUBTITULO, DT_CADASTRO, ISBN,"
					+ "EDITORA, LOCAL_PUBLICACAO, ANO_PUBLICACAO, NRO_PAGINAS, GENERO,"
					+ "CLASSIFICACAO, PUBLICO_ALVO, DT_AQUISICAO, TIPO_AQUISICAO,"
					+ "NM_DOADOR, ID_PESSOA_CADASTRADORA, SITUACAO, AUTOR, QTD_EXEMPLARES, QTD_DISPONIVEIS) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);			
			pstmt.setString(1, book.getTitle().toUpperCase());
			pstmt.setString(2, book.getSubtitle().toUpperCase());
			pstmt.setDate(3, new java.sql.Date(book.getDateOfRegistration().getTime()) );
			pstmt.setString(4, book.getIsbn().toUpperCase());
			pstmt.setString(5, book.getPublishingCompany().toUpperCase());
			pstmt.setString(6, book.getPublishingCity().toUpperCase());
			pstmt.setObject(7, book.getPublishingYear());
			pstmt.setObject(8, book.getNumberOfPages());
			pstmt.setString(9, book.getGenre());
			pstmt.setString(10, book.getClassification().toUpperCase());
			pstmt.setString(11, book.getTargetAudience().toUpperCase());
			pstmt.setDate(12, new java.sql.Date(book.getDateOfAcquisition().getTime()));
			pstmt.setObject(13, book.getTypeOfAcquisition() != null ? book.getTypeOfAcquisition().toString() : null);
			pstmt.setString(14, book.getDonorName().toUpperCase());
			pstmt.setObject(15, book.getRegisterer() != null ? book.getRegisterer().getId() : null);
			pstmt.setObject(16, book.getStatus() != null ? book.getStatus().toString() : null );
			pstmt.setString(17, book.getAuthorsNameInline().toUpperCase());
			pstmt.setInt(18, book.getNumberOfCopies());
			pstmt.setInt(19, book.getNumberAvailable());
			pstmt.executeUpdate();
			logger.debug("Inserção concluída!");
		} catch (SQLException e) {
			logger.error(e);
			throw e;			
		} finally {
			if (pstmt != null && !pstmt.isClosed()) pstmt.close();
			if (conn != null && !conn.isClosed()) conn.close();			
		}
	}

	@Override
	public void update(Book livro) throws SQLException {
		logger.debug("Atualizando: " + livro);
		try {			
			conn = DriverManager.getConnection(URL_DB);
			String sql = "UPDATE LIVROS SET "
					+ "TITULO = ?, SUBTITULO = ?, DT_CADASTRO = ?, ISBN = ?,"
					+ "EDITORA = ?, LOCAL_PUBLICACAO = ?, ANO_PUBLICACAO = ?, NRO_PAGINAS = ?, GENERO = ?,"
					+ "CLASSIFICACAO = ?, PUBLICO_ALVO = ?, DT_AQUISICAO = ?, TIPO_AQUISICAO = ?,"
					+ "NM_DOADOR = ?, ID_PESSOA_CADASTRADORA = ?, SITUACAO = ?, AUTOR = ?, "
					+ "QTD_EXEMPLARES = ?, QTD_DISPONIVEIS = ?"
					+ "	WHERE ID = ?";
			pstmt = conn.prepareStatement(sql);			
			pstmt.setString(1, livro.getTitle().toUpperCase());
			pstmt.setString(2, livro.getSubtitle().toUpperCase());
			pstmt.setDate(3, new java.sql.Date(livro.getDateOfRegistration().getTime()) );
			pstmt.setString(4, livro.getIsbn().toUpperCase());
			pstmt.setString(5, livro.getPublishingCompany().toUpperCase());
			pstmt.setString(6, livro.getPublishingCity().toUpperCase());
			pstmt.setInt(7, livro.getPublishingYear());
			pstmt.setInt(8, livro.getNumberOfPages());
			pstmt.setString(9, livro.getGenre().toUpperCase());
			pstmt.setString(10, livro.getClassification().toUpperCase());
			pstmt.setString(11, livro.getTargetAudience().toUpperCase());
			pstmt.setDate(12, new java.sql.Date(livro.getDateOfAcquisition().getTime()));
			pstmt.setString(13, livro.getTypeOfAcquisition().toString());
			pstmt.setString(14, livro.getDonorName().toUpperCase());
			pstmt.setInt(15, livro.getRegisterer() != null ? livro.getRegisterer().getId() : 0);
			pstmt.setString(16, livro.getStatus().toString());
			pstmt.setString(17, livro.getAuthorsNameInline().toUpperCase());
			pstmt.setInt(18, livro.getNumberOfCopies());
			pstmt.setInt(19, livro.getNumberAvailable());
			pstmt.setInt(20, livro.getId());
			pstmt.executeUpdate();
			logger.debug("Atualização concluída!");
		} catch (SQLException e) {
			logger.error(e);
			throw e;			
		} finally {
			if (pstmt != null && !pstmt.isClosed()) pstmt.close();
			if (conn != null && !conn.isClosed()) conn.close();
		}
	}

	@Override
	public void delete(int id) throws SQLException {
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
			if (pstmt != null && !pstmt.isClosed()) pstmt.close();
			if (conn != null && !conn.isClosed()) conn.close();
		}

	}

	@Override
	public Book findById(int id) throws SQLException {
		logger.debug("Consultar com id: " + id);
		Book p = null;
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
			if (pstmt != null && !pstmt.isClosed()) pstmt.close();
			if (conn != null && !conn.isClosed()) conn.close();
		}
	}

	@Override
	public List<Book> findByFields(String titulo, String autor, String isbn, String classificacao, String publico) throws SQLException {
		logger.debug("Consultando com parametros: " + titulo + " , " + autor + ", " + isbn + " , " + classificacao + " , " + publico);
		List<Book> lista = new ArrayList<>();
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
				Book l = carregarObjeto(rs);
				lista.add(l);
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
	public List<Book> findAll() throws SQLException {
		logger.debug("Consultando todos 'livros'");
		List<Book> lista = new ArrayList<>();
		try {			
			conn = DriverManager.getConnection(URL_DB);
			StringBuilder sql = new StringBuilder("SELECT * FROM LIVROS");
			pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Book l = carregarObjeto(rs);
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
	public Book findByIsbn(String isbn) throws SQLException {
		logger.debug("Consultar com isbn: " + isbn);
		Book p = null;
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
			if (pstmt != null && !pstmt.isClosed()) pstmt.close();
			if (conn != null && !conn.isClosed()) conn.close();
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
	public List<Book> findByAuthorNameLike(String autor) throws SQLException {
		logger.debug("Consultando por autor aproximado: " + autor);
		List<Book> lista = new ArrayList<>();
		try {
			conn = DriverManager.getConnection(URL_DB);
			String sql = "SELECT * FROM LIVROS WHERE AUTOR LIKE ?";
			pstmt = conn.prepareStatement(sql);			
			pstmt.setString(1, "%" + autor.toUpperCase() + "%");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Book l = carregarObjeto(rs);
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
	public List<Book> findByTitleLike(String titulo) throws SQLException {
		logger.debug("Consultando por titulo aproximado: " + titulo);
		List<Book> lista = new ArrayList<>();
		try {
			conn = DriverManager.getConnection(URL_DB);
			String sql = "SELECT * FROM LIVROS WHERE TITULO LIKE ?";
			pstmt = conn.prepareStatement(sql);			
			pstmt.setString(1, "%" + titulo.toUpperCase() + "%");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Book l = carregarObjeto(rs);
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
