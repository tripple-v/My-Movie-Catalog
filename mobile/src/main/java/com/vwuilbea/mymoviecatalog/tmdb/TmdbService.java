package com.vwuilbea.mymoviecatalog.tmdb;

import android.widget.TextView;

import com.vwuilbea.mymoviecatalog.MovieCatalog;
import com.vwuilbea.mymoviecatalog.util.RestClient;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Valentin on 31/08/2014.
 */
public class TmdbService {

    public static final String PREFIX_IMAGE = "https://image.tmdb.org/t/p/";
    public static final String PREFIX_REQUEST = "https://api.themoviedb.org";

    public static final String SUFFIX_DETAILS = "/3/movie/";
    public static final String SUFFIX_SEARCH = "/3/search/movie";
    public static final String SUFFIX_TEST = SUFFIX_DETAILS+"550";

    private static final String API_KEY = "77760f5193f0b833f1ffb4a3d3b297a3";

    public static final String[] POSTER_SIZES = {"w45","w154","w185","w342","w500","w780"};

    private static final String PARAM_API_KEY = "api_key";
    private static final String PARAM_LANGUAGE = "language";
    private static final String PARAM_QUERY = "query";
    private static final String PARAM_SORT_BY = "sort_by";

    private RestClient.ExecutionListener executionListener;

    public TmdbService(RestClient.ExecutionListener executionListener) {
        this.executionListener = executionListener;
    }

    private void sendRequest(String suffix, Map<String, String> params) {
        RestClient client = new RestClient(PREFIX_REQUEST+suffix, executionListener);
        client.addHeader("content-type", "application/json");
        if(params!=null) {
            for(String key:params.keySet()) client.addParam(key,params.get(key));
        }

        client.addParam(PARAM_API_KEY, API_KEY);
        client.addParam(PARAM_LANGUAGE, MovieCatalog.LANGUAGE);

        try {
            client.executeGet();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendTestRequest() {
        sendRequest(SUFFIX_TEST,null);
    }

    public void sendSearchRequest(String query) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(PARAM_QUERY, query);
        sendRequest(SUFFIX_SEARCH,map);
    }

    public void sendDetailsRequest(int id) {
        sendRequest(SUFFIX_DETAILS+id,null);
    }

}
