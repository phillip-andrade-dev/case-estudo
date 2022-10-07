package br.com.banco.contas.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.banco.contas.entity.Correntista;

public interface CorrentistaRepository extends JpaRepository<Correntista, Long> {

    public Optional<Correntista> findByCpf (String cpf);

    public Optional<Correntista> findByConta (String conta);
}
