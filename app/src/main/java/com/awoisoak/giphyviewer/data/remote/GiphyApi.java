package com.awoisoak.giphyviewer.data.remote;


import android.arch.lifecycle.LiveData;

import com.awoisoak.giphyviewer.data.Resource;
import com.awoisoak.giphyviewer.data.remote.impl.responses.GiphyResponse;

/**
 * Interface for the Giphy API
 * https://developers.giphy.com/docs/
 */

public interface GiphyApi {

    /**
     * Enum to specify the type of request
     */
    public enum REQUEST_TYPE {
        TRENDING,
        SEARCH
    }

    /**
     * Max number of records returned when searching for gifs
     */
    int MAX_NUMBER_SEARCH_GIFS_RETURNED = 25;

    /**
     * Max number of records returned for trending gifs
     */
    int MAX_NUMBER_TRENDING_GIFS_RETURNED = 5;

    /**
     * Search all GIPHY GIFs for a word or phrase.
     * The maximum number of records to return per query is {@link GiphyApi#MAX_NUMBER_SEARCH_GIFS_RETURNED}
     *
     * @param text,   Search query term or phrase.
     * @param offset, An optional results offset. Defaults to 0.
     * @return resource, a Livedata with a Resource wrapper over a ListGifsResponse
     */
   void search(String text, int offset);


    /**
     * Fetch GIFs currently trending online. Hand curated by the GIPHY editorial team.
     * The data returned mirrors the GIFs showcased on the GIPHY homepage.
     * Returns {@link GiphyApi#MAX_NUMBER_TRENDING_GIFS_RETURNED}
     *
     * @return resource, a Livedata with a Resource wrapper over a ListGifsResponse

     */

    void trending();


    /**
     * Return the observable object for search gifs
     * @return
     */
    public LiveData<Resource<GiphyResponse>> getSearchGifs();

    /**
     * Return the observable object for trending gifs
     * @return
     */
    public LiveData<Resource<GiphyResponse>> getTrendingGifs();

}
