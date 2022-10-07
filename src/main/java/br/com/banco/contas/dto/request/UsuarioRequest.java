package br.com.banco.contas.dto.request;

import javax.validation.constraints.NotBlank;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public record UsuarioRequest(
		@NotBlank(message = "username é um campo obrigatório") String username, 
		@NotBlank(message= "senha é um campo obrigatório") String senha) {
	
	public UsernamePasswordAuthenticationToken converter() {
		return new UsernamePasswordAuthenticationToken(username, senha);
	}
}
