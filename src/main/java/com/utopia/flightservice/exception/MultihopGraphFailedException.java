package com.utopia.flightservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class MultihopGraphFailedException extends ResponseStatusException {
    private static final long serialVersionUID = 1L;

    public MultihopGraphFailedException(Throwable cause) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "Multihop Graph failed!",
                cause);
    }

}
