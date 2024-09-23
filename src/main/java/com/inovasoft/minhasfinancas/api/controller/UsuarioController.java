package com.inovasoft.minhasfinancas.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.PostExchange;

import com.inovasoft.minhasfinancas.api.dto.UsuarioDTO;
import com.inovasoft.minhasfinancas.exception.ErroAutenticacao;
import com.inovasoft.minhasfinancas.exception.RegraNegocioException;
import com.inovasoft.minhasfinancas.model.entity.Usuario;
import com.inovasoft.minhasfinancas.service.UsuarioService;

@RestController //Anotação para trabalhar com Rest
@RequestMapping("/api/usuarios")
public class UsuarioController {

	private UsuarioService service;
	
	//Contrutor que receberá a injeção de dependências
	public UsuarioController(UsuarioService service) { 
		this.service = service;
	}
	
	//Implementação método autenticar(). OBS: Como já possui um método/requisição POST(PostMapping) sem indicar um mapeamento, ao utilizar um novo método (PostMapping)
	//precisamos indicar uma rota,por exemplo (/autenticar), ficando acessível no endereço (/api/usuarios/autenticar)
	@PostMapping("/autenticar")
	public ResponseEntity autenticar( @RequestBody UsuarioDTO dto) {
		try {
		Usuario usuarioAutenticado = service.autenticar(dto.getEmail(), dto.getSenha()); //A interface UsuarioService possui a interface que direciona cada objeto para sua classe. 
		return ResponseEntity.ok(usuarioAutenticado);
		} catch (ErroAutenticacao e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	//Implementação método salvar()
	@PostMapping//Resposta requisições com a classe DTO do banco de dados
	public ResponseEntity salvar(@RequestBody UsuarioDTO dto) {
		Usuario usuario = Usuario.builder().nome(dto.getNome()).email(dto.getEmail()).senha(dto.getSenha()).build();
		
		try {
			Usuario usuarioSalvo = service.salvarUsuario(usuario);
			return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
}




/*
 * 
 *	@GetMapping("/")//Mapeia o método index() através de uma requisição recebida através do método (GET)
 *	public String index() {
 *		return "Bem Vindo ao sistema!";
 *	}
 * 
 * @RequestBody = Sinaliza para que os dados que vierem em formato JSON da requisição da API, 
 * sejam transformados em formato de objeto de banco de dados(DTO), exigindo que os dados que
 * vierem sejam nos formato dos atributos (nome, email, senha) declarados da classe (UsuarioDTO) 
 * caso contrário não será aceito.
 * 
 * 
 * */
