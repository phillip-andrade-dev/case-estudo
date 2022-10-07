package br.com.icarros.icontas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import br.com.banco.contas.config.security.data.SecurityConstsICarros;
import br.com.banco.contas.config.security.data.UserDetailsICarros;
import br.com.banco.contas.config.security.service.UserDetailsServiceImpl;
import br.com.banco.contas.dto.request.UsuarioRequest;
import br.com.banco.contas.entity.Usuario;
import br.com.banco.contas.exception.UsuarioEOuSenhaInvalidoException;
import br.com.banco.contas.repository.UsuarioRepository;
import br.com.banco.contas.service.LoginService;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DisplayName("Teste Login Service")	
public class LoginServiceTest {

	@InjectMocks
	LoginService loginService;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@MockBean
	UserDetailsServiceImpl userDetailsServiceImpl;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@MockBean
	UsuarioRepository usuarioRepository;
	
	UserDetailsICarros userDetailsICarros;
	
	Authentication authentication;
	
	@BeforeEach
	public void setup() {
		Usuario  usuario = new Usuario();
		usuario.setId(1L);
		usuario.setUsername("1111");
		usuario.setPapel("ROLE_CORRENTISTA");
		usuario.setVersion(0);
		usuario.setSenha(bCryptPasswordEncoder.encode("123456"));
		userDetailsICarros = new UserDetailsICarros(usuario);
	}
	
	@Test
	@DisplayName("Deve retornar um token contendo agência")
	void shouldReturnStringTokenTest() throws UsuarioEOuSenhaInvalidoException {
		when(userDetailsServiceImpl.loadUserByUsername(anyString())).thenReturn(userDetailsICarros);
		UsuarioRequest usuarioRequest = new UsuarioRequest("1111", "123456");
		UsernamePasswordAuthenticationToken userToken = usuarioRequest.converter();
		authentication = authenticationManager.authenticate(userToken);
		String token = loginService.geraToken(authentication);
		String username = JWT.require(Algorithm.HMAC512(SecurityConstsICarros.getTokenSecret())).build().verify(token).getSubject();
		assertEquals("1111", username);
	}
	
	@Test
	@DisplayName("Deve lançar uma exceção de BadCredentialsException")
	void shouldThrowsUsuarioEOuSenhaInvalidoException() throws UsuarioEOuSenhaInvalidoException {
		when(userDetailsServiceImpl.loadUserByUsername(anyString())).thenThrow(UsernameNotFoundException.class);
		UsuarioRequest usuarioRequest = new UsuarioRequest("1123", "123456");
		UsernamePasswordAuthenticationToken userToken = usuarioRequest.converter();
		assertThrows(BadCredentialsException.class,
				() -> {
					authentication = authenticationManager.authenticate(userToken);
					String token = loginService.geraToken(authentication);
				}
		);
	}
}
