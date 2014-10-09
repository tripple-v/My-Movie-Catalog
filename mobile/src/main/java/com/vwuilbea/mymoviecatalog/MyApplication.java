package com.vwuilbea.mymoviecatalog;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vwuilbea.mymoviecatalog.database.DatabaseHelper;
import com.vwuilbea.mymoviecatalog.database.MovieCatalogContract;
import com.vwuilbea.mymoviecatalog.model.Movie;
import com.vwuilbea.mymoviecatalog.model.Series;
import com.vwuilbea.mymoviecatalog.model.Video;

import java.util.ArrayList;
import java.util.List;

/**
 * Class which contains global variables
 */
public class MyApplication extends Application {

    private static final String LOG = MyApplication.class.getSimpleName();

    public static final int ERROR = DatabaseHelper.ERROR;
    public static final int OK = DatabaseHelper.OK;

    private ArrayList<Video> allVideos = new ArrayList<Video>();
    private SQLiteDatabase dbW, dbR;

    public void initializeDB(final DBListener listener) {
        DatabaseHelper dbHelper = new DatabaseHelper(this, new DatabaseHelper.DBListener() {
            @Override
            public void onReady(SQLiteDatabase dbR, SQLiteDatabase dbW) {
                setDbR(dbR);
                setDbW(dbW);
                addVideos(getVideosFromDb(dbR));
                Log.d(LOG, "Get " + allVideos.size() + " videos");
                listener.onReady();
            }

            public void aa() {
            }
        });
        dbHelper.getDB();
    }

    public ArrayList<Video> getAllVideos() {
        return allVideos;
    }

    public void setAllVideos(ArrayList<Video> allVideos) {
        this.allVideos = allVideos;
    }

    public int addVideo(Video video, boolean putInDB) {
        if (video != null && !allVideos.contains(video)) {
            if (putInDB && dbW != null && dbR != null) {
                int res = video.putInDB(dbW, dbR);
                if (res == ERROR) return ERROR;
            }
            allVideos.add(video);
            Log.d(LOG,"Video added: "+video);
        }
        return OK;
    }

    public void addVideos(List<? extends Video> videos) {
        for (Video video : videos)
            if (video != null && !allVideos.contains(video))
                addVideo(video, false);
    }

    public int removeVideo(Video video, boolean fromDB) {
        if (video != null && allVideos.contains(video)) {
            if (fromDB && dbW != null) {
                int res = video.removeFromDB(dbW);
                if (res < 1) return ERROR;
            }
            allVideos.remove(video);
        }
        return OK;
    }

    public int updateVideo(Video video) {
        int res = video.updateInDb(dbW);
        if(res<1) return ERROR;
        return OK;
    }

    public SQLiteDatabase getDbW() {
        return dbW;
    }

    public void setDbW(SQLiteDatabase dbW) {
        this.dbW = dbW;
    }

    public SQLiteDatabase getDbR() {
        return dbR;
    }

    public void setDbR(SQLiteDatabase dbR) {
        this.dbR = dbR;
    }

    public interface DBListener {
        public void onReady();
    }

    public static List<Video> getVideosFromDb(SQLiteDatabase dbR) {
        ArrayList<Video> videos = new ArrayList<Video>();
        videos.addAll(getMoviesFromDb(dbR));
        videos.addAll(getSeriesFromDb(dbR));
        return videos;
    }

    protected static List<Movie> getMoviesFromDb(SQLiteDatabase dbR) {
        ArrayList<Movie> movies = new ArrayList<Movie>();
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = MovieCatalogContract.MovieEntry.ALL_COLUMNS;
        Cursor cursor = dbR.query(MovieCatalogContract.MovieEntry.TABLE_NAME,projection,null,null,null,null,null);
        while(cursor.moveToNext()) movies.add(new Movie(cursor, dbR));
        return movies;
    }

    protected static List<Series> getSeriesFromDb(SQLiteDatabase dbR) {
        ArrayList<Series> series = new ArrayList<Series>();
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = MovieCatalogContract.SeriesEntry.ALL_COLUMNS;
        Cursor cursor = dbR.query(MovieCatalogContract.SeriesEntry.TABLE_NAME,projection,null,null,null,null,null);
        cursor.moveToFirst();
        do {
            Series s = new Series(cursor, dbR);
            series.add(s);
        } while(cursor.moveToNext());
        return series;
    }

}
