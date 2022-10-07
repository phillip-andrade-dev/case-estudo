package br.com.banco.contas.config.security.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.banco.contas.entity.Usuario;
import br.com.banco.contas.entity.enums.Papel;

public class UserDetailsICarros implements UserDetails {

	private static final long serialVersionUID = 1L;

	private final Usuario usuario;
	
	public UserDetailsICarros(Usuario usuario) {
		this.usuario = usuario;
	}	
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if(usuario.getPapel() == null ) {
			return null;
		}
		return List.of(Papel.valueOf(usuario.getPapel()));
	}

	@Override
	public String getPassword() {
		return usuario.getSenha();
	}

	@Override
	public String getUsername() {
		return usuario.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
