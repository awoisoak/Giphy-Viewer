package com.awoisoak.giphyviewer.presentation.main.fragment1;

import android.app.Fragment;

import com.awoisoak.giphyviewer.data.Gif;

import java.util.List;


public interface GifsOnlineView{

    /**
     * Hide the initial ProgressBar displayed before the trending gifs are retrieved
     */
    void hideProgressBar();

    /**
     * Bind the Gifs retrieved.
     * The implementation should create the adapter and set them to the RecyclerView
     * @param gifs
     */
    void bindPostsList(List<Gif> gifs);

    /**
     * Update the adapter with the new gifs received
     * @param gifs
     */
    void updatePostsList(List<Gif> gifs);

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
     * Set the specific gif as favourite.
     * It will have to change the icon to be displayed as favourite by the user
     */
    void setGifAsFavourite(boolean favourite);

}
