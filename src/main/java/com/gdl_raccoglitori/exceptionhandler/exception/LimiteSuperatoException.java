package com.gdl_raccoglitori.exceptionhandler.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class LimiteSuperatoException extends RuntimeException 
{
	private static final long serialVersionUID = 1L;

	public LimiteSuperatoException(String message) 
    {
        super(message);
    }

    public LimiteSuperatoException(String message, Throwable cause) 
    {
        super(message, cause);
    }
}
