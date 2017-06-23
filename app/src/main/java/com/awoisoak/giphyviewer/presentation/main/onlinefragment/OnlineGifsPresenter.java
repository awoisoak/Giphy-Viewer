package com.awoisoak.giphyviewer.presentation.main.onlinefragment;


import com.awoisoak.giphyviewer.data.Gif;
import com.awoisoak.giphyviewer.presentation.IFragmentPresenter;

public interface OnlineGifsPresenter extends IFragmentPresenter {

    /**
     * Called when the user choose to retry a search request which had failed
     */
    void onRetrySearchGifsRequest();

    /**
     * Called when the user choose to retry the automatic trending request which had failed
     */
    void onRetryTrendingGifsRequest();

    /**
     * Called when the user submit a new search
     * @param query text introduced by the user to be searched
     */
    void onSearchSubmitted(String query);

    /**
     * Called when the user reaches the bottom of the RecyclerView
     */
    void onBottomReached();

    /**
     * Called when the user set/unset as favourite one of the gifs
     */
    void onGifSetAsFavourite(Gif gif);


}
