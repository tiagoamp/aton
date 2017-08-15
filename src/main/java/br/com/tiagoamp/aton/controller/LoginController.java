package br.com.tiagoamp.aton.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.tiagoamp.aton.model.AtonBOException;
import br.com.tiagoamp.aton.model.Person;
import br.com.tiagoamp.aton.model.Role;
import br.com.tiagoamp.aton.model.User;
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
	
		
	@RequestMapping(value="login", method=RequestMethod.GET)
	public String pageLogin(HttpServletRequest request, Model model) {
		Person person = new Person();
		model.addAttribute("person", person);
	    return "login";
	}
	
	@RequestMapping(value="login", method=RequestMethod.POST)
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
			// checking credentials
			person.setPassword(DigestUtils.sha1Hex(person.getPassword()));
			if (personFromDB.getPassword().equals(person.getPassword()) && personFromDB.getRole() != Role.LEITOR) {
				// creating obj 'user' not managed without password attribute setted for security reasons, obj will be setted in session
				User user = new User(personFromDB.getEmail(), personFromDB.getName(), personFromDB.getRole());
				session.setAttribute("usuario", user);
				logger.info("Usuário autenticado: " + user);
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
		session.removeAttribute("usuario");
		session.invalidate();		
		model.addAttribute("mensagem",new MessageTO("Logout concluído!", MessaType.SUCESSO));
	    return "aton";
	}
		
}
