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
@Entity(tableName = "gifs", indices = {@Index(value = {"local_id"}, unique = true)})
public class Gif {


    /**
     * We use serverKey as a global identifier and local_id as a primary key of the database.
     * Basically local_id will be used to set an unique long id needed to called to
     * setHasStableIds(true) in the offlineadapter to end with the blinking in the recyclerview
     */
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public long local_id;
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
    @Ignore
    public Gif(String server_id, String url) {
        this.server_id = server_id;
        this.url = url;
    }

    /**
     * Constructor to be used by Room
     *
     * @param local_id  auto-generated local id by room
     * @param server_id server id
     */
    public Gif(long local_id, String server_id, String url) {
        this.local_id = local_id;
        this.server_id = server_id;
        this.url = url;
    }


    public long getLocal_id() {
        return local_id;
    }

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
