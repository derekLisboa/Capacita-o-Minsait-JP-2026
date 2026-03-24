package br.com.indra.derek_lisboa.exception;

public class InsufficientStockException extends RuntimeException{

    public InsufficientStockException(String message) {
        super(message);
    }
}
