package br.com.banco.contas.dto.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaqueResponse {
	
	private BigDecimal valor;
	private BigDecimal saldoAtual;

}
