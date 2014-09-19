package com.vwuilbea.mymoviecatalog;

import android.app.Activity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.SearchView;

import com.vwuilbea.mymoviecatalog.database.DatabaseHelper;
import com.vwuilbea.mymoviecatalog.model.Movie;
import com.vwuilbea.mymoviecatalog.model.Series;
import com.vwuilbea.mymoviecatalog.model.Video;
import com.vwuilbea.mymoviecatalog.operations.details.DetailsResultsActivity;
import com.vwuilbea.mymoviecatalog.operations.search.SearchResultsActivity;

import java.util.ArrayList;
import java.util.List;


public class MovieCatalog extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        DisplayVideosFragment.OnFragmentInteractionListener
{

    private static final String LOG = MovieCatalog.class.getSimpleName();

    //Used to startActivityForResult
    public static final int REQUEST_SEARCH = 0;

    public static String LANGUAGE;

    /** Fragment managing the behaviors, interactions and presentation of the navigation drawer */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /** Used to store the last screen title. For use in {@link #restoreActionBar()} */
    private CharSequence mTitle;

    //Is previous screen splash screen ?
    private boolean firstResume=true;

    private int currentPosition=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG, "onCreate");
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.title_cat_all));
        LANGUAGE = getResources().getString(R.string.language);
        setContentView(R.layout.activity_movie_catalog);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(firstResume) {
            firstResume = false;
        }
        else {
            onNavigationDrawerItemSelected(currentPosition);
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        ArrayList<Video> videos = ((MyApplication) this.getApplication()).getAllVideos();
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, DisplayVideosFragment.newInstance(position,videos))
                .commit();
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.movie_catalog, menu);
            restoreActionBar();

            // Associate searchable configuration with the SearchView
            SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
            manageSearch(searchView);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    private void manageSearch(SearchView searchView) {
        Log.d(LOG,"manageSearch");
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_search:
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(LOG, "onActivityResult: "+ requestCode + ", resultCode:"+resultCode);
        switch (requestCode) {
            case REQUEST_SEARCH:
                if(resultCode == RESULT_CANCELED) onBackPressed();
                break;
            default: break;
        }
    }

    @Override
    public void onVideoClicked(Video video) {
        Intent intent = new Intent(this, DetailsResultsActivity.class);
        intent.putExtra(DetailsResultsActivity.PARAM_VIDEO, video);
        intent.putExtra(DetailsResultsActivity.PARAM_MORE, false);
        startActivity(intent);
    }

    @Override
    public void onSectionAttached(int category) {
        Log.d(LOG, "onSectionAttached: "+category);
        switch (category) {
            case DisplayVideosFragment.CATEGORY_ALL:
                mTitle = getString(R.string.title_cat_all);
                break;
            case DisplayVideosFragment.CATEGORY_MOVIE :
                mTitle = getString(R.string.title_cat_movies);
                break;
            case DisplayVideosFragment.CATEGORY_SERIES :
                mTitle = getString(R.string.title_cat_series);
                break;
            default:
                break;
        }
    }
}
