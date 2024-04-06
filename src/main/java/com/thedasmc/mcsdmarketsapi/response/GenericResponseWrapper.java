package com.thedasmc.mcsdmarketsapi.response;

import com.thedasmc.mcsdmarketsapi.response.impl.ErrorResponse;

/**
 * Generic response
 * @param <T> Successful response object type
 */
public class GenericResponseWrapper<T> {

    protected boolean successful = false;
    protected T successfulResponse;
    protected ErrorResponse errorResponse;

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public T getSuccessfulResponse() {
        return successfulResponse;
    }

    public void setSuccessfulResponse(T successfulResponse) {
        this.successfulResponse = successfulResponse;
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }

    public void setErrorResponse(ErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }

    @Override
    public String toString() {
        return "GenericResponse{" +
            "successful=" + successful +
            ", successfulResponse=" + successfulResponse +
            ", errorResponse=" + errorResponse +
            '}';
    }
}
