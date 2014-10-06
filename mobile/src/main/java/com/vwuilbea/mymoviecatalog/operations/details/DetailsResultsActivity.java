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
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.vwuilbea.mymoviecatalog.MovieCatalog;
import com.vwuilbea.mymoviecatalog.MyApplication;
import com.vwuilbea.mymoviecatalog.R;
import com.vwuilbea.mymoviecatalog.model.Actor;
import com.vwuilbea.mymoviecatalog.model.Episode;
import com.vwuilbea.mymoviecatalog.model.Genre;
import com.vwuilbea.mymoviecatalog.model.Movie;
import com.vwuilbea.mymoviecatalog.model.Role;
import com.vwuilbea.mymoviecatalog.model.Season;
import com.vwuilbea.mymoviecatalog.model.Series;
import com.vwuilbea.mymoviecatalog.model.Video;
import com.vwuilbea.mymoviecatalog.operations.add.AddToDBActivity;
import com.vwuilbea.mymoviecatalog.operations.details.series.ExpandableListAdapter;
import com.vwuilbea.mymoviecatalog.operations.details.series.SeasonAdapter;
import com.vwuilbea.mymoviecatalog.util.textjustify.TextViewEx;
import com.vwuilbea.mymoviecatalog.tmdb.TmdbService;
import com.vwuilbea.mymoviecatalog.tmdb.responses.credits.CreditsResponse;
import com.vwuilbea.mymoviecatalog.tmdb.responses.details.DetailsMovieResponse;
import com.vwuilbea.mymoviecatalog.tmdb.responses.details.DetailsSeasonResponse;
import com.vwuilbea.mymoviecatalog.tmdb.responses.details.DetailsSeriesResponse;
import com.vwuilbea.mymoviecatalog.util.RestClient;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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
    private static final int REQUEST_DETAILS_SEASONS = 4;

    private static final int MAX_ACTORS_DISPLAYED = 5;
    private final int CELL_DEFAULT_HEIGHT = 200;

    /**
     * Map used to know if requests are finished or not
     */
    private SparseBooleanArray mapRequests = new SparseBooleanArray();

    private Video video;
    private boolean more;
    private boolean isVideoInDB = false;
    private boolean isMovie = true;

    private int currentSeasonId = 0;
    private List<Season> seasons;

    private View progressView;
    private View parallaxView;

    private ImageView coverImage;
    private ImageView posterImage;
    private TextView textTitle;
    private TextView textRuntime;
    private TextView textActors;
    private TextView textGenres;
    private TextView textDate;
    private RatingBar ratingBarPublic;
    private RatingBar ratingBarPrivate;
    private TextViewEx textOverview;
    private RadioGroup radioGroupQuality;
    private Switch switchDimension;
    private RelativeLayout layoutSeasons;
    private TextView testSeason;
    private ExpandableListView listSeason;

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
            if(isMovie) {
                DetailsMovieResponse.parse(result, video);
                fillDetails(video);
            }
            else {
                DetailsSeriesResponse.parse(result, (Series)video);
                seasons = ((Series) video).getSeasons();
                if(seasons.size()>0) {
                    mapRequests.put(REQUEST_DETAILS_SEASONS,false);
                    TmdbService.sendDetailsSeasonRequest(video.getId(), seasons.get(currentSeasonId).getNumber(), detailsSeasonCallback);
                }
            }
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

    private RestClient.ExecutionListener detailsSeasonCallback = new RestClient.ExecutionListener() {
        @Override
        public void onExecutionFinished(String url, String result) {
            Log.d(LOG, "url:" + url + ", result:" + result);
            DetailsSeasonResponse.parse(result, seasons.get(currentSeasonId));
            if(currentSeasonId + 1 < seasons.size()) {
                //There is other season
                currentSeasonId++;
                TmdbService.sendDetailsSeasonRequest(video.getId(), seasons.get(currentSeasonId).getNumber(), this);
            }
            else {
                fillDetails(video);
                onRequestFinished(REQUEST_DETAILS_SEASONS);
            }
        }

        @Override
        public void onExecutionProgress(String url, Integer progress) {

        }

        @Override
        public void onExecutionFailed(String url, Exception e) {
            onRequestFinished(REQUEST_DETAILS_SEASONS);
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

    private RatingBar.OnRatingBarChangeListener ratingBarChangeListener = new RatingBar.OnRatingBarChangeListener() {
        @Override
        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
            if(fromUser) {
                video.setVotePrivate(rating);
            }
        }
        public void aa(){}
    };

    private RadioGroup.OnCheckedChangeListener qualityChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            video.setQuality(checkedId);
        }
        public void aa(){}
    };

    private CompoundButton.OnCheckedChangeListener dimensionChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            video.setThreeD(isChecked);
        }
        public void aa(){}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();
        showProgressView();

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
            //To update video in adapter
            Intent data = new Intent();
            data.putExtra(PARAM_VIDEO,video);
            setResult(RESULT_OK,data);

            List<Video> videos = ((MyApplication) this.getApplication()).getAllVideos();
            isVideoInDB = videos.contains(video);
            ratingBarPrivate.setOnRatingBarChangeListener(ratingBarChangeListener);
            radioGroupQuality.setOnCheckedChangeListener(qualityChangeListener);
            switchDimension.setOnCheckedChangeListener(dimensionChangeListener);
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
                isMovie = video instanceof Movie;
                TmdbService.sendDetailsRequest(video.getId(), detailsCallback, isMovie);
                TmdbService.sendCreditsRequest(video.getId(), creditsCallback, isMovie);
            } else {
                fillDetails(video);
                fillRoles(video);
            }
        }
        else {
            Toast.makeText(this,getString(R.string.no_video),Toast.LENGTH_LONG).show();
            finish();
        }

    }

    private void initViews() {
        setContentView(R.layout.activity_details_results);
        textTitle = (TextView) findViewById(R.id.text_title);
        textRuntime = (TextView) findViewById(R.id.text_runtime);
        textActors = (TextView) findViewById(R.id.text_actors);
        textGenres = (TextView) findViewById(R.id.text_genre);
        textDate = (TextView) findViewById(R.id.text_date);
        progressView = findViewById(R.id.progress_view);
        parallaxView = findViewById(R.id.parallax_view);
        ratingBarPublic = (RatingBar) findViewById(R.id.rating_bar_public);
        ratingBarPrivate = (RatingBar) findViewById(R.id.rating_bar_private);
        textOverview = (TextViewEx) findViewById(R.id.text_overview);
        coverImage = (ImageView) findViewById(R.id.image_cover);
        posterImage = (ImageView) findViewById(R.id.image_poster);
        radioGroupQuality = (RadioGroup) findViewById(R.id.group_quality);
        switchDimension = (Switch) findViewById(R.id.switch_dimension);
        layoutSeasons = (RelativeLayout) findViewById(R.id.layout_seasons);
        testSeason = (TextView) findViewById(R.id.test_season);
        listSeason = (ExpandableListView) findViewById(R.id.season_list);

        RadioButton radioButtonLow = (RadioButton) findViewById(R.id.quality_low);
        radioButtonLow.setId(Video.Quality.LOW.getId());

        RadioButton radioButtonMedium = (RadioButton) findViewById(R.id.quality_normal);
        radioButtonMedium.setId(Video.Quality.NORMAL.getId());

        RadioButton radioButton720 = (RadioButton) findViewById(R.id.quality_720);
        radioButton720.setId(Video.Quality.HD720.getId());

        RadioButton radioButton1080 = (RadioButton) findViewById(R.id.quality_1080);
        radioButton1080.setId(Video.Quality.HD1080.getId());

        RadioButton radioButton4k = (RadioButton) findViewById(R.id.quality_4k);
        radioButton4k.setId(Video.Quality.ULTRAHD.getId());
    }

    @Override
    public void onStop() {
        if(isVideoInDB) {
            ((MyApplication)  getApplication()).updateVideo(video);
        }
        super.onStop();
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
        Log.d(LOG,"fillDetails: "+video);
        String genres = "";
        String overview = video.getOverview();
        String title = textTitle.getText() + "";
        String runtime = minutesToHours(video.getRuntime());
        String oldDate = video.getReleaseDate();
        String newDate = oldDate;
        float publicRating = video.getVoteAverage();
        float privateRating = video.getVotePrivate();
        int quality = video.getQuality();
        boolean is3D = video.isThreeD();
        if(video.getCountries().size()>0) {
            runtime += " - " + video.getCountries().get(0).getName();
        }
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(oldDate);
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
        ratingBarPublic.setRating(publicRating/2);
        ratingBarPrivate.setRating(privateRating);
        if (overview != null && !overview.equals("null")) textOverview.setText(overview,true);
        radioGroupQuality.check(quality);
        switchDimension.setChecked(is3D);
        fillSeasons(video);
    }

    private void fillSeasons(Video video) {
        if(video instanceof Series) {
            List<Season> seasons = ((Series) video).getSeasons();
            HashMap<Season, List<Episode>> map = new HashMap<Season, List<Episode>>();
            for(Season season:seasons) {
                map.put(season, season.getEpisodes());
            }
            //SeasonAdapter adapter = new SeasonAdapter(this,seasons,map);
            ExpandableListAdapter adapter = new ExpandableListAdapter(this, seasons);
            testSeason.setText(adapter.getGroupCount() + " seasons"); //A TextView to test adapter
            listSeason.setAdapter(adapter);
        }
        else {
            layoutSeasons.setVisibility(View.INVISIBLE);
        }
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
        if (isVideoInDB) {
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

    private void showProgressView() {
        progressView.setVisibility(View.VISIBLE);
        parallaxView.setVisibility(View.INVISIBLE);
    }

    private void addVideo() {
        Intent i = new Intent();
        i.setClass(this, AddToDBActivity.class);
        i.putExtra(AddToDBActivity.PARAM_VIDEO, video);
        startActivity(i);
        finish();
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
