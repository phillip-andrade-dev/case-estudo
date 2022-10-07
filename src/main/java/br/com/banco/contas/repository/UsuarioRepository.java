package br.com.banco.contas.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import br.com.banco.contas.entity.Usuario;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {
	Optional<Usuario> findByUsername(String username);
}
