package br.com.tiagoamp.aton.service;

import br.com.tiagoamp.aton.model.Role;
import br.com.tiagoamp.aton.model.User;
import br.com.tiagoamp.aton.model.to.AuthorizerResultTO;

/**
 * Classe responsável por agrupar regras de autorizações 
 * do sistema por funcionalidade.
 * 
 * @author tiagoamp
 */
public class AtonFeaturesAuthorizer {

	/**
	 * Regras de autorização para Livros.
	 * 
	 * @param user
	 * @param action
	 * @return AutorizacaoResultadoTO
	 */
	public static AuthorizerResultTO authorizeBook(User user, String action) {
		AuthorizerResultTO to = null;
		if (action == null || action.equals("alterar") || action.equals("excluir") || action.equals("emprestar") ) { // ADMIN
			if (user.getRole() != Role.ADMINISTRADOR && user.getRole() != Role.BIBLIOTECARIO) {
				to = new AuthorizerResultTO("livros", "Ação autorizada somente para perfil 'Administrador' e 'Bibliotecário'.");
			}
		}
		return to;
	}
	
	/**
	 * Authorization rules for 'person'.
	 * 
	 * Only Administrator and Librarian can insert/update/delete/view person records.
	 * 
	 * @param user
	 * @param action
	 * @return AuthorizerResultTO
	 */
	public static AuthorizerResultTO authorizePerson(User user, String action) {
		AuthorizerResultTO to = null;
		if (user.getRole() != Role.ADMINISTRADOR && user.getRole() != Role.BIBLIOTECARIO) {
			to = new AuthorizerResultTO("pessoas", "Ação autorizada somente para perfil 'Administrador' e 'Bibliotecário'.");
		} 
		return to;
	}
}
