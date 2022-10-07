package br.com.banco.contas.resource;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.banco.contas.base.ServerSideResponse;
import br.com.banco.contas.dto.request.CorrentistaRequest;
import br.com.banco.contas.dto.response.CorrentistaResponse;
import br.com.banco.contas.dto.response.ListaCorrentistaResponse;
import br.com.banco.contas.exception.CorrentistaJaAtivoException;
import br.com.banco.contas.exception.RegraDeNegocioException;
import br.com.banco.contas.service.CorrentistaService;

@RestController
@RequestMapping("/correntista")
public class CorrentistaController {

	@Autowired
	private CorrentistaService correntistaService;

	@PostMapping(consumes = "application/json", produces = "application/json")
	public ResponseEntity<ServerSideResponse<CorrentistaResponse>> post(
			@Valid @RequestBody CorrentistaRequest correntistaRequest)
			throws CorrentistaJaAtivoException, RegraDeNegocioException {
		
		CorrentistaResponse correntistaResponse;		

		correntistaResponse = correntistaService.create(correntistaRequest);

		ServerSideResponse<CorrentistaResponse> ssr = ServerSideResponse.<CorrentistaResponse>builder()
				.dados(correntistaResponse).statusCode(HttpStatus.CREATED.value()).build();

		return new ResponseEntity<ServerSideResponse<CorrentistaResponse>>(ssr, HttpStatus.CREATED);

	}

	@DeleteMapping(path = "/{numConta}")
	public ResponseEntity<ServerSideResponse<CorrentistaResponse>> delete(@PathVariable String numConta) throws RegraDeNegocioException {

		CorrentistaResponse correntistaResponse;
		correntistaResponse= correntistaService.delete(numConta);

		ServerSideResponse<CorrentistaResponse> ssr = ServerSideResponse.<CorrentistaResponse>builder()
				.dados(correntistaResponse).statusCode(HttpStatus.OK.value()).build();

		return new ResponseEntity<ServerSideResponse<CorrentistaResponse>>(ssr, HttpStatus.OK);
	}

	@PutMapping(path = "/{numConta}")
	public ResponseEntity<ServerSideResponse<CorrentistaResponse>> update(
			@Valid @RequestBody CorrentistaRequest correntistaRequest, @PathVariable String numConta) throws RegraDeNegocioException {

		CorrentistaResponse correntistaResponse;
		correntistaResponse = correntistaService.update(correntistaRequest, numConta);

		ServerSideResponse<CorrentistaResponse> ssr = ServerSideResponse.<CorrentistaResponse>builder()
				.dados(correntistaResponse).statusCode(HttpStatus.OK.value()).build();

		return new ResponseEntity<ServerSideResponse<CorrentistaResponse>>(ssr, HttpStatus.OK);
	}
	
	@GetMapping
	public List<ListaCorrentistaResponse> listaCorrentista(){
		
		return correntistaService.listaCorrentista();
	}
//	public Page<CorrentistaRequest> listaCorrentista(@PageableDefault(sort = "nome", direction = Direction.DESC, page = 0, size = 10) Pageable paginacao){
//		Page<CorrentistaRequest> listaCorrentista = correntistaService.findBy
//		return 		
//	}
}
