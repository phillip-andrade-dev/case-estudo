package br.com.banco.contas.exception;

public class UsuarioSemRoleException extends InfraestruturaException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public UsuarioSemRoleException(String mensagem) {
		super(mensagem);
	}
	
	public UsuarioSemRoleException() {
	}

	
}
