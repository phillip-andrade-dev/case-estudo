package br.com.banco.contas.exception;

public class InfraestruturaException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InfraestruturaException(String mensagem) {
		super(mensagem);
	}
	
	public InfraestruturaException() {
	}

}
