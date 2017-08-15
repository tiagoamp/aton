package br.com.tiagoamp.aton.service.interceptor;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import br.com.tiagoamp.aton.model.MessaType;
import br.com.tiagoamp.aton.model.User;
import br.com.tiagoamp.aton.model.to.AuthorizerResultTO;
import br.com.tiagoamp.aton.model.to.MessageTO;
import br.com.tiagoamp.aton.service.AtonFeaturesAuthorizer;

public class AtonAuthorizerInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object controller) throws Exception {
		String uri = request.getRequestURI();
		User user = (User) request.getSession().getAttribute("usuario");
		
		// PAGES QUE PRECISAM DE AUTENTICAÇÃO
		if ( uri.endsWith("pessoas") || uri.endsWith("livrocadastrado") || uri.endsWith("emprestimos") || uri.endsWith("emprestimolivro") ) { 
			if (user == null) {
				response.sendRedirect("autorizacao");				
				return false;
			}
		} else if (uri.endsWith("livros/cadastro")) { // MANUT LIVROS
			if (user == null) {
				response.sendRedirect("../autorizacao");				
				return false;
			}
			AuthorizerResultTO autTO = AtonFeaturesAuthorizer.authorizeBook(user, request.getParameter("acao"));
			if (autTO != null) {
				if (autTO.getMsgErro() != null) request.setAttribute("mensagem", new MessageTO(autTO.getMsgErro(), MessaType.ERRO));
				RequestDispatcher rd = request.getRequestDispatcher(autTO.getUrlRedirect());
				rd.forward(request, response);
				return false;
			}
		} else if (uri.contains("pessoas/cadastro")) { // MANUT PESSOAS
			if (user == null) {
				response.sendRedirect("../autorizacao");				
				return false;
			}
			AuthorizerResultTO autTO = AtonFeaturesAuthorizer.authorizePerson(user, request.getParameter("acao"));
			if (autTO != null) {
				if (autTO.getMsgErro() != null) request.setAttribute("mensagem", new MessageTO(autTO.getMsgErro(), MessaType.ERRO));
				RequestDispatcher rd = request.getRequestDispatcher(autTO.getUrlRedirect());
				rd.forward(request, response);
				return false;
			}
		}
    	return true;			
	}
	
}

