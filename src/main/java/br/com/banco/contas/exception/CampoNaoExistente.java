package br.com.banco.contas.exception;

public class CampoNaoExistente extends RegraDeNegocioException {

	public CampoNaoExistente(String mensagem) {
		super(mensagem);
	}

}
