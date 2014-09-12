package com.vwuilbea.mymoviecatalog.operations.add;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.vwuilbea.mymoviecatalog.R;
import com.vwuilbea.mymoviecatalog.database.DatabaseHelper;
import com.vwuilbea.mymoviecatalog.model.Role;
import com.vwuilbea.mymoviecatalog.tmdb.responses.credits.CreditsResponse;

import java.util.List;

public class AddToDBActivity extends Activity {

    private static final String LOG = AddToDBActivity.class.getSimpleName();
    private List<Role> roles;

    TextView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_db);
        test = (TextView) findViewById(R.id.text_credits);
        String credits;
        if(getIntent().getExtras() != null ) {
            CreditsResponse creditsResponse = (CreditsResponse) getIntent().getExtras().get("credits");
            if(creditsResponse!=null) {
                credits = "Credits for movie '"+creditsResponse.getId()+"': \n\n";
                roles = creditsResponse.getRoles();
                for(Role role:roles) {
                    credits+= role+"\n\n";
                }
            }
            else credits = "credits extra is null";
        }
        else credits = "No extra";
        test.setText(credits);
        GetDB getDB = new GetDB();
        getDB.doInBackground(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_to_db, menu);
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

    private void onDBReady(SQLiteDatabase dbW, SQLiteDatabase dbR) {
        Log.i(LOG, "db is ready!");
        if(roles!=null) {
            roles.get(0).addInDB(dbW, dbR);
        }

    }

    private class GetDB extends AsyncTask {

        @Override
        protected SQLiteDatabase doInBackground(Object[] params) {
            DatabaseHelper databaseHelper = new DatabaseHelper(getBaseContext());
            // Gets the data repository in write mode
            SQLiteDatabase dbW = databaseHelper.getWritableDatabase();
            SQLiteDatabase dbR = databaseHelper.getReadableDatabase();
            onDBReady(dbW, dbR);
            return null;
        }
    }
}
