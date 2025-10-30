package com.gdl_raccoglitori.exceptionhandler.exception;

public class OperazioneNonAutorizzataException extends RuntimeException 
{
	private static final long serialVersionUID = 1L;

	public OperazioneNonAutorizzataException(String message)
	{
        super(message);
    }
}