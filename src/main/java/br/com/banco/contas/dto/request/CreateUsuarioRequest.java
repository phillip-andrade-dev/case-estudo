package br.com.banco.contas.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUsuarioRequest {
	@Positive
	@NotBlank(message = "conta é um campo obrigatório")
    private String conta;
	
}
