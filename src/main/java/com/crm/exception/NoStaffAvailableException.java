package com.crm.exception;



public class NoStaffAvailableException extends RuntimeException 
{
    public NoStaffAvailableException(String message) {
        super(message);
    }
}