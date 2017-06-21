package com.awoisoak.giphyviewer.data.remote;

import android.util.Log;

import com.awoisoak.giphyviewer.data.Gif;
import com.awoisoak.giphyviewer.data.remote.responses.ErrorResponse;
import com.awoisoak.giphyviewer.data.remote.responses.ListsGifsResponse;

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
//TODO check the number of pages is bigger than 1
        GiphyManager.getInstance().search("Putin", 0, new GiphyListener<ListsGifsResponse>() {
            @Override
            public void onResponse(ListsGifsResponse response) {
                final List<Gif> list = response.getList();
                assert (list.size() == GiphyApi.MAX_NUMBER_GIFS_RETURNED);
                for (Gif gif : list){
                    assertNotNull(gif.getUrl());
                    assertNotEquals(gif.getUrl(),"");
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
        //TODO check the number of pages is just 1

        GiphyManager.getInstance().trending(new GiphyListener<ListsGifsResponse>() {
            @Override
            public void onResponse(ListsGifsResponse response) {
                final List<Gif> list = response.getList();
                assert (list.size() == GiphyApi.MAX_NUMBER_GIFS_RETURNED);
                for (Gif gif : list){
                    assertNotNull(gif.getUrl());
                    assertNotEquals(gif.getUrl(),"");
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