package br.com.banco.contas.config.security.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.banco.contas.config.security.data.SecurityConstsICarros;
import br.com.banco.contas.config.security.data.UserDetailsICarros;
import br.com.banco.contas.entity.Usuario;
import br.com.banco.contas.exception.InfraestruturaException;

public class CorrentistaJWTAuthenticationFilterProcess extends UsernamePasswordAuthenticationFilter {
	
	private final AuthenticationManager authenticationManager;


	public CorrentistaJWTAuthenticationFilterProcess(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
		setFilterProcessesUrl(SecurityConstsICarros.SIGN_UP_CORRENTISTA_URL); 
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {
			Usuario usuario = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);
			return this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
						usuario.getUsername(), usuario.getSenha(), new ArrayList<>()
					));
		} catch (IOException e) {
			throw new InfraestruturaException("Falha ao autenticar o usuário: " + e.getMessage());
		}
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, 
			HttpServletResponse response, 
			FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		UserDetailsICarros userDetailsICarros = (UserDetailsICarros) authResult.getPrincipal();
		String role = userDetailsICarros.getAuthorities() != null ? userDetailsICarros.getAuthorities().toArray()[0].toString() : "";
		String token = JWT.create()
				.withSubject(userDetailsICarros.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstsICarros.TOKEN_EXPIRACAO))
				.withClaim("ROLE", role)
				.sign(Algorithm.HMAC512(SecurityConstsICarros.getTokenSecret().getBytes()));
		response.getWriter().write(token);
		response.getWriter().flush();
	}
}
