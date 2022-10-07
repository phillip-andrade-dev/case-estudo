package br.com.banco.contas.config.security.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import br.com.banco.contas.config.security.data.UserDetailsICarros;
import br.com.banco.contas.entity.Usuario;
import br.com.banco.contas.repository.UsuarioRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private static final String USUARIO_NAO_ENCONTRADO = "Dados de login inválidos.";

	private final UsuarioRepository usuarioRepository;

	public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Usuario> usuario = usuarioRepository.findByUsername(username);
		return new UserDetailsICarros(usuario.orElseThrow(() -> new UsernameNotFoundException(USUARIO_NAO_ENCONTRADO)));
	}

}
