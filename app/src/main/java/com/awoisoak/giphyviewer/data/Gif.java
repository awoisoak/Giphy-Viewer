package com.awoisoak.giphyviewer.data;



import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Gif Object
 */

@DatabaseTable(tableName = "gifs")

public class Gif {
    public final static String ID = "id";
    public final static String URL = "url";
    public final static String FAVORITE = "favorite";

    @DatabaseField(id = true, columnName = ID)
    String id;


    @DatabaseField(columnName = URL, dataType = DataType.STRING, canBeNull = false)
    String url;

    @DatabaseField(columnName = FAVORITE, dataType = DataType.BOOLEAN, canBeNull = false)
    boolean favorite;


    public Gif(String id, String url, boolean favorite) {
        this.id = id;
        this.url = url;
        this.favorite = favorite;
    }


    public String getId() {
        return id;
    }


    public String getUrl() {
        return url;
    }

    public boolean isFavorite() {
        return favorite;
    }

}
