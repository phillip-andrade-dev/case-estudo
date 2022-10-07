package br.com.icarros.icontas.config.security.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.banco.contas.config.security.service.UserDetailsServiceImpl;
import br.com.banco.contas.entity.Usuario;
import br.com.banco.contas.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplUnitTest {
	
	@InjectMocks
	UserDetailsServiceImpl userDetailsServiceImpl;

	@Mock
	UsuarioRepository usuarioRepository;
	

	@Mock
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	Usuario usuario;
	
	@BeforeEach
	void setup() {
		usuario = new Usuario();
		usuario.setId(1L);
		usuario.setUsername("joao@gmail.com");
		usuario.setPapel("ROLE_CORRENTISTA");
		usuario.setVersion(1);
		usuario.setSenha(bCryptPasswordEncoder.encode("1234"));
	}
	
	@Test
	void testObtemUsuario_Sucesso() {
		when(usuarioRepository.findByUsername(anyString())).thenReturn(Optional.of(usuario));
		UserDetails usuarioRetornado = userDetailsServiceImpl.loadUserByUsername("joao@gmail.com");
		assertNotNull(usuarioRetornado);
		assertEquals("joao@gmail.com", usuarioRetornado.getUsername());
		assertEquals("ROLE_CORRENTISTA", usuarioRetornado.getAuthorities().toArray()[0].toString());
	}
	
	@Test
	void testObtemUsuario_UsernameNotFoundException() {
		when(usuarioRepository.findByUsername(anyString())).thenReturn(Optional.empty());
		assertThrows(UsernameNotFoundException.class,
				() -> {
					userDetailsServiceImpl.loadUserByUsername("sr_ninguem@gmail.com");
				}
		);
	}
	
}
