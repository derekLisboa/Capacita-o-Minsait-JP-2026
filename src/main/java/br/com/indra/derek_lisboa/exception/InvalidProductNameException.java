package br.com.indra.derek_lisboa.exception;

public class InvalidProductNameException extends RuntimeException{

    public InvalidProductNameException(String message) {
        super(message);
    }
}
