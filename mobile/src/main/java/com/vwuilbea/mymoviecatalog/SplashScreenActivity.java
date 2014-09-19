package com.vwuilbea.mymoviecatalog;

import com.vwuilbea.mymoviecatalog.database.DatabaseHelper;
import com.vwuilbea.mymoviecatalog.model.Movie;
import com.vwuilbea.mymoviecatalog.model.Series;
import com.vwuilbea.mymoviecatalog.model.Video;
import com.vwuilbea.mymoviecatalog.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SplashScreenActivity extends Activity {

    private static final String LOG = SplashScreenActivity.class.getSimpleName();

    //Not start app before this time
    private static final int WAIT_TIME_IN_MILLIS = 3000;

    private boolean isDBReady = false;
    private boolean isTimerFinished = false;


    //The first between timer et dbListener which has finished, will start the app
    private CountDownTimer timer = new CountDownTimer(WAIT_TIME_IN_MILLIS, WAIT_TIME_IN_MILLIS) {
        @Override
        public void onTick(long millisUntilFinished) {
            //Never used
        }

        @Override
        public void onFinish() {
            isTimerFinished = true;
            if(isDBReady) startApp();
        }
    };

    private MyApplication.DBListener listener = new MyApplication.DBListener() {
        @Override
        public void onReady() {
            isDBReady = true;
            if(isTimerFinished) startApp();
        }
        public void aa() {}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        timer.start();
        setContentView(R.layout.activity_splash_screen);
        getActionBar().hide();
        ((MyApplication) getApplication()).initializeDB(listener);
    }

    private void startApp() {
        Intent intent = new Intent();
        intent.setClass(SplashScreenActivity.this, MovieCatalog.class);
        startActivity(intent);
        SplashScreenActivity.this.finish();
    }
}
