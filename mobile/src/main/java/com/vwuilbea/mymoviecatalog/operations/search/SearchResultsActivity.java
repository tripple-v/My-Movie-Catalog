package com.vwuilbea.mymoviecatalog.operations.search;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.vwuilbea.mymoviecatalog.MovieAdapter;
import com.vwuilbea.mymoviecatalog.R;
import com.vwuilbea.mymoviecatalog.model.Movie;
import com.vwuilbea.mymoviecatalog.model.Video;
import com.vwuilbea.mymoviecatalog.operations.details.DetailsResultsActivity;
import com.vwuilbea.mymoviecatalog.tmdb.TmdbService;
import com.vwuilbea.mymoviecatalog.tmdb.responses.SearchMovieResponse;
import com.vwuilbea.mymoviecatalog.util.RestClient;


public class SearchResultsActivity
        extends Activity
        implements AdapterView.OnItemClickListener,
        View.OnClickListener
{

    private static final String LOG = SearchResultsActivity.class.getSimpleName();

    private MovieAdapter adapter;
    private ListView listView;

    private RestClient.ExecutionListener executionListener = new RestClient.ExecutionListener() {
        @Override
        public void onExecutionFinished(String url, String result) {
            Log.d(LOG, "execution of '"+url+"' finished.\nResult:"+result);
            SearchMovieResponse response = new SearchMovieResponse(result);
            if(response.getMovies().size()>0) {
                String msg = "Response:";
                msg+="\npage:"+response.getPage();
                msg+="\ntotal pages:"+response.getTotalPages();
                msg+="\ntotal results:"+response.getTotalResults();
                for(Movie movie:response.getMovies()) {
                    msg+="\nid:"+movie.getId();
                    msg+="\ntitle:"+movie.getTitle();
                    msg+="\noriginal title:"+movie.getOriginalTitle();
                    msg+="\nposter path:"+movie.getPosterPath();
                    msg+="\nreleaseDate:"+movie.getReleaseDate();
                    msg+="\nvote average:"+movie.getVoteAverage();
                    msg+="\nvote count:"+movie.getVoteCount();
                    adapter.add(movie);
                }
                adapter.sort(Video.COMPARATOR_DATE);
                Log.d(LOG,msg);
            }
            else {
                Log.e(LOG,"No result");
                Toast.makeText(getApplicationContext(),R.string.no_result,Toast.LENGTH_SHORT).show();
                finish();
            }
        }

        @Override
        public void onExecutionProgress(String url, Integer progress) {
            Log.d(LOG, "execution of '"+url+"' is progressing.\nProgress:"+progress);
        }

        @Override
        public void onExecutionFailed(String url, Exception e) {
            Log.e(LOG, "execution of '" + url + "' failed.\nException:" + e);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_search_results);
        adapter = new MovieAdapter(this,R.layout.list_search_result);
        listView = (ListView) findViewById(R.id.layout_list);
        listView.setOnItemClickListener(this);
        listView.setAdapter(adapter);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d(LOG, "onNewIntent");
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        Log.d(LOG,"handleIntent:"+intent);
        if (intent.getExtras() != null) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            setTitle(getResources().getString(R.string.search)+": "+query);
            query = query.trim();
            TmdbService.sendSearchRequest(query, executionListener);
        }
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_button_add:
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
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
