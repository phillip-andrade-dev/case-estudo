package br.com.banco.contas.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import br.com.banco.contas.dto.request.CreateUsuarioRequest;
import br.com.banco.contas.entity.Correntista;
import br.com.banco.contas.entity.Usuario;
import br.com.banco.contas.exception.UsuarioJaCriado;
import br.com.banco.contas.repository.CorrentistaRepository;
import br.com.banco.contas.repository.GerenteRepository;
import br.com.banco.contas.repository.UsuarioRepository;
import lombok.AllArgsConstructor;

@Service 
@AllArgsConstructor
public class UsuarioService {
	
	private UsuarioRepository usuarioRepository;
	private CorrentistaRepository correntistaRepository;
	
	private final ModelMapper modelMapper ;
	@Transactional
	public String createUser(CreateUsuarioRequest request) throws UsuarioJaCriado {
		Optional<Usuario> usuarioOptional = usuarioRepository.findByUsername(request.getConta());
		Optional<Correntista> correntistaOptional = correntistaRepository.findByConta(request.getConta()); 
		if(usuarioOptional.isPresent()) throw new UsuarioJaCriado("usuario já existente");
		Usuario usr = this.fromDTO(request);
		usr.setSenha("$2a$10$AVGA6EPiQ1L9L/1EC9AzbOFmN1v3MCon03doZVOwHrIR1nW7guGS.");
		usr.setPapel("ROLE_CORRENTISTA");
		usr.setUsername(request.getConta());
		Usuario usuarioSalvo = usuarioRepository.save(usr);
		if(correntistaOptional.isPresent()) {
			Correntista correntista = correntistaOptional.get();
			correntista.setUsuario(usuarioSalvo);
			correntistaRepository.save(correntista);
		}
		
		return "Usuario Criado";
		
	}
	
	
	
    public Usuario fromDTO(CreateUsuarioRequest request) {
    	return modelMapper.map(request, Usuario.class);
    }
}
