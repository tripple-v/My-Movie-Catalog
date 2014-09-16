package com.vwuilbea.mymoviecatalog.tmdb.responses.credits;

import android.os.Parcel;
import android.os.Parcelable;

import com.vwuilbea.mymoviecatalog.model.Movie;
import com.vwuilbea.mymoviecatalog.model.Role;
import com.vwuilbea.mymoviecatalog.model.Video;
import com.vwuilbea.mymoviecatalog.tmdb.TmdbFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Valentin on 04/09/2014.
 */
public class CreditsResponse {

    private static final String LOG = CreditsResponse.class.getSimpleName();

    private static final String PARAM_ID = "id";
    private static final String PARAM_CAST = "cast";

    public static void parse(String result, Video video) {
        int id;
        List<Role> roles = new ArrayList<Role>();
        try {
            JSONObject object = new JSONObject(result);
            id = object.getInt(PARAM_ID);
            JSONArray array = object.getJSONArray(PARAM_CAST);
            for (int i = 0; i < array.length(); i++)
                roles.add(TmdbFactory.getRoleFromCredit(new Credit(array.getJSONObject(i), id),video));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
