package com.awoisoak.giphyviewer.source.remote.responses;

/**
 *  Giphy Response
 */

public class GiphyResponse {
    int code;
    int totalPages;
    int totalRecords;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }
}
