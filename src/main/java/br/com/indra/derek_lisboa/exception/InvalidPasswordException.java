package br.com.indra.derek_lisboa.exception;

public class InvalidPasswordException extends RuntimeException{

    public InvalidPasswordException(String message) {
        super(message);
    }
}
