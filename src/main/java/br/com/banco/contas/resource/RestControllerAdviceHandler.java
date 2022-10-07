package br.com.banco.contas.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;

import br.com.banco.contas.base.ServerSideResponse;
import br.com.banco.contas.exception.CampoNaoExistente;
import br.com.banco.contas.exception.CorrentistaJaAtivoException;
import br.com.banco.contas.exception.CorrentistaNaoEncontradoException;
import br.com.banco.contas.exception.GerenteInexistenteException;
import br.com.banco.contas.exception.GerenteJaAtivo;
import br.com.banco.contas.exception.RegraDeNegocioException;
import br.com.banco.contas.exception.SaldoInsuficienteException;
import br.com.banco.contas.exception.UsuarioDesativadoException;
import br.com.banco.contas.exception.UsuarioJaCriado;
import br.com.banco.contas.exception.UsuarioSemRoleException;

@RestControllerAdvice
public class RestControllerAdviceHandler {
	
	@ExceptionHandler(CorrentistaJaAtivoException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public ServerSideResponse<?> correntistaJaExiste(CorrentistaJaAtivoException e, WebRequest request) {
		return ServerSideResponse.builder().mensagem("Este correntista já existe").statusCode(HttpStatus.BAD_REQUEST.value()).build();
	}
	@ExceptionHandler(CampoNaoExistente.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public ServerSideResponse<?> campoNaoExiste(CampoNaoExistente e, WebRequest request) {
		return ServerSideResponse.builder().mensagem(e.getMessage()).statusCode(HttpStatus.BAD_REQUEST.value()).build();
	}
	
	@ExceptionHandler(GerenteJaAtivo.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public ServerSideResponse<?> gerenteJaExiste(GerenteJaAtivo e, WebRequest request) {
		return ServerSideResponse.builder().mensagem("Este gerente já existe").statusCode(HttpStatus.BAD_REQUEST.value()).build();
	}

	@ExceptionHandler(GerenteInexistenteException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public ServerSideResponse<?> gerenteInexiste(GerenteInexistenteException e, WebRequest request) {
		return ServerSideResponse.builder().mensagem("Gerente informado não encontrado.").statusCode(HttpStatus.BAD_REQUEST.value()).build();
	}
  
	@ExceptionHandler(UsuarioJaCriado.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public ServerSideResponse<?> gerenteInexiste(UsuarioJaCriado e, WebRequest request) {
		return ServerSideResponse.builder().mensagem("Usuário já criado anteriormente.").statusCode(HttpStatus.BAD_REQUEST.value()).build();
  }
	
	@ExceptionHandler(BadCredentialsException.class)
	@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
	public ServerSideResponse<?> dadosLoginInvalidos(BadCredentialsException e, WebRequest request) {
		return ServerSideResponse.builder().mensagem("Usuário e/ou senha incorretos.").statusCode(HttpStatus.UNAUTHORIZED.value()).build();
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public ServerSideResponse<?> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
		List<String> details = new ArrayList<>();
		for (ObjectError error : ex.getBindingResult().getAllErrors()) {
			details.add(error.getDefaultMessage());
		}

		Map<String, Object> errosDeValidacao = new HashMap<String, Object>();

		errosDeValidacao.put("detalhes", details);

		return ServerSideResponse.builder().mensagem("Validação: parâmetros de entrada estão incorretos")
				.statusCode(HttpStatus.BAD_REQUEST.value()).extra(errosDeValidacao).build();
	}

	@ExceptionHandler(RegraDeNegocioException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public ServerSideResponse<?> regraDeNegocioException(RegraDeNegocioException e, WebRequest request) {
		return ServerSideResponse.builder().mensagem(e.getMessage()).statusCode(HttpStatus.BAD_REQUEST.value()).build();
	}


	@ExceptionHandler(CorrentistaNaoEncontradoException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public ServerSideResponse<?> correntistaNaoEcontradoException(CorrentistaNaoEncontradoException e, WebRequest request) {
		return ServerSideResponse.builder().mensagem(e.getMessage()).statusCode(HttpStatus.NOT_FOUND.value()).build();
	}
	
	@ExceptionHandler(SaldoInsuficienteException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public ServerSideResponse<?> saldoInsuficienteException(SaldoInsuficienteException e, WebRequest request) {
		return ServerSideResponse.builder().mensagem("Você não possui saldo suficiente").statusCode(HttpStatus.BAD_REQUEST.value()).build();
	}
	
	@ExceptionHandler(TokenExpiredException.class)
	@ResponseStatus(code = HttpStatus.FORBIDDEN)
	public ServerSideResponse<?> tokenExpirado(TokenExpiredException e, WebRequest request) {
		return ServerSideResponse.builder().mensagem("Token expirado").statusCode(HttpStatus.FORBIDDEN.value()).build();
	}
	
	@ExceptionHandler(InvalidClaimException.class)
	@ResponseStatus(code = HttpStatus.FORBIDDEN)
	public ServerSideResponse<?> tokenInvalido(InvalidClaimException e, WebRequest request) {
		return ServerSideResponse.builder().mensagem("Token inválido").statusCode(HttpStatus.FORBIDDEN.value()).build();
	}
	
	@ExceptionHandler(SignatureVerificationException.class)
	@ResponseStatus(code = HttpStatus.FORBIDDEN)
	public ServerSideResponse<?> tokenAssinatura(SignatureVerificationException e, WebRequest request) {
		return ServerSideResponse.builder().mensagem("Assinatura inválida.").statusCode(HttpStatus.FORBIDDEN.value()).build();
	}
	
	
	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(code = HttpStatus.FORBIDDEN)
	public ServerSideResponse<?> tokenAssinatura(AccessDeniedException e, WebRequest request) {
		return ServerSideResponse.builder().mensagem("Acesso negado").statusCode(HttpStatus.FORBIDDEN.value()).build();
	}
	
	@ExceptionHandler(UsuarioSemRoleException.class)
	@ResponseStatus(code = HttpStatus.FORBIDDEN)
	public ServerSideResponse<?> tokenAssinatura(UsuarioSemRoleException e, WebRequest request) {
		return ServerSideResponse.builder().mensagem("Usuário sem permissões").statusCode(HttpStatus.FORBIDDEN.value()).build();
	}
	
	
	@ExceptionHandler(UsuarioDesativadoException.class)
	@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
	public ServerSideResponse<?> tokenAssinatura(UsuarioDesativadoException e, WebRequest request) {
		return ServerSideResponse.builder().mensagem("Usuário sem permissões").statusCode(HttpStatus.UNAUTHORIZED.value()).build();
	}
	
	
}
