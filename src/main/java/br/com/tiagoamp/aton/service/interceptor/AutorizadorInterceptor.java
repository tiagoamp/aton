package br.com.tiagoamp.aton.service.interceptor;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import br.com.tiagoamp.aton.model.Pessoa;
import br.com.tiagoamp.aton.model.TipoMensagem;
import br.com.tiagoamp.aton.model.to.AutorizacaoResultadoTO;
import br.com.tiagoamp.aton.model.to.MensagemTO;
import br.com.tiagoamp.aton.service.AutorizacaoFuncionalidades;

public class AutorizadorInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object controller) throws Exception {
		String uri = request.getRequestURI();
		Pessoa usuario = (Pessoa) request.getSession().getAttribute("usuario");
		
		// PAGES QUE PRECISAM DE AUTENTICAÇÃO
		if ( uri.endsWith("emprestimos") || uri.endsWith("emprestimolivro") || 
				uri.endsWith("pessoas") || uri.endsWith("livrocadastrado") ) { 
			if (usuario == null) {
				response.sendRedirect("autorizacao");				
				return false;
			}
		} else if (uri.endsWith("cadastrolivro")) { // MANUT LIVROS
			AutorizacaoResultadoTO autTO = AutorizacaoFuncionalidades.autorizarManutencaoLivros(usuario, request.getParameter("acao"));
			if (autTO != null) {
				if (autTO.getMsgErro() != null) request.setAttribute("mensagem", new MensagemTO(autTO.getMsgErro(), TipoMensagem.ERRO));
				RequestDispatcher rd = request.getRequestDispatcher(autTO.getUrlRedirect());
				rd.forward(request, response);
				return false;
			}
		} else if (uri.endsWith("cadastropessoa")) { // MANUT PESSOAS
			AutorizacaoResultadoTO autTO = AutorizacaoFuncionalidades.autorizarManutencaoPessoas(usuario, request.getParameter("acao"));
			if (autTO != null) {
				if (autTO.getMsgErro() != null) request.setAttribute("mensagem", new MensagemTO(autTO.getMsgErro(), TipoMensagem.ERRO));
				RequestDispatcher rd = request.getRequestDispatcher(autTO.getUrlRedirect());
				rd.forward(request, response);
				return false;
			}
		}
    	return true;			
	}
	
}

