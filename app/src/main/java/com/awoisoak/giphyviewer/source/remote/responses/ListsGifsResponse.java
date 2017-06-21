package com.awoisoak.giphyviewer.source.remote.responses;

import com.awoisoak.giphyviewer.source.Gif;

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
