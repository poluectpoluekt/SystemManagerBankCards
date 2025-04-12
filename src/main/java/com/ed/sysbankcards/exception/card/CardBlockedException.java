package com.ed.sysbankcards.exception.card;

public class CardBlockedException extends RuntimeException{
    public CardBlockedException(){
        super("One or both cards blocked");
    }
}
