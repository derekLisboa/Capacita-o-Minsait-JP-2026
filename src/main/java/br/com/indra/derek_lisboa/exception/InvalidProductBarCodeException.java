package br.com.indra.derek_lisboa.exception;

public class InvalidProductBarCodeException extends RuntimeException {
    public InvalidProductBarCodeException(String message) {
        super(message);
    }
}
