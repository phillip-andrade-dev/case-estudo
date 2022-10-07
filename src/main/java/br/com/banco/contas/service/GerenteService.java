package br.com.banco.contas.service;

import java.lang.reflect.Field;
import java.util.Optional;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import br.com.banco.contas.dto.request.GerenteRequest;
import br.com.banco.contas.dto.request.GerenteRequestPatch;
import br.com.banco.contas.dto.response.GerenteResponse;
import br.com.banco.contas.entity.Gerente;
import br.com.banco.contas.exception.GerenteInexistenteException;
import br.com.banco.contas.exception.GerenteJaAtivo;
import br.com.banco.contas.repository.GerenteRepository;
import lombok.AllArgsConstructor;

@Service 
@AllArgsConstructor
public class GerenteService {
	private final GerenteRepository gerenteRepository;
	private final ModelMapper modelMapper;
	
	public GerenteResponse createGerente(GerenteRequest request) throws GerenteJaAtivo {
		Optional<Gerente> findByCpf = gerenteRepository.findByCpf(request.getCpf());
		if(findByCpf.isPresent()) throw new GerenteJaAtivo("gerente não encontrado");
		Gerente gerente = this.fromDTO(request);
		gerenteRepository.save(gerente);
		return this.toDTO(gerente);
		
	}
	@Transactional
	public GerenteResponse updateTotalyGerente(GerenteRequest request,Long id) throws GerenteJaAtivo, GerenteInexistenteException {
		Optional<Gerente> gerenteOptional = gerenteRepository.findById(id);
		if(!gerenteOptional.isPresent()) throw new GerenteInexistenteException("gerente não encontrado");
		Gerente gerente = gerenteOptional.get();
		if(gerente.getCpf()!=request.getCpf()) {
			Optional<Gerente> findByCpf = gerenteRepository.findByCpf(request.getCpf());
			if(!findByCpf.isPresent()) gerente.setCpf(request.getCpf());
			else throw new GerenteJaAtivo("gerente não encontrado");
		}
		gerente.setEmail(request.getEmail());
		gerente.setNome(request.getNome());
		return this.toDTO(gerente);
	}
	@Transactional
	public GerenteResponse updatePartityGerente(GerenteRequestPatch request,Long id) throws GerenteInexistenteException, GerenteJaAtivo {
		Optional<Gerente> gerenteOptional = gerenteRepository.findById(id);
		if(!gerenteOptional.isPresent())throw new GerenteInexistenteException("gerente não encontrado");
		Gerente gerente = gerenteOptional.get();
		String cpf = request.getCpf();
		String nome = request.getNome();
		String email = request.getEmail();
		if(cpf!=null && cpf!=gerente.getCpf()) {
			if(gerente.getCpf()!=request.getCpf()) {
				Optional<Gerente> findByCpf = gerenteRepository.findByCpf(cpf);
				if(!findByCpf.isPresent()) gerente.setCpf(request.getCpf());
				else throw new GerenteJaAtivo("CPF já cadastrado");
			}
		}
		if(nome!=null)gerente.setNome(nome);
		if(email!=null)gerente.setEmail(email);
		return this.toDTO(gerente);
	}
	
    public Gerente fromDTO(GerenteRequest request) {
    	return modelMapper.map(request, Gerente.class);
    }
    public GerenteResponse toDTO(Gerente entity) {
    	return modelMapper.map(entity, GerenteResponse.class);
    }
	
}
