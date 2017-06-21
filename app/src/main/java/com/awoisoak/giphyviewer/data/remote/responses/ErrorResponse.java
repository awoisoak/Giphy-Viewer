package com.awoisoak.giphyviewer.data.remote.responses;

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
