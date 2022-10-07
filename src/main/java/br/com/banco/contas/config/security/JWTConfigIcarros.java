package br.com.banco.contas.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import br.com.banco.contas.config.security.data.SecurityConstsICarros;
import br.com.banco.contas.config.security.filter.TokenValidatorFilterProcess;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class JWTConfigIcarros {

	private UserDetailsService userDetailsService;

	private PasswordEncoder encoder;

	// handler necessário para garantir que o ControllerAdvice
	// manipule as exceções lançadas no filtro TokenValidatorFilterProcess.
	private HandlerExceptionResolver handlerExceptionResolver;

	/**
	 * Cria a configuração de segurança (autenticação e autorização) por meio da DI
	 * da implementação do UserDetailsSerice, PasswordEncoder e
	 * HandlerExceptionResolver
	 * 
	 * @param userDetailsService       O contrato do serviço de busca de usuário por
	 *                                 nome (username/agência).
	 * @param encoder                  O contrato de codificação da senha.
	 * @param handlerExceptionResolver O contrato da manipulação de exceção para
	 *                                 garantir que as exceções sejam interceptadas
	 *                                 pelo <code>RestControlerAdvice</code>. Sua
	 *                                 implementação é passada para o
	 *                                 <code>TokenValidatorFilterProcess</code>
	 *                                 poder lançar as exceções em cenários
	 *                                 relacionados ao token, como por exemplo,
	 *                                 expiração do token.
	 */
	public JWTConfigIcarros(UserDetailsService userDetailsService, PasswordEncoder encoder,
			HandlerExceptionResolver handlerExceptionResolver) {
		this.userDetailsService = userDetailsService;
		this.encoder = encoder;
		this.handlerExceptionResolver = handlerExceptionResolver;
	}

	/**
	 * Confiruação de segurança, especificando as URLs de logins e os respectivos
	 * filtros para a gestão dos tokens.
	 * 
	 * @return
	 */
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder = http
				.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(encoder);
		AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

		http.csrf().disable().authorizeHttpRequests()
				.antMatchers(SecurityConstsICarros.ALLOWED_URL).permitAll().antMatchers("/correntista/**")
				.hasRole("GERENTE").antMatchers("/transacao/**").hasRole("CORRENTISTA").anyRequest().authenticated()
				.and().authenticationManager(authenticationManager)
				.addFilterBefore(new TokenValidatorFilterProcess(userDetailsService, handlerExceptionResolver),
						UsernamePasswordAuthenticationFilter.class)
				.headers().frameOptions().disable()
				.and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		return http.build();

	}

}
