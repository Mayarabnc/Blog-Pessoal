package com.generation.blogpessoal.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.generation.blogpessoal.model.Usuario;



//indicando que a classe UsuarioRepositoryTest é uma classe de test, que vai rodar em uma porta aleatoria a cada teste realizado
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)

//cria uma instancia de testes, que define que o ciclo de vida do teste vai respeitar o ciclo de vida da classe(será executado e resetado após o uso)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioRepositoryTest {
	
	@Autowired
	private UsuarioRepository repository;
	
	//o que fazer antes de iniciar o teste
	@BeforeAll
	void start() {

		repository.save(new Usuario(0L, "Maiar Silva", "isadora@gmail.com", "51 e pinga", "https://i.imgur.com/FETvs2O.jpg","Normal"));

		repository.save(new Usuario(0L, "Michael Silva", "michaeltrimundial@gmail.com", "nunca fui ", "https://i.imgur.com/FETvs2O.jpg","Normal"));

		repository.save(new Usuario(0L, "Brocco Silva", "broco@gmail.com", "broccolis", "https://i.imgur.com/FETvs2O.jpg","Normal"));

		repository.save(new Usuario(0L, "Mayara", "will31smith@gmail.com", "cenoura12345", "https://i.imgur.com/FETvs2O.jpg","Normal"));
	}

	@Test
	@DisplayName("Teste que retorna 1 usuario")
	public void retornaUmUsuario() {
		Optional<Usuario> usuario = repository.findByUsuario("michaeltrimundial@gmail.com");
		assertTrue(usuario.get().getUsuario().equals("michaeltrimundial@gmail.com"));
	}

	@Test
	@DisplayName("Teste que retorna 3 usuarios")
	public void RetornaTresUsuarios() {
		List<Usuario> listaDeUsuarios = repository.findByNome("Silva");
		assertEquals(3,listaDeUsuarios.size());
		assertTrue(listaDeUsuarios.get(0).getNome().equals("Maiar Silva"));
		assertTrue(listaDeUsuarios.get(1).getNome().equals("Michae Silva"));
		assertTrue(listaDeUsuarios.get(2).getNome().equals("Brocco Silva"));
	}
	
	@AfterAll
	public void end() {
		repository.deleteAll();
	}
}
