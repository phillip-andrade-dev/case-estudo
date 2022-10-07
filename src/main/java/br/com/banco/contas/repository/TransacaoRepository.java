package br.com.banco.contas.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.banco.contas.entity.Transacao;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
	
	public Optional<Transacao> findTopByCorrentistaIdOrderByIdDesc (Long id);

	public Optional<Transacao> findByCorrentista(Long id);
}
