package br.com.icarros.icontas.service;

import br.com.banco.contas.dto.request.CorrentistaRequest;
import br.com.banco.contas.dto.request.GerenteCorrentistaRequest;
import br.com.banco.contas.dto.response.ListaCorrentistaResponse;
import br.com.banco.contas.entity.Correntista;
import br.com.banco.contas.entity.Gerente;
import br.com.banco.contas.entity.enums.UF;
import br.com.banco.contas.exception.CorrentistaNaoEncontradoException;
import br.com.banco.contas.exception.RegraDeNegocioException;
import br.com.banco.contas.repository.CorrentistaRepository;
import br.com.banco.contas.repository.GerenteRepository;
import br.com.banco.contas.service.CorrentistaService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CorrentistaServiceTest {

    @InjectMocks
    private CorrentistaService correntistaService;

    @Mock
    private CorrentistaRepository correntistaRepository;

    @Mock
    private GerenteRepository gerenteRepository;

    @Mock
    private ModelMapper modelMapper;

    Gerente gerente;

    Correntista correntista;

    CorrentistaRequest correntistaRequest;

    @BeforeEach
    public void setup() {
        correntista = stubCorrentista();
        correntistaRequest = stubCorrentistaRequest();
        gerente = stubGerente();
    }

    @Test
    public void testCadastraCorrentista_Sucesso() throws RegraDeNegocioException {
        when(correntistaRepository.findByCpf(anyString())).thenReturn(Optional.empty());
        when(correntistaRepository.findByConta(anyString())).thenReturn(Optional.empty());
        when(gerenteRepository.findByCpf(anyString())).thenReturn(Optional.ofNullable(stubGerente()));
        when(correntistaService.fromDTO(correntistaRequest)).thenReturn(correntista);

        correntistaService.create(correntistaRequest);

        verify(correntistaRepository).save(any());
    }

    @Test
    public void testCadastroCorrentista_CorrentistaJaAtivoException(){
        when(correntistaRepository.findByCpf(anyString())).thenReturn(Optional.ofNullable(stubCorrentista()));
        assertThrows(RegraDeNegocioException.class,
                () -> {
                    correntistaService.create(stubCorrentistaRequest());
                }
        );
    }

    @Test
    public void testCadastroCorrentista_RegraDeNegocioException(){
        when(correntistaRepository.findByCpf(anyString())).thenReturn(Optional.empty());
        when(correntistaRepository.findByConta(anyString())).thenReturn(Optional.ofNullable(stubCorrentista()));
        assertThrows(RegraDeNegocioException.class,
                () -> {
                    correntistaService.create(stubCorrentistaRequest());
                }
        );
    }

    @Test
    public void testDeletaCorrentista_Sucesso() throws RegraDeNegocioException {
        Correntista stubCorrentista = stubCorrentista();
        stubCorrentista.setSituacao(true);
        when(correntistaRepository.findByConta(anyString())).thenReturn(Optional.of(stubCorrentista));

        correntistaService.delete("12345");

        verify(correntistaRepository).findByConta(anyString());
        assertEquals(false, stubCorrentista.getSituacao());
    }

    @Test
    public void testDeletaCorrentista_CorrentistaNaoEcontradoException(){
        when(correntistaRepository.findByConta(anyString())).thenReturn(Optional.empty());

        assertThrows(CorrentistaNaoEncontradoException.class,
                () -> {
                    correntistaService.delete("12345");
                }
        );
    }

    @Test
    public void testDeletaCorrentista_RegraDeNegocioException(){
        Correntista stubCorrentista = stubCorrentista();
        stubCorrentista.setSituacao(false);
        when(correntistaRepository.findByConta(anyString())).thenReturn(Optional.ofNullable(stubCorrentista));

        assertThrows(RegraDeNegocioException.class,
                () -> {
                    correntistaService.delete("12345");
                }
        );
    }

    @Test
    public void testUpdateCorrentista_Sucesso() throws RegraDeNegocioException {
        when(correntistaRepository.findByConta("123456")).thenReturn(Optional.of(stubCorrentista()));
        when(correntistaRepository.findByConta("12345")).thenReturn(Optional.empty());
        when(gerenteRepository.findByCpf(anyString())).thenReturn(Optional.ofNullable(stubGerente()));

        correntistaService.update(stubCorrentistaRequest(), "123456");

        verify(correntistaRepository).save(any());
    }

    @Test
    public void testUpdateCorrentista_CorrentistaNaoEcontradoException() throws RegraDeNegocioException {
        when(correntistaRepository.findByConta(anyString())).thenReturn(Optional.empty());

        assertThrows(RegraDeNegocioException.class,
                () -> {
                    correntistaService.update(stubCorrentistaRequest(), "1234");
                }
        );
    }

    @Test
    public void testListaCorrentista_Sucesso(){
        when(correntistaRepository.findAll(Sort.by(Sort.Direction.ASC, "nome"))).thenReturn(stubListaCorrentista());

        List<ListaCorrentistaResponse> listaCorrentistaResponses = correntistaService.listaCorrentista();

        assertNotNull(listaCorrentistaResponses);
    }

    private Correntista stubCorrentista(){
        return Correntista.builder()
                .cpf("73602050858")
                .agencia("001")
                .conta("12345")
                .nome("PESSOA_1")
                .email("pessoa1@icarros.com")
                .telefone("11940074048")
                .endereco("Rua Osias Correia")
                .cep("64204-245")
                .bairro("Reis Veloso")
                .cidade("Parnaíba")
                .uf(UF.PI)
                .usuario(null)
                .gerente(null)
                .transacoes(null)
                .situacao(true)
                .build();
    }

    private CorrentistaRequest stubCorrentistaRequest(){
        GerenteCorrentistaRequest gerenteCorrentistaRequest = new GerenteCorrentistaRequest();
        gerenteCorrentistaRequest.setCpf("40710878893");
        return CorrentistaRequest.builder()
                .cpf("73602050858")
                .agencia("001")
                .conta("12345")
                .nome("PESSOA_1")
                .email("pessoa1@icarros.com")
                .telefone("11940074048")
                .endereco("Rua Osias Correia")
                .cep("64204-245")
                .bairro("Reis Veloso")
                .cidade("Parnaíba")
                .uf(UF.PI)
                .gerente(gerenteCorrentistaRequest)
                .build();
    }

    private Gerente stubGerente(){
        return Gerente.builder()
                .nome("PESSOA_2")
                .email("pessoa2@icarros.com")
                .cpf("04027512057")
                .correntistas(null)
                .build();
    }

    private List<Correntista> stubListaCorrentista(){
        List<Correntista> listaCorrentista = new ArrayList<>();
        listaCorrentista.add(Correntista.builder()
                .cpf("73602050858")
                .agencia("001")
                .conta("12345")
                .nome("PESSOA_1")
                .email("pessoa1@icarros.com")
                .telefone("11940074048")
                .endereco("Rua Osias Correia")
                .cep("64204-245")
                .bairro("Reis Veloso")
                .cidade("Parnaíba")
                .uf(UF.PI)
                .usuario(null)
                .gerente(null)
                .transacoes(null)
                .situacao(true)
                .build());
        listaCorrentista.add(Correntista.builder()
                .cpf("40710878893")
                .agencia("001")
                .conta("321654")
                .nome("PESSOA_2")
                .email("pessoa2@icarros.com")
                .telefone("11940074012")
                .endereco("Rua Osias Correia")
                .cep("64204-245")
                .bairro("Reis Veloso")
                .cidade("Parnaíba")
                .uf(UF.PI)
                .usuario(null)
                .gerente(null)
                .transacoes(null)
                .situacao(true)
                .build());
        return listaCorrentista;
    }
}
