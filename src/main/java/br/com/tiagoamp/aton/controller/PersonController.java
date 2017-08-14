package br.com.tiagoamp.aton.controller;

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

import br.com.tiagoamp.aton.model.AtonBOException;
import br.com.tiagoamp.aton.model.MessaType;
import br.com.tiagoamp.aton.model.Person;
import br.com.tiagoamp.aton.model.Role;
import br.com.tiagoamp.aton.model.to.MessageTO;
import br.com.tiagoamp.aton.service.PersonService;

@Controller
@RequestMapping("pessoas")
public class PersonController {
	
	public PersonController() {
		personService = new PersonService();
	}
	
	Logger logger = Logger.getLogger(PersonController.class);
	
	private PersonService personService;
	
		
	@RequestMapping(value="cadastro", method=RequestMethod.GET)
	public String cadastrarPessoa(HttpServletRequest request,  
	        @RequestParam(value="acao", required=false) String pAcao, 
	        @RequestParam(value="identificador", required=false) String pId, 
	        Model model) {
		
		Person person = new Person();
		
		if (pId != null && !pId.isEmpty()) {
			try {		
				person = personService.findById(Integer.parseInt(pId));				 				
			} catch (AtonBOException e) {
				logger.error("Erro: " + e);
				model.addAttribute("mensagem",new MessageTO(e.getBusinessMessage(), MessaType.ERRO));
				return "pessoas";
			}
			model.addAttribute("acao",pAcao);
		}
		
		if (pAcao != null && pAcao.equals("excluir")) {
			model.addAttribute("mensagem",new MessageTO("Confirma exclusão com os dados abaixo?", MessaType.ALERTA));
		}
				
		model.addAttribute("person", person);
	    return "pessoas/cadastro";
	}
	
	@RequestMapping(value="cadastro", method=RequestMethod.POST)
	public String salvarPessoa(@Valid Person person, BindingResult result, Model model) {
		boolean hasErrors = false;
		if(result.hasErrors()) {
			hasErrors = true;
		}
		if (person.getRole() != null && person.getRole() != Role.READER && person.getPassword().equals("")) {
			result.reject("senha", "Senha deve ser preenchida para perfil 'Administrador' ou 'Bibliotecário'.");
			hasErrors = true;
		}
		if (hasErrors) {
			person.setRole(null);
			model.addAttribute("person", person);
		} else {
			try {
				// digest da senha
				if (person.getPassword() != null && !person.getPassword().isEmpty()) {
					person.setPassword(DigestUtils.sha1Hex(person.getPassword()));
				}
				// gravando pessoas
				if (person.getId() == null) {
					personService.insert(person); // insert
				} else {
					personService.update(person); // update
				}
				model.addAttribute("mensagem",new MessageTO("Gravação com sucesso: " + person.toString(), MessaType.SUCESSO));
				model.addAttribute("acao", "consultar");
				logger.info("Pessoa cadastrada: " + person);
			} catch (AtonBOException e) {
				logger.error("Erro: " + e);
				model.addAttribute("person", person);
				model.addAttribute("mensagem",new MessageTO(e.getBusinessMessage(), MessaType.ERRO));
			}
		}					
		
		return "pessoas/cadastro";		
	}
	
	
	
	
	
	@RequestMapping("listapessoas")
	public String listarPessoas(HttpServletRequest request, Model model) {
		List<Person> lista = new ArrayList<>();
		
		try {		
			lista = personService.getAll();
			if (lista.isEmpty()) {
				throw new AtonBOException("Consulta sem resultados!");
			}
			Set<Person> listaOrdenada = new TreeSet<>();
			listaOrdenada.addAll(lista);
			model.addAttribute("listapessoas", listaOrdenada);
		} catch (AtonBOException e) {
			logger.error("Erro: " + e);
			model.addAttribute("mensagem",new MessageTO(e.getBusinessMessage(), MessaType.ERRO));
			return "pessoas";
		}
		
	    return "pessoas";
	}
	
	@RequestMapping("exclusaopessoa")
	public String excluirPessoa(HttpServletRequest request,  
	        @RequestParam(value="acao", required=false) String pAcao, 
	        @RequestParam(value="identificador", required=false) String pId, 
	        Model model) {		
		Person pessoa;
		try {		
			pessoa = personService.findById(Integer.parseInt(pId));
			if (pessoa == null) {
				throw new AtonBOException("Erro na exclusão de pessoa: Identificador inválido!");
			}
			personService.delete(pessoa.getId());
			model.addAttribute("mensagem",new MessageTO("Exclusão com sucesso: " + pessoa.toString(), MessaType.SUCESSO));
			logger.info("Pessoa excluída: " + pessoa);
		} catch (AtonBOException e) {
			logger.error("Erro: " + e);
			model.addAttribute("mensagem",new MessageTO(e.getBusinessMessage(), MessaType.ERRO));			
		}		
		return "pessoas";
	}
	
	
	@RequestMapping(value = "consultapessoa", method = RequestMethod.POST)
	public String consultarPessoa(HttpServletRequest request,  
	        @RequestParam(value="tEmail", required=false) String pEmail, 
	        @RequestParam(value="tDados", required=false) String pDados, 
	        Model model){
		List<Person> lista = new ArrayList<>();
		try {
			lista = this.pesquisarPessoasPorParametros(pEmail, pDados);
			if (lista.isEmpty()) {
				throw new AtonBOException("Consulta sem resultados!");
			}
			model.addAttribute("listapessoas", lista);
		} catch (AtonBOException e) {
			logger.error("Erro: " + e);
			model.addAttribute("mensagem",new MessageTO(e.getBusinessMessage(), MessaType.ERRO));
		}
		return "pessoas";		
	}
		
	private List<Person> pesquisarPessoasPorParametros(String email, String param) throws AtonBOException {
		List<Person> lista = new ArrayList<>();
		if (email != null && !email.isEmpty()) { // CAMPO DE PESQ EMAIL PREENCHIDO
			// pesquisa por e-mail
			Person p = personService.findByEmail(email.trim().toUpperCase());
			if (p != null)
				lista.add(p);
		} else { // CAMPO DE PESQ DADOS PREENCHIDO
			if (param != null && !param.isEmpty()) {
				param = param.trim().toUpperCase();
				// pesquisa por perfil
				for (Role perfil : Role.values()) {
					if (perfil.toString().equals(param))
						lista = personService.findByFields(null, null, Role.valueOf(param));
				}
				if (lista.isEmpty()) {
					// pesquisa por telefone
					lista = personService.findByFields(null, param, null);
					if (lista.isEmpty()) {
						// pesquisa por nome aproximado
						lista = personService.findByName(param);
					}
				}
			}
		}
		return lista;
	}
				
}
