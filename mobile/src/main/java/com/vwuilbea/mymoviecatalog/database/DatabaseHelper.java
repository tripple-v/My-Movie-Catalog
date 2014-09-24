package com.vwuilbea.mymoviecatalog.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

/**
 * Created by Valentin on 06/09/2014.
 */
public class DatabaseHelper
        extends SQLiteOpenHelper {

    private static final String LOG = DatabaseHelper.class.getSimpleName();

    public static final String NULL = "null";

    public static final int OK = 0;
    public static final int ALREADY_IN_DB = -10;
    public static final int MULTIPLE_RESULTS = -20;
    public static final int ERROR = -30;

    // Database Version
    private static final int DATABASE_VERSION = 10;

    // Database Name
    private static final String DATABASE_NAME = "movieCatalog.db";

    private DBListener listener;

    public DatabaseHelper(Context context, DBListener listener) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.listener = listener;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(MovieCatalogContract.ActorEntry.CREATE_TABLE);
        db.execSQL(MovieCatalogContract.CountryEntry.CREATE_TABLE);
        db.execSQL(MovieCatalogContract.GenreEntry.CREATE_TABLE);
        db.execSQL(MovieCatalogContract.LocationEntry.CREATE_TABLE);
        db.execSQL(MovieCatalogContract.MovieEntry.CREATE_TABLE);
        db.execSQL(MovieCatalogContract.ProductionCompanyEntry.CREATE_TABLE);
        db.execSQL(MovieCatalogContract.RealisatorEntry.CREATE_TABLE);
        db.execSQL(MovieCatalogContract.SeriesEntry.CREATE_TABLE);
        db.execSQL(MovieCatalogContract.SeasonEntry.CREATE_TABLE);
        db.execSQL(MovieCatalogContract.RoleEntry.CREATE_TABLE);
        db.execSQL(MovieCatalogContract.VideoGenreEntry.CREATE_TABLE);
        db.execSQL(MovieCatalogContract.VideoCountryEntry.CREATE_TABLE);
        db.execSQL(MovieCatalogContract.PersonCountryEntry.CREATE_TABLE);
        db.execSQL(MovieCatalogContract.VideoProductionEntry.CREATE_TABLE);
        db.execSQL(MovieCatalogContract.VideoRealisatorEntry.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + MovieCatalogContract.ActorEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieCatalogContract.CountryEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieCatalogContract.GenreEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieCatalogContract.LocationEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieCatalogContract.MovieEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieCatalogContract.ProductionCompanyEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieCatalogContract.RealisatorEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieCatalogContract.SeriesEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieCatalogContract.SeasonEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieCatalogContract.RoleEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieCatalogContract.VideoGenreEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieCatalogContract.VideoCountryEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieCatalogContract.PersonCountryEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieCatalogContract.VideoProductionEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieCatalogContract.VideoRealisatorEntry.TABLE_NAME);

        // create new tables
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db,oldVersion,newVersion);
    }

    public void getDB() {
        GetDB getDB = new GetDB();
        getDB.doInBackground(null);
    }

    public interface DBListener {
        public void onReady (SQLiteDatabase dbR, SQLiteDatabase dbW);
    }

    private class GetDB extends AsyncTask {

        @Override
        protected SQLiteDatabase doInBackground(Object[] params) {
            // Gets the data repository in write mode
            SQLiteDatabase dbW = getWritableDatabase();
            SQLiteDatabase dbR = getReadableDatabase();
            if(listener!=null) listener.onReady(dbW, dbR);
            return null;
        }
    }

}
