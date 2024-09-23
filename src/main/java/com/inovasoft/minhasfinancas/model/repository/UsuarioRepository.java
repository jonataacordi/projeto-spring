package com.inovasoft.minhasfinancas.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inovasoft.minhasfinancas.model.entity.Usuario;

/*
 * Usuario = Entidade
 * Long = Tipo de dados da chave primária (private Long id)
 * 
 * O UsuarioRepository irá prover os métodos de CRUD (Consultar, Salvar, Atualizar, Deletar). Não precisa implementar
 * nada pois o JPA já realiza tudo de forma default em tempo de execução. 
 * */
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	/*Query Methods = Realiza uma busca (SELECT) dentro da base de dados por (email), não precisando realizar o SELECT tradicional (SELECT * FROM usuario WHERE email = "jonata@gmail.com";), 
	  Optional<Usuario> findByEmail(String email); //(SELECT * FROM usuario WHERE email = "jonata@gmail.com";)
	  
	  Optional<Usuario> findByEmailAndNome(String email, String nome); //(SELECT * FROM usuario WHERE email = "jonata@gmail.com" AND nome = "Jonata";)
	*/
	//Verifica se existe o email
	Boolean existsByEmail(String email);
	
	//Buscando o usuário por email
	Optional<Usuario> findByEmail(String email);
}
