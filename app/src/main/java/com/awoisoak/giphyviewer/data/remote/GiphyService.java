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
     * The maximum number of records to return per query is {@link GiphyApi#MAX_NUMBER_SEARCH_GIFS_RETURNED}
     *
     * @param text   Search query term or prhase.
     * @param offset Defaults to 0.
     * @param limit  The maximum number of records to return. (default 25)
     */
    @GET("/v1/gifs/search")
    Call<ListGifsResponse> search(@Query("q") String text, @Query("offset") int offset, @Query("limit") int limit);

    /**
     * Fetch GIFs currently trending online. Hand curated by the GIPHY editorial team.
     * The data returned mirrors the GIFs showcased on the GIPHY homepage.
     * Returns {@link GiphyApi#MAX_NUMBER_TRENDING_GIFS_RETURNED}
     *
     * @param limit The maximum number of records to return. (default 25)
     */
    @GET("/v1/gifs/trending")
    Call<ListGifsResponse> trending(@Query("limit") int limit);
}
