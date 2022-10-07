package br.com.banco.contas.dto.response;

import br.com.banco.contas.entity.enums.UF;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListaCorrentistaResponse {
	
	private String agencia;
	private String bairro;
	private String cep;
	private String cidade;
	private String conta;
	private String cpf;
	private String email;
	private String endereco;
	private String nome;
	private String telefone;
	private UF uf;
	private Boolean situacao;

}
