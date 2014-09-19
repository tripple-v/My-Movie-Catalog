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
import com.vwuilbea.mymoviecatalog.textjustify.TextViewEx;
import com.vwuilbea.mymoviecatalog.tmdb.TmdbService;
import com.vwuilbea.mymoviecatalog.tmdb.responses.credits.CreditsResponse;
import com.vwuilbea.mymoviecatalog.tmdb.responses.DetailsResponse;
import com.vwuilbea.mymoviecatalog.util.RestClient;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DetailsResultsActivity extends Activity {

    private static final String LOG = DetailsResultsActivity.class.getSimpleName();
    private static String PREFIX_IMAGE = TmdbService.PREFIX_IMAGE + TmdbService.POSTER_SIZES[TmdbService.POSTER_SIZES.length - 1];
    private static final int ERROR = -1;

    public static final String PARAM_VIDEO = "video";
    public static final String PARAM_MORE = "more";

    private static final int REQUEST_COVER = 0;
    private static final int REQUEST_POSTER = 1;
    private static final int REQUEST_DETAILS = 2;
    private static final int REQUEST_CREDITS = 3;

    private static final int MAX_ACTORS_DISPLAYED = 5;

    /**
     * Map used to know if requests are finished or not
     */
    private SparseBooleanArray mapRequests = new SparseBooleanArray();

    private Video video;
    private boolean more;

    private View progressView;
    private View parallaxView;

    private ImageView coverImage;
    private ImageView posterImage;
    private TextView textTitle;
    private TextView textRuntime;
    private TextView textActors;
    private TextView textGenres;
    private TextView textDate;
    private TextViewEx textOverview;

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
            DetailsResponse.parse(result, video);
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

    private Callback coverCallback = new Callback() {
        @Override
        public void onSuccess() {
            onRequestFinished(REQUEST_COVER);
        }

        @Override
        public void onError() {
            onRequestFinished(REQUEST_COVER);
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
        textRuntime = (TextView) findViewById(R.id.text_runtime);
        textActors = (TextView) findViewById(R.id.text_actors);
        textGenres = (TextView) findViewById(R.id.text_genre);
        textDate = (TextView) findViewById(R.id.text_date);
        progressView = findViewById(R.id.progress_view);
        parallaxView = findViewById(R.id.parallax_view);
        textOverview = (TextViewEx) findViewById(R.id.text_overview);
        coverImage = (ImageView) findViewById(R.id.image_cover);
        posterImage = (ImageView) findViewById(R.id.image_poster);

        if (savedInstanceState == null) {
            if (getIntent().getExtras() != null) {
                video = getIntent().getExtras().getParcelable(PARAM_VIDEO);
                more = getIntent().getExtras().getBoolean(PARAM_MORE);
                invalidateOptionsMenu();
            }
        } else {
            video = savedInstanceState.getParcelable(PARAM_VIDEO);
            more = false;
        }
        if (video != null) {
            initMapRequests();
            setTitle(video.getTitle());
            textTitle.setText(video.getTitle());
            if (video.getPosterPath() != null) Picasso.with(this)
                    .load(PREFIX_IMAGE + video.getPosterPath())
                    .fit()
                    .placeholder(android.R.drawable.ic_menu_report_image)
                    .into(posterImage, posterCallback)
                    ;
            if (video.getCoverPath() != null) Picasso.with(this)
                    .load(PREFIX_IMAGE + video.getCoverPath())
                    .fit().centerCrop()
                    .placeholder(android.R.drawable.ic_menu_report_image)
                    .into(coverImage, coverCallback)
                    ;
            if (more) {
                TmdbService.sendDetailsRequest(video.getId(), detailsCallback);
                TmdbService.sendCreditsRequest(video.getId(), creditsCallback);
            } else {
                fillDetails(video);
                fillRoles(video);
            }
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        bundle.putParcelable(PARAM_VIDEO, video);
    }

    private String minutesToHours(int minutes) {
        int hours = minutes / 60;
        int min = minutes % 60;
        String format = "%dh%02dmin";
        return String.format(format,hours, min);
    }

    private void fillDetails(Video video) {
        String genres = "";
        String overview = video.getOverview();
        String title = textTitle.getText() + "";
        String runtime = minutesToHours(video.getRuntime());
        String oldDate = video.getReleaseDate();
        String newDate = oldDate;
        if(video.getCountries().size()>0) {
            runtime += " - " + video.getCountries().get(0).getName();
        }
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(oldDate);
            DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM);
            newDate = dateFormatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
        textTitle.setSelected(true);
        textRuntime.setText(runtime);
        if (genres.length() > 0) textGenres.setText(genres);
        textDate.setText(newDate);
        if (overview != null && !overview.equals("null")) textOverview.setText(overview,true);
    }

    private void fillRoles(Video video) {
        List<Role> roles = video.getRoles();
        String creditsString = "";
        boolean first = true;
        for (int i = 0; i < Math.min(MAX_ACTORS_DISPLAYED, roles.size()); i++) {
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
        List<Video> videos = ((MyApplication) this.getApplication()).getAllVideos();
        Log.d(LOG, "videos:\n" + videos + "\nvideo:\n" + video);
        if (videos.contains(video)) {
            MenuItem addButton = menu.findItem(R.id.action_add_movie);
            addButton.setVisible(false);
        } else {
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
                addVideo();
                return true;
            case R.id.action_delete_movie:
                deleteVideo();
                return true;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initMapRequests() {
        mapRequests.put(REQUEST_POSTER, false);
        mapRequests.put(REQUEST_COVER, false);
        if (more) {
            mapRequests.put(REQUEST_DETAILS, false);
            mapRequests.put(REQUEST_CREDITS, false);
        }
    }

    private void onRequestFinished(int requestKey) {
        mapRequests.put(requestKey, true);
        //To hide progress bar, all requests have to be finished
        boolean finished = true;
        for (int i = 0; i < mapRequests.size(); i++) {
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

    private void addVideo() {
        Intent i = new Intent();
        i.setClass(this, AddToDBActivity.class);
        i.putExtra(AddToDBActivity.PARAM_VIDEO, video);
        startActivity(i);
    }

    private void deleteVideo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.confirmation))
                .setPositiveButton("Yes", removeListener)
                .setNegativeButton("No", removeListener)
                .show();
    }

    private DialogInterface.OnClickListener removeListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    int res = ((MyApplication) getApplication()).removeVideo(video, true);
                    if(res == MyApplication.OK) finish();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }

        public void aa() {
        }
    };

}
