package com.awoisoak.giphyviewer.data.remote.impl;


import com.awoisoak.giphyviewer.data.Gif;
import com.awoisoak.giphyviewer.data.remote.GiphyService;
import com.awoisoak.giphyviewer.data.remote.impl.responses.GiphyResponse;
import com.awoisoak.giphyviewer.data.remote.impl.responses.ListGifsResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Specific Deserializer for ListGifsResponse
 *
 */


class ListGifsDeserializer<T extends GiphyResponse> implements JsonDeserializer<ListGifsResponse> {


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
    public ListGifsResponse deserialize(JsonElement je, Type type, JsonDeserializationContext jdc)
            throws JsonParseException {

        JsonElement totalCount =
                je.getAsJsonObject().get(GiphyService.PAGINATION).getAsJsonObject().get(GiphyService.TOTAL_COUNT);

        /**
         * trending gifs request do not return totalcount
         */
        int totalGifs = totalCount == null ? -1 : totalCount.getAsInt();

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
        ListGifsResponse r = new ListGifsResponse(GifsList);
        r.setTotalRecords(totalGifs);
        return r;
    }
}