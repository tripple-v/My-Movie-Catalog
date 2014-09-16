package com.vwuilbea.mymoviecatalog.database;

import android.provider.BaseColumns;

/**
 * Created by vwuilbea on 12/09/2014.
 */
public class MovieCatalogContract {

    //Types Names
    private static final String TYPE_TEXT = " TEXT";
    private static final String TYPE_INTEGER = " INTEGER";
    private static final String TYPE_BOOLEAN = " BOOLEAN";
    private static final String TYPE_REAL = " REAL";
    private static final String TYPE_PRIMARY = " INTEGER PRIMARY KEY";
    private static final String TYPE_PRIMARY_TEXT = " TEXT PRIMARY KEY";

    public MovieCatalogContract() {
    }

    /* Inner class that defines the table Actor */
    public static abstract class ActorEntry implements BaseColumns {
        public static final String TABLE_NAME = "actor";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_FIRSTNAME = "firstname";
        public static final String COLUMN_BIRTHDAY = "birthday";
        public static final String COLUMN_COUNTRY_ID = "country_id";
        public static final String COLUMN_PROFILE_PATH = "profile_path";

        // Actor table Create Statements
        public static final String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME
                + "(" + _ID + TYPE_PRIMARY + ","
                + COLUMN_NAME + TYPE_TEXT + ","
                + COLUMN_FIRSTNAME + TYPE_TEXT + ","
                + COLUMN_BIRTHDAY + TYPE_TEXT + ","
                + COLUMN_COUNTRY_ID + TYPE_INTEGER + ","
                + COLUMN_PROFILE_PATH + TYPE_TEXT + ")";
    }

    /* Inner class that defines the table Country */
    public static abstract class CountryEntry implements BaseColumns {
        public static final String TABLE_NAME = "country";
        public static final String COLUMN_NAME = "name";

        // Country table Create Statements
        public static final String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME
                + "(" + _ID + TYPE_PRIMARY + ","
                + COLUMN_NAME + TYPE_TEXT + ")";
    }

    /* Inner class that defines the table Genre */
    public static abstract class GenreEntry implements BaseColumns {
        public static final String TABLE_NAME = "genre";
        public static final String COLUMN_NAME = "name";

        public static final String[] ALL_COLUMNS = {_ID, COLUMN_NAME};

        // Genre table Create Statements
        public static final String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME
                + "(" + _ID + TYPE_PRIMARY + ","
                + COLUMN_NAME + TYPE_TEXT + ")";
    }

    /* Inner class that defines the table Location */
    public static abstract class LocationEntry implements BaseColumns {
        public static final String TABLE_NAME = "location";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_FOLDER = "folder";

        // Location table Create Statements
        public static final String CREATE_TABLE= "CREATE TABLE "
                + TABLE_NAME
                + "(" + _ID + TYPE_PRIMARY + ","
                + COLUMN_NAME + TYPE_TEXT + ","
                + COLUMN_TYPE + TYPE_INTEGER + ","
                + COLUMN_FOLDER + TYPE_TEXT + ")";
    }

    /* Inner class that defines the table Movie */
    public static abstract class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_TAG_LINE = "tag_line";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RUNTIME = "runtime";
        public static final String COLUMN_DATE = "release_date";
        public static final String COLUMN_LANGUAGE = "language";
        public static final String COLUMN_SUBTITLE = "subtitle";
        public static final String COLUMN_LOCATION_ID = "location_id";
        public static final String COLUMN_ADULT = "adult";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_BUDGET = "budget";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_VOTE_COUNT = "vote_count";

        public static final String[] ALL_COLUMNS = {
                _ID, COLUMN_TITLE, COLUMN_ORIGINAL_TITLE, COLUMN_TAG_LINE, COLUMN_OVERVIEW, COLUMN_RUNTIME,
                COLUMN_DATE, COLUMN_LANGUAGE, COLUMN_SUBTITLE, COLUMN_LOCATION_ID, COLUMN_ADULT, COLUMN_POSTER_PATH,
                COLUMN_BUDGET, COLUMN_VOTE_AVERAGE, COLUMN_VOTE_COUNT
        };

        // Movie table Create Statements
        public static final String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME
                + "(" + _ID + TYPE_PRIMARY + ","
                + COLUMN_TITLE + TYPE_TEXT + ","
                + COLUMN_ORIGINAL_TITLE + TYPE_TEXT + ","
                + COLUMN_TAG_LINE + TYPE_TEXT + ","
                + COLUMN_OVERVIEW + TYPE_TEXT + ","
                + COLUMN_RUNTIME + TYPE_INTEGER + ","
                + COLUMN_DATE + TYPE_TEXT + ","
                + COLUMN_LANGUAGE + TYPE_TEXT + ","
                + COLUMN_SUBTITLE + TYPE_TEXT + ","
                + COLUMN_LOCATION_ID + TYPE_INTEGER + ","
                + COLUMN_ADULT + TYPE_BOOLEAN + ","
                + COLUMN_POSTER_PATH + TYPE_TEXT + ","
                + COLUMN_BUDGET + TYPE_INTEGER + ","
                + COLUMN_VOTE_AVERAGE+ TYPE_REAL + ","
                + COLUMN_VOTE_COUNT+ TYPE_INTEGER + ")";
    }

    /* Inner class that defines the table ProductionCompany */
    public static abstract class ProductionCompanyEntry implements BaseColumns {
        public static final String TABLE_NAME = "productionCompany";
        public static final String COLUMN_NAME = "name";

        // ProductionCompany table Create Statements
        public static final String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME
                + "(" + _ID + TYPE_PRIMARY + ","
                + COLUMN_NAME + TYPE_TEXT + ")";
    }

    /* Inner class that defines the table Realisator */
    public static abstract class RealisatorEntry implements BaseColumns {
        public static final String TABLE_NAME = "realisator";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_FIRSTNAME = "firstname";
        public static final String COLUMN_BIRTHDAY = "birthday";
        public static final String COLUMN_COUNTRY_ID = "country_id";
        public static final String COLUMN_PROFILE_PATH = "profile_path";

        // Realisator table Create Statements
        public static final String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME
                + "(" + _ID + TYPE_PRIMARY + ","
                + COLUMN_NAME + TYPE_TEXT + ","
                + COLUMN_FIRSTNAME + TYPE_TEXT + ","
                + COLUMN_BIRTHDAY + TYPE_TEXT + ","
                + COLUMN_COUNTRY_ID + TYPE_INTEGER + ","
                + COLUMN_PROFILE_PATH + TYPE_TEXT + ")";
    }

    /* Inner class that defines the table Series */
    public static abstract class SeriesEntry implements BaseColumns {
        public static final String TABLE_NAME = "series";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_TAG_LINE = "tag_line";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RUNTIME = "runtime";
        public static final String COLUMN_DATE = "release_date";
        public static final String COLUMN_LANGUAGE = "language";
        public static final String COLUMN_SUBTITLE = "subtitle";
        public static final String COLUMN_LOCATION_ID = "location_id";
        public static final String COLUMN_ADULT = "adult";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_BUDGET = "budget";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_VOTE_COUNT = "vote_count";
        public static final String COLUMN_SEASON = "season";

        // Series table Create Statements
        public static final String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME
                + "(" + _ID + TYPE_PRIMARY + ","
                + COLUMN_TITLE + TYPE_TEXT + ","
                + COLUMN_ORIGINAL_TITLE + TYPE_TEXT + ","
                + COLUMN_TAG_LINE + TYPE_TEXT + ","
                + COLUMN_OVERVIEW + TYPE_TEXT + ","
                + COLUMN_RUNTIME + TYPE_INTEGER + ","
                + COLUMN_DATE + TYPE_TEXT + ","
                + COLUMN_LANGUAGE + TYPE_TEXT + ","
                + COLUMN_SUBTITLE + TYPE_TEXT + ","
                + COLUMN_LOCATION_ID + TYPE_INTEGER + ","
                + COLUMN_ADULT + TYPE_BOOLEAN + ","
                + COLUMN_POSTER_PATH + TYPE_TEXT + ","
                + COLUMN_BUDGET + TYPE_INTEGER + ","
                + COLUMN_VOTE_AVERAGE+ TYPE_REAL + ","
                + COLUMN_VOTE_COUNT+ TYPE_INTEGER + ","
                + COLUMN_SEASON+ TYPE_TEXT + ")";
    }

    /* Inner class that defines the table Role */
    public static abstract class RoleEntry implements BaseColumns {
        public static final String TABLE_NAME = "role";
        public static final String COLUMN_VIDEO_ID = "video_id";
        public static final String COLUMN_ACTOR_ID = "actor_id";
        public static final String COLUMN_CHARACTER = "character";
        public static final String[] ALL_COLUMNS = { _ID, COLUMN_ACTOR_ID, COLUMN_VIDEO_ID, COLUMN_CHARACTER};

        // Role table Create Statements
        public static final String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME
                + "(" + _ID + TYPE_PRIMARY_TEXT + ","
                + COLUMN_VIDEO_ID + TYPE_INTEGER + ","
                + COLUMN_ACTOR_ID + TYPE_INTEGER + ","
                + COLUMN_CHARACTER + TYPE_TEXT
                + ")";
    }

    /* Inner class that defines the table VideoGenre */
    public static abstract class VideoGenreEntry implements BaseColumns {
        public static final String TABLE_NAME = "videoGenre";
        public static final String COLUMN_VIDEO_ID = "video_id";
        public static final String COLUMN_GENRE_ID = "genre_id";
        public static final String[] ALL_COLUMNS = { COLUMN_VIDEO_ID, COLUMN_GENRE_ID};

        // VideoCountry table Create Statements
        public static final String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME
                + "(" + COLUMN_GENRE_ID + TYPE_INTEGER + ","
                + COLUMN_VIDEO_ID + TYPE_INTEGER
                + ", PRIMARY KEY ("
                + COLUMN_GENRE_ID + ","
                + COLUMN_VIDEO_ID + ")"
                + ")";

    }

    /* Inner class that defines the table VideoCountry */
    public static abstract class VideoCountryEntry implements BaseColumns {
        public static final String TABLE_NAME = "videoCountry";
        public static final String COLUMN_VIDEO_ID = "video_id";
        public static final String COLUMN_COUNTRY_ID = "country_id";

        // VideoCountry table Create Statements
        public static final String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME
                + "(" + COLUMN_COUNTRY_ID + TYPE_INTEGER + ","
                + COLUMN_VIDEO_ID + TYPE_INTEGER
                + ", PRIMARY KEY ("
                + COLUMN_COUNTRY_ID + ","
                + COLUMN_VIDEO_ID + ")"
                + ")";
    }

    /* Inner class that defines the table VideoProduction */
    public static abstract class VideoProductionEntry implements BaseColumns {
        public static final String TABLE_NAME = "videoProduction";
        public static final String COLUMN_VIDEO_ID = "video_id";
        public static final String COLUMN_PRODUCTION_ID = "production_id";

        // VideoProduction table Create Statements
        public static final String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME
                + "(" + COLUMN_PRODUCTION_ID + TYPE_INTEGER + ","
                + COLUMN_VIDEO_ID + TYPE_INTEGER
                + ", PRIMARY KEY ("
                + COLUMN_PRODUCTION_ID + ","
                + COLUMN_VIDEO_ID + ")"
                + ")";
    }

    /* Inner class that defines the table VideoRealisator */
    public static abstract class VideoRealisatorEntry implements BaseColumns {
        public static final String TABLE_NAME = "videoRealisator";
        public static final String COLUMN_VIDEO_ID = "video_id";
        public static final String COLUMN_REALISATOR_ID = "realisator_id";

        // VideoRealisator table Create Statements
        public static final String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME
                + "(" + COLUMN_REALISATOR_ID + TYPE_INTEGER + ","
                + COLUMN_VIDEO_ID + TYPE_INTEGER
                + ", PRIMARY KEY ("
                + COLUMN_REALISATOR_ID + ","
                + COLUMN_VIDEO_ID + ")"
                + ")";
    }

}
