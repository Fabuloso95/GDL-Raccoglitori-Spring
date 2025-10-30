package com.gdl_raccoglitori.exceptionhandler.exception;

public class ConflittoDatiException extends RuntimeException 
{
	private static final long serialVersionUID = 1L;

	public ConflittoDatiException(String message)
	{
        super(message);
    }
}