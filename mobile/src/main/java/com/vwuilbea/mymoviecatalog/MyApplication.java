package com.vwuilbea.mymoviecatalog;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.vwuilbea.mymoviecatalog.database.DatabaseHelper;
import com.vwuilbea.mymoviecatalog.model.Movie;
import com.vwuilbea.mymoviecatalog.model.Video;

import java.util.ArrayList;
import java.util.List;

/**
 * Class which contains global variables
 */
public class MyApplication extends Application {

    public static final int ERROR = DatabaseHelper.ERROR;
    public static final int OK = DatabaseHelper.OK;

    private ArrayList<Video> allVideos = new ArrayList<Video>();

    public ArrayList<Video> getAllVideos() {
        return allVideos;
    }

    public void setAllVideos(ArrayList<Video> allVideos) {
        this.allVideos = allVideos;
    }

    public int addVideo(Video video, boolean putInDB, SQLiteDatabase dbW, SQLiteDatabase dbR) {
        if (video != null && !allVideos.contains(video)) {
            if(putInDB && dbW!=null && dbR!=null) {
                if(video instanceof Movie) {
                     int res = ((Movie) video).putInDB(dbW, dbR);
                    if(res==ERROR) return ERROR;
                }
            }
            allVideos.add(video);
        }
        return OK;
    }

    public void addVideos(List<? extends Video> videos) {
        for (Video video : videos)
            if (video != null && !allVideos.contains(video))
                allVideos.add(video);
    }

    public int removeVideo(Video video, boolean fromDB, SQLiteDatabase dbW) {
        if(video!=null && allVideos.contains(video)) {
            if(fromDB && dbW!=null) {
                if(video instanceof Movie) {
                    int res = ((Movie) video).removeFromDB(dbW);
                    if(res<1) return ERROR;
                }
            }
            allVideos.remove(video);
        }
        return OK;
    }
}
