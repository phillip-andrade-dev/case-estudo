package br.com.banco.contas.dto.request;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

import org.hibernate.validator.constraints.br.CPF;

import br.com.banco.contas.entity.enums.UF;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CorrentistaRequest {
	
	@CPF(message = "cpf inválido")
    private String cpf;
	
	@NotBlank(message = "agência é um campo obrigatório")
    private String agencia;
	@Positive
	@NotBlank(message = "conta é um campo obrigatório")
    private String conta;

	@NotBlank(message = "nome é um campo obrigatório")
    private String nome;

    @Email(message = "email inválido.")
    private String email;

    @NotBlank(message ="telefone inválido.")
    @Pattern(regexp = "(\\d{2})\\d{9}")
    private String telefone;

    @NotBlank(message = "endereco inválido.")
    private String endereco;

    @NotBlank(message = "cep inválido.")
    @Pattern(regexp = "\\d{5}-\\d{3}")
    private String cep;

    @NotBlank(message = "bairro é um campo obrigatório")
    private String bairro;

    @NotBlank(message = "cidade é um campo obrigatório")
    private String cidade;  
    
    @NotNull(message = "uf é um campo obrigatório")
    @Enumerated(EnumType.STRING)
    private UF uf;

    private GerenteCorrentistaRequest gerente;

    private Boolean situacao = true;


}
