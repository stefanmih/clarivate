package com.clarivate.exception;

public class InvalidObjectTypeException extends Exception {
    public InvalidObjectTypeException(String actual, String expected) {
        super("Invalid object type on this position: Actual " + actual + ", expected " + expected);
    }
}
