package com.vwuilbea.mymoviecatalog.operations.search;

import android.app.Activity;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.vwuilbea.mymoviecatalog.R;
import com.vwuilbea.mymoviecatalog.operations.details.DetailsResultsActivity;
import com.vwuilbea.mymoviecatalog.tmdb.TmdbService;
import com.vwuilbea.mymoviecatalog.tmdb.responses.SearchResponse;
import com.vwuilbea.mymoviecatalog.util.RestClient;


public class SearchResultsActivity
        extends Activity
        implements AdapterView.OnItemClickListener,
        View.OnClickListener
{

    private static final String LOG = SearchResultsActivity.class.getSimpleName();

    private SearchAdapter adapter;
    private ListView listView;

    private RestClient.ExecutionListener executionListener = new RestClient.ExecutionListener() {
        @Override
        public void onExecutionFinished(String url, String result) {
            Log.d(LOG, "execution of '"+url+"' finished.\nResult:"+result);
            SearchResponse response = new SearchResponse(result);
            if(response.getResults()!=null && response.getResults().size()>0) {
                String msg = "Response:";
                msg+="\npage:"+response.getPage();
                msg+="\ntotal pages:"+response.getTotalPages();
                msg+="\ntotal results:"+response.getTotalResults();
                for(SearchResponse.SearchResult searchResult:response.getResults()) {
                    msg+="\nid:"+searchResult.getId();
                    msg+="\ntitle:"+searchResult.getTitle();
                    msg+="\noriginal title:"+searchResult.getOriginalTitle();
                    msg+="\nbackdrop path:"+searchResult.getBackdropPath();
                    msg+="\nposter path:"+searchResult.getPosterPath();
                    msg+="\nrelease date:"+searchResult.getReleaseDate();
                    msg+="\npopularity:"+searchResult.getPopularity();
                    msg+="\nvote average:"+searchResult.getVoteAverage();
                    msg+="\nvote count:"+searchResult.getVoteCount();
                    adapter.add(searchResult);
                }
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
        Log.d(LOG,"onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        adapter = new SearchAdapter(this,R.layout.list_search_result);
        listView = (ListView) findViewById(R.id.layout_list);
        listView.setOnItemClickListener(this);
        listView.setAdapter(adapter);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d(LOG,"onNewIntent");
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
        SearchResponse.SearchResult result = adapter.getItem(position);
        Intent newIntent = new Intent();
        newIntent.setClass(this, DetailsResultsActivity.class);
        newIntent.putExtra(DetailsResultsActivity.PARAM_MOVIE_ID, result.getId());
        newIntent.putExtra(DetailsResultsActivity.PARAM_MOVIE_TITLE, result.getTitle());
        newIntent.putExtra(DetailsResultsActivity.PARAM_MOVIE_POSTER_PATH, result.getPosterPath());
        startActivity(newIntent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_button_add:
                break;
        }
    }
}
