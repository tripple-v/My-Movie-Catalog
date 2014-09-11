package com.vwuilbea.mymoviecatalog.operations.add;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.vwuilbea.mymoviecatalog.R;
import com.vwuilbea.mymoviecatalog.tmdb.responses.credits.Credit;
import com.vwuilbea.mymoviecatalog.tmdb.responses.credits.CreditsResponse;

public class AddToDBActivity extends Activity {

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
                credits = "Credits for movie '"+creditsResponse.getId()+"'\n";
                for(Credit credit:creditsResponse.getCredits()) {
                    credits+= credit+"\n";
                }
            }
            else credits = "credits extra is null";
        }
        else credits = "No extra";
        test.setText(credits);
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
}
