package br.com.banco.contas.dto.request;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.br.CPF;

import lombok.Data;

@Data
public class GerenteRequest {
	@NotBlank(message = "Cpf é um campo obrigatório")
	@CPF(message = "cpf inválido")
    private String cpf;
	
	@NotBlank(message = "nome é um campo obrigatório")
    private String nome;

	@NotBlank(message = "email é um campo obrigatório")
    @Email(message = "email inválido.")
    private String email;

}
