package br.com.tiagoamp.aton.service;

import br.com.tiagoamp.aton.model.Perfil;
import br.com.tiagoamp.aton.model.Pessoa;
import br.com.tiagoamp.aton.model.to.AutorizacaoResultadoTO;

/**
 * Classe responsável por agrupar regras de autorizações 
 * do sistema por funcionalidade.
 * 
 * @author tiagoamp
 */
public class AutorizacaoFuncionalidades {

	/**
	 * Regras de autorização para Livros.
	 * 
	 * @param usuario
	 * @param acao
	 * @return AutorizacaoResultadoTO
	 */
	public static AutorizacaoResultadoTO autorizarManutencaoLivros(Pessoa usuario, String acao) {
		AutorizacaoResultadoTO to = null;
		if (acao == null || acao.equals("alterar") || acao.equals("excluir")) { // ADMIN
			if (usuario == null) {
				to = new AutorizacaoResultadoTO("autorizacao", null);
			} else {
				if (usuario.getPerfil() != Perfil.ADMINISTRADOR) {
					to = new AutorizacaoResultadoTO("livros", "Ação autorizada somente para perfil 'Administrador'.");
				}
			}
		} else { // BIBLIOTECARIO  
			if (acao.equals("emprestar") && usuario != null) {
				if (usuario.getPerfil() != Perfil.ADMINISTRADOR || usuario.getPerfil() != Perfil.BIBLIOTECARIO) {
					to = new AutorizacaoResultadoTO("livros", "Ação autorizada somente para perfil 'Bibliotecário'.");
				}
			}
		}
		return to;
	}
	
	/**
	 * Regras de autorização para Pessoas.
	 * 
	 * @param usuario
	 * @param acao
	 * @return AutorizacaoResultadoTO
	 */
	public static AutorizacaoResultadoTO autorizarManutencaoPessoas(Pessoa usuario, String acao) {
		AutorizacaoResultadoTO to = null;
		if (acao == null || acao.equals("alterar") || acao.equals("excluir")) { // ADMIN
			if (usuario == null) {
				to = new AutorizacaoResultadoTO("autorizacao", null);
			} else {
				if (usuario.getPerfil() != Perfil.ADMINISTRADOR) {
					to = new AutorizacaoResultadoTO("pessoas", "Ação autorizada somente para perfil 'Administrador'.");
				}
			}
		} 
		return to;
	}
}
