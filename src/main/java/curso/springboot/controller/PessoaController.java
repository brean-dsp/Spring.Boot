package curso.springboot.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import curso.springboot.model.Pessoa;
import curso.springboot.model.Telefone;
import curso.springboot.repository.PessoaRepository;
import curso.springboot.repository.TelefoneRepository;

@Controller
public class PessoaController {
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private TelefoneRepository telefoneRepository;
	
//Método que direciona para a pagina de cadastro assim que é logado o sistema 
@RequestMapping(method = RequestMethod.GET, value = "/cadastropessoa")
	public ModelAndView inicio() {
		
	//Inícia o objeto e retorna para a página
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
		
		modelAndView.addObject("pessoaobj", new Pessoa());
		
		//retorna a tabela preenchida com os dados postados do objeto Pessoa
		Iterable<Pessoa> pessoasIt = pessoaRepository.findAll();
		
		modelAndView.addObject("pessoas", pessoasIt);
		
		return modelAndView;
	}
	
	
//Método que salva  a pessoa cadastrada e retorna a lista na mesma tela de cadastro
@RequestMapping(method = RequestMethod.POST, value = "**/salvarpessoa")
	public ModelAndView salvar(@Valid Pessoa pessoa, BindingResult bindingResult) {
	
		if(bindingResult.hasErrors()) {
			ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
			
			Iterable<Pessoa> pessoasIt = pessoaRepository.findAll();
			
			modelAndView.addObject("pessoas", pessoasIt);
			modelAndView.addObject("pessoaobj", pessoa);
			
			List<String> msg = new ArrayList<String>();
			for(ObjectError objectError : bindingResult.getAllErrors()) {
				msg.add(objectError.getDefaultMessage()); //vem das anotações @notempty e @notnull
			}
			
			modelAndView.addObject("msg", msg);
			return modelAndView;
		}
	
	
		pessoaRepository.save(pessoa);
		
	//Inícia o objeto e retorna para a página
		ModelAndView andView = new ModelAndView("cadastro/cadastropessoa");
		
		Iterable<Pessoa> pessoasIt = pessoaRepository.findAll();
		
		andView.addObject("pessoas", pessoasIt);
		andView.addObject("pessoaobj", new Pessoa());
		
		return andView;
	}
	
	
//Método que cria a lista de pessoas cadastrados numa pagina especifica
@RequestMapping(method = RequestMethod.GET, value = "/listapessoas")
	public ModelAndView pessoas() {
		
	//Inícia o objeto e retorna para a página
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
		
	//Inícia o objeto e retorna para a página
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
		
		modelAndView.addObject("pessoaobj", pessoa.get());
		
		return modelAndView;
		
	}
	
	
//Método que cria a função de excluir os dados de um usuario da tabela e banco de dados
@GetMapping("/removerpessoa/{idpessoa}")
	public ModelAndView excluir(@PathVariable("idpessoa") Long idpessoa) {
		
		pessoaRepository.deleteById(idpessoa);
		
	//Inícia o objeto e retorna para a página
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
		
		modelAndView.addObject("pessoas", pessoaRepository.findAll());
		modelAndView.addObject("pessoaobj", new Pessoa());
		
		return modelAndView;
		
	}
	
	
	
//Método que cria a função de pesquisar um nome cadastrado da tabela e banco de dados
	
//@PostMapping é uma anotação para resumir o "@RequestMapping(method = RequestMethod.POST
@PostMapping("**/pesquisarpessoa")
	public ModelAndView pesquisar(@RequestParam("nomepesquisa") String nomepesquisa) {
	
	//Inícia o objeto e retorna para a página
		ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
		
		modelAndView.addObject("pessoas", pessoaRepository.findPessoaByName(nomepesquisa));
		modelAndView.addObject("pessoaobj", new Pessoa());
		
		return modelAndView;
	}
	
	
//Método que direciona para a página telefones e exibe os números para cada usuário
@GetMapping("**/telefones/{idpessoa}")
	public ModelAndView telefones(@PathVariable("idpessoa") Long idpessoa) {
		
		Optional<Pessoa> pessoa = pessoaRepository.findById(idpessoa);
		
	//Inícia o objeto e retorna para a página
		ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
		
		modelAndView.addObject("pessoaobj", pessoa.get());
		modelAndView.addObject("telefones", telefoneRepository.getTelefones(idpessoa));
		
		return modelAndView;
		
	}
	
	
//Método responsável por adicionar um numero(ou vários) para cada pessoa cadastrada
@PostMapping("**/addFonePessoa/{pessoaid}")
	public ModelAndView addFonePessoa(Telefone telefone, @PathVariable("pessoaid") Long pessoaid) {
	
//Consulta a pessoa no banco de dados
	Pessoa pessoa = pessoaRepository.findById(pessoaid).get();
	
//Pega o telefone digitado na tela e adiciona a pessoa para poder identificar 
	telefone.setPessoa(pessoa);	
	
//Salva o telefone e amarra no bando de dados
	telefoneRepository.save(telefone);

	
//Inícia o objeto e retorna para a página
		ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
		modelAndView.addObject("pessoaobj", pessoa);
		modelAndView.addObject("telefones", telefoneRepository.getTelefones(pessoaid));
		
		return modelAndView;
	}


//Método que exclui telefone da pagina de telefones
@GetMapping("/removertelefone/{idtelefone}")
	public ModelAndView removertelefone(@PathVariable("idtelefone") Long idtelefone) {
	
	Pessoa pessoa = telefoneRepository.findById(idtelefone).get().getPessoa();
	
	telefoneRepository.deleteById(idtelefone);
	
//Inícia o objeto e retorna para a página
	ModelAndView modelAndView = new ModelAndView("cadastro/telefones");
	
	modelAndView.addObject("pessoaobj", pessoa);
	modelAndView.addObject("telefones", telefoneRepository.getTelefones(pessoa.getId()));
	
	return modelAndView;
	
}
	
}
