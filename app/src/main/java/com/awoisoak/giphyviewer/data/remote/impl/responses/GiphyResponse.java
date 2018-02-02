package com.awoisoak.giphyviewer.data.remote.impl.responses;

import com.awoisoak.giphyviewer.data.remote.GiphyApi;

/**
 * Giphy Response
 */

public class GiphyResponse {
    public static final String TAG = ListGifsResponse.class.getSimpleName();


    private GiphyApi.REQUEST_TYPE type;
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

    public GiphyApi.REQUEST_TYPE getTYPE() {
        return type;
    }

    public void setType(GiphyApi.REQUEST_TYPE type) {
        this.type = type;
    }

}
