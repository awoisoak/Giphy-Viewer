package com.awoisoak.giphyviewer.data;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Room;
import android.support.annotation.NonNull;


/**
 * Gif Object
 */
//TODO set id as an index?
@Entity(tableName = "gifs",indices = {@Index(value = {"id"}, unique = true)})
public class Gif {
    public final static String URL = "url";
    public final static String FAVORITE = "favorite";

    /**
     * Makes sure the id is the primary key (ensures uniqueness)
     */
    @PrimaryKey
    @NonNull
    String id;
    String url;


    /**
     * Need an empty constructor to run the instrumentation test
     */
    @Ignore
    public Gif() {
    }

    public Gif(String id, String url) {
        this.id = id;
        this.url = url;
    }


    public String getId() {
        return id;
    }


    public String getUrl() {
        return url;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Gif)) {
            return false;
        }
        Gif other = (Gif) o;
        return id.equals(other.id);
    }
}
