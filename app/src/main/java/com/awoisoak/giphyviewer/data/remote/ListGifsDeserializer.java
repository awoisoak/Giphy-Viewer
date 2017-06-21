package com.awoisoak.giphyviewer.data.remote;


import com.awoisoak.giphyviewer.data.Gif;
import com.awoisoak.giphyviewer.data.remote.responses.GiphyResponse;
import com.awoisoak.giphyviewer.data.remote.responses.ListsGifsResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Specific Deserializer for ListGifsResponse
 * <p>
 */


class ListGifsDeserializer<T extends GiphyResponse> implements JsonDeserializer<ListsGifsResponse> {


    public ListGifsDeserializer() {
    }


    /**
     * @param je
     * @param type
     * @param jdc
     * @return
     * @throws JsonParseException
     */
    @Override
    public ListsGifsResponse deserialize(JsonElement je, Type type, JsonDeserializationContext jdc)
            throws JsonParseException {

        JsonElement data = je.getAsJsonObject().get(GiphyService.DATA);
        JsonArray arrayOfGifs = data.getAsJsonArray();
        ArrayList<Gif> GifsList = new ArrayList<>();

        JsonElement id;
        JsonElement url;


        for (JsonElement Gif : arrayOfGifs) {
            id = Gif.getAsJsonObject().get(GiphyService.ID);
            url = Gif.getAsJsonObject().get(GiphyService.IMAGES).getAsJsonObject().get(GiphyService.FIXED_HEIGHT)
                    .getAsJsonObject().get(GiphyService.URL);

            GifsList.add(new Gif(id.getAsString(), url.getAsString()));
        }
        ListsGifsResponse r = new ListsGifsResponse(GifsList);
        return r;
    }
}