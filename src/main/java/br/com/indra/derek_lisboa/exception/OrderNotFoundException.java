package br.com.indra.derek_lisboa.exception;

public class OrderNotFoundException  extends  RuntimeException{
    public OrderNotFoundException(String message) {
        super(message);
    }
}
