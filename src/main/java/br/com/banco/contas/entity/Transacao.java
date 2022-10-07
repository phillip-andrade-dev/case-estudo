package br.com.banco.contas.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.banco.contas.dto.request.DepositaRequest;
import br.com.banco.contas.entity.enums.TipoOperacao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="tb_transacao")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transacao extends AbstractEntity implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	private BigDecimal valor;
	
	@Column(nullable = false)
	private BigDecimal saldoAnterior;
	
	@Column(nullable = false)
	private BigDecimal saldoAtual;
	
	@Column(nullable = false)
	private TipoOperacao tipoOperacao;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "correntista_id")
	private Correntista correntista;
	
}
