package com.awoisoak.giphyviewer.data.remote;

import com.awoisoak.giphyviewer.data.Gif;
import com.awoisoak.giphyviewer.data.remote.impl.GiphyManager;
import com.awoisoak.giphyviewer.data.remote.impl.responses.ErrorResponse;
import com.awoisoak.giphyviewer.data.remote.impl.responses.ListGifsResponse;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.List;

/**
 * Created by awo on 21/06/17.
 */
public class GiphyManagerTest {
    private static final String TAG = GiphyManagerTest.class.getSimpleName();

    @Test
    public void search_request() throws Exception {
        GiphyManager.getInstance().search("Putin", 0, new GiphyListener<ListGifsResponse>() {
            @Override
            public void onResponse(ListGifsResponse response) {
                final List<Gif> list = response.getList();
                assertTrue(list.size() <= GiphyApi.MAX_NUMBER_SEARCH_GIFS_RETURNED);
                for (Gif gif : list) {
                    assertNotNull(gif.getUrl());
                    assertNotEquals(gif.getUrl(), "");
                }

            }

            @Override
            public void onError(ErrorResponse error) {
                System.out.println(TAG + "search onError =" + error.getMessage());
                fail();
            }
        });
    }


    @Test
    public void trending_request() throws Exception {

        GiphyManager.getInstance().trending(new GiphyListener<ListGifsResponse>() {
            @Override
            public void onResponse(ListGifsResponse response) {
                final List<Gif> list = response.getList();
                assertTrue (list.size() == GiphyApi.MAX_NUMBER_TRENDING_GIFS_RETURNED);
                for (Gif gif : list) {
                    assertNotNull(gif.getUrl());
                    assertNotEquals(gif.getUrl(), "");
                }
            }

            @Override
            public void onError(ErrorResponse error) {
                System.out.println(TAG + "trending onError =" + error.getMessage());
                fail();
            }
        });
    }


}