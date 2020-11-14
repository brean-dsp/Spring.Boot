package curso.springboot.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import curso.springboot.model.Pessoa;
import curso.springboot.repository.PessoaRepository;

@Controller
public class PessoaController {
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	//Método que direciona para a pagina de cadastro e retorna os campos vazio do objeto Pessoa
	@RequestMapping(method = RequestMethod.GET, value = "/cadastropessoa")
	public ModelAndView inicio() {
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
		modelAndView.addObject("pessoaobj", new Pessoa());
		return modelAndView;
	}
	
	
	//Método que salva  a pessoa cadastrada e retorna a lista na mesma tela de cadastro
	@RequestMapping(method = RequestMethod.POST, value = "**/salvarpessoa")
	public ModelAndView salvar(Pessoa pessoa) {
		pessoaRepository.save(pessoa);
		
		ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");
		Iterable<Pessoa> pessoasIt = pessoaRepository.findAll();
		andView.addObject("pessoas", pessoasIt);
		andView.addObject("pessoaobj", new Pessoa());
		
		return andView;
	}
	
	
	//Método que cria a lista de pessoas cadastrados numa pagina especifica
	@RequestMapping(method = RequestMethod.GET, value = "/listapessoas")
	public ModelAndView pessoas() {
		
		ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");
		Iterable<Pessoa> pessoasIt = pessoaRepository.findAll();
		andView.addObject("pessoas", pessoasIt);
		andView.addObject("pessoaobj", new Pessoa());
		
		return andView;
	}
	
	
	//Método que cria a função de editar os dados de uma pessoa cadastrada da tabela e banco de dados
	
	//@GetMapping é uma anotação para resumir o "@RequestMapping(method = RequestMethod.GET"
	
	@GetMapping("/editarpessoa/{idpessoa}")
	public ModelAndView editar(@PathVariable("idpessoa") Long idpessoa) {
		
		Optional<Pessoa> pessoa = pessoaRepository.findById(idpessoa);
		
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
		modelAndView.addObject("pessoaobj", pessoa.get());
		return modelAndView;
		
	}
	
	
	//Método que cria a função de excluir os dados de um usuario da tabela e banco de dados
	@GetMapping("/removerpessoa/{idpessoa}")
	public ModelAndView excluir(@PathVariable("idpessoa") Long idpessoa) {
		
		pessoaRepository.deleteById(idpessoa);
		
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
		modelAndView.addObject("pessoas", pessoaRepository.findAll());
		modelAndView.addObject("pessoaobj", new Pessoa());
		return modelAndView;
		
	}
	
}
