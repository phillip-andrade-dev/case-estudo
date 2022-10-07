package br.com.banco.contas.dto.request;

import javax.validation.constraints.Email;

import org.hibernate.validator.constraints.br.CPF;

import lombok.Data;
@Data
public class GerenteRequestPatch {
	
	@CPF(message = "cpf inválido")
    private String cpf;
	
	
    private String nome;

	
    @Email(message = "email inválido.")
    private String email;
}
