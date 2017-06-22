package com.awoisoak.giphyviewer.data.remote;


import com.awoisoak.giphyviewer.data.remote.responses.ListGifsResponse;

/**
 * Interface for the Giphy API
 * https://developers.giphy.com/docs/
 */

public interface GiphyApi {

    /**
     * Default max number of records in Giphy API
     */
    int MAX_NUMBER_GIFS_RETURNED = 25;

    /**
     * Search all GIPHY GIFs for a word or phrase.
     * The maximum number of records to return per query is {@link GiphyApi#MAX_NUMBER_GIFS_RETURNED}
     *
     * @param text, Search query term or phrase.
     * @param offset, An optional results offset. Defaults to 0.
     */
    void search(String text, int offset, GiphyListener<ListGifsResponse> l);


    /**
     * Fetch GIFs currently trending online. Hand curated by the GIPHY editorial team.
     * The data returned mirrors the GIFs showcased on the GIPHY homepage.
     * Returns {@link GiphyApi#MAX_NUMBER_GIFS_RETURNED}
     *
     * @return
     */

    void trending(GiphyListener<ListGifsResponse> l);





}
