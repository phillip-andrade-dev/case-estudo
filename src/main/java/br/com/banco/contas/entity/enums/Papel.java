package br.com.banco.contas.entity.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Papel implements GrantedAuthority {
	ROLE_CORRENTISTA,
	ROLE_GERENTE;
	
	@Override
	public String getAuthority() {
		return this.name();
	}

}
