package com.vwuilbea.mymoviecatalog.tmdb;

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

    public static final String SUFFIX_SEARCH = "/3/search/movie";
    public static final String SUFFIX_DETAILS = "/3/movie/";
    public static final String SUFFIX_CREDITS = "/3/movie/{id}/credits";

    private static final String API_KEY = "77760f5193f0b833f1ffb4a3d3b297a3";

    public static final String[] POSTER_SIZES = {"w45","w154","w185","w342","w500","w780"};

    private static final String PARAM_API_KEY = "api_key";
    private static final String PARAM_LANGUAGE = "language";
    private static final String PARAM_QUERY = "query";
    private static final String PARAM_SORT_BY = "sort_by";

    private static void sendRequest(String suffix, Map<String, String> params, RestClient.ExecutionListener executionListener) {
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

    public static void sendSearchRequest(String query, RestClient.ExecutionListener executionListener) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(PARAM_QUERY, query);
        sendRequest(SUFFIX_SEARCH,map, executionListener);
    }

    public static void sendDetailsRequest(int id, RestClient.ExecutionListener executionListener) {
        sendRequest(SUFFIX_DETAILS+id,null, executionListener);
    }

    public static void sendCreditsRequest(int id, RestClient.ExecutionListener executionListener) {
        sendRequest(SUFFIX_CREDITS.replace("{id}",String.valueOf(id)),null, executionListener);
    }

}
