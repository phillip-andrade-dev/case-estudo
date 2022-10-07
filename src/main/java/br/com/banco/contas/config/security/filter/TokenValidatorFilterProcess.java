package br.com.banco.contas.config.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;

import br.com.banco.contas.config.security.data.SecurityConstsICarros;
import br.com.banco.contas.exception.UsuarioDesativadoException;

public class TokenValidatorFilterProcess extends OncePerRequestFilter {

	private UserDetailsService userDetailsService;
	
	private HandlerExceptionResolver handlerExceptionResolver;

	public TokenValidatorFilterProcess(UserDetailsService userDetailsService, HandlerExceptionResolver handlerExceptionResolver) {
		this.userDetailsService = userDetailsService;
		this.handlerExceptionResolver = handlerExceptionResolver;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		
		try {
			String  atributo = request.getHeader(SecurityConstsICarros.HEADER_ATRIBUTO);
			
			if (atributo == null || !atributo.startsWith(SecurityConstsICarros.TOKEN_PREFIXO)) {
				chain.doFilter(request, response);
				return;
			}
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = getAuthenticationToken(atributo
					.replace(SecurityConstsICarros.TOKEN_PREFIXO, ""));
			
			SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			chain.doFilter(request, response);
		} catch (UsuarioDesativadoException | 
				TokenExpiredException | 
				SignatureVerificationException | 
				InvalidClaimException | 
				AlgorithmMismatchException e) {
			handlerExceptionResolver.resolveException(request, response, null, e);
		}		
	}
	
	private UsernamePasswordAuthenticationToken getAuthenticationToken(String token) {
			
		DecodedJWT decoded = JWT.require(Algorithm.HMAC512(SecurityConstsICarros.getTokenSecret())).build().verify(token);
		
		String username = decoded.getSubject();
		
		UserDetails details = userDetailsService.loadUserByUsername(username);
		
		if(!details.isEnabled()) {
			throw new UsuarioDesativadoException();
		}
		
		
		return new UsernamePasswordAuthenticationToken(details.getUsername(), null, details.getAuthorities());
	}
	
}
