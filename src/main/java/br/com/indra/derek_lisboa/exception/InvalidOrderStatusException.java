package br.com.indra.derek_lisboa.exception;

public class InvalidOrderStatusException extends RuntimeException{

    public InvalidOrderStatusException(String message){
        super(message);
    }
}
