package com.awoisoak.giphyviewer.source.remote;

import com.awoisoak.giphyviewer.source.remote.responses.ErrorResponse;
import com.awoisoak.giphyviewer.source.remote.responses.GiphyResponse;
import com.awoisoak.giphyviewer.source.remote.responses.ListsGifsResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Giphy Manager to attack Giphy public API
 */


public class GiphyManager implements GiphyApi {

    /**
     * +info
     * https://giphy.api-docs.io/
     */
    private static final String PUBLIC_BETA_KEY = "dc6zaTOxFJmzC";
    private static final String API_KEY = "api_key";


    String URL = "http://api.giphy.com/";
    static GiphyService service;
    static int NO_CODE = -1;
    private static GiphyManager instance;
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(ListsGifsResponse.class, new ListGifsDeserializer<ListsGifsResponse>())
            .disableHtmlEscaping()
            .create();

    public static GiphyManager getInstance() {
        if (instance == null) {
            instance = new GiphyManager();
        }
        return instance;
    }

    public GiphyManager() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(addPublicBetaKey())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        service = retrofit.create(GiphyService.class);
    }


    @Override
    public void search(String text, int offset, GiphyListener<ListsGifsResponse> l) {
        Call<ListsGifsResponse> c = service.search(text, offset);
        responseRequest(c, l);
    }

    @Override
    public void trending(GiphyListener<ListsGifsResponse> l) {
        Call<ListsGifsResponse> c = service.trending();
        responseRequest(c, l);
    }


    private <T extends GiphyResponse> void responseRequest(Call<T> c, GiphyListener<T> l) {
        try {
            Response<T> retrofitResponse = c.execute();
            T response = retrofitResponse.body();
            if (retrofitResponse.isSuccessful()) {
                //                //TODO in Giphy the totalrecords come in the response. Ignore it?
                //                String totalPages = retrofitResponse.headers().get(GiphyService.HEADER_TOTAL_PAGES);
                //                String totalRecords = retrofitResponse.headers().get(GiphyService.HEADER_TOTAL_RECORDS);
                //                if (totalPages != null && totalRecords != null) {
                //                    response.setTotalPages(Integer.parseInt(totalPages));
                //                    response.setTotalRecords(Integer.parseInt(totalRecords));
                //                }
                response.setCode(retrofitResponse.code());
                l.onResponse(response);
            } else {
                ErrorResponse errorResponse = convertErrorBody(retrofitResponse);
                l.onError(errorResponse);
            }
        } catch (IOException e) {
            ErrorResponse errorResponse = new ErrorResponse("IOexception while communicating with the Giphy site");
            errorResponse.setCode(NO_CODE);
            l.onError(errorResponse);
            e.printStackTrace();
        }
    }

    private ErrorResponse convertErrorBody(Response retrofitResponse) {
        ErrorResponse errorResponse = new ErrorResponse(retrofitResponse.message());
        errorResponse.setCode(retrofitResponse.code());
        return errorResponse;
    }


    /**
     * Add API key to all requests
     *
     * @return
     */
    private Interceptor addPublicBetaKey() {
        return new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                HttpUrl url = request.url().newBuilder().addQueryParameter(API_KEY, PUBLIC_BETA_KEY).build();
                request = request.newBuilder().url(url).build();
                return chain.proceed(request);
            }
        };
    }

}
