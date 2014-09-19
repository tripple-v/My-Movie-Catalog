package com.vwuilbea.mymoviecatalog.operations.search;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vwuilbea.mymoviecatalog.MovieAdapter;
import com.vwuilbea.mymoviecatalog.MyApplication;
import com.vwuilbea.mymoviecatalog.R;
import com.vwuilbea.mymoviecatalog.model.Movie;
import com.vwuilbea.mymoviecatalog.model.Video;
import com.vwuilbea.mymoviecatalog.operations.add.AddToDBActivity;
import com.vwuilbea.mymoviecatalog.operations.details.DetailsResultsActivity;
import com.vwuilbea.mymoviecatalog.tmdb.TmdbService;
import com.vwuilbea.mymoviecatalog.tmdb.responses.DetailsResponse;
import com.vwuilbea.mymoviecatalog.tmdb.responses.SearchMovieResponse;
import com.vwuilbea.mymoviecatalog.tmdb.responses.credits.CreditsResponse;
import com.vwuilbea.mymoviecatalog.util.RestClient;

import java.util.ArrayList;


public class SearchResultsActivity
        extends Activity
        implements AdapterView.OnItemClickListener,
        View.OnClickListener {

    private static final String LOG = SearchResultsActivity.class.getSimpleName();
    private static final String PARAM_VIDEOS = "videos";
    private static final int MAX_RESULTS = 500;

    private static final int REQUEST_DETAILS = 0;
    private static final int REQUEST_CREDITS = 1;

    private MovieAdapter adapter;
    private AbsListView listView;
    private ProgressBar progressBar;

    private ArrayList<Video> videos = new ArrayList<Video>();
    private Video selectedVideo;
    private String query;

    /**
     * Map used to know if requests are finished or not
     */
    private SparseBooleanArray mapRequests = new SparseBooleanArray();
    private TextView textOverview;

    private RestClient.ExecutionListener executionListenerCredits = new RestClient.ExecutionListener() {
        @Override
        public void onExecutionFinished(String url, String result) {
            Log.d(LOG, "url:" + url + ", result:" + result);
            CreditsResponse.parse(result, selectedVideo);
            onRequestFinished(REQUEST_CREDITS);
        }

        @Override
        public void onExecutionProgress(String url, Integer progress) {

        }

        @Override
        public void onExecutionFailed(String url, Exception e) {
            Log.e(LOG, "execution of '" + url + "' failed.\nException:" + e);
            onRequestFinished(REQUEST_CREDITS);
        }
    };

    private RestClient.ExecutionListener executionListenerDetails = new RestClient.ExecutionListener() {

        @Override
        public void onExecutionFinished(String url, String result) {
            Log.d(LOG, "execution of '" + url + "' finished.\nResult:" + result);
            DetailsResponse.parse(result, selectedVideo);
            onRequestFinished(REQUEST_DETAILS);
        }

        @Override
        public void onExecutionProgress(String url, Integer progress) {
            Log.d(LOG, "execution of '" + url + "' is progressing.\nProgress:" + progress);
        }

        @Override
        public void onExecutionFailed(String url, Exception e) {
            Log.e(LOG, "execution of '" + url + "' failed.\nException:" + e);
            onRequestFinished(REQUEST_DETAILS);
        }
    };

    private RestClient.ExecutionListener executionListenerSearch = new RestClient.ExecutionListener() {

        @Override
        public void onExecutionFinished(String url, String result) {
            Log.d(LOG, "execution of '" + url + "' finished.\nResult:" + result);
            SearchMovieResponse response = new SearchMovieResponse(result);
            if (response.getMovies().size() > 0) {
                String msg = "Response:";
                int totalResults = response.getTotalResults();
                if(totalResults>MAX_RESULTS) {
                    finish(R.string.too_much_results);
                    return;
                }
                int totalPages =  response.getTotalPages();
                int currentPage = response.getPage();
                msg += "\npage " + currentPage + " on "+ totalPages + "( "+totalResults+" )";
                msg += "\ntotal results:" + response.getTotalResults();
                for (Movie movie : response.getMovies())  videos.add(movie);
                if(currentPage<totalPages) {
                    TmdbService.sendSearchRequest(query, String.valueOf(currentPage+1), this);
                }
                else {
                    fillAdapter();
                }
                Log.d(LOG, msg);
            } else {
                Log.e(LOG, "No result");
                finish(R.string.no_result);
            }
        }

        @Override
        public void onExecutionProgress(String url, Integer progress) {
            Log.d(LOG, "execution of '" + url + "' is progressing.\nProgress:" + progress);
        }

        @Override
        public void onExecutionFailed(String url, Exception e) {
            Log.e(LOG, "execution of '" + url + "' failed.\nException:" + e);
            finish(R.string.httpErrorIO);
        }
    };

    //Finish the activity with a message and a result
    public void finish(final Integer stringId) {
        Log.d(LOG, "finish : "+ stringId);
        if(stringId!=null) {
            SearchResultsActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getBaseContext(), getString(stringId), Toast.LENGTH_LONG).show();
                }
            });
        }
        finish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_search_results);
        adapter = new MovieAdapter(this, R.layout.list_search_result, this);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        listView = (AbsListView) findViewById(R.id.list);
        listView.setOnItemClickListener(this);
        listView.setAdapter(adapter);
        if (savedInstanceState == null) {
            handleIntent(getIntent());
        } else {
            videos = savedInstanceState.getParcelableArrayList(PARAM_VIDEOS);
            fillAdapter();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        selectedVideo = null;
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        bundle.putParcelableArrayList(PARAM_VIDEOS, videos);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Video video = adapter.getItem(position);
        Intent newIntent = new Intent();
        newIntent.setClass(this, DetailsResultsActivity.class);
        newIntent.putExtra(DetailsResultsActivity.PARAM_VIDEO, video);
        newIntent.putExtra(DetailsResultsActivity.PARAM_MORE, true);
        startActivity(newIntent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_button_corner:
                selectedVideo = (Video) v.getTag();
                if (MovieAdapter.DELETE.equals(v.getContentDescription()))
                    deleteVideo();
                else
                    addVideo(false);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                finish(null);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleIntent(Intent intent) {
        Log.d(LOG,"handleIntent:"+intent.getAction());
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            setTitle(getResources().getString(R.string.search) + ": " + query);
            query = query.trim();
            TmdbService.sendSearchRequest(query, executionListenerSearch);
        }
    }

    private void fillAdapter() {
        adapter.clear();
        adapter.addAll(videos);
        adapter.sort(Video.COMPARATOR_DATE);
        showList();
    }

    private void hideList() {
        progressBar.setVisibility(View.VISIBLE);
        listView.setVisibility(View.INVISIBLE);
    }

    private void showList() {
        progressBar.setVisibility(View.INVISIBLE);
        listView.setVisibility(View.VISIBLE);
    }

    private void addVideo(boolean hasDetails) {
        if(hasDetails) {
            showList();
            Intent i = new Intent();
            i.setClass(this, AddToDBActivity.class);
            i.putExtra(AddToDBActivity.PARAM_VIDEO, selectedVideo);
            startActivity(i);
        }
        else {
            hideList();
            initMapRequests();
            TmdbService.sendDetailsRequest(selectedVideo.getId(), executionListenerDetails);
            TmdbService.sendCreditsRequest(selectedVideo.getId(), executionListenerCredits);
        }
    }

    private void deleteVideo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.confirmation))
                .setPositiveButton("Yes", removeListener)
                .setNegativeButton("No", removeListener)
                .show();
    }

    private void initMapRequests() {
        mapRequests.put(REQUEST_DETAILS, false);
        mapRequests.put(REQUEST_CREDITS, false);
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
        if (finished) addVideo(true);
    }

    private DialogInterface.OnClickListener removeListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    int res = ((MyApplication) getApplication()).removeVideo(selectedVideo, true);
                    if (res == MyApplication.OK) adapter.notifyDataSetChanged();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    selectedVideo = null;
                    break;
            }
        }

        public void aa() {
        }
    };
}
