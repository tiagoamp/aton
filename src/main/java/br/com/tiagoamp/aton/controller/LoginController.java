package br.com.tiagoamp.aton.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.tiagoamp.aton.model.AtonBOException;
import br.com.tiagoamp.aton.model.Person;
import br.com.tiagoamp.aton.model.Role;
import br.com.tiagoamp.aton.model.MessaType;
import br.com.tiagoamp.aton.model.to.MessageTO;
import br.com.tiagoamp.aton.service.PersonService;

@Controller
public class LoginController {
	
	public LoginController() {
		personService = new PersonService();
	}
	
	Logger logger = Logger.getLogger(LoginController.class);
	
	private PersonService personService;
	
		
	@RequestMapping("login")
	public String pageLogin(HttpServletRequest request, Model model) {
		Person person = new Person();
		model.addAttribute("person", person);
	    return "login";
	}
	
	@RequestMapping("efetuarlogin")
	public String efetuarLogin(Person person, BindingResult result, Model model, HttpSession session) {
		if (person.getEmail().isEmpty() || person.getPassword().isEmpty()) {
			model.addAttribute("mensagem",new MessageTO("Campos não preenchidos!", MessaType.ERRO));
			return "login";
		}
		Person personFromDB = null;
		try {
			personFromDB = personService.findByEmail(person.getEmail());
			if (personFromDB == null) {
				model.addAttribute("mensagem",new MessageTO("Usuário inexistente!", MessaType.ERRO));
				return "login";
			} else if (personFromDB.getPassword() == null && personFromDB.getPassword().isEmpty()) {
				model.addAttribute("mensagem",new MessageTO("Usuário sem acesso cadastrado.", MessaType.ERRO));
				return "login";
			}
			// verificando credenciais
			person.setPassword(DigestUtils.sha1Hex(person.getPassword()));
			if (personFromDB.getPassword().equals(person.getPassword()) && personFromDB.getRole() != Role.READER) {
				personFromDB.setPassword(null); // null for security reasons, obj will be setted in session
				session.setAttribute("usuario", personFromDB);
				logger.info("Usuario autenticado: " + personFromDB);
			} else {
				model.addAttribute("mensagem",new MessageTO("Credenciais inválidas!", MessaType.ERRO));
				return "login";
			}
		} catch (AtonBOException e) {
			logger.error("Erro: " + e);
			model.addAttribute("mensagem",new MessageTO(e.getBusinessMessage(), MessaType.ERRO));
			return "login";
		}
		model.addAttribute("mensagem",new MessageTO("Usuário autenticado!", MessaType.SUCESSO));
		return "aton";
	}
	
	@RequestMapping("logout")
	public String logout(HttpSession session, HttpServletRequest request, Model model) {
		session.invalidate();
		model.addAttribute("mensagem",new MessageTO("Logout concluído!", MessaType.SUCESSO));
	    return "aton";
	}
		
}
