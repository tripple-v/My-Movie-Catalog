package com.vwuilbea.mymoviecatalog.operations.details.series;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vwuilbea.mymoviecatalog.R;
import com.vwuilbea.mymoviecatalog.model.Season;
import com.vwuilbea.mymoviecatalog.model.Series;

public class SeasonsActivity extends Activity {

    public static final String PARAM_SERIES = "series";
    public static final String PARAM_SEASON = "season";

    private Series series;
    private Season season;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seasons);
        ExpandableListView listSeason = (ExpandableListView) findViewById(R.id.season_list);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey(PARAM_SERIES)) {
                series = extras.getParcelable(PARAM_SERIES);
                SeasonAdapter adapter = new SeasonAdapter(this, series.getSeasons());
                listSeason.setAdapter(adapter);
            } else if (extras.containsKey(PARAM_SEASON)) {
                season = extras.getParcelable(PARAM_SEASON);
            }
        } else {
            Toast.makeText(this, "No extras", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.seasons, menu);
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
}
