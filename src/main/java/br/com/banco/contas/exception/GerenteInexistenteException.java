package br.com.banco.contas.exception;

public class GerenteInexistenteException extends RegraDeNegocioException{

	public GerenteInexistenteException(String mensagem) {
		super(mensagem);
	}

}
