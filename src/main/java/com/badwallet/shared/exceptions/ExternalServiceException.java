package com.badwallet.shared.exceptions;

/**
 * Exception levée lorsque l'appel au micro-service externe
 * payment-service (via le Proxy) échoue.
 */
public class ExternalServiceException extends RuntimeException {
    public ExternalServiceException(String message) {
        super(message);
    }

    public ExternalServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
