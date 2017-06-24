package com.awoisoak.giphyviewer.presentation.main.onlinefragment;

import com.awoisoak.giphyviewer.data.Gif;

import java.util.List;


public interface OnlineGifsView {

    /**
     * Hide the initial ProgressBar displayed before the trending gifs are retrieved
     */
    void hideProgressBar();

    /**
     * Bind the Gifs retrieved.
     * The implementation should create the adapter and set them to the RecyclerView
     *
     * @param gifs
     */
    void bindGifsList(List<Gif> gifs);

    /**
     * Update the adapter with the new gifs received
     *
     * @param gifs
     */
    void updateGifsList(List<Gif> gifs);

    /**
     * Display Snackbar to inform the user of new gifs being retrieved
     */
    void showLoadingSnackbar();


    /**
     * Display Error Snackbar to inform the user there was an error and ask if he/she want to retry.
     */
    void showErrorSnackbar();


    /**
     * Hide any of the previous Snackbar
     */
    void hideSnackbar();


    /**
     * Returns the last search term introduced by the user
     */
    String getSearchText();

    /**
     * Display a toast with the passed message
     *
     * @param message
     */
    void showToast(String message);


}
