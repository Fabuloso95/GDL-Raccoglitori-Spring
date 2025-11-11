package com.gdl_raccoglitori.exceptionhandler.exception;

public class TitoloDuplicatoException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public TitoloDuplicatoException(String titolo) 
	{
        super("Esiste già un libro con il titolo: " + titolo);
    }
}