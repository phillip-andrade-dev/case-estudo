package br.com.banco.contas.exception;

public class UsuarioJaCriado extends RegraDeNegocioException {

	public UsuarioJaCriado(String mensagem) {
		super(mensagem);
	}

}
