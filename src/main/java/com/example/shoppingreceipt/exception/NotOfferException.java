package com.example.shoppingreceipt.exception;

import lombok.Data;

@Data
public class NotOfferException extends RuntimeException{
    private int status;
    private String message;

    public NotOfferException(int status, String message) {
        this.status = status;
        this.message = message;
    }
}