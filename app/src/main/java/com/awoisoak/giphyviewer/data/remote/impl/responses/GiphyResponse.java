package com.awoisoak.giphyviewer.data.remote.impl.responses;

/**
 *  Giphy Response
 */

public class GiphyResponse {
    int code;
    int totalRecords;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }
}
