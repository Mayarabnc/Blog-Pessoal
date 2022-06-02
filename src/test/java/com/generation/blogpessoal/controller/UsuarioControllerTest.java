package com.generation.blogpessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.generation.blogpessoal.model.Usuario;
import com.generation.blogpessoal.service.UsuarioService;

//indica que a Classe UsuarioControllerTest é uma Classe Spring Boot Testing
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
				//indica que caso a porta principal(8080 para uso local) esteja ocupada, o Spring irá atribuir uma outra porta automacamente

//indica que o teste ira ser por classe (indica que o Ciclo de vida da Classe de Teste será por Classe)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)

//indica em qual ordem os testes serão executados
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
				//indica que os testes serão executados na ordem indicada pela anotação @Order inserida em cada teste
public class UsuarioControllerTest {
	
	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Test
	
	//indica que o método será o primeiro a ser executado
	@Order(1)
	@DisplayName("Cadastrar um usuário")
	public void CriarUsuario() {
		
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(new Usuario(0L,"Michael 1", "michaeltrimundial@gmail.com", "nunca fui ", "https://i.imgur.com/FETvs2O.jpg", "Normal"));
		
		ResponseEntity<Usuario> resposta = testRestTemplate.exchange("/usuarios/cadastrar", HttpMethod.POST, requisicao, Usuario.class);
		
		assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
		assertEquals(requisicao.getBody().getNome(), resposta.getBody().getNome());
		assertEquals(requisicao.getBody().getUsuario(), resposta.getBody().getNome());
	}
	
	@Test
	@Order(2)
	@DisplayName("Não deve perimtir duplicação do usuário")
	public void semDuplicarUsuario() {
		
		usuarioService.cadastraUsuario(new Usuario(0L,"Michael 1", "michaeltrimundial@gmail.com", "nunca fui ", "https://i.imgur.com/FETvs2O.jpg","Normal"));
		
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(new Usuario(0L, "Michael Silva", "michaeltrimundial@gmail.com", "nunca fui ", "https://i.imgur.com/FETvs2O.jpg","Normal"));
		
		ResponseEntity<Usuario> resposta = testRestTemplate.exchange("/usuarios/cadastrar", HttpMethod.POST, requisicao, Usuario.class);
		
		assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
	}
	
	@Test
	@Order(3)
	@DisplayName("Alterar um usuário")
	public void AtualizarUsuario() {
		
		Optional<Usuario> usuarioCreate = usuarioService.cadastraUsuario(new Usuario(0L, "Novo Usuario", "novousuario@gmail.com", "usurio1234", "https://i.imgur.com/FETvs2O.jpg","Administrador"));
		
		Usuario usuarioUpdate = new Usuario(usuarioCreate.get().getId(), "Novo Usuario", "novousuario@gmail.com", "usurio1234", "https://i.imgur.com/FETvs2O.jpg","Administrador");
		
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(usuarioUpdate);
		
		ResponseEntity<Usuario> resposta = testRestTemplate
				.withBasicAuth("root","root")
				.exchange("/usuarios/atualizar", HttpMethod.PUT, requisicao, Usuario.class);
		
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertEquals(usuarioUpdate.getNome(), resposta.getBody().getNome());
		assertEquals(usuarioUpdate.getUsuario(), resposta.getBody().getUsuario());
	}
	
	@Test
	@Order(4)
	@DisplayName("Listar todos os usuários")
	public void mostrarTodosUsuarios() {
		
		usuarioService.cadastraUsuario(new Usuario(0L,"Michael Silva", "michaeltrimundial@gmail.com", "nunca fui ", "https://i.imgur.com/FETvs2O.jpg","Normal"));
		
		usuarioService.cadastraUsuario(new Usuario(0L,"Michael Silva", "michaeltrimundial@gmail.com", "nunca fui ", "https://i.imgur.com/FETvs2O.jpg","Normal"));
		
		ResponseEntity<String> resposta = testRestTemplate
				.withBasicAuth("root","root")
				.exchange("/usuarios/all", HttpMethod.GET, null, String.class);
		
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
	}
}


