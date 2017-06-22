package com.awoisoak.giphyviewer.data.remote;


import com.awoisoak.giphyviewer.data.remote.responses.ListGifsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * WordPress services
 */

public interface GiphyService {
//TODO we might use ORIGINAL if the UIX is better

    String DATA = "data";
    String ID = "id";
    String IMAGES = "images";
    String FIXED_HEIGHT = "fixed_height";
    String URL = "url";
    String PAGINATION = "pagination";
    String TOTAL_COUNT = "total_count";

    /**
     * Search all GIPHY GIFs for a word or phrase.
     * The maximum number of records to return per query is {@link GiphyApi#MAX_NUMBER_GIFS_RETURNED}
     *
     *
     * @param offset, An optional results offset. Defaults to 0.
     */
    @GET("/v1/gifs/search")
    Call<ListGifsResponse> search(@Query("q") String text, @Query("offset") int offset);

    /**
     * Fetch GIFs currently trending online. Hand curated by the GIPHY editorial team.
     * The data returned mirrors the GIFs showcased on the GIPHY homepage.
     * Returns {@link GiphyApi#MAX_NUMBER_GIFS_RETURNED}
     *
     * @return
     */
    @GET("/v1/gifs/trending")
    Call<ListGifsResponse> trending();
}
