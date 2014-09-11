package com.vwuilbea.mymoviecatalog.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

    //Types Names
    private static final String TYPE_TEXT = " TEXT";
    private static final String TYPE_INTEGER = " INTEGER";
    private static final String TYPE_BOOLEAN = " BOOLEAN";
    private static final String TYPE_REAL = " REAL";
    private static final String TYPE_PRIMARY = " INTEGER PRIMARY KEY";

    // Table Names
    private static final String TABLE_ACTOR = "actor";
    private static final String TABLE_COUNTRY = "country";
    private static final String TABLE_GENRE = "genre";
    private static final String TABLE_LOCATION = "location";
    private static final String TABLE_MOVIE = "movie";
    private static final String TABLE_PRODUCTION_COMPANY = "productionCompany";
    private static final String TABLE_REALISATOR = "realisator";
    private static final String TABLE_SERIES = "series";
    // Join table names
    private static final String TABLE_ROLE = "role";
    private static final String TABLE_VIDEO_GENRE = "videoGenre";
    private static final String TABLE_VIDEO_COUNTRY = "videoCountry";
    private static final String TABLE_VIDEO_PRODUCTION = "videoProduction";
    private static final String TABLE_VIDEO_REALISATOR = "videoRealisator";

    // Common column names
    private static final String KEY_ID = "id";

    // ENTITY Tables - column names
    private static final String COLUMN_NAME = "name";

    // PERSON Tables - column names
    private static final String COLUMN_FIRSTNAME = "firstname";
    private static final String COLUMN_BIRTHDAY = "birthday";
    private static final String COLUMN_COUNTRY_ID = "country_id";
    private static final String COLUMN_PROFILE_PATH = "profile_path";

    // LOCATION Table - column names
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_FOLDER = "folder";

    // SERIES Table - column names
    private static final String COLUMN_SEASON = "season";

    // VIDEO Table - column names
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_ORIGINAL_TITLE = "original_title";
    private static final String COLUMN_TAG_LINE = "tag_line";
    private static final String COLUMN_OVERVIEW = "overview";
    private static final String COLUMN_RUNTIME = "runtime";
    private static final String COLUMN_YEAR = "year";
    private static final String COLUMN_LANGUAGE = "language";
    private static final String COLUMN_SUBTITLE = "subtitle";
    private static final String COLUMN_LOCATION_ID = "location_id";
    private static final String COLUMN_ADULT = "adult";
    private static final String COLUMN_POSTER_PATH = "poster_path";
    private static final String COLUMN_BUDGET = "budget";
    private static final String COLUMN_VOTE_AVERAGE = "vote_average";
    private static final String COLUMN_VOTE_COUNT = "vote_count";

    // ROLE Table - column names
    private static final String COLUMN_ACTOR_ID = "actor_id";
    private static final String COLUMN_MOVIE_ID = "movie_id";
    private static final String COLUMN_CHARACTER = "character";

    // VIDEOPRODUCTION Table - column names
    private static final String COLUMN_GENRE_ID = "genre_id";

    // VIDEOPRODUCTION Table - column names
    private static final String COLUMN_PRODUCTION_ID = "production_id";

    // VIDEOREALISATOR Table - column names
    private static final String COLUMN_REALISATOR_ID = "realisator_id";

    // Actor table Create Statements
    private static final String CREATE_TABLE_ACTOR = "CREATE TABLE "
            + TABLE_ACTOR
            + "(" + KEY_ID + TYPE_PRIMARY + ","
            + COLUMN_NAME + TYPE_TEXT + ","
            + COLUMN_FIRSTNAME + TYPE_TEXT + ","
            + COLUMN_BIRTHDAY + TYPE_TEXT + ","
            + COLUMN_COUNTRY_ID + TYPE_INTEGER + ","
            + COLUMN_PROFILE_PATH + TYPE_TEXT + ")";

    // Country table Create Statements
    private static final String CREATE_TABLE_COUNTRY = "CREATE TABLE "
            + TABLE_COUNTRY
            + "(" + KEY_ID + TYPE_PRIMARY + ","
            + COLUMN_NAME + TYPE_TEXT + ")";

    // Genre table Create Statements
    private static final String CREATE_TABLE_GENRE = "CREATE TABLE "
            + TABLE_GENRE
            + "(" + KEY_ID + TYPE_PRIMARY + ","
            + COLUMN_NAME + TYPE_TEXT + ")";

    // Location table Create Statements
    private static final String CREATE_TABLE_LOCATION = "CREATE TABLE "
            + TABLE_LOCATION
            + "(" + KEY_ID + TYPE_PRIMARY + ","
            + COLUMN_NAME + TYPE_TEXT + ","
            + COLUMN_TYPE + TYPE_INTEGER + ","
            + COLUMN_FOLDER + TYPE_TEXT + ")";

    // Movie table Create Statements
    private static final String CREATE_TABLE_MOVIE = "CREATE TABLE "
            + TABLE_MOVIE
            + "(" + KEY_ID + TYPE_PRIMARY + ","
            + COLUMN_TITLE + TYPE_TEXT + ","
            + COLUMN_ORIGINAL_TITLE + TYPE_TEXT + ","
            + COLUMN_TAG_LINE + TYPE_TEXT + ","
            + COLUMN_OVERVIEW + TYPE_TEXT + ","
            + COLUMN_RUNTIME + TYPE_INTEGER + ","
            + COLUMN_YEAR + TYPE_INTEGER + ","
            + COLUMN_LANGUAGE + TYPE_TEXT + ","
            + COLUMN_SUBTITLE + TYPE_TEXT + ","
            + COLUMN_LOCATION_ID + TYPE_INTEGER + ","
            + COLUMN_ADULT + TYPE_BOOLEAN + ","
            + COLUMN_POSTER_PATH + TYPE_TEXT + ","
            + COLUMN_BUDGET + TYPE_INTEGER + ","
            + COLUMN_VOTE_AVERAGE+ TYPE_REAL + ","
            + COLUMN_VOTE_COUNT+ TYPE_INTEGER + ")";

    // ProductionCompany table Create Statements
    private static final String CREATE_TABLE_PRODUCTION = "CREATE TABLE "
            + TABLE_PRODUCTION_COMPANY
            + "(" + KEY_ID + TYPE_PRIMARY + ","
            + COLUMN_NAME + TYPE_TEXT + ")";

    // Realisator table Create Statements
    private static final String CREATE_TABLE_REALISATOR = "CREATE TABLE "
            + TABLE_REALISATOR
            + "(" + KEY_ID + TYPE_PRIMARY + ","
            + COLUMN_NAME + TYPE_TEXT + ","
            + COLUMN_FIRSTNAME + TYPE_TEXT + ","
            + COLUMN_BIRTHDAY + TYPE_TEXT + ","
            + COLUMN_COUNTRY_ID + TYPE_INTEGER + ","
            + COLUMN_PROFILE_PATH + TYPE_TEXT + ")";

    // Series table Create Statements
    private static final String CREATE_TABLE_SERIES = "CREATE TABLE "
            + TABLE_SERIES
            + "(" + KEY_ID + TYPE_PRIMARY + ","
            + COLUMN_TITLE + TYPE_TEXT + ","
            + COLUMN_ORIGINAL_TITLE + TYPE_TEXT + ","
            + COLUMN_TAG_LINE + TYPE_TEXT + ","
            + COLUMN_OVERVIEW + TYPE_TEXT + ","
            + COLUMN_RUNTIME + TYPE_INTEGER + ","
            + COLUMN_YEAR + TYPE_INTEGER + ","
            + COLUMN_LANGUAGE + TYPE_TEXT + ","
            + COLUMN_SUBTITLE + TYPE_TEXT + ","
            + COLUMN_LOCATION_ID + TYPE_INTEGER + ","
            + COLUMN_ADULT + TYPE_BOOLEAN + ","
            + COLUMN_POSTER_PATH + TYPE_TEXT + ","
            + COLUMN_BUDGET + TYPE_INTEGER + ","
            + COLUMN_VOTE_AVERAGE+ TYPE_REAL + ","
            + COLUMN_VOTE_COUNT+ TYPE_INTEGER + ","
            + COLUMN_SEASON+ TYPE_TEXT + ")";

    // Role table Create Statements
    private static final String CREATE_TABLE_ROLE = "CREATE TABLE "
            + TABLE_ROLE
            + "(" + COLUMN_ACTOR_ID + TYPE_INTEGER + ","
            + COLUMN_MOVIE_ID + TYPE_INTEGER + ","
            + COLUMN_CHARACTER+ TYPE_TEXT
            + " PRIMARY KEY ("
            + COLUMN_ACTOR_ID + ","
            + COLUMN_MOVIE_ID + ","
            + COLUMN_CHARACTER + ")"
            + ")";

    // VideoCountry table Create Statements
    private static final String CREATE_TABLE_VIDEO_COUNTRY = "CREATE TABLE "
            + TABLE_VIDEO_COUNTRY
            + "(" + COLUMN_COUNTRY_ID + TYPE_INTEGER + ","
            + COLUMN_MOVIE_ID + TYPE_INTEGER
            + " PRIMARY KEY ("
            + COLUMN_COUNTRY_ID + ","
            + COLUMN_MOVIE_ID + ")"
            + ")";

    // VideoCountry table Create Statements
    private static final String CREATE_TABLE_VIDEO_GENRE = "CREATE TABLE "
            + TABLE_VIDEO_GENRE
            + "(" + COLUMN_GENRE_ID+ TYPE_INTEGER + ","
            + COLUMN_MOVIE_ID + TYPE_INTEGER
            + " PRIMARY KEY ("
            + COLUMN_GENRE_ID+ ","
            + COLUMN_MOVIE_ID + ")"
            + ")";

    // VideoProduction table Create Statements
    private static final String CREATE_TABLE_VIDEO_PRODUCTION = "CREATE TABLE "
            + TABLE_VIDEO_PRODUCTION
            + "(" + COLUMN_PRODUCTION_ID + TYPE_INTEGER + ","
            + COLUMN_MOVIE_ID + TYPE_INTEGER
            + " PRIMARY KEY ("
            + COLUMN_PRODUCTION_ID + ","
            + COLUMN_MOVIE_ID + ")"
            + ")";

    // VideoProduction table Create Statements
    private static final String CREATE_TABLE_VIDEO_REALISATOR = "CREATE TABLE "
            + TABLE_VIDEO_REALISATOR
            + "(" + COLUMN_REALISATOR_ID + TYPE_INTEGER + ","
            + COLUMN_MOVIE_ID + TYPE_INTEGER
            + " PRIMARY KEY ("
            + COLUMN_REALISATOR_ID + ","
            + COLUMN_MOVIE_ID + ")"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_ACTOR);
        db.execSQL(CREATE_TABLE_COUNTRY);
        db.execSQL(CREATE_TABLE_GENRE);
        db.execSQL(CREATE_TABLE_LOCATION);
        db.execSQL(CREATE_TABLE_MOVIE);
        db.execSQL(CREATE_TABLE_PRODUCTION);
        db.execSQL(CREATE_TABLE_REALISATOR);
        db.execSQL(CREATE_TABLE_SERIES);
        db.execSQL(CREATE_TABLE_ROLE);
        db.execSQL(CREATE_TABLE_VIDEO_COUNTRY);
        db.execSQL(CREATE_TABLE_VIDEO_GENRE);
        db.execSQL(CREATE_TABLE_VIDEO_PRODUCTION);
        db.execSQL(CREATE_TABLE_VIDEO_REALISATOR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_ACTOR);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_COUNTRY);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_GENRE);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_LOCATION);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_MOVIE);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_PRODUCTION);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_REALISATOR);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_SERIES);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_ROLE);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_VIDEO_COUNTRY);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_VIDEO_GENRE);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_VIDEO_PRODUCTION);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_VIDEO_REALISATOR);

        // create new tables
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db,oldVersion,newVersion);

    }

}
