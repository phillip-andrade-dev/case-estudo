package br.com.banco.contas.exception;

public class CorrentistaNaoEncontradoException extends RegraDeNegocioException{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CorrentistaNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
