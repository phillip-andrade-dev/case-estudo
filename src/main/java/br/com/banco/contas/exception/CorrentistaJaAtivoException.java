package br.com.banco.contas.exception;

public class CorrentistaJaAtivoException extends RegraDeNegocioException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CorrentistaJaAtivoException(String mensagem) {
		super(mensagem);
	}

}
