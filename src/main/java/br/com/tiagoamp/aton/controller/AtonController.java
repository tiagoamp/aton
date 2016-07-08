package br.com.tiagoamp.aton.controller;

import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import br.com.tiagoamp.aton.model.BibException;
import br.com.tiagoamp.aton.model.Livro;
import br.com.tiagoamp.aton.model.Perfil;
import br.com.tiagoamp.aton.model.Pessoa;
import br.com.tiagoamp.aton.model.TipoMensagem;
import br.com.tiagoamp.aton.model.to.MensagemTO;
import br.com.tiagoamp.aton.service.AtonService;

@Controller
public class AtonController {
	
	Logger logger = Logger.getLogger(AtonController.class);
	
	AtonService service = new AtonService();
	
	@RequestMapping("/aton")
	public String pageInicial() {
	    return "aton";
	}
	
	@RequestMapping("/pessoas")
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
			} catch (BibException e) {
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
				throw new BibException("Consulta sem resultados!");
			}
			Set<Pessoa> listaOrdenada = new TreeSet<>();
			listaOrdenada.addAll(lista);
			model.addAttribute("listapessoas", listaOrdenada);
		} catch (BibException e) {
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
				throw new BibException("Erro na exclusão de pessoa: Identificador inválido!");
			}
			service.apagarPessoa(pessoa.getId());
			model.addAttribute("mensagem",new MensagemTO("Exclusão com sucesso: " + pessoa.toString(), TipoMensagem.SUCESSO));
			logger.info("Pessoa excluída: " + pessoa);
		} catch (BibException e) {
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
		} catch (BibException e) {
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
			if (pEmail != null && !pEmail.isEmpty()) { // CAMPO DE PESQ EMAIL PREENCHIDO				
							// pesquisa por e-mail
				Pessoa p = service.consultarPessoa(pEmail.trim().toUpperCase());
				if (p != null) lista.add(p);
			} else {								   // CAMPO DE PESQ DADOS PREENCHIDO
				if (pDados != null && !pDados.isEmpty()) { 
					pDados = pDados.trim().toUpperCase();
							// pesquisa por perfil
					for (Perfil perfil : Perfil.values()) {
						if (perfil.toString().equals(pDados)) lista = service.consultarPessoas(null, null, Perfil.valueOf(pDados));
					}
					if (lista.isEmpty()) {
							// pesquisa por telefone
						lista = service.consultarPessoas(null, pDados, null);
						if (lista.isEmpty()) {
							// pesquisa por nome aproximado	
							lista = service.consultarPessoasPorNomeAproximado(pDados);
						}
					}				
				}
			}
			if (lista.isEmpty()) {
				throw new BibException("Consulta sem resultados!");
			}
			model.addAttribute("listapessoas", lista);
		} catch (BibException e) {
			logger.error("Erro: " + e);
			model.addAttribute("mensagem",new MensagemTO(e.getMsg(), TipoMensagem.ERRO));
			return "pessoas";
		}		
		return "pessoas";		
	}
	
	@RequestMapping("/livros")
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
			} catch (BibException e) {
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
	
	
	/*@RequestMapping(value="uploadFile", method = RequestMethod.POST)
	public String uploadCapaLivro(HttpServletRequest request, Livro livro, BindingResult result,
	        @RequestParam(value="file", required=false) MultipartFile pFile, Model model) throws Exception {
		
		MultipartFile multipartFile = (MultipartFile) pFile;
		
		String fileName="";
		
		
		if(multipartFile!=null){
			fileName = multipartFile.getOriginalFilename();
			//do whatever you want
		}
		
		model.addAttribute("livro", livro);
		return "livros/cadastro";
	}*/
	
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
		} catch (BibException e) {
			logger.error("Erro: " + e);
			model.addAttribute("livro", livro);
			model.addAttribute("mensagem",new MensagemTO(e.getMsg(), TipoMensagem.ERRO));
			return "livros/cadastro";
		}		
		return "livros";
	}
	
}
