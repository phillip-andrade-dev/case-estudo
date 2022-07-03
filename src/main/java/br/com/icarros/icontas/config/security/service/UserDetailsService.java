package br.com.icarros.icontas.config.security.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import br.com.icarros.icontas.config.security.data.UserDetailsICarros;
import br.com.icarros.icontas.entity.Usuario;
import br.com.icarros.icontas.repository.UsuarioRepository;

@Component
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

	private static final String USUARIO_NAO_ENCONTRADO = "Usuário não encontrato na tentativa de login.";

	private final UsuarioRepository usuarioRepository;

	public UserDetailsService(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Usuario> usuario = usuarioRepository.findByUsername(username);
		return new UserDetailsICarros(usuario.orElseThrow(() -> new UsernameNotFoundException(USUARIO_NAO_ENCONTRADO)));
	}

}
