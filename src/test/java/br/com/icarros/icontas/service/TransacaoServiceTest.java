package br.com.icarros.icontas.service;

import br.com.banco.contas.config.security.data.UserDetailsICarros;
import br.com.banco.contas.config.security.service.UserDetailsServiceImpl;
import br.com.banco.contas.dto.request.CorrentistaRequest;
import br.com.banco.contas.dto.request.DepositaRequest;
import br.com.banco.contas.dto.request.GerenteCorrentistaRequest;
import br.com.banco.contas.dto.response.DepositaResponse;
import br.com.banco.contas.dto.response.SaldoResponse;
import br.com.banco.contas.entity.Correntista;
import br.com.banco.contas.entity.Transacao;
import br.com.banco.contas.entity.Usuario;
import br.com.banco.contas.entity.enums.TipoOperacao;
import br.com.banco.contas.entity.enums.UF;
import br.com.banco.contas.exception.RegraDeNegocioException;
import br.com.banco.contas.repository.CorrentistaRepository;
import br.com.banco.contas.repository.TransacaoRepository;
import br.com.banco.contas.service.TransacaoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TransacaoServiceTest {

    @Autowired
    private TransacaoService transacaoService;

    @MockBean
    private CorrentistaRepository correntistaRepository;

    @MockBean
    private TransacaoRepository transacaoRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @MockBean
    private SecurityContext securityContext;

    @Autowired
    private ModelMapper modelMapper;

    Correntista correntista;

    Transacao transacao;

    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken;

    Authentication authentication;

    DepositaRequest depositaRequest;

    DepositaResponse depositaResponse;

    @BeforeEach
    public void setup() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("1111");
        usuario.setPapel("ROLE_CORRENTISTA");
        usuario.setVersion(0);
        usuario.setSenha(bCryptPasswordEncoder.encode("123456"));

        usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken("1111", "123456");
        when(userDetailsServiceImpl.loadUserByUsername(anyString())).thenReturn(new UserDetailsICarros(usuario));
        authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

    }

    @Test
    public void testGetSaldo_Sucesso() throws RegraDeNegocioException {
        correntista = stubCorrentista();
        transacao = stubTransacao();

        when(correntistaRepository.findByConta(anyString())).thenReturn(Optional.of(correntista));
        when(transacaoRepository.findTopByCorrentistaIdOrderByIdDesc(correntista.getId())).thenReturn(Optional.of(transacao));

        SaldoResponse saldoResponse = transacaoService.saldo();

        assertNotNull(saldoResponse);
        assertEquals(transacao.getSaldoAtual(), saldoResponse.getSaldoAtual());
    }

    @Test
    public void testGetSaldo_ContaSemTransacao() throws RegraDeNegocioException {
        correntista = stubCorrentista();
        transacao = stubTransacao();

        when(correntistaRepository.findByConta(anyString())).thenReturn(Optional.of(correntista));
        when(transacaoRepository.findTopByCorrentistaIdOrderByIdDesc(anyLong())).thenReturn(Optional.of(transacao));

        SaldoResponse saldoResponse = transacaoService.saldo();

        assertNotNull(saldoResponse);
        assertEquals(new BigDecimal(0), saldoResponse.getSaldoAtual());
    }

    @Test
    public void testGetSaldo_CorrentistaNaoEncontradoException() throws RegraDeNegocioException {
        when(correntistaRepository.findByConta(anyString())).thenReturn(Optional.empty());
        assertThrows(RegraDeNegocioException.class,
                () -> {
                    transacaoService.saldo();
                }
        );
    }

    private Transacao stubTransacao(){
        return Transacao.builder()
                .valor(new BigDecimal(200))
                .saldoAnterior(new BigDecimal(0))
                .saldoAtual(new BigDecimal(100))
                .build();
    }

    private Transacao stubTransacaoDeposita(){
        return Transacao.builder()
                .valor(new BigDecimal(200))
                .saldoAnterior(new BigDecimal(0))
                .saldoAtual(new BigDecimal(100))
                .tipoOperacao(TipoOperacao.ENTRADA)
                .build();
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

    private DepositaRequest stubDeposita(){
        return DepositaRequest.builder()
                .vlrDeposita(new BigDecimal(150))
                .build();
    }

    private DepositaResponse stubDepositaResponse(){
        return DepositaResponse.builder()
                .valor(new BigDecimal(150))
                .build();
    }
}
