package com.vwuilbea.mymoviecatalog.operations.add;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.vwuilbea.mymoviecatalog.MyApplication;
import com.vwuilbea.mymoviecatalog.R;
import com.vwuilbea.mymoviecatalog.database.DatabaseHelper;
import com.vwuilbea.mymoviecatalog.model.Movie;
import com.vwuilbea.mymoviecatalog.model.Role;
import com.vwuilbea.mymoviecatalog.model.Video;

import java.util.List;

public class AddToDBActivity extends Activity {

    private static final String LOG = AddToDBActivity.class.getSimpleName();

    public static final String PARAM_VIDEO = "video";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getIntent().getExtras() != null ) {
            Video video = (Video) getIntent().getExtras().get(PARAM_VIDEO);
            if(video!=null) {
                addVideo(video);
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_to_db, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_settings :
                return true;
            case android.R.id.home :
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addVideo(Video video) {
        long res = ((MyApplication) AddToDBActivity.this.getApplication()).addVideo(video, true);
        if(res == MyApplication.ERROR)
            Log.d(LOG, "An error occurred when add video '" + video.getTitle() + "' ("+video.getId()+") on DB");
        else if(res == MyApplication.OK) {
            Log.d(LOG, "video '" + video.getTitle() + "' ("+video.getId()+") has been added on DB");
        }
    }
}
