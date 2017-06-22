package com.awoisoak.giphyviewer.data.remote;

import android.util.Log;


import com.awoisoak.giphyviewer.data.Gif;
import com.awoisoak.giphyviewer.data.remote.responses.ListGifsResponse;

import java.util.ArrayList;
import java.util.List;

/**
 *  Mock Wordpress Manager
 */

public class MockGiphyManager implements GiphyApi {
    private static final String TAG = MockGiphyManager.class.getSimpleName();

    /**
     * It assumes there is 30 gifs in the website. As the default per_page argument is 25, it means 2 total pages
     * +info: https://developers.giphy.com/docs/
     */
    private static int TOTAL_SEARCH_RECORDS = 30;
    private static int TOTAL_SEARCH_PAGES = 2;

    /**
     * By default trending query will return 25 items in a single page
     * +info: https://developers.giphy.com/docs/
     */
    private static int TOTAL_TRENDING_RECORDS = 25;
    private static int TOTAL_TRENDING_PAGES = 1;

    private static int STATUS_OK = 200;

    static MockGiphyManager mInstance;

    public static MockGiphyManager getInstance() {
        if (mInstance == null) {
            mInstance = new MockGiphyManager();
        }
        return mInstance;
    }



    @Override
    public void search(String text, int offset, GiphyListener<ListGifsResponse> l) {
        Log.d(TAG,"MockGiphyManager | search");

        List<Gif> gifList = new ArrayList<>();
        ListGifsResponse r = null;

        //Fill the List with all Gifs available
        Gif Gif;
        for (int i = 0; i < TOTAL_SEARCH_RECORDS; i++) {
            Gif = new Gif(Integer.toString(i), "https://media1.giphy.com/media/5zGPIuq3IOfXG/giphy.gif",false);
            gifList.add(Gif);
        }
        // Modify the List depending on the offset
        if (offset < gifList.size()) {
            gifList = gifList.subList(offset, gifList.size() - 1);
            r = new ListGifsResponse(gifList);
            r.setCode(STATUS_OK);
            r.setTotalPages(TOTAL_SEARCH_PAGES);
            r.setTotalRecords(TOTAL_SEARCH_RECORDS);
        } else {
            List<Gif> emptyList = new ArrayList<>();
            r = new ListGifsResponse(emptyList);
            r.setCode(STATUS_OK);
            r.setTotalPages(0);
            r.setTotalRecords(0);
        }

        try {
            Log.d(TAG,"Sending search request to the Mock server.... ;)");
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        l.onResponse(r);
        
    }

    @Override
    public void trending(GiphyListener<ListGifsResponse> l) {
        Log.d(TAG,"MockGiphyManager | search");

        List<Gif> gifList = new ArrayList<>();

        //Fill the List with all Gifs available
        Gif Gif;
        for (int i = 0; i < TOTAL_SEARCH_RECORDS; i++) {
            Gif = new Gif(Integer.toString(i), "http://i.imgur.com/h5rfQ6V.gif",false);
            gifList.add(Gif);
        }
        ListGifsResponse r = new ListGifsResponse(gifList);

        // Modify the List depending on the offset
        r.setCode(STATUS_OK);
        r.setTotalPages(TOTAL_TRENDING_PAGES);
        r.setTotalRecords(TOTAL_TRENDING_RECORDS);

        try {
            Log.d(TAG,"Sending trending request to the Mock server.... ;)");
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        l.onResponse(r);
    }
}
