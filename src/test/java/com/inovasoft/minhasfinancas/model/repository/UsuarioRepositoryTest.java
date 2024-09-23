package com.inovasoft.minhasfinancas.model.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.inovasoft.minhasfinancas.model.entity.Usuario;

//Realizando testes de integração na base de dados
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test") //Profile do banco de teste (h2)
@DataJpaTest //Cria uma instância do banco de dados na memória e ao finalizar o teste ela limpa a memória.
@AutoConfigureTestDatabase(replace = Replace.NONE)  //Não deixa sobreescrever os dados do banco de dados de teste que está em memória(application-test.properties)
public class UsuarioRepositoryTest {
	
	@Autowired
	UsuarioRepository repository; //Possui uma instância dentro de (TestEntityManager)
	
	@Autowired
	TestEntityManager entityManager; /*Essa classe é responsável por realizar as operações na base de dados(Persistor, Slavar, Atualizar, Deletar...). Será utilizado por fazer a manipulação de cenário.
	 														Esse é apenas um (TestEntityManager) criado para teste através do Spring DataJpa para realizar testes com (EntityManager)*/
	
	@Test
	public void verificarExistenciaEmail() {
		//Cenário. Criando e salvando um novo usuario
		Usuario usuario = criarUsuario();
		entityManager.persist(usuario);
		
		//Ação / Execução. Verificando se já existe o email
		boolean result = repository.existsByEmail("usuario@gmail.com");
		
		//Verificação. Está verificando se o email que está vindo de (result) é verdadeiro, ou seja, se já existe.
		Assertions.assertThat(result).isTrue();
		
	}
	
	//Retorna falso quando não tiver houver usuário cadastrado para o e-mail informado
	@Test
	public void returnFalseIsNotEmailUser() {
		/*Cenário. Limpa a base dados para garantir que quando for buscar um usuário pelo e-mail, esse não exista no banco de dados.
		 * 
		 * Não precisou mais utilizar o (repository.deleteAll()) ao utiizar o (TestEntityManager)  pois tudo que for persistido dentro do (entityManager.persist(usuario))
		 * logo após será realizado um (rollback), não estando mais na base dados quando acabar o teste. Sendo assim a base de dados estará vazia quando entrar nesse método
		 * não precisando deletar todos os registros de testes criado para poder realizar o teste abaixo.
		 * */
	
		//Ação / Execução. Está verificando se o email que está vindo de (result) é verdadeiro, ou seja, se já existe.
		boolean result = repository.existsByEmail("usuario@gmail.com");
		
		
		//Verificação. Verificando se o retorno foi (false)
		Assertions.assertThat(result).isFalse();
	}
	
	
	//Método que cria e salva o usuário na base de dados de teste.
	@Test
	public void persistirUserDb() {
		//Cenário. Criar usuário na base de dados
		Usuario usuario = criarUsuario();
		
		//Ação. Salvando o usuário criado no banco de dados de teste.
		Usuario usuarioSalvo = repository.save(usuario);
		
		//Verificação. Verifica se o usuário possui (id), ou seja, quando o usuário é criado ele possui (id). Como criamos o usuário acima, a assertiva deve ser verdadeira.
		Assertions.assertThat(usuarioSalvo.getId()).isNotNull();
		
	}
	
	//Método que busca o usuário por email
	@Test
	public void buscarUserEmail() {
		//Cenário
		Usuario usuario = criarUsuario();
		entityManager.persist(usuario);
		
		//Verificação
		Optional<Usuario> result = repository.findByEmail("usuario@gmail.com");
		Assertions.assertThat(result.isPresent()).isTrue(); //Se o resultado(possui a instancia com email) retrnará (true)
	}
	
	//Método que retorna vazio se o usuário a ser consultado por email não estiver cadastrado na Db.
		@Test
		public void returnEmptySearchUserEmailNotDb() { //Retorna vazio se o usuário a ser consultado por email não estiver cadastrado na Db.
			//Cenário
			
			//Verificação
			Optional<Usuario> result = repository.findByEmail("usuario@gmail.com");
			
			Assertions.assertThat(result.isPresent()).isFalse(); //Se o existir(isPresent) dados vindo do (result) faremos com sejá retornado (false) para testar a busca de um usuario por email  e não encontrou o email
		}
	
	//Criando método estatico para não precisar criar uma instancia
	public static Usuario criarUsuario() {
		return Usuario.builder()
				.nome("usuario")
				.email("usuario@gmail.com")
				.senha("abc123")
				.build();
	}
	
}





