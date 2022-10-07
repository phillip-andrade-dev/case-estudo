package br.com.banco.contas.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.banco.contas.dto.request.CorrentistaRequest;
import br.com.banco.contas.dto.response.CorrentistaResponse;
import br.com.banco.contas.dto.response.ListaCorrentistaResponse;
import br.com.banco.contas.entity.Correntista;
import br.com.banco.contas.entity.Gerente;
import br.com.banco.contas.exception.CorrentistaJaAtivoException;
import br.com.banco.contas.exception.CorrentistaNaoEncontradoException;
import br.com.banco.contas.exception.GerenteInexistenteException;
import br.com.banco.contas.exception.RegraDeNegocioException;
import br.com.banco.contas.repository.CorrentistaRepository;
import br.com.banco.contas.repository.GerenteRepository;
import lombok.AllArgsConstructor;

@Service 
@AllArgsConstructor
public class CorrentistaService {

    private final CorrentistaRepository correntistaRepository;

    private final GerenteRepository gerenteRepository;

    private final ModelMapper mapper;
    
    @Transactional
	public CorrentistaResponse create(CorrentistaRequest correntistaRequest) throws RegraDeNegocioException, CorrentistaJaAtivoException {

    	Optional<Correntista> correntista = correntistaRepository.findByCpf(correntistaRequest.getCpf());

    	 if (correntista.isPresent()){
               throw  new CorrentistaJaAtivoException("Esse correntista já possui um cadastro ativo.");
         }

        validaNumeroConta(correntistaRequest.getConta());

        Gerente gerente = validaGerente(correntistaRequest);

        Correntista newCorrentista = fromDTO(correntistaRequest);
        newCorrentista.setGerente(gerente);
        newCorrentista.setSituacao(true);
        correntistaRepository.save(newCorrentista);
        return toResponse(newCorrentista);
    }

    @Transactional
    public CorrentistaResponse delete(String numConta) throws RegraDeNegocioException {
        Correntista correntista = correntistaRepository.findByConta(numConta)
                .orElseThrow(() -> new CorrentistaNaoEncontradoException("Correntista não encontrado"));

        if (!correntista.getSituacao()){
            throw new RegraDeNegocioException("Correntista Inativo");
        }

        correntista.setSituacao(false);
        correntistaRepository.save(correntista);
        return toResponse(correntista);
    }

    @Transactional
    public CorrentistaResponse update(CorrentistaRequest request, String numConta) throws RegraDeNegocioException {

        Correntista correntista = correntistaRepository.findByConta(numConta)
                .orElseThrow(() -> new CorrentistaNaoEncontradoException("Correntista não encontrado"));

        validaNumeroConta(request.getConta());


        Gerente gerente = validaGerente(request);

        correntista.setCpf(request.getCpf());
        correntista.setAgencia(request.getAgencia());
        correntista.setConta(request.getConta());
        correntista.setNome(request.getNome());
        correntista.setEmail(request.getEmail());
        correntista.setTelefone(request.getTelefone());
        correntista.setEndereco(request.getEndereco());
        correntista.setCep(request.getCep());
        correntista.setBairro(request.getBairro());
        correntista.setCidade(request.getCidade());
        correntista.setUf(request.getUf());
        correntista.setGerente(gerente);

        correntistaRepository.save(correntista);

        return toResponse(correntista);
    }

    public List<ListaCorrentistaResponse> listaCorrentista(){
    	List<Correntista> listaCorrentistas = (List<Correntista>) correntistaRepository.findAll(Sort.by(Sort.Direction.ASC, "nome"));
    	List<ListaCorrentistaResponse> listaCorrentistaResponse = new ArrayList();
    	for(Correntista c: listaCorrentistas) {
    		listaCorrentistaResponse.add(toListaResponse(c));
    	}
    	return listaCorrentistaResponse;
    }
    
    private ListaCorrentistaResponse toListaResponse(Correntista correntista) {
    	return mapper.map(correntista, ListaCorrentistaResponse.class);
    }

    public Correntista fromDTO(CorrentistaRequest request) {
        return mapper.map(request, Correntista.class);
    }

    private CorrentistaResponse toResponse(Correntista correntista) {
        return mapper.map(correntista, CorrentistaResponse.class);
    }

    public void validaNumeroConta(String numConta) throws RegraDeNegocioException {
        Optional<Correntista> correntista = correntistaRepository.findByConta(numConta);

        if(correntista.isPresent()){
            throw new RegraDeNegocioException("Número da conta já cadastrado. Favor altera-lo.");
        }
    }

    public Gerente validaGerente(CorrentistaRequest correntistaRequest) throws GerenteInexistenteException {
        return gerenteRepository.findByCpf(correntistaRequest.getGerente().getCpf())
                .orElseThrow(() -> new GerenteInexistenteException("Gerente informado não encontrado."));
    }

}
