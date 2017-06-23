package com.awoisoak.giphyviewer.presentation;


/**
 * Interface to be implemented by all Presenters
 * <p>
 * - Signals registration should be done in onCreate/onDestroy or onStart/onStop depending on the requirements
 * - View reference shall be set to null in onDestroy
 */
public interface IPresenter {

    void onCreate();

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();


}
