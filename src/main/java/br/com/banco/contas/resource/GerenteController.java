package br.com.banco.contas.resource;

import java.net.URI;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.banco.contas.base.ServerSideResponse;
import br.com.banco.contas.dto.request.GerenteRequest;
import br.com.banco.contas.dto.request.GerenteRequestPatch;
import br.com.banco.contas.dto.response.CorrentistaResponse;
import br.com.banco.contas.dto.response.GerenteResponse;
import br.com.banco.contas.exception.GerenteInexistenteException;
import br.com.banco.contas.exception.GerenteJaAtivo;
import br.com.banco.contas.service.GerenteService;

@RestController
@RequestMapping("/gerente")
public class GerenteController {
	@Autowired
	private GerenteService gereneteService;
	
	@PostMapping
	public ResponseEntity<ServerSideResponse<GerenteResponse>>postGerente(@RequestBody @Valid GerenteRequest request) throws GerenteJaAtivo{
		GerenteResponse body = gereneteService.createGerente(request);
		ServerSideResponse<GerenteResponse> ssr = ServerSideResponse.<GerenteResponse>builder()
				.dados(body).statusCode(HttpStatus.CREATED.value()).build();
		return new ResponseEntity<ServerSideResponse<GerenteResponse>>(ssr, HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ServerSideResponse<GerenteResponse>> putGerente(@RequestBody @Valid GerenteRequest request,@PathVariable Long id) throws GerenteJaAtivo, GerenteInexistenteException{
		GerenteResponse body = gereneteService.updateTotalyGerente(request, id);
		ServerSideResponse<GerenteResponse> ssr = ServerSideResponse.<GerenteResponse>builder()
				.dados(body).statusCode(HttpStatus.OK.value()).build();
		return new ResponseEntity<ServerSideResponse<GerenteResponse>>(ssr, HttpStatus.OK);
	}
	@PatchMapping("/{id}")
	public ResponseEntity<ServerSideResponse<GerenteResponse>> patchGerente(@RequestBody @Valid GerenteRequestPatch request,@PathVariable Long id) throws GerenteInexistenteException, GerenteJaAtivo{
		GerenteResponse body = gereneteService.updatePartityGerente(request, id);
		ServerSideResponse<GerenteResponse> ssr = ServerSideResponse.<GerenteResponse>builder()
				.dados(body).statusCode(HttpStatus.OK.value()).build();
		return new ResponseEntity<ServerSideResponse<GerenteResponse>>(ssr, HttpStatus.OK);
		
	}
	
}
