package br.com.tiagoamp.aton.service;

import br.com.tiagoamp.aton.model.Role;
import br.com.tiagoamp.aton.model.User;
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
	 * @param action
	 * @return AutorizacaoResultadoTO
	 */
	public static AutorizacaoResultadoTO autorizarManutencaoLivros(User usuario, String action) {
		AutorizacaoResultadoTO to = null;
		if (action == null || action.equals("alterar") || action.equals("excluir")) { // ADMIN
			if (usuario == null) {
				to = new AutorizacaoResultadoTO("autorizacao", null);
			} else {
				if (usuario.getRole() != Role.ADMINISTRATOR) {
					to = new AutorizacaoResultadoTO("livros", "Ação autorizada somente para perfil 'Administrador'.");
				}
			}
		} else { // BIBLIOTECARIO  
			if (action.equals("emprestar") && usuario != null) {
				if (usuario.getRole() != Role.ADMINISTRATOR || usuario.getRole() != Role.LIBRARIAN) {
					to = new AutorizacaoResultadoTO("livros", "Ação autorizada somente para perfil 'Bibliotecário'.");
				}
			}
		}
		return to;
	}
	
	/**
	 * Regras de autorização para Pessoas.
	 * 
	 * @param user
	 * @param action
	 * @return AutorizacaoResultadoTO
	 */
	public static AutorizacaoResultadoTO autorizarManutencaoPessoas(User user, String action) {
		AutorizacaoResultadoTO to = null;
		if (action == null || action.equals("alterar") || action.equals("excluir")) { // ADMIN
			if (user == null) {
				to = new AutorizacaoResultadoTO("autorizacao", null);
			} else {
				if (user.getRole() != Role.ADMINISTRATOR) {
					to = new AutorizacaoResultadoTO("pessoas", "Ação autorizada somente para perfil 'Administrador'.");
				}
			}
		} 
		return to;
	}
}
