package com.inovasoft.minhasfinancas.exception;

public class RegraNegocioException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RegraNegocioException(String msgError) {
		super(msgError);
	}
}
