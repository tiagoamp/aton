package br.com.tiagoamp.aton;

import br.com.tiagoamp.aton.model.Perfil;
import br.com.tiagoamp.aton.model.Pessoa;

public class TestHelper {
	
	public static Pessoa getPessoaTeste() {
		Pessoa pessoa = new Pessoa("TESTE@TESTEMAIL.COM", "NOME DE TESTE", "11-1111-1111", Perfil.ADMINISTRADOR);
		pessoa.setSenha("123");
		return pessoa;
	}
	
	/*public static Biblioteca getBibliotecaTeste() {
		return new Biblioteca("Nome Biblioteca", "uri do simbolo");
	}
	
	public static Livro getLivroTeste() {
		Livro livro = new Livro();
		livro.setAnoPublicacao(2016);
		livro.setAutor("Autor de Teste");
		livro.setClassificacao("Classificacao de Teste");
		livro.setDataAquisicao(new Date());
		livro.setDataCadastro(new Date());
		livro.setEditora("Editora de Teste");
		livro.setGenero("Genero de Teste");
		Pessoa pessoa = TestHelper.getPessoaTeste();
		pessoa.setId(1);
		livro.setPessoaCadastradora(pessoa);
		livro.setIsbn("ISBN");
		livro.setLocalPublicacao("Local de Teste");
		livro.setNomeDoador("Doador de Teste");
		livro.setNroPaginas(100);
		livro.setPathFotoCapa("path/to/arquivo");
		livro.setPublicoAlvo("Publico de Teste");
		livro.setSituacao(Situacao.DISPONIVEL);
		livro.setSubtitulo("Subtitulo de Teste");
		livro.setTipoAquisicao(TipoAquisicao.DOACAO);
		livro.setTitulo("Titulo de Teste");
		return livro;
	}
	
	public static Emprestimo getEmprestimoTeste() {
		Emprestimo emp = new Emprestimo();
		Livro livro = TestHelper.getLivroTeste();
		livro.setId(1);
		emp.setLivro(livro);
		Pessoa pessoa = TestHelper.getPessoaTeste();
		pessoa.setId(1);
		emp.setPessoa(pessoa);
		emp.setDataEmprestimo(new Date());
		return emp;
	}*/

}
