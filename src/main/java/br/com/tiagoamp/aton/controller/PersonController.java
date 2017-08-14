package br.com.tiagoamp.aton.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
	
	@RequestMapping(method = RequestMethod.POST)
	public String pesquisarPessoas(HttpServletRequest request,  
	        @RequestParam(value="tEmail", required=false) String pEmail, 
	        @RequestParam(value="tFields", required=false) String pFields,
	        @RequestParam(value="tList", required=false) String pList,
	        Model model){
		List<Person> list = new ArrayList<>();
		try {
			
			if (pEmail != null && !pEmail.isEmpty()) {
				Person p = searchByEmail(pEmail);
				if (p != null) list.add(p);
			} else if (pFields != null && !pFields.isEmpty()) {
				list = searchByRoleOrName(pFields);
			} else if (pEmail == null && pFields == null) {
				list = personService.getAll();
			}
			
			if (list.isEmpty()) {
				model.addAttribute("mensagem",new MessageTO("Consulta sem resultados!", MessaType.ERRO));				
			}
			
			Collections.sort(list);
			model.addAttribute("listofpeople", list);			
		} catch (AtonBOException e) {
			logger.error("Erro: " + e);
			model.addAttribute("mensagem",new MessageTO(e.getBusinessMessage(), MessaType.ERRO));
		}
		
		return "pessoas/pessoas";				
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
	
				
	private Person searchByEmail(String email) throws AtonBOException {
		return personService.findByEmail(email.trim().toUpperCase());
	}
	
	private List<Person> searchByRoleOrName(String param) throws AtonBOException {
		List<Person> lista = new ArrayList<>();
		param = param.trim().toUpperCase();
		for (Role role : Role.values()) {  // role
			if (role.toString().equals(param)) {
				lista = personService.findByFields(null, null, Role.valueOf(param));
			}
		}
		if (lista.isEmpty()) {  // name
			lista = personService.findByName(param);
		}
		return lista;
	}
					
}
