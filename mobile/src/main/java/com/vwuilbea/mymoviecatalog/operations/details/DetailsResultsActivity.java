package com.vwuilbea.mymoviecatalog.operations.details;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.vwuilbea.mymoviecatalog.MovieCatalog;
import com.vwuilbea.mymoviecatalog.R;
import com.vwuilbea.mymoviecatalog.operations.add.AddToDBActivity;
import com.vwuilbea.mymoviecatalog.tmdb.TmdbService;
import com.vwuilbea.mymoviecatalog.tmdb.responses.credits.Credit;
import com.vwuilbea.mymoviecatalog.tmdb.responses.credits.CreditsResponse;
import com.vwuilbea.mymoviecatalog.tmdb.responses.DetailsResponse;
import com.vwuilbea.mymoviecatalog.util.RestClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailsResultsActivity extends Activity {

    private static final String LOG = DetailsResultsActivity.class.getSimpleName();
    private static String PREFIX_IMAGE = TmdbService.PREFIX_IMAGE + TmdbService.POSTER_SIZES[TmdbService.POSTER_SIZES.length - 1];
    private static final int ERROR = -1;

    public static final String PARAM_MOVIE_ID = "movie_id";
    public static final String PARAM_MOVIE_TITLE = "movie_title";
    public static final String PARAM_MOVIE_POSTER_PATH = "movie_poser_path";

    private static final int REQUEST_POSTER = 0;
    private static final int REQUEST_DETAILS = 1;
    private static final int REQUEST_CREDITS = 2;

    private static final int MAX_ACTORS_DISPLAYED = 5;

    /**
     * Map used to know if requests are finished or not
     */
    private Map<Integer, Boolean> mapRequests = new HashMap<Integer, Boolean>();

    private int movieId = ERROR;
    private String movieTitle;
    private String moviePosterPath;

    private DetailsResponse detailsResponse;
    private CreditsResponse creditsResponse;

    View progressView;
    View parallaxView;

    private ImageView posterImage;
    private TextView textTitle;
    private TextView textActors;
    private TextView textGenres;
    private TextView textOverview;

    private RestClient.ExecutionListener creditsCallback = new RestClient.ExecutionListener() {
        @Override
        public void onExecutionFinished(String url, String result) {
            Log.d(LOG, "url:" + url + ", result:" + result);
            creditsResponse = new CreditsResponse(result);
            List<Credit> credits = creditsResponse.getCredits();
            String creditsString = "";
            boolean first = true;
            for (int i=0; i<Math.min(MAX_ACTORS_DISPLAYED,credits.size()); i++) {
                Credit credit = credits.get(i);
                if (first) {
                    creditsString += credit.getName();
                    first = false;
                } else creditsString += ", " + credit.getName();
            }
            textActors.setText(creditsString);
            onRequestFinished(REQUEST_CREDITS);

        }

        @Override
        public void onExecutionProgress(String url, Integer progress) {

        }

        @Override
        public void onExecutionFailed(String url, Exception e) {
            onRequestFinished(REQUEST_CREDITS);
        }
    };

    private RestClient.ExecutionListener detailsCallback = new RestClient.ExecutionListener() {
        @Override
        public void onExecutionFinished(String url, String result) {
            Log.d(LOG, "url:" + url + ", result:" + result);
            detailsResponse = new DetailsResponse(result);
            String genres = "";
            boolean first = true;
            for (DetailsResponse.Genre genre : detailsResponse.getGenres()) {
                if (first) {
                    genres += genre.getName();
                    first = false;
                } else genres += ", " + genre.getName();
            }
            textGenres.setText(genres);
            textOverview.setText(detailsResponse.getOverview());
            if (detailsResponse.getLanguages().size()>0 &&
                    !MovieCatalog.LANGUAGE.equalsIgnoreCase(detailsResponse.getLanguages().get(0).getIso()) &&
                    !detailsResponse.getTitle().equals(detailsResponse.getOriginalTitle()))
                textTitle.setText(textTitle.getText() + " (" + detailsResponse.getOriginalTitle() + ")");
            onRequestFinished(REQUEST_DETAILS);
        }

        @Override
        public void onExecutionProgress(String url, Integer progress) {

        }

        @Override
        public void onExecutionFailed(String url, Exception e) {
            onRequestFinished(REQUEST_DETAILS);
        }
    };

    private Callback posterCallback = new Callback() {
        @Override
        public void onSuccess() {
            onRequestFinished(REQUEST_POSTER);
        }

        @Override
        public void onError() {
            onRequestFinished(REQUEST_POSTER);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details_results);
        textTitle = (TextView) findViewById(R.id.text_title);
        textActors = (TextView) findViewById(R.id.text_actors);
        textGenres = (TextView) findViewById(R.id.text_genre);
        progressView = findViewById(R.id.progress_view);
        parallaxView = findViewById(R.id.parallax_view);
        textOverview = (TextView) findViewById(R.id.text_overview);

        initMapRequests();
        if (getIntent().getExtras() != null) {
            movieId = getIntent().getExtras().getInt(PARAM_MOVIE_ID, ERROR);
            movieTitle = getIntent().getExtras().getString(PARAM_MOVIE_TITLE, null);
            moviePosterPath = getIntent().getExtras().getString(PARAM_MOVIE_POSTER_PATH, null);
        }
        posterImage = (ImageView) findViewById(R.id.image_poster);
        if (movieTitle != null) {
            setTitle(movieTitle);
            textTitle.setText(movieTitle);
        }
        if (moviePosterPath != null) Picasso.with(this)
                .load(PREFIX_IMAGE + moviePosterPath)
                .placeholder(android.R.drawable.ic_menu_report_image)
                .into(posterImage, posterCallback)
                ;
        if (movieId != ERROR) {
            TmdbService.sendDetailsRequest(movieId, detailsCallback);
            TmdbService.sendCreditsRequest(movieId, creditsCallback);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.details_results, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_add_movie:
                addMovie();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initMapRequests() {
        mapRequests.put(REQUEST_POSTER, false);
        mapRequests.put(REQUEST_DETAILS, false);
        mapRequests.put(REQUEST_CREDITS, false);
    }

    private void onRequestFinished(int requestKey) {
        mapRequests.put(requestKey, true);
        //To hide progress bar, all requests have to be finished
        boolean finished = true;
        for (Integer integer : mapRequests.keySet()) {
            if (!mapRequests.get(integer)) {
                finished = false;
                break;
            }
        }
        if (finished) hideProgressView();
    }

    private void hideProgressView() {
        progressView.setVisibility(View.INVISIBLE);
        parallaxView.setVisibility(View.VISIBLE);
    }

    private void addMovie() {
        Intent i = new Intent();
        i.setClass(this, AddToDBActivity.class);
        i.putExtra("credits",creditsResponse);
        startActivity(i);
    }

}
