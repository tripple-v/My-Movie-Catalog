package com.vwuilbea.mymoviecatalog.tmdb.responses.details;

import com.vwuilbea.mymoviecatalog.model.Episode;
import com.vwuilbea.mymoviecatalog.model.Genre;
import com.vwuilbea.mymoviecatalog.model.ProductionCompany;
import com.vwuilbea.mymoviecatalog.model.Season;
import com.vwuilbea.mymoviecatalog.model.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Valentin on 04/09/2014.
 */
public class DetailsSeasonResponse {

    private static final String LOG = DetailsSeasonResponse.class.getSimpleName();

    private static final String PARAM_AIR_DATE = "air_date";
    private static final String PARAM_EPISODES = "episodes";
    private static final String PARAM_ID = "id";
    private static final String PARAM_POSTER_PATH = "poster_path";
    private static final String PARAM_SEASON_NUMBER = "season_number";
    private static final String PARAM_NAME = "name";
    private static final String PARAM_OVERVIEW = "overview";


    public static void parse(String result, Season season) {
        try {
            JSONObject object = new JSONObject(result);
            JSONArray array;
            if(!object.isNull(PARAM_AIR_DATE)) season.setFirstDate(object.getString(PARAM_AIR_DATE));
            if(!object.isNull(PARAM_OVERVIEW)) season.setOverview(object.getString(PARAM_OVERVIEW));
            if(!object.isNull(PARAM_POSTER_PATH)) season.setPosterPath(object.getString(PARAM_POSTER_PATH));
            if(!object.isNull(PARAM_EPISODES)) {
                array = object.getJSONArray(PARAM_EPISODES);
                for (int i = 0; i < array.length(); i++)
                    season.addEpisode(EpisodeResult.parse(array.getJSONObject(i), season));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static class EpisodeResult {

        private static final String PARAM_EPISODE_ID = PARAM_ID;
        private static final String PARAM_EPISODE_AIR_DATE = PARAM_AIR_DATE;
        private static final String PARAM_EPISODE_NUMBER = "episode_number";
        private static final String PARAM_EPISODE_NAME = PARAM_NAME;
        private static final String PARAM_EPISODE_OVERVIEW = PARAM_OVERVIEW;
        private static final String PARAM_EPISODE_POSTER_PATH= "still_path";
        private static final String PARAM_EPISODE_VOTE_AVERAGE = "vote_average";
        private static final String PARAM_EPISODE_VOTE_COUNT = "vote_count";

        static Episode parse(JSONObject object, Season season) {
            try {
                if(!object.isNull(PARAM_EPISODE_ID)) {
                    Episode episode = new Episode(object.getInt(PARAM_EPISODE_ID), season);
                    if(!object.isNull(PARAM_EPISODE_AIR_DATE)) episode.setDate(object.getString(PARAM_EPISODE_AIR_DATE));
                    if(!object.isNull(PARAM_EPISODE_NUMBER)) episode.setNumber(object.getInt(PARAM_EPISODE_NUMBER));
                    if(!object.isNull(PARAM_EPISODE_NAME)) episode.setTitle(object.getString(PARAM_EPISODE_NAME));
                    if(!object.isNull(PARAM_EPISODE_OVERVIEW)) episode.setOverview(object.getString(PARAM_EPISODE_OVERVIEW));
                    if(!object.isNull(PARAM_EPISODE_POSTER_PATH)) episode.setPosterPath(object.getString(PARAM_EPISODE_POSTER_PATH));
                    if(!object.isNull(PARAM_EPISODE_VOTE_AVERAGE)) episode.setVoteAverage((float)object.getDouble(PARAM_EPISODE_VOTE_AVERAGE));
                    if(!object.isNull(PARAM_EPISODE_VOTE_COUNT)) episode.setVoteCount(object.getInt(PARAM_EPISODE_VOTE_COUNT));
                    return episode;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

    }

}
