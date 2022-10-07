package br.com.banco.contas.base;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@JsonInclude(Include.NON_NULL)
public class ServerSideResponse<T> {

	private T dados;
	
	private Map<String, Object> extra;
	
	private String mensagem;
	
	@Builder.Default
	private long timestamp = System.currentTimeMillis();
	
	private int statusCode;
	
	
}
