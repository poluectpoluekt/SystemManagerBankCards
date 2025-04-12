package com.ed.sysbankcards.exception.card;

public class InsufficientFundsException extends RuntimeException{

    public InsufficientFundsException(){
        super("Insufficient Funds");
    }
}
