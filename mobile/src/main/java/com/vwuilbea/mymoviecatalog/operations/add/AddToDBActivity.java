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
    private static final long ERROR = -1;

    public static final String PARAM_VIDEO = "video";

    private Video video;

    TextView test;

    private DatabaseHelper.DBListener dbListener = new DatabaseHelper.DBListener() {
        @Override
        public void onReady(SQLiteDatabase dbR, SQLiteDatabase dbW) {
            Log.d(LOG, "db is ready!");
            if(video!=null) {
                long res = ((MyApplication) AddToDBActivity.this.getApplication()).addVideo(video, true, dbW, dbR);
                if(res == MyApplication.ERROR)
                    Log.d(LOG, "An error occurred when add video '"+video.getId()+"' on DB");
                else if(res == MyApplication.OK) {
                    Log.d(LOG, "video '" + video.getId() + "' has been added on DB (" + res + ")");
                }
            }
        }
        public void aa() {}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_add_to_db);
        test = (TextView) findViewById(R.id.text_credits);
        String credits;
        if(getIntent().getExtras() != null ) {
            video = (Video) getIntent().getExtras().get(PARAM_VIDEO);
            if(video!=null) {
                List<Role> roles = video.getRoles();
                credits = "Credits for video '"+video.getId()+"': \n\n";
                for(Role role:roles) {
                    credits+= role+"\n\n";
                }
            }
            else credits = "video extra is null";
        }
        else credits = "No extra";
        test.setText(credits);
        DatabaseHelper dbHelper = new DatabaseHelper(this, dbListener);
        dbHelper.getDB();
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
}
