package com.awoisoak.giphyviewer.data.local.impl;

import com.awoisoak.giphyviewer.data.Gif;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;




import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Testing class for SQLiteGifDataStore class
 */
public class SQLiteGifDataStoreTest {
    SQLiteGifDataStore ds = new SQLiteGifDataStore();
    static final int LIST_SIZE = 10;
    static final int LONG_LIST_SIZE = 100;

    @Before
    public void setUp() throws Exception {
        ds.removeAllGifs();
    }

    @Test
    public void addGif() {
        Gif gif = new Gif("666", "http://www.awoisoak.com");
        ds.addGif(gif);
        assertTrue (ds.getAllGifs().size() == 1);

    }

    @Test
    public void addGifs()  {
        ds.addGifs(getGifList());
        assertTrue (ds.getAllGifs().size() == LIST_SIZE);
    }

    @Test
    public void removeGif() {
        ds.addGifs(getGifList());
        assertTrue (ds.getAllGifs().size() == LIST_SIZE);
        ds.removeGif("5");
        assertTrue (ds.getAllGifs().size() == LIST_SIZE - 1);

    }

    @Test
    public void getAllGifs()  {
        ds.addGifs(getGifList());
        assertTrue (ds.getAllGifs().size() == LIST_SIZE);
    }

    @Test
    public void getGifs()  {
        ds.addGifs(getLongGifList());
        List<Gif> gifsRetrieved = ds.getGifs(0);
       assertTrue(gifsRetrieved.size() == SQLiteGifDataStore.MAX_NUMBER_GIFS_RETURNED);

         gifsRetrieved = ds.getGifs(90);
        assertTrue(gifsRetrieved.size() == 10);
    }

    @Test
    public void removeAllGifs() {
        ds.addGifs(getGifList());
        ds.removeAllGifs();
        assertTrue(ds.getAllGifs().size() == 0);
    }



    private List<Gif> getGifList() {
        List<Gif> list = new ArrayList<>();
        Gif gif;
        for (int i = 0; i < LIST_SIZE; i++) {
            gif = new Gif(String.valueOf(i), "http://www.awoisoak.com");
            list.add(gif);
        }
        return list;
    }

    private List<Gif> getLongGifList() {
        List<Gif> list = new ArrayList<>();
        Gif gif;
        for (int i = 0; i < LONG_LIST_SIZE; i++) {
            gif = new Gif(String.valueOf(i), "http://www.awoisoak.com");
            list.add(gif);
        }
        return list;
    }
}