package br.com.tiagoamp.aton.controller;

import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import br.com.tiagoamp.aton.model.AtonBOException;
import br.com.tiagoamp.aton.model.Emprestimo;
import br.com.tiagoamp.aton.model.Livro;
import br.com.tiagoamp.aton.model.Perfil;
import br.com.tiagoamp.aton.model.Pessoa;
import br.com.tiagoamp.aton.model.Situacao;
import br.com.tiagoamp.aton.model.TipoMensagem;
import br.com.tiagoamp.aton.model.to.MensagemTO;
import br.com.tiagoamp.aton.service.AtonService;

@Controller
public class AtonController {
	
	Logger logger = Logger.getLogger(AtonController.class);
	
	private AtonService service = new AtonService();
	
	@RequestMapping("/aton")
	public String pageInicial() {
	    return "aton";
	}
	
	@RequestMapping("pessoas")
	public String pagePessoas() {
	    return "pessoas";
	}
	
	@RequestMapping("cadastropessoa")
	public String cadastrarPessoa(HttpServletRequest request,  
	        @RequestParam(value="acao", required=false) String pAcao, 
	        @RequestParam(value="identificador", required=false) String pId, 
	        Model model) {
		
		Pessoa pessoa = new Pessoa();
		
		if (pId != null && !pId.isEmpty()) {
			try {		
				pessoa = service.consultarPessoa(Integer.parseInt(pId));				 				
			} catch (AtonBOException e) {
				logger.error("Erro: " + e);
				model.addAttribute("mensagem",new MensagemTO(e.getMsg(), TipoMensagem.ERRO));
				return "pessoas";
			}
			model.addAttribute("acao",pAcao);
		}
		
		if (pAcao != null && pAcao.equals("excluir")) {
			model.addAttribute("mensagem",new MensagemTO("Confirma exclusão com os dados abaixo?", TipoMensagem.ALERTA));
		}
				
		model.addAttribute("pessoa", pessoa);
	    return "pessoas/cadastro";
	}
	
	@RequestMapping("listapessoas")
	public String listarPessoas(HttpServletRequest request, Model model) {
		List<Pessoa> lista = new ArrayList<>();
		
		try {		
			lista = service.consultarPessoas();
			if (lista.isEmpty()) {
				throw new AtonBOException("Consulta sem resultados!");
			}
			Set<Pessoa> listaOrdenada = new TreeSet<>();
			listaOrdenada.addAll(lista);
			model.addAttribute("listapessoas", listaOrdenada);
		} catch (AtonBOException e) {
			logger.error("Erro: " + e);
			model.addAttribute("mensagem",new MensagemTO(e.getMsg(), TipoMensagem.ERRO));
			return "pessoas";
		}
		
	    return "pessoas";
	}
	
	@RequestMapping("exclusaopessoa")
	public String excluirPessoa(HttpServletRequest request,  
	        @RequestParam(value="acao", required=false) String pAcao, 
	        @RequestParam(value="identificador", required=false) String pId, 
	        Model model) {		
		Pessoa pessoa;
		try {		
			pessoa = service.consultarPessoa(Integer.parseInt(pId));
			if (pessoa == null) {
				throw new AtonBOException("Erro na exclusão de pessoa: Identificador inválido!");
			}
			service.apagarPessoa(pessoa.getId());
			model.addAttribute("mensagem",new MensagemTO("Exclusão com sucesso: " + pessoa.toString(), TipoMensagem.SUCESSO));
			logger.info("Pessoa excluída: " + pessoa);
		} catch (AtonBOException e) {
			logger.error("Erro: " + e);
			model.addAttribute("mensagem",new MensagemTO(e.getMsg(), TipoMensagem.ERRO));			
		}		
		return "pessoas";
	}
	
	@RequestMapping(value="pessoacadastrada", method = RequestMethod.POST)
	public String salvarPessoa(@Valid Pessoa pessoa, BindingResult result, Model model) {
		boolean hasErrors = false;
		if(result.hasErrors()) {
			hasErrors = true;
		}
		if (pessoa.getPerfil() != null && pessoa.getPerfil() != Perfil.LEITOR && pessoa.getSenha().equals("")) {
			result.reject("senha", "Senha deve ser preenchida para perfil 'Administrador' ou 'Bibliotecário'.");
			hasErrors = true;
		}
		if (hasErrors) {
			pessoa.setPerfil(null);
			model.addAttribute("pessoa", pessoa);
			return "pessoas/cadastro";
		}
		
		try {
			// digest da senha
			if (pessoa.getSenha() != null && !pessoa.getSenha().isEmpty()) {
				pessoa.setSenha(DigestUtils.sha1Hex(pessoa.getSenha()));
			}
			// gravando pessoas
			if (pessoa.getId() == null) {
				service.inserirPessoa(pessoa); // insert
			} else {
				service.atualizarPessoa(pessoa); // update
			}
			model.addAttribute("mensagem",new MensagemTO("Gravação com sucesso: " + pessoa.toString(), TipoMensagem.SUCESSO));
			logger.info("Pessoa cadastrada: " + pessoa);
		} catch (AtonBOException e) {
			logger.error("Erro: " + e);
			model.addAttribute("pessoa", pessoa);
			model.addAttribute("mensagem",new MensagemTO(e.getMsg(), TipoMensagem.ERRO));
			return "pessoas/cadastro";
		}			
		
		return "pessoas";
	}
		
	@RequestMapping(value = "consultapessoa", method = RequestMethod.POST)
	public String consultarPessoa(HttpServletRequest request,  
	        @RequestParam(value="tEmail", required=false) String pEmail, 
	        @RequestParam(value="tDados", required=false) String pDados, 
	        Model model){
		List<Pessoa> lista = new ArrayList<>();
		try {
			lista = this.pesquisarPessoasPorParametros(pEmail, pDados);
			if (lista.isEmpty()) {
				throw new AtonBOException("Consulta sem resultados!");
			}
			model.addAttribute("listapessoas", lista);
		} catch (AtonBOException e) {
			logger.error("Erro: " + e);
			model.addAttribute("mensagem",new MensagemTO(e.getMsg(), TipoMensagem.ERRO));
		}
		return "pessoas";		
	}
		
	@RequestMapping("livros")
	public String pageLivros() {
	    return "livros";
	}
	
	@RequestMapping("cadastrolivro")
	public String cadastrarLivro(HttpServletRequest request,  
	        @RequestParam(value="acao", required=false) String pAcao, 
	        @RequestParam(value="identificador", required=false) String pId, 
	        Model model) {
		
		Livro livro = new Livro();
				
		if (pId != null && !pId.isEmpty()) {
			try {		
				livro = service.consultarLivro(Integer.parseInt(pId));				 				
			} catch (AtonBOException e) {
				logger.error("Erro: " + e);
				model.addAttribute("mensagem",new MensagemTO(e.getMsg(), TipoMensagem.ERRO));
				return "livros";
			}
			model.addAttribute("acao",pAcao);
		}
		
		if (pAcao != null && pAcao.equals("excluir")) {
			model.addAttribute("mensagem",new MensagemTO("Confirma exclusão com os dados abaixo?", TipoMensagem.ALERTA));
		}
				
		model.addAttribute("livro", livro);
	    return "livros/cadastro";
	}
	
	@RequestMapping(value = "consultalivro", method = RequestMethod.POST)
	public String consultarLivro(HttpServletRequest request,  
	        @RequestParam(value="tISBN", required=false) String pISBN, 
	        @RequestParam(value="tDados", required=false) String pDados, 
	        Model model){
		List<Livro> lista = new ArrayList<>();
		try {
			if (pISBN != null && !pISBN.isEmpty()) { // CAMPO DE PESQ ISBN PREENCHIDO
				// pesquisa por isbn
				Livro l = service.consultarLivroPorIsbn(pISBN.trim().toUpperCase());
				if (l != null)
					lista.add(l);
			} else { // CAMPO DE PESQ DADOS PREENCHIDO
				if (pDados != null && !pDados.isEmpty()) {
					pDados = pDados.trim().toUpperCase();
					// pesquisa por titulo aproximado
					lista = service.consultarLivrosPorTituloAproximado(pDados);
					if (lista.isEmpty()) {
						// pesquisa por autor aproximado
						lista = service.consultarLivrosPorAutorAproximado(pDados);						
					}
				}
			}
			if (lista.isEmpty()) {
				throw new AtonBOException("Consulta sem resultados!");
			}
			model.addAttribute("listalivros", lista);
		} catch (AtonBOException e) {
			logger.error("Erro: " + e);
			model.addAttribute("mensagem",new MensagemTO(e.getMsg(), TipoMensagem.ERRO));
		}
		return "livros";		
	}
		
	@RequestMapping(value="livrocadastrado", method = RequestMethod.POST)
	public String salvarLivro(@Valid Livro livro, BindingResult result, Model model, 
			HttpServletRequest request, @RequestParam(value="file", required=false) MultipartFile pFile) {
		boolean hasErrors = false;
		if(result.hasErrors()) {
			hasErrors = true;
		}
		if (!livro.getDataAquisicaoFormatada().isEmpty()) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			try {
				livro.setDataAquisicao(sdf.parse(livro.getDataAquisicaoFormatada()));
			} catch (ParseException e) {
				result.reject("dataAquisicao", "Data de Aquisição em formato inválido.");
				hasErrors = true;
			}
		} else {
			result.reject("dataAquisicaoFormatada", "Campo obrigatório não preenchido: Data de Aquisição.");
			hasErrors = true;
		}
		if (hasErrors) {
			livro.setTipoAquisicao(null);
			model.addAttribute("livro", livro);
			return "livros/cadastro";
		}
		
		MultipartFile mFile = pFile;
		try {			
			if (!mFile.isEmpty()) {  // capa do livro
				Path path = service.inserirFotoCapaLivro(mFile, livro.getIsbn());
				livro.setPathFotoCapa(path);
			}			
			if (livro.getId() == null) {
				service.inserirLivro(livro); // insert				
			} else {
				service.atualizarLivro(livro); // update
			}
			model.addAttribute("mensagem",new MensagemTO("Gravação com sucesso: " + livro.toString(), TipoMensagem.SUCESSO));
			logger.info("Livro cadastrado: " + livro);
		} catch (AtonBOException e) {
			logger.error("Erro: " + e);
			model.addAttribute("livro", livro);
			model.addAttribute("mensagem",new MensagemTO(e.getMsg(), TipoMensagem.ERRO));
			return "livros/cadastro";
		}		
		return "livros";
	}
	
	@RequestMapping("listalivros")
	public String listarLivros(HttpServletRequest request, Model model) {
		List<Livro> lista = new ArrayList<>();
		try {		
			lista = service.consultarLivros();
			if (lista.isEmpty()) {
				throw new AtonBOException("Consulta sem resultados!");
			}
			Set<Livro> listaOrdenada = new TreeSet<>();
			listaOrdenada.addAll(lista);
			model.addAttribute("listalivros", listaOrdenada);
		} catch (AtonBOException e) {
			logger.error("Erro: " + e);
			model.addAttribute("mensagem",new MensagemTO(e.getMsg(), TipoMensagem.ERRO));
			return "livros";
		}		
	    return "livros";
	}
	
	@RequestMapping("emprestimos")
	public String pageEmprestimos() {
	    return "emprestimos";
	}

	@RequestMapping("emprestimolivro")
	public String emprestarLivro(HttpServletRequest request,  
	        @RequestParam(value="acao", required=false) String pAcao, 
	        @RequestParam(value="identificador", required=false) String pId, 
	        Model model) {		
		Livro livro = new Livro();				
		if (pId != null && !pId.isEmpty()) {
			try {		
				livro = service.consultarLivro(Integer.parseInt(pId));				 				
			} catch (AtonBOException e) {
				logger.error("Erro: " + e);
				model.addAttribute("mensagem",new MensagemTO(e.getMsg(), TipoMensagem.ERRO));
				return "livros";
			}
			model.addAttribute("acao",pAcao);
		}		
		Emprestimo emprestimo = new Emprestimo(livro, new Pessoa(), new Date(), null, null);
		model.addAttribute("emprestimo", emprestimo);
		return "emprestimos/emprestimolivro";
	}
	
	@RequestMapping(value = "consultapessoaemprestimo", method = RequestMethod.POST)
	public String consultarPessoaParaEmprestimo(HttpServletRequest request,  
	        @RequestParam(value="tEmail", required=false) String pEmail, 
	        @RequestParam(value="tDados", required=false) String pDados,
	        @RequestParam(value="tIdLivro", required=false) String pIdLivro,
	        Model model){
		List<Pessoa> lista = new ArrayList<>();
		Livro livro = null;
		try {
			livro = service.consultarLivro(Integer.parseInt(pIdLivro)); // recarregando livro
			lista = this.pesquisarPessoasPorParametros(pEmail, pDados); 
			if (lista.isEmpty()) {
				throw new AtonBOException("Consulta sem resultados!");
			}
			model.addAttribute("listapessoas", lista);
		} catch (AtonBOException e) {
			logger.error("Erro: " + e);
			model.addAttribute("mensagem",new MensagemTO(e.getMsg(), TipoMensagem.ERRO));
		}				
		Emprestimo emprestimo = new Emprestimo(livro, new Pessoa(), new Date(), null, null);
		model.addAttribute("emprestimo", emprestimo);
		return "emprestimos/emprestimolivro";
	}
	
	@RequestMapping(value = "emprestimoselecionarpessoa", method = RequestMethod.POST)
	public String selecionarPessoaParaEmprestimo(HttpServletRequest request,  
	        @RequestParam(value="acao", required=false) String pAcao, 
	        @RequestParam(value="identificador", required=false) String pId,
	        @RequestParam(value="idLivro", required=false) String pIdLivro,
	        Model model){
		Livro livro = null;
		Pessoa pessoa = null;
		try {
			livro = service.consultarLivro(Integer.parseInt(pIdLivro)); 
			pessoa = service.consultarPessoa(Integer.parseInt(pId)); 			
		} catch (AtonBOException e) {
			logger.error("Erro: " + e);
			model.addAttribute("mensagem",new MensagemTO(e.getMsg(), TipoMensagem.ERRO));
		}
		// Regra da data sugerida de devolução ==> D + 10
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, 10);
		
		Emprestimo emprestimo = new Emprestimo(livro, pessoa, new Date(), new Date(calendar.getTimeInMillis()), null);
		model.addAttribute("emprestimo", emprestimo);
		return "emprestimos/emprestimolivro";	
	}
	
	private List<Pessoa> pesquisarPessoasPorParametros(String email, String param) throws AtonBOException {
		List<Pessoa> lista = new ArrayList<>();
		if (email != null && !email.isEmpty()) { // CAMPO DE PESQ EMAIL PREENCHIDO
			// pesquisa por e-mail
			Pessoa p = service.consultarPessoaPorEmail(email.trim().toUpperCase());
			if (p != null)
				lista.add(p);
		} else { // CAMPO DE PESQ DADOS PREENCHIDO
			if (param != null && !param.isEmpty()) {
				param = param.trim().toUpperCase();
				// pesquisa por perfil
				for (Perfil perfil : Perfil.values()) {
					if (perfil.toString().equals(param))
						lista = service.consultarPessoas(null, null, Perfil.valueOf(param));
				}
				if (lista.isEmpty()) {
					// pesquisa por telefone
					lista = service.consultarPessoas(null, param, null);
					if (lista.isEmpty()) {
						// pesquisa por nome aproximado
						lista = service.consultarPessoasPorNomeAproximado(param);
					}
				}
			}
		}
		return lista;
	}
	
	@RequestMapping(value="livroemprestado", method = RequestMethod.POST)
	public String emprestarLivro(@Valid Emprestimo emprestimo, BindingResult result, Model model, HttpServletRequest request) {
		boolean hasErrors = false;
		if(result.hasErrors()) {
			hasErrors = true;
		}
		try { // recuperando livro e pessoa
			emprestimo.setLivro(service.consultarLivro(emprestimo.getLivro().getId()));
			emprestimo.setPessoa(service.consultarPessoa(emprestimo.getPessoa().getId()));			
		} catch (AtonBOException e) {
			logger.error("Erro: " + e);
			model.addAttribute("mensagem",new MensagemTO(e.getMsg(), TipoMensagem.ERRO));
		}
		
		if (emprestimo.getPessoa() == null || emprestimo.getPessoa().getId() == null) {
			result.reject("pessoa.nome", "Pessoa não selecionada para fazer empréstimo do livro.");
			hasErrors = true;
		}
		if (emprestimo.getDataEmprestimoFormatada().isEmpty()) {
			result.reject("dataEmprestimoFormatada", "Campo obrigatório não preenchido: Data de Empresstimo.");
			hasErrors = true;
		} else if (emprestimo.getDataDevolucaoProgramadaFormatada().isEmpty()) {
			result.reject("dataDevolucaoProgramadaFormatada", "Campo obrigatório não preenchido: Data de Devolução.");
			hasErrors = true;
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			try {
				emprestimo.setDataEmprestimo(sdf.parse(emprestimo.getDataEmprestimoFormatada()));
				emprestimo.setDataDevolucaoProgramada(sdf.parse(emprestimo.getDataDevolucaoProgramadaFormatada()));
			} catch (ParseException e) {
				result.reject("dataEmprestimoFormatada", "Data de Empréstimo ou Devolução em formato inválido.");
				hasErrors = true;
			}
		} 
		if (hasErrors) {
			model.addAttribute("emprestimo", emprestimo);
			return "emprestimos/emprestimolivro";
		}
		
		try {			
			service.inserirEmprestimo(emprestimo);
			Livro livro = emprestimo.getLivro();
			livro.setSituacao(Situacao.EMPRESTADO);
			service.atualizarLivro(livro);			
			model.addAttribute("mensagem",new MensagemTO("Gravação com sucesso: " + emprestimo.toString(), TipoMensagem.SUCESSO));
			logger.info("Emprestimo cadastrado: " + emprestimo);
		} catch (AtonBOException e) {
			logger.error("Erro: " + e);
			model.addAttribute("emprestimo", emprestimo);
			model.addAttribute("mensagem",new MensagemTO(e.getMsg(), TipoMensagem.ERRO));
			return "emprestimos/emprestimolivro";
		}	
		return "livros";
	}
	
	@RequestMapping(value = "consultaemprestimo", method = RequestMethod.POST)
	public String consultarEmprestimo(HttpServletRequest request,  
	        @RequestParam(value="tLivro", required=false) String pLivro, 
	        @RequestParam(value="tPessoa", required=false) String pPessoa, 
	        Model model){
		List<Emprestimo> lista = new ArrayList<>();
		try {
			if (pLivro != null && !pLivro.isEmpty()) { // CAMPO DE PESQ LIVRO PREENCHIDO
				pLivro = pLivro.trim().toUpperCase();
				List<Livro> livros = new ArrayList<>();
				livros.addAll(service.consultarLivrosPorTituloAproximado(pLivro));
				livros.addAll(service.consultarLivrosPorAutorAproximado(pLivro));
				for (int i = 0; i < livros.size(); i++) {
					Livro livro = livros.get(i);
					lista = service.consultarEmprestimos(livro.getId(), null, null, null);
				}
			} else { // CAMPO DE LEITOR(PESSOA) PREENCHIDO
				if (pPessoa != null && !pPessoa.isEmpty()) {
					pPessoa = pPessoa.trim().toUpperCase();
					List<Pessoa> pessoas = new ArrayList<>();
					pessoas.addAll(service.consultarPessoasPorNomeAproximado(pPessoa));
					for (int i = 0; i < pessoas.size(); i++) {
						Pessoa pessoa = pessoas.get(i);
						lista = service.consultarEmprestimos(null, pessoa.getId(), null, null);
					}
				}
			}
			if (lista.isEmpty()) {
				throw new AtonBOException("Consulta sem resultados!");
			}
			Collections.sort(lista);
			model.addAttribute("listaemprestimos", lista);
		} catch (AtonBOException e) {
			logger.error("Erro: " + e);
			model.addAttribute("mensagem",new MensagemTO(e.getMsg(), TipoMensagem.ERRO));
		}
		return "emprestimos";		
	}
	
	@RequestMapping("listaemprestimos")
	public String listarEmprestimos(HttpServletRequest request, Model model) {
		List<Emprestimo> lista = new ArrayList<>();
		try {		
			lista = service.consultarEmprestimos();
			if (lista.isEmpty()) {
				throw new AtonBOException("Consulta sem resultados!");
			}
			Collections.sort(lista);			
			model.addAttribute("listaemprestimos", lista);
		} catch (AtonBOException e) {
			logger.error("Erro: " + e);
			model.addAttribute("mensagem",new MensagemTO(e.getMsg(), TipoMensagem.ERRO));			
		}		
	    return "emprestimos";
	}
	
	@RequestMapping("listaemprestimosabertos")
	public String listarEmprestimosEmAberto(HttpServletRequest request, Model model) {
		List<Emprestimo> lista = new ArrayList<>();
		try {		
			lista = service.consultarEmprestimosEmAberto();
			if (lista.isEmpty()) {
				throw new AtonBOException("Consulta sem resultados!");
			}
			Collections.sort(lista);			
			model.addAttribute("listaemprestimos", lista);
		} catch (AtonBOException e) {
			logger.error("Erro: " + e);
			model.addAttribute("mensagem",new MensagemTO(e.getMsg(), TipoMensagem.ERRO));			
		}		
	    return "emprestimos";
	}
	
	@RequestMapping("devolucaolivro")
	public String DevolverLivro(HttpServletRequest request,  
	        @RequestParam(value="acao", required=false) String pAcao, 
	        @RequestParam(value="identificador", required=false) String pId, 
	        Model model) {		
		Emprestimo emprestimo = null;				
		if (pId != null && !pId.isEmpty()) {
			try {		
				emprestimo = service.consultarEmprestimo(Integer.parseInt(pId));
				emprestimo.setDataDevolucao(new Date());
				service.atualizarEmprestimo(emprestimo);
				Livro livro = emprestimo.getLivro();
				livro.setSituacao(Situacao.DISPONIVEL);
				service.atualizarLivro(livro);
				model.addAttribute("mensagem",new MensagemTO("Devolução com sucesso: " + emprestimo.toString(), TipoMensagem.SUCESSO));
				logger.info("Emprestimo devolvido: " + emprestimo);
			} catch (AtonBOException e) {
				logger.error("Erro: " + e);
				model.addAttribute("mensagem",new MensagemTO(e.getMsg(), TipoMensagem.ERRO));
				return "emprestimos";
			}
			model.addAttribute("acao",pAcao);
		}		
		model.addAttribute("emprestimo", emprestimo);
		return "emprestimos";
	}
	
	@RequestMapping("login")
	public String pageLogin(HttpServletRequest request, Model model) {
		Pessoa pessoa = new Pessoa();
		model.addAttribute("pessoa", pessoa);
	    return "login";
	}
	
}
