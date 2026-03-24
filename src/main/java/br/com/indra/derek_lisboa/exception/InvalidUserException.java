package br.com.indra.derek_lisboa.exception;

public class InvalidUserException extends RuntimeException{

    public InvalidUserException(String message) {
        super(message);
    }
}
