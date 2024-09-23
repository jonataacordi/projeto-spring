package com.inovasoft.minhasfinancas.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.inovasoft.minhasfinancas.exception.ErroAutenticacao;
import com.inovasoft.minhasfinancas.exception.RegraNegocioException;
import com.inovasoft.minhasfinancas.model.entity.Usuario;
import com.inovasoft.minhasfinancas.model.repository.UsuarioRepository;
import com.inovasoft.minhasfinancas.service.impl.UsuarioServiceImpl;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

	/*Não precisa mais injetar a notation @Autowired nas instâncias originais pois estaremos realizando testes unitários através do Mock. Não utilizaremos 
	 * mais a implementação do: (UsuarioService service) e sim a do método de implementação original do (UsuarioServiceImp service)
	*/
	
	/*Agora está tudo serndo geranciado pelo Bean do Mock, ou seja, quando formos utilizar as classes originais de serviço(UsuarioServiceImpl/)
	 *o Bean saberá que será essa classe e quando formos utilziar a classe de teste (não original) através do (repository) ele também saberá.*/
	@SpyBean
	UsuarioServiceImpl service;
	
	@MockBean //É um Bean gerenciável, ou seja, ao subir o contexto eirá criar uma instância de (UsuarioRepository) real, será criada uma intância Mockada, susbstituindo as instâncias que não precisar
	UsuarioRepository repository; //Instância original Mockada, ou seja, em teste.

	
	//OBS: Não utilizamos mais a chamada do método "setUpTests()" e sim a anotação "@SpyBean" diretamente no "UsuarioServiceImpl" para trabalhar com os métodos originais.
	// @BeforeEach //Utilzida para que esse método seja executado primeiro antes dos testes com anotation @Test
	// public void setUpTests() {
	//	 /*repository = Mockito.mock(UsuarioRepository.class);  //Chamadas através dos métodos FAKE através da instância (repository)
	//	   Chamadas através métodos reais através da instância (service).
	//	   Ao iniciar o contexto com ainstância original será criado o Mock através (@MockBean) sobre o (UsuarioRepository repository) 
	//	   que será adicionado dentro desse(UsuarioServiceImpl(repository)
	//	  * 
	//	  * Agora não chamamos mais: service = new UsuarioServiceImpl(repository); e sim o (Mockito.spy)
	//	  */
	//	service = Mockito.spy(UsuarioServiceImpl.class); 
	//}
	
	
	/*Mockito.spy = É parecido com Mock, a diferença é que ele sempre chama os métodos originais da interface (UsuarioService) porém para utilizar essa classe de 
	 * (UsuarioService) com (Spy) precisaremos deixar implicito no código qual o comportamento que esse método original terá, caso contrário o comportamento 
	 * será o default desse método, ou seja, para que possamos testar um método original do Service, que é a interface (UsuarioService) precisaremos deixar 
	 * implicito qual comportamento esse método terá. Já o (Mock) sempre utilizará os métodos FAKES.
	 * 
	 *Mockito no(Repository) = As instâncias de (Mock) nunca chamam o método original.
	 *UsuarioService = Sempre chama o método original. E para testarmos essa classe não poderemos utilizar o (@MockBean) caso contrário não estaremos testando
	 *os métodos (autenticar(), salvarUsuario(), validarEmail())
	 */
	
	
	//1 Cenário: Método Salvar Usuário que ocorre sem erros, ou seja, quando os dados informados pelo usuário estão corretos na base de dados.
	@Test
	public void saveUser() {
		//Classe onde ficam as implementações das chamadas dos métodos originais dos usuários da classe (UsuarioServiceImpl).
		/*Ao chamar o método(when) ele criará o (Mock) no (spy),//para depois (Mockar) o método (validarEmail). Ele manda 
		 *não fazer nada quando chamar o método validar e-mail, não lançando nenhum erro.*/ 
		
		//Cenário
		Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
		Usuario usuario = Usuario.builder().id(1l).nome("nome").email("email@gmail.com").senha("senha").build();
		
		Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario); //Através da instância (repository) o passar qualquer usuário ele retornará o usuário cadastrado na instância (usuario)
		
		//Ação
		Usuario usuarioSalvo = service.salvarUsuario(new Usuario()); //Retorna o usuário salvo acima
		
		//Verificação
		Assertions.assertThat(usuarioSalvo).isNotNull();
		Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(1l);
		Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo("nome");
		Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo("email@gmail.com");
		Assertions.assertThat(usuarioSalvo.getSenha()).isEqualTo("senha");
		
	}
	
	//2 Cenário: Método Salvar Usuário que ocorre com erros, ou seja, simulará um erro onde aque ao tentar salvar o usuário ele não vai salvar e lavantará um erro de exceção.
	
	@Test
	public void errorSalverUserIsEmailExists() {
		//Cenário. Nesse cenário será lançado o erro ao tentar cadastrar um usuário com e-mail já cadastrado, onde esse usuário não será salvo
		String email = "email@gmail.com";
		Usuario usuario = Usuario.builder().email(email).build(); //Só preciso testar o email do método (validarEmail())
		/*Levantará uma exceção ao entrar no método (validarEmail) da classe de serviços (UsuarioServiceImpl). Esse método só verifica/valida se o email 
		já existe ou não ao cadastrar um usuário com um email. Caso já exista levantará uma exceção e não salvará o usuário.
		*/
		Mockito.doThrow(RegraNegocioException.class).when(service).validarEmail(email);  
	
		//Ação. Chamar o método() para causar o erro 
		assertThrows(RegraNegocioException.class, () -> {	//Testar exceção a partir do Junit5
			service.salvarUsuario(usuario);
		});
		
		//Verificação
		Mockito.verify( repository, Mockito.never() ).save(usuario);//Verifica se nunca foi passado o método(save) passando o usuário cadastrado acima(usuario) através do email(email@gmail.com)
	}
	
	/*No Junit5 não precisa utiizar a anotação: @Test(expected = Test.None.class) para não levantar exceções, 
	 * ele passará automaticamente não levantando execeções. Agora caso necessitamos que levante exceções
	 * assertThrows(RegraNegocioException.class, () -> {  //Inserir método que queremos levantar uma exceção  }); }
	 * */	
	@Test 
	public void authUserSucess() {//Método autenticar
		//Cenário
		String email = "jonata@gmail.com";
		String senha = "abc123";
		
		Usuario usuario = Usuario.builder().email(email).senha(senha).id(1l).build();
		Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(usuario)); //Quando ele for na base do repository e executar o método(findByEmail) e passar o email(email), ele retornará um (Optional) com o (usuario)
		
		//Ação
		Usuario result = service.autenticar(email, senha);
		
		//Verificação
		Assertions.assertThat(result).isNotNull(); //Verifique se o resultado (email, senha) não são nulos, ou seja, se retornou uma instância de usuário.
		
	}
	
	@Test //Erro de exceção (ErroAutenticacao) ao não encontrar um usuário cadastrado na base dados com email passado
	public void authErrorNotUserEmailInfo() {
	
			//Cenário
			Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty()); //Retornando um objeto(Option) vazio para forçar o erro para levantar a exceção
			
			//Ação
			Throwable exception = Assertions.catchThrowable( () -> service.autenticar("email@gmail.com", "senha"));
			Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Usuário não encontrado para o e-mail informado.");

	}
	
	//Encontrou usuário na base de dados porém a senha está errada, lançando erro de exceção (ErroAutenticacao)
	@Test
	public void authLoginEmailSucessSenhaError() {
		
			//Cenário
			String senha = "senha";
			Usuario usuario = Usuario.builder().email("email@gmail.com").senha(senha).build();
			
			Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario)); //Retorna o usuário independente do e-mail que for passado, pois o que interesse é o teste da senha
			
			//Ação
			Throwable exception = Assertions.catchThrowable( () -> service.autenticar("email@gmail.com", "senhaErrada")); //Capturando a excação que está sendo levantada ao passar a senha errada para simular o erro na autenticação do usuário ao passar o (email) correto porém senha (errada)
			
			//Verificação. Realizando as verificações na exception
			Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Senha inválida.");
	}
	
	@Test//No Junit5 não precisa utiizar a anotação: @Test(expected = Test.None.class) para não levantar exceções, passará automaticamente
	public void validarEmail() {
		
		//Cenário. Não precisa mais do: repository.deleteAll();
		/*Qualquer String que for passada por parâmetro ao chamar o método (existsByEmail) será retornado (false), pois queremos testar a validação do e-mail que irá buscar através do (repository) do (Mockito)
		 * O Mock ficará dentro da classe em que se deseja realizar os testes
		 * */
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
		
		//Ação
		service.validarEmail("jonata@gmail.com");
		
		//OBS: Aqui não precisa da verificação, pois quem verificará será o método validarEmail()) da interface (service)
		
	}
	
	//Deverá levantar erro quando tentar cadastrar um e-mail que já foi cadastrado
	@Test
	public void lancaErroValidarIsEmailCadastrado() {
		
		//Cenário. Criando um usuário com um e-mail já cadastrado para testar a validação
		/*Usuario usuario = Usuario.builder().nome("jonata").email("jonata@gmail.com").build();
		repository.save(usuario);*/ 
		//Retorna (true) informando que já existe um usuário com esse e-mail cadastrado, onde que ao método (lancaErroValidarIsEmailCadastrado) verificar que esse e-mail já foi cadastrado, leantará uma exceção.
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true); 
		
		//Ação. Verificar a validação do e-mail existente no usuário cadastrado
		assertThrows(RegraNegocioException.class, () -> {	//Testar exceção a partir do Junit5
			service.validarEmail("jonata@gmail.com");
		});
		
	}
	
	
	
}


/* TESTES COM MOCK = Mockito
 * Simula a chamada de métodos e retorno de propriedades. 
 * Com Mock criamos instacias FAKE do Repository original para simular testes nos métodos das classes ao invés de ficar testando no Repository original.
 * */