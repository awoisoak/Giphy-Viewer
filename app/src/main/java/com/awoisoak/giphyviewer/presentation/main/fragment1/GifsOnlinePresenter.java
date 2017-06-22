package com.awoisoak.giphyviewer.presentation.main.fragment1;


import com.awoisoak.giphyviewer.presentation.IPresenter;

public interface GifsOnlinePresenter extends IPresenter {

    /**
     * Called when the user choose to retry a request who had failed
     */
    void onRetryGifsRequest();

    /**
     * Called when the user reaches the bottom of the RecyclerView
     */
    void onBottomReached();

    /**
     * Called when the user set/unset as favourite one of the gifs
     */
    void onGifSetAsFavourite(boolean favourite);

}
