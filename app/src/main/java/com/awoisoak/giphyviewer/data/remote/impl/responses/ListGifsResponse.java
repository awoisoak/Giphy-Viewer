package com.awoisoak.giphyviewer.data.remote.impl.responses;

import com.awoisoak.giphyviewer.data.Gif;

import java.util.List;

/**
 * Response for a List Gifs Requests
 */
public class ListGifsResponse extends GiphyResponse {

    List<Gif> list;

    public ListGifsResponse(List<Gif> list) {
        this.list = list;
    }

    public List<Gif> getList() {
        return list;
    }
}
