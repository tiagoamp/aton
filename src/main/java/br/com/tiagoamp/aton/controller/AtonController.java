package br.com.tiagoamp.aton.controller;

import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.tiagoamp.aton.model.AtonBOException;
import br.com.tiagoamp.aton.model.Person;
import br.com.tiagoamp.aton.model.Role;
import br.com.tiagoamp.aton.service.PersonService;

@Controller
public class AtonController {
	
	public AtonController() {
		personService = new PersonService();
	}
	
	Logger logger = Logger.getLogger(AtonController.class);
	
	private PersonService personService;
	
	@RequestMapping("/aton")
	public String pageInicial() {
		return "aton";
	}
	
	@RequestMapping("initializeAndDeleteThisEntry")
	public String pageInitialize() {
		//TODO: Delete this method after initializing the application
		
		try {
			List<Person> list = personService.getAll();
			if (list.isEmpty()) {
				Person person = new Person("admin@email.com", "System Administrator", null, Role.ADMINISTRADOR);
				person.setPassword(DigestUtils.sha1Hex("admin"));
				personService.insert(person);
			}				
		} catch (AtonBOException e) {
			e.printStackTrace();
		}
	    return "aton";
	}
	
	@RequestMapping("pessoas")
	public String pagePessoas() {
	    return "pessoas/pessoas";
	}
					
	@RequestMapping("livros")
	public String pageLivros() {
	    return "livros/livros";
	}	
		
	@RequestMapping("emprestimos")
	public String pageEmprestimos() {
	    return "emprestimos/emprestimos";
	}
	
	@RequestMapping("autorizacao")
	public String pageAutorizacao() {
	    return "autorizacao";
	}
	
	@RequestMapping("sobre")
	public String pageSobre() {
		return "sobre";
	}
			
}
