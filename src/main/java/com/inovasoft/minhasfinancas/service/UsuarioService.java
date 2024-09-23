package com.inovasoft.minhasfinancas.service;

import com.inovasoft.minhasfinancas.model.entity.Usuario;

//Essa interface é quem definirá os métodos que irão trabalhar com aentidade Usuario
public interface UsuarioService {
	
	//Método para autenticação (login)
	Usuario autenticar(String email, String senha);
	
	//Enquanto não existir esse usuario que estará sendo passado ele não possui (id). Ao salvar esse usuario na base de dados, e consultar será retornado um usuário persistido, ou seja, com (id) 
	Usuario salvarUsuario(Usuario usuario);
	
	//E-mail só será cadastrado uma única vez, e ao salvar um novo usuário (salvarUsuario) for verificado ao validar o e-mail (validarEmail) que já existe esse e-mail salvo na base de dados não será permitido, retornando uma mensagem para o usuário.
	void validarEmail(String email);
	
}
