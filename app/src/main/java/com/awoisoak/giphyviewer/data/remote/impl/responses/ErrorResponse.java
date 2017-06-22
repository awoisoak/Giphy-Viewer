package com.awoisoak.giphyviewer.data.remote.impl.responses;

/**
 * Generic Error Response
 */

public class ErrorResponse extends GiphyResponse {

    String message;

    public ErrorResponse(String message) {
        this.message = message;

    }

    public String getMessage() {
        return message;
    }

}
