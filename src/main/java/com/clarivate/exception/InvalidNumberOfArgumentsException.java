package com.clarivate.exception;

public class InvalidNumberOfArgumentsException extends Exception {
    public InvalidNumberOfArgumentsException(int provided, int expected) {
        super("Invalid number of objects: Actual " + provided + ", expected " + expected);
    }
}
