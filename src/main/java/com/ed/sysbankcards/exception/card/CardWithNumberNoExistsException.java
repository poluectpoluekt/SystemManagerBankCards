package com.ed.sysbankcards.exception.card;

public class CardWithNumberNoExistsException extends RuntimeException{

    public CardWithNumberNoExistsException(String cardNumber){
        super(String.format("Card number %s does not exist", cardNumber));
    }
}
