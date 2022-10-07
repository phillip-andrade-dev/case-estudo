package br.com.banco.contas.service;

import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import br.com.banco.contas.config.security.data.SecurityConstsICarros;
import br.com.banco.contas.config.security.data.UserDetailsICarros;
import br.com.banco.contas.exception.UsuarioEOuSenhaInvalidoException;

@Service
public class LoginService {
	

	public String geraToken(Authentication auth) throws UsuarioEOuSenhaInvalidoException {
		if(auth != null && auth.isAuthenticated()) {
			UserDetailsICarros userDetailsICarros = (UserDetailsICarros) auth.getPrincipal();
			String role = userDetailsICarros.getAuthorities() != null ? userDetailsICarros.getAuthorities().toArray()[0].toString() : "";
			return JWT.create()
					.withSubject(userDetailsICarros.getUsername())
					.withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstsICarros.TOKEN_EXPIRACAO))
					.withClaim("ROLE", role)
					.sign(Algorithm.HMAC512(SecurityConstsICarros.getTokenSecret().getBytes()));			
		} else {
			throw new UsuarioEOuSenhaInvalidoException("Usuário e/ou senha inválidos.");
		}
	}
}
