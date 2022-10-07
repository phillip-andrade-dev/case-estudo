package br.com.banco.contas.config.security.data;

import br.com.banco.contas.config.AppProperties;
import br.com.banco.contas.config.SpringApplicationContext;

public class SecurityConstsICarros {

	public SecurityConstsICarros() {
		// Construtor padrão
	}

	public static final long TOKEN_EXPIRACAO = 3_600_000; // 60 mins
	public static final String TOKEN_PREFIXO = "Bearer ";
	public static final String HEADER_ATRIBUTO = "Authorization";
	public static final String SIGN_UP_CORRENTISTA_URL = "/login";
	public static final String GERENTE_URL = "/gerente/**";
	public static final String USUARIO_URL = "/usuario/**";
	public static final String ALLOWED_URL[] = { "/swagger-ui/**", "/v2/api-docs", "/v3/api-docs/", "/configuration/ui",
			"/swagger-resources/**", "/configuration/security", "/webjars/**", "/actuator/**", "/h2-console/**", USUARIO_URL, SIGN_UP_CORRENTISTA_URL, GERENTE_URL };

	/**
	 * Obtem o secret token a partir do application properties.
	 * 
	 * @return
	 */
	public static String getTokenSecret() {
		AppProperties appProperties = (AppProperties) SpringApplicationContext.getBean("AppProperties");
		return appProperties.getTokenSecret();
	}
}
