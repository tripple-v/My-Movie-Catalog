package com.vwuilbea.mymoviecatalog.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

/**
 * Created by Valentin on 06/09/2014.
 */
public class DatabaseHelper
        extends SQLiteOpenHelper {

    private static final String LOG = DatabaseHelper.class.getSimpleName();

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "movieCatalog.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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
        db.execSQL(MovieCatalogContract.RoleEntry.CREATE_TABLE);
        db.execSQL(MovieCatalogContract.VideoCountryEntry.CREATE_TABLE);
        db.execSQL(MovieCatalogContract.VideoProductionEntry.CREATE_TABLE);
        db.execSQL(MovieCatalogContract.VideoRealisatorEntry.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + MovieCatalogContract.ActorEntry.CREATE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MovieCatalogContract.CountryEntry.CREATE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MovieCatalogContract.GenreEntry.CREATE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MovieCatalogContract.LocationEntry.CREATE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MovieCatalogContract.MovieEntry.CREATE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MovieCatalogContract.ProductionCompanyEntry.CREATE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MovieCatalogContract.RealisatorEntry.CREATE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MovieCatalogContract.SeriesEntry.CREATE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MovieCatalogContract.RoleEntry.CREATE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MovieCatalogContract.VideoCountryEntry.CREATE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MovieCatalogContract.VideoProductionEntry.CREATE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MovieCatalogContract.VideoRealisatorEntry.CREATE_TABLE);

        // create new tables
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db,oldVersion,newVersion);

    }

}
