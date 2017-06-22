package com.awoisoak.giphyviewer.domain.interactors;

import com.awoisoak.giphyviewer.data.remote.GiphyApi;

public interface GifsRequestInteractor {

    /**
     * Search all GIPHY GIFs for the passed word or phrase.
     * The maximum number of records to retrieved per query is {@link GiphyApi#MAX_NUMBER_SEARCH_GIFS_RETURNED}
     */
    void searchGifs(String query, int offset);

    /**
     * Fetch GIFs currently trending online.
     *
     * Retrieves {@link GiphyApi#MAX_NUMBER_TRENDING_GIFS_RETURNED} gifs
     *
     */
    void getTrendingGifs();
}
