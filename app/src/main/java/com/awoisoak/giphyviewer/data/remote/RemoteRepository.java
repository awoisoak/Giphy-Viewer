package com.awoisoak.giphyviewer.data.remote;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.awoisoak.giphyviewer.data.Resource;
import com.awoisoak.giphyviewer.data.remote.impl.GiphyService;
import com.awoisoak.giphyviewer.data.remote.impl.ListGifsDeserializer;
import com.awoisoak.giphyviewer.data.remote.impl.responses.ErrorResponse;
import com.awoisoak.giphyviewer.data.remote.impl.responses.GiphyResponse;
import com.awoisoak.giphyviewer.data.remote.impl.responses.ListGifsResponse;
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


public class RemoteRepository implements GiphyApi {
    public static String TAG = RemoteRepository.class.getSimpleName();

    private static MutableLiveData<Resource<GiphyResponse>> searchGifs = new MutableLiveData<>();
    private static MutableLiveData<Resource<GiphyResponse>> trendingGifs = new MutableLiveData<>();



    /**
     * +info
     * https://giphy.api-docs.io/
     */
    private static final String PUBLIC_BETA_KEY = "dc6zaTOxFJmzC";
    private static final String API_KEY = "api_key";


    String URL = "http://api.giphy.com/";
    static GiphyService service;
    static int NO_CODE = -1;
    private static RemoteRepository instance;
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(ListGifsResponse.class,
                    new ListGifsDeserializer<ListGifsResponse>())
            .disableHtmlEscaping()
            .create();

    public static RemoteRepository getInstance() {
        if (instance == null) {
            instance = new RemoteRepository();
        }
        return instance;
    }

    public RemoteRepository() {
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

    //like in http://www.zoftino.com/android-livedata-examples
    public LiveData<Resource<GiphyResponse>> getSearchGifs() {
        return searchGifs;
    }

    public LiveData<Resource<GiphyResponse>> getTrendingGifs() {
        return trendingGifs;
    }

    @Override
    public void search(String text, int offset) {
        Call<ListGifsResponse> c = service.search(text, offset,
                GiphyApi.MAX_NUMBER_SEARCH_GIFS_RETURNED);
        responseRequest(c, REQUEST_TYPE.SEARCH);
    }

    @Override
    public void trending() {
        Call<ListGifsResponse> c = service.trending(GiphyApi.MAX_NUMBER_TRENDING_GIFS_RETURNED);
        responseRequest(c, REQUEST_TYPE.TRENDING);
    }

    //TODO do we need here the generic at all?
    private <T extends GiphyResponse> void responseRequest(Call<T> c, REQUEST_TYPE REQUESTTYPE) {
        try {
            Response<T> retrofitResponse = c.execute();
            T response = retrofitResponse.body();
            if (retrofitResponse.isSuccessful()) {
                response.setCode(retrofitResponse.code());
                response.setType(REQUESTTYPE);
                updateLiveData(Resource.success(response), REQUESTTYPE);
            } else {
                ErrorResponse errorResponse = convertErrorBody(retrofitResponse);
                response.setType(REQUESTTYPE);
                updateLiveData(Resource.error(retrofitResponse.message(), errorResponse),
                        REQUESTTYPE);
            }
        } catch (IOException e) {
            String genericError = "IOException while communicating with the Giphy site";
            ErrorResponse errorResponse = new ErrorResponse(genericError);
            errorResponse.setCode(NO_CODE);
            errorResponse.setType(REQUESTTYPE);
            updateLiveData(Resource.error(genericError, errorResponse), REQUESTTYPE);
            e.printStackTrace();
        }
    }

    private void updateLiveData(Resource resource, REQUEST_TYPE REQUESTTYPE) {
        switch (REQUESTTYPE) {
            case SEARCH:
                searchGifs.postValue(resource);
                break;
            case TRENDING:
                trendingGifs.postValue(resource);
                break;
            default:
                Log.e(TAG, "Unknown FETCH type | FETCH = " + REQUESTTYPE.toString());
        }

    }


    private ErrorResponse convertErrorBody(Response retrofitResponse) {
        ErrorResponse errorResponse = new ErrorResponse(retrofitResponse.message());
        errorResponse.setCode(retrofitResponse.code());
        return errorResponse;
    }


    /**
     * Add API key to all requests
     */
    private Interceptor addPublicBetaKey() {
        return new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                HttpUrl url = request.url().newBuilder().addQueryParameter(API_KEY,
                        PUBLIC_BETA_KEY).build();
                request = request.newBuilder().url(url).build();
                return chain.proceed(request);
            }
        };
    }

}
