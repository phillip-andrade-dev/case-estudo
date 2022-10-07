package br.com.banco.contas.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Table(name = "tb_gerente")
@AllArgsConstructor
@Builder
public class Gerente extends AbstractEntity implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	private String nome;
	
	@Column(nullable = false)
	private String email;

	@Column(nullable = false, unique = true)
	private String cpf;
	
	@OneToMany(mappedBy = "gerente")
	private List<Correntista> correntistas;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "usuario_id", nullable = true)
	private Usuario usuario;
	
	public Gerente() {
		// Construtor padrão
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}



	public List<Correntista> getCorrentistas() {
		return correntistas;
	}

	public void setCorrentistas(List<Correntista> correntistas) {
		this.correntistas = correntistas;
	}

}
