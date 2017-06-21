package com.awoisoak.giphyviewer.data.remote.responses;

import com.awoisoak.giphyviewer.data.Gif;

import java.util.List;

/**
 * Response for a List Posts Requests
 */
public class ListsGifsResponse extends GiphyResponse {

    List<Gif> list;

    public ListsGifsResponse(List<Gif> list) {
        this.list = list;
    }

    public List<Gif> getList() {
        return list;
    }
}
