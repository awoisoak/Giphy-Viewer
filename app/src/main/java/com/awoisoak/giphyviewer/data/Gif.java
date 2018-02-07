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
@Entity(tableName = "gifs", indices = {@Index(value = {"server_id"}, unique = true)})
public class Gif {
    /**
     * We use serverKey as an identifier and local_id as a primerary key of the database.
     * Basically local_id will be used to set the Gifs in order within
     * the database (needed for offset queries)
     */
//    @PrimaryKey(autoGenerate = true)
//    public int local_id;
    @PrimaryKey
    @NonNull
    public String server_id;
    public String url;


    /**
     * Need an empty constructor to run the instrumentation test
     */
    @Ignore
    public Gif() {
    }

    /**
     * Default constructor
     */
//    @Ignore
    public Gif(String server_id, String url) {
        this.server_id = server_id;
        this.url = url;
    }
//
//    /**
//     * Constructor to be used by Room
//     *
//     * @param local_id  auto-generated local id by room
//     * @param server_id server id
//     */
//    public Gif(int local_id, String server_id, String url) {
//        this.local_id = local_id;
//        this.server_id = server_id;
//        this.url = url;
//    }


//    public int getId() {
//        return local_id;
//    }

    public String getServerId() {
        return server_id;
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
        return server_id == other.server_id;
    }
}
