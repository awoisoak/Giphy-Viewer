package com.awoisoak.giphyviewer.presentation.main.offlinefragment;

import com.awoisoak.giphyviewer.data.Gif;

import java.util.List;


public interface OfflineGifsView {


    /**
     * Bind the Gifs retrieved.
     * The implementation should create the adapter and set them to the RecyclerView
     * @param gifs
     */
    void bindGifsList(List<Gif> gifs);

    /**
     * Update the adapter with the new gifs received
     * @param gifs
     */
    void updateGifsList(List<Gif> gifs);

    /**
     * Display a toast with the passed message
     * @param message
     */
    void showtoast(String message);

}
