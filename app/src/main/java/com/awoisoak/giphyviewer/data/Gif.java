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

    @DatabaseField(id = true, columnName = ID)
    String id;


    @DatabaseField(columnName = URL, dataType = DataType.STRING, canBeNull = false)
    String url;



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

}
