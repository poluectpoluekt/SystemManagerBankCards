package com.ed.sysbankcards.exception.card;

public class CardByIdNotExistException extends RuntimeException {
    public CardByIdNotExistException(Long id) {
        super(String.format("Card with id %d does not exist", id));
    }
}
