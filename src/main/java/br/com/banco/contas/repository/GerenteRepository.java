package br.com.banco.contas.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.banco.contas.entity.Gerente;

import java.util.Optional;

public interface GerenteRepository extends CrudRepository<Gerente, Long> {

    public Optional<Gerente> findByCpf (String cpf);

    public Optional<Gerente> findByEmail (String email);
}
