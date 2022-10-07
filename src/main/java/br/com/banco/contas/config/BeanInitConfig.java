package br.com.banco.contas.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import br.com.banco.contas.config.security.data.SecurityConstsICarros;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class BeanInitConfig {

	/**
	 * Utilizado em UserServiceDetailsImpl. Como BCryptPassword não é um @Component,
	 * foi inicializado como bean.
	 * 
	 * @return
	 */
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * Permite fazer a injeção desse conjunto de constantes em Beans.
	 * 
	 * @return
	 */
	@Bean
	public SecurityConstsICarros getSecConsts() {
		return new SecurityConstsICarros();
	}

	/**
	 * So SpringApplicationContext.getBean pode ser utilizado para obter um bean em
	 * uma classe não gerenciada pelo spring, por exemplo: 1. Obter AppProperties em
	 * SecurityConstsIcarros. SecurityConstsIcarros não é um bean.
	 * 
	 * @return
	 */
	@Bean
	public SpringApplicationContext springApplicationContext() {
		return new SpringApplicationContext();
	}

	/**
	 * Utilizado para obter o AppProperties em SecurityConstsIcarros, pois
	 * SecurityConsts não tem anotação @Service ou @Component. É utilizado o
	 * SpringApplicationContext para obter esse bean. Nota: SpringApplicationContext
	 * é uma classe personalizada nesste projeto.
	 * 
	 * @return
	 */
	@Bean(name = "AppProperties")
	public AppProperties getAppProperties() {
		return new AppProperties();
	}

	/**
	 * Retorna o bean responsável por fazer o mapeamento dos objetos dto e entity.
	 * 
	 * @return O bean implementação do ModelMapper.
	 */
	@Bean
	public ModelMapper getModelMapper() {
		return new ModelMapper();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	/**
	 * Retorna o bean responsável por nomear o usuário de criação e edição de
	 * registros para procedimento de auditoria
	 * 
	 * @return O bean de implementação da auditoria.
	 */
	@Bean
	public AuditorAware<String> auditorAware() {
		return new AuditorAwareImpl();
	}

	/**
	 * Configuração do CORS, liberada qualquer origem para testes.
	 * 
	 * @return
	 */
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedMethods("*");
			}
		};
	}

}
