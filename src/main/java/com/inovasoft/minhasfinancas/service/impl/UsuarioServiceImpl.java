package com.inovasoft.minhasfinancas.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.inovasoft.minhasfinancas.exception.ErroAutenticacao;
import com.inovasoft.minhasfinancas.exception.RegraNegocioException;
import com.inovasoft.minhasfinancas.model.entity.Usuario;
import com.inovasoft.minhasfinancas.model.repository.UsuarioRepository;
import com.inovasoft.minhasfinancas.service.UsuarioService;

import jakarta.transaction.Transactional;

/*Container de injeção de dependências (@Autowired).
 * @Service = Transforma a implementação nessa classe em um (Bean) gerenciável. Quando inserimos essa anotação
 * em cima da classe, é solicitado para que seja gerenciada a instancia dessa classe, ou seja, ele irá criar uma instância  
 * e vai adicionar um container para quando precisar injetar em outras classes.
 * */
@Service
public class UsuarioServiceImpl implements UsuarioService{
	
	/*Criando uma dependencia para o usuario repositpry, pois a camada de serviço(model.service) irá acessar essa camada de modelo (model.repository) para poder executar as operações com as entidades (Usuario, Lancamento) na base de dados. 
	pois como o (UsuarioServiceImpl) não poderá acessar diretamente a base de dados ele irá precisar do (UsuarioRepository) para poder realizar as operações.
	*/
	private UsuarioRepository repository;

	/*Esse contrutor receberá a implementação do (UsuarioRepository) como parâmetro. Para o (UsuarioServiceImpl) funcionar ele precisará ser construido através de uma instancia de (UsuarioRepository)
	 * @Autowired = Injeta a dependencia direto no construtor 
	 * */

	public UsuarioServiceImpl(UsuarioRepository repository) {
		super();
		this.repository = repository;
	}
	
	
	@Override
	public Usuario autenticar(String email, String senha) {
		
		Optional<Usuario> usuario = repository.findByEmail(email);
		
		//Verifica se existe o usuário
		if(!usuario.isPresent()) {
			throw new ErroAutenticacao("Usuário não encontrado para o e-mail informado.") ; //Entra no bloco quando não encontrou e-mail para o usuário que foi passado
		}
		//Verifica se a senha digitada é igual a senha no banco de dados, se não for entrará no bloco (if) e levantará exceção
		if(!usuario.get().getSenha().equals(senha)) {//Entra no bloco quando não encontrou a senha para o usuário que foi passado
			throw new ErroAutenticacao("Senha inválida.");
		}
		
		return usuario.get(); //Ao passar pelas duas condições, ou seja, não entrar no bloco de código das condições, retornará  a instância do usuário, pois validou corretamente o usuario e senha.
	}


	@Override
	@Transactional //Abre uma transação na base de dados para salvar o usuário no BD e depois realizar o (commit)
	public Usuario salvarUsuario(Usuario usuario) {
		validarEmail(usuario.getEmail());
		return repository.save(usuario);
	}

	@Override
	public void validarEmail(String email) {
		Boolean existe =  repository.existsByEmail(email);
		if(existe) {
			throw new RegraNegocioException("Já existe um usuário cadastrado com esse email.");
		}
	}

}
