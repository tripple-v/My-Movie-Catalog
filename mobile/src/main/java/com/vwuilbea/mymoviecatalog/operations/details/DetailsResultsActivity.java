package com.vwuilbea.mymoviecatalog.operations.details;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.vwuilbea.mymoviecatalog.MovieCatalog;
import com.vwuilbea.mymoviecatalog.MyApplication;
import com.vwuilbea.mymoviecatalog.R;
import com.vwuilbea.mymoviecatalog.model.Actor;
import com.vwuilbea.mymoviecatalog.model.Genre;
import com.vwuilbea.mymoviecatalog.model.Role;
import com.vwuilbea.mymoviecatalog.model.Video;
import com.vwuilbea.mymoviecatalog.operations.add.AddToDBActivity;
import com.vwuilbea.mymoviecatalog.tmdb.TmdbService;
import com.vwuilbea.mymoviecatalog.tmdb.responses.credits.CreditsResponse;
import com.vwuilbea.mymoviecatalog.tmdb.responses.DetailsResponse;
import com.vwuilbea.mymoviecatalog.util.RestClient;

import java.util.List;

public class DetailsResultsActivity extends Activity {

    private static final String LOG = DetailsResultsActivity.class.getSimpleName();
    private static String PREFIX_IMAGE = TmdbService.PREFIX_IMAGE + TmdbService.POSTER_SIZES[TmdbService.POSTER_SIZES.length - 1];
    private static final int ERROR = -1;

    public static final String PARAM_VIDEO = "video";
    public static final String PARAM_MORE = "more";

    private static final int REQUEST_POSTER = 0;
    private static final int REQUEST_DETAILS = 1;
    private static final int REQUEST_CREDITS = 2;

    private static final int MAX_ACTORS_DISPLAYED = 5;

    /**
     * Map used to know if requests are finished or not
     */
    private SparseBooleanArray mapRequests = new SparseBooleanArray();

    private Video video;
    private boolean more;

    private View progressView;
    private View parallaxView;

    private ImageView posterImage;
    private TextView textTitle;
    private TextView textActors;
    private TextView textGenres;
    private TextView textOverview;

    private RestClient.ExecutionListener creditsCallback = new RestClient.ExecutionListener() {
        @Override
        public void onExecutionFinished(String url, String result) {
            Log.d(LOG, "url:" + url + ", result:" + result);
            CreditsResponse.parse(result, video);
            fillRoles(video);
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
            DetailsResponse detailsResponse = new DetailsResponse(result, video);
            fillDetails(video);
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
        getActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_details_results);
        textTitle = (TextView) findViewById(R.id.text_title);
        textActors = (TextView) findViewById(R.id.text_actors);
        textGenres = (TextView) findViewById(R.id.text_genre);
        progressView = findViewById(R.id.progress_view);
        parallaxView = findViewById(R.id.parallax_view);
        textOverview = (TextView) findViewById(R.id.text_overview);
        posterImage = (ImageView) findViewById(R.id.image_poster);

        if (getIntent().getExtras() != null) {
            video = getIntent().getExtras().getParcelable(PARAM_VIDEO);
            more = getIntent().getExtras().getBoolean(PARAM_MORE);
            invalidateOptionsMenu();
        }
        if (video != null) {
            initMapRequests();
            setTitle(video.getTitle());
            textTitle.setText(video.getTitle());
            if (video.getPosterPath() != null) Picasso.with(this)
                    .load(PREFIX_IMAGE + video.getPosterPath())
                    .placeholder(android.R.drawable.ic_menu_report_image)
                    .into(posterImage, posterCallback)
                    ;
            if(more) {
                TmdbService.sendDetailsRequest(video.getId(), detailsCallback);
                TmdbService.sendCreditsRequest(video.getId(), creditsCallback);
            }
            else {
                fillDetails(video);
                fillRoles(video);
            }
         }
    }

    private void fillDetails(Video video) {
        String genres = "";
        String overview = video.getOverview();
        String title = textTitle.getText()+"";
        boolean first = true;
        for (Genre genre : video.getGenres()) {
            if (first) {
                genres += genre.getName();
                first = false;
            } else genres += ", " + genre.getName();
        }
        if (!MovieCatalog.LANGUAGE.equalsIgnoreCase(video.getLanguage()) &&
                !video.getTitle().equals(video.getOriginalTitle()))
            title += " (" + video.getOriginalTitle() + ")";

        textTitle.setText(title);
        if(genres.length()>0) textGenres.setText(genres);
        if(overview!=null && !overview.equals("null")) textOverview.setText(overview);
    }

    private void fillRoles(Video video) {
        List<Role> roles = video.getRoles();
        String creditsString = "";
        boolean first = true;
        for (int i=0; i<Math.min(MAX_ACTORS_DISPLAYED, roles.size()); i++) {
            Role role = roles.get(i);
            Actor actor = role.getActor();
            if (first) first = false;
            else creditsString += ", ";
            creditsString += actor.getFirstname() + " " + actor.getName().toUpperCase();
        }
        textActors.setText(creditsString);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.details_results, menu);
        List<Video> videos =  ((MyApplication)this.getApplication()).getAllVideos();
        Log.d(LOG, "videos:\n"+videos+"\nvideo:\n"+video);
        if(videos.contains(video)) {
            MenuItem addButton = menu.findItem(R.id.action_add_movie);
            addButton.setVisible(false);
        }
        else {
            MenuItem deleteButton = menu.findItem(R.id.action_delete_movie);
            deleteButton.setVisible(false);
        }
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
            case R.id.action_delete_movie:
                deleteMovie();
                return true;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initMapRequests() {
        mapRequests.put(REQUEST_POSTER, false);
        if(more) {
            mapRequests.put(REQUEST_DETAILS, false);
            mapRequests.put(REQUEST_CREDITS, false);
        }
    }

    private void onRequestFinished(int requestKey) {
        mapRequests.put(requestKey, true);
        //To hide progress bar, all requests have to be finished
        boolean finished = true;
        for (int i=0; i<mapRequests.size(); i++) {
            if (!mapRequests.get(mapRequests.keyAt(i))) {
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
        i.putExtra(AddToDBActivity.PARAM_VIDEO,video);
        startActivity(i);
    }

    private DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    //Yes button clicked
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
        public void aa(){}
    };

    private void deleteMovie() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener)
                .show();
    }

}
