package br.com.indra.derek_lisboa.exception;

public class InvalidCartException extends  RuntimeException{

    public InvalidCartException(String message) {
        super(message);
    }
}
