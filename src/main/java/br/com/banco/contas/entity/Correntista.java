package br.com.banco.contas.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.com.banco.contas.entity.enums.UF;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "tb_correntista")
public class Correntista extends AbstractEntity implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	private String cpf;

	@Column(nullable = false)
	private String agencia;
	
	@Column(nullable = false)
	private String conta;
	
	@Column(nullable = false)
	private String nome;
	
	@Column(nullable = false)
	private String email;
	
	@Column(nullable = false)
	private String telefone;
	
	@Column(nullable = false)
	private String endereco;
	
	@Column(nullable = false)
	private String cep;
	
	@Column(nullable = false)
	private String bairro;
	
	@Column(nullable = false)
	private String cidade;
	
	@Column(nullable = false)
	private UF uf;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "usuario_id", nullable = true)
	private Usuario usuario;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "gerente_id", nullable = false)
	private Gerente gerente;
	
	@OneToMany(mappedBy = "correntista", fetch = FetchType.LAZY)
	private List<Transacao> transacoes;

	@Column(nullable = false)
	private Boolean situacao;
}
