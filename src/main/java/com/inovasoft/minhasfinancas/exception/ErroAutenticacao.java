package com.inovasoft.minhasfinancas.exception;

public class ErroAutenticacao extends RuntimeException {
	
	public ErroAutenticacao (String msgErroAutenticacao) {
		super(msgErroAutenticacao);
	}

}
