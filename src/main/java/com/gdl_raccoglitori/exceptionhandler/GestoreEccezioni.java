package com.gdl_raccoglitori.exceptionhandler;

import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import com.gdl_raccoglitori.exceptionhandler.exception.RisorsaNonTrovataException;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GestoreEccezioni
{
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MessaggioErrore> handleValidazioneAutomatica(MethodArgumentNotValidException ex, WebRequest req)
    {
        Map<String, String> errori = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField, DefaultMessageSourceResolvable::getDefaultMessage));

        MessaggioErrore errore = new MessaggioErrore(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.name(),
                req.getDescription(false),
                errori
        );

        return ResponseEntity.badRequest().body(errore);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<MessaggioErrore> handleValidazioneManuale(ConstraintViolationException ex, WebRequest req)
    {
        Map<String, String> errori = ex.getConstraintViolations()
                .stream()
                .collect(Collectors.toMap(
                        v -> v.getPropertyPath().toString(),
                        v -> v.getMessage()
                ));

        MessaggioErrore errore = new MessaggioErrore(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.name(),
                req.getDescription(false),
                errori
        );

        return ResponseEntity.badRequest().body(errore);
    }

    @ExceptionHandler(RisorsaNonTrovataException.class)
    public ResponseEntity<MessaggioErrore> handleRisorsaNonTrovata(RisorsaNonTrovataException ex, WebRequest req)
    {
        MessaggioErrore errore = new MessaggioErrore(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.name(),
                req.getDescription(false),
                Map.of("messaggio", ex.getMessage())
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errore);
    }
}