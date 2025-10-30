package com.gdl_raccoglitori.exceptionhandler.exception;

public class RisorsaNonTrovataException extends RuntimeException 
{
	private static final long serialVersionUID = 1L;

    public RisorsaNonTrovataException(String message) 
    {
        super(message);
    }

    public RisorsaNonTrovataException(String message, Throwable cause) 
    {
        super(message, cause);
    }
}