package org.kubis.exception;

// Default Application exception created for the purpose of demo task
public class ApplicationException extends RuntimeException {
    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
