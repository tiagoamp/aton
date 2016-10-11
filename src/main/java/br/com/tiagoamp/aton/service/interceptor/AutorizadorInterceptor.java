package br.com.tiagoamp.aton.service.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class AutorizadorInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object controller) throws Exception {
		String uri = request.getRequestURI();
		if ( uri.endsWith("emprestimos") || uri.endsWith("emprestimolivro") || 
				uri.endsWith("pessoas") ) { // pages que precisam de autenticacao
			if (request.getSession().getAttribute("usuario") == null) {
				response.sendRedirect("autorizacao");				
				return false;
			}
		}
		return true;			
	}
	
}

