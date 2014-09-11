package com.vwuilbea.mymoviecatalog.operations.details;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vwuilbea.mymoviecatalog.MovieCatalog;
import com.vwuilbea.mymoviecatalog.R;
import com.vwuilbea.mymoviecatalog.tmdb.TmdbService;
import com.vwuilbea.mymoviecatalog.tmdb.responses.DetailsResponse;
import com.vwuilbea.mymoviecatalog.util.RestClient;

public class DetailsResultsActivity extends Activity {

    private static final String LOG = DetailsResultsActivity.class.getSimpleName();
    private static String PREFIX_IMAGE = TmdbService.PREFIX_IMAGE+TmdbService.POSTER_SIZES[TmdbService.POSTER_SIZES.length-1];
    private static final int ERROR = -1;

    public static final String PARAM_MOVIE_ID = "movie_id";
    public static final String PARAM_MOVIE_TITLE = "movie_title";
    public static final String PARAM_MOVIE_POSTER_PATH = "movie_poser_path";

    private int movieId = ERROR;
    private String movieTitle;
    private String moviePosterPath;

    private ImageView posterImage;
    private TextView textTitle;
    private TextView textGenre;
    private TextView textOverview;

    private TmdbService tmdbService;

    private RestClient.ExecutionListener executionListener = new RestClient.ExecutionListener() {
        @Override
        public void onExecutionFinished(String url, String result) {
            Log.d(LOG,"url:"+url+", result:"+result);
            if(url.contains(TmdbService.SUFFIX_DETAILS)) {
                DetailsResponse response = new DetailsResponse(result);
                String genres = "";
                boolean first = true;
                for(DetailsResponse.Genre genre:response.getGenres()) {
                    if(first) {
                        genres+=genre.getName();
                        first=false;
                    }
                    else genres+=", "+genre.getName();
                }
                textGenre.setText(genres);
                textOverview.setText(response.getOverview());
                if(!MovieCatalog.LANGUAGE.equalsIgnoreCase("EN"))
                    textTitle.setText(textTitle.getText()+" ("+response.getOriginalTitle()+")");
            }
        }

        @Override
        public void onExecutionProgress(String url, Integer progress) {

        }

        @Override
        public void onExecutionFailed(String url, Exception e) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_results);
        textTitle = (TextView) findViewById(R.id.text_title);
        textGenre = (TextView) findViewById(R.id.text_genre);
        textOverview = (TextView) findViewById(R.id.text_overview);
        if(getIntent().getExtras()!=null) {
            movieId = getIntent().getExtras().getInt(PARAM_MOVIE_ID,ERROR);
            movieTitle = getIntent().getExtras().getString(PARAM_MOVIE_TITLE, null);
            moviePosterPath = getIntent().getExtras().getString(PARAM_MOVIE_POSTER_PATH, null);
        }
        posterImage = (ImageView) findViewById(R.id.image_poster);
        if(movieTitle!=null) {
            setTitle(movieTitle);
            textTitle.setText(movieTitle);
        }
        if(moviePosterPath!=null) Picasso.with(this)
                .load(PREFIX_IMAGE+moviePosterPath)
                .placeholder(android.R.drawable.ic_menu_report_image)
                .into(posterImage)
                ;
        if(movieId!=ERROR) {
            tmdbService = new TmdbService(executionListener);
            tmdbService.sendDetailsRequest(movieId);
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
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
