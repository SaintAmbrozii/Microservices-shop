package com.example.productservice.exception;

public class ProductPurchaseRequestException extends RuntimeException{

    public ProductPurchaseRequestException(String msg){
        super(msg);
    }
}
