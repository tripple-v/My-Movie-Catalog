package com.vwuilbea.mymoviecatalog.database;

import android.provider.BaseColumns;

import com.vwuilbea.mymoviecatalog.model.Actor;
import com.vwuilbea.mymoviecatalog.model.Country;
import com.vwuilbea.mymoviecatalog.model.Episode;
import com.vwuilbea.mymoviecatalog.model.Genre;
import com.vwuilbea.mymoviecatalog.model.Location;
import com.vwuilbea.mymoviecatalog.model.Movie;
import com.vwuilbea.mymoviecatalog.model.ProductionCompany;
import com.vwuilbea.mymoviecatalog.model.Realisator;
import com.vwuilbea.mymoviecatalog.model.Role;
import com.vwuilbea.mymoviecatalog.model.Season;
import com.vwuilbea.mymoviecatalog.model.Series;
import com.vwuilbea.mymoviecatalog.model.Video;

import java.util.ArrayList;
import java.util.List;

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

    /* Inner class that defines the abstract class Video */
    public static abstract class VideoEntry implements BaseColumns {
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
        public static final String COLUMN_COVER_PATH = "cover_path";
        public static final String COLUMN_BUDGET = "budget";
        public static final String COLUMN_QUALITY = "quality";
        public static final String COLUMN_THREE_D = "three_d";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_VOTE_COUNT = "vote_count";
        public static final String COLUMN_VOTE_PRIVATE = "vote_private";

        public static final String[] ALL_COLUMNS = {
                _ID, COLUMN_TITLE, COLUMN_ORIGINAL_TITLE, COLUMN_TAG_LINE, COLUMN_OVERVIEW, COLUMN_RUNTIME,
                COLUMN_DATE, COLUMN_LANGUAGE, COLUMN_SUBTITLE, COLUMN_LOCATION_ID, COLUMN_ADULT, COLUMN_POSTER_PATH,
                COLUMN_COVER_PATH, COLUMN_BUDGET, COLUMN_QUALITY, COLUMN_THREE_D, COLUMN_VOTE_AVERAGE, COLUMN_VOTE_COUNT, COLUMN_VOTE_PRIVATE
        };

        // Movie table Create Statements
        public static final String CREATE_TABLE = "CREATE TABLE %s"
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
                + COLUMN_COVER_PATH + TYPE_TEXT + ","
                + COLUMN_BUDGET + TYPE_INTEGER + ","
                + COLUMN_QUALITY + TYPE_INTEGER + ","
                + COLUMN_THREE_D + TYPE_BOOLEAN + ","
                + COLUMN_VOTE_AVERAGE+ TYPE_REAL + ","
                + COLUMN_VOTE_COUNT+ TYPE_INTEGER + ","
                + COLUMN_VOTE_PRIVATE+ TYPE_REAL
                + "%s)";
    }

    /* Inner class that defines the abstract class Entity */
    public static abstract class EntityEntry implements BaseColumns {
        public static final String COLUMN_NAME = "name";

        public static final String[] ALL_COLUMNS = {_ID, COLUMN_NAME};

        // Movie table Create Statements
        public static final String CREATE_TABLE = "CREATE TABLE %s"
                + "(" + _ID + TYPE_PRIMARY + ","
                + COLUMN_NAME+ TYPE_TEXT
                + "%s)";
    }

    /* Inner class that defines the abstract class VideoEntity */
    public static abstract class VideoEntityEntry implements BaseColumns {
        public static final String COLUMN_VIDEO_ID = "video_id";
        public static final String COLUMN_ENTITY_ID = "entity_id";
        public static final String[] ALL_COLUMNS = { COLUMN_VIDEO_ID, COLUMN_ENTITY_ID};

        // VideoEntity table Create Statements
        public static final String CREATE_TABLE = "CREATE TABLE %s"
                + "(" + COLUMN_ENTITY_ID + TYPE_INTEGER + ","
                + COLUMN_VIDEO_ID + TYPE_INTEGER
                + ", PRIMARY KEY ("
                + COLUMN_ENTITY_ID + ","
                + COLUMN_VIDEO_ID + ")"
                + ")";

    }

    /* Inner class that defines the abstract class Person */
    public static abstract class PersonEntry implements BaseColumns {
        public static final String COLUMN_FIRSTNAME = "firstname";
        public static final String COLUMN_BIRTHDAY = "birthday";
        public static final String COLUMN_COUNTRY_ID = "country_id";
        public static final String COLUMN_PROFILE_PATH = "profile_path";

        private static final String[] COLUMNS = { _ID, COLUMN_FIRSTNAME, COLUMN_BIRTHDAY,
                COLUMN_COUNTRY_ID, COLUMN_PROFILE_PATH};

        public static final String[] ALL_COLUMNS = new String[EntityEntry.ALL_COLUMNS.length+COLUMNS.length];
        static {
            System.arraycopy(EntityEntry.ALL_COLUMNS,0,ALL_COLUMNS,0,EntityEntry.ALL_COLUMNS.length);
            System.arraycopy(COLUMNS,0,ALL_COLUMNS,EntityEntry.ALL_COLUMNS.length,COLUMNS.length);
        }

        // Person table Create Statements
        public static final String CREATE_TABLE = "CREATE TABLE "
                + "%s"
                + "(" + _ID + TYPE_PRIMARY + ","
                + EntityEntry.COLUMN_NAME + TYPE_TEXT + ","
                + COLUMN_FIRSTNAME + TYPE_TEXT + ","
                + COLUMN_BIRTHDAY + TYPE_TEXT + ","
                + COLUMN_COUNTRY_ID + TYPE_INTEGER + ","
                + COLUMN_PROFILE_PATH + TYPE_TEXT
                + "%s)";
    }

    /* Inner class that defines the table Actor */
    public static abstract class ActorEntry implements BaseColumns {
        public static final String TABLE_NAME = Actor.class.getSimpleName().toLowerCase();

        public static final String[] ALL_COLUMNS = PersonEntry.ALL_COLUMNS;
        // Actor table Create Statements
        public static final String CREATE_TABLE = String.format(PersonEntry.CREATE_TABLE,TABLE_NAME,null);

    }

    /* Inner class that defines the table Country */
    public static abstract class CountryEntry implements BaseColumns {
        public static final String TABLE_NAME = Country.class.getSimpleName().toLowerCase();
        public static final String COLUMN_ISO = "iso";

        private static final String[] COLUMNS = {COLUMN_ISO};
        public static final String[] ALL_COLUMNS = new String[EntityEntry.ALL_COLUMNS.length+COLUMNS.length];
        static {
            System.arraycopy(EntityEntry.ALL_COLUMNS,0,ALL_COLUMNS,0,EntityEntry.ALL_COLUMNS.length);
            System.arraycopy(COLUMNS,0,ALL_COLUMNS,EntityEntry.ALL_COLUMNS.length,COLUMNS.length);
        }

        // Country table Create Statements
        public static final String CREATE_TABLE = String.format(EntityEntry.CREATE_TABLE, TABLE_NAME, ","
                + COLUMN_ISO + TYPE_TEXT);
    }

    /* Inner class that defines the table Genre */
    public static abstract class GenreEntry implements BaseColumns {
        public static final String TABLE_NAME = Genre.class.getSimpleName().toLowerCase();

        public static String[] ALL_COLUMNS = EntityEntry.ALL_COLUMNS;

        public static final String CREATE_TABLE = String.format(EntityEntry.CREATE_TABLE, TABLE_NAME, null);
    }

    /* Inner class that defines the table Location */
    public static abstract class LocationEntry implements BaseColumns {
        public static final String TABLE_NAME = Location.class.getSimpleName().toLowerCase();
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_FOLDER = "folder";

        private static final String[] COLUMNS = {COLUMN_TYPE, COLUMN_FOLDER};
        public static final String[] ALL_COLUMNS = new String[EntityEntry.ALL_COLUMNS.length+COLUMNS.length];
        static {
            System.arraycopy(EntityEntry.ALL_COLUMNS,0,ALL_COLUMNS,0,EntityEntry.ALL_COLUMNS.length);
            System.arraycopy(COLUMNS,0,ALL_COLUMNS,EntityEntry.ALL_COLUMNS.length,COLUMNS.length);
        }

        // Location table Create Statements
        public static final String CREATE_TABLE = String.format(EntityEntry.CREATE_TABLE, TABLE_NAME, ","
                + COLUMN_TYPE + TYPE_INTEGER + ","
                + COLUMN_FOLDER + TYPE_TEXT);

    }

    /* Inner class that defines the table Movie */
    public static abstract class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME = Movie.class.getSimpleName().toLowerCase();

        public static final String[] ALL_COLUMNS = VideoEntry.ALL_COLUMNS;

        // Movie table Create Statements
        public static final String CREATE_TABLE = String.format(VideoEntry.CREATE_TABLE, TABLE_NAME, null);
    }

    /* Inner class that defines the table ProductionCompany */
    public static abstract class ProductionCompanyEntry implements BaseColumns {
        public static final String TABLE_NAME = ProductionCompany.class.getSimpleName().toLowerCase();

        public static String[] ALL_COLUMNS = EntityEntry.ALL_COLUMNS;

        public static final String CREATE_TABLE = String.format(EntityEntry.CREATE_TABLE, TABLE_NAME, null);
    }

    /* Inner class that defines the table Realisator */
    public static abstract class RealisatorEntry implements BaseColumns {
        public static final String TABLE_NAME = Realisator.class.getSimpleName().toLowerCase();

        public static final String[] ALL_COLUMNS = PersonEntry.ALL_COLUMNS;
        // Realisator table Create Statements
        public static final String CREATE_TABLE = String.format(PersonEntry.CREATE_TABLE,TABLE_NAME,null);
    }

    /* Inner class that defines the table Series */
    public static abstract class SeriesEntry implements BaseColumns {
        public static final String TABLE_NAME = Series.class.getSimpleName().toLowerCase();
        public static final String COLUMN_LAST_DATE = "last_date";
        public static final String COLUMN_IN_PRODUCTION = "in_production";

        private static final String[] COLUMNS = { COLUMN_LAST_DATE, COLUMN_IN_PRODUCTION};

        public static final String[] ALL_COLUMNS = new String[VideoEntry.ALL_COLUMNS.length+COLUMNS.length];
        static {
            System.arraycopy(VideoEntry.ALL_COLUMNS,0,ALL_COLUMNS,0,VideoEntry.ALL_COLUMNS.length);
            System.arraycopy(COLUMNS,0,ALL_COLUMNS,VideoEntry.ALL_COLUMNS.length,COLUMNS.length);
        }


        // Series table Create Statements
        public static final String CREATE_TABLE = String.format(VideoEntry.CREATE_TABLE, TABLE_NAME,
                "," + COLUMN_LAST_DATE + TYPE_TEXT +
                "," + COLUMN_IN_PRODUCTION + TYPE_BOOLEAN);
    }

    /* Inner class that defines the table Series */
    public static abstract class SeasonEntry implements BaseColumns {
        public static final String TABLE_NAME = Season.class.getSimpleName().toLowerCase();
        public static final String COLUMN_SERIES_ID = "series_id";
        public static final String COLUMN_NUMBER = "number";
        public static final String COLUMN_POSSESSED = "possessed";
        public static final String COLUMN_QUALITY = "quality";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_FIRST_DATE = "first_date";

        public static final String[] ALL_COLUMNS = {
                _ID, COLUMN_SERIES_ID, COLUMN_NUMBER, COLUMN_POSSESSED, COLUMN_QUALITY,
                COLUMN_OVERVIEW, COLUMN_POSTER_PATH, COLUMN_FIRST_DATE
        };

        // Season table Create Statements
        public static final String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME
                + "(" + _ID + TYPE_PRIMARY_TEXT + ","
                + COLUMN_SERIES_ID + TYPE_INTEGER + ","
                + COLUMN_NUMBER + TYPE_INTEGER + ","
                + COLUMN_POSSESSED + TYPE_BOOLEAN + ","
                + COLUMN_QUALITY + TYPE_INTEGER + ","
                + COLUMN_OVERVIEW + TYPE_TEXT + ","
                + COLUMN_POSTER_PATH + TYPE_TEXT + ","
                + COLUMN_FIRST_DATE + TYPE_TEXT
                + ")";
    }

    /* Inner class that defines the table Series */
    public static abstract class EpisodeEntry implements BaseColumns {
        public static final String TABLE_NAME = Episode.class.getSimpleName().toLowerCase();
        public static final String COLUMN_SEASON_ID = "season_id";
        public static final String COLUMN_NUMBER = "number";
        public static final String COLUMN_POSSESSED = "possessed";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_QUALITY = "quality";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_VOTE_COUNT = "vote_count";


        public static final String[] ALL_COLUMNS = {
                _ID, COLUMN_SEASON_ID, COLUMN_NUMBER, COLUMN_POSSESSED, COLUMN_DATE,
                COLUMN_TITLE, COLUMN_QUALITY, COLUMN_OVERVIEW, COLUMN_POSTER_PATH,
                COLUMN_VOTE_AVERAGE, COLUMN_VOTE_COUNT
        };

        // Season table Create Statements
        public static final String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME
                + "(" + _ID + TYPE_PRIMARY_TEXT + ","
                + COLUMN_SEASON_ID + TYPE_INTEGER + ","
                + COLUMN_NUMBER + TYPE_INTEGER + ","
                + COLUMN_POSSESSED + TYPE_BOOLEAN + ","
                + COLUMN_DATE + TYPE_TEXT + ","
                + COLUMN_TITLE + TYPE_TEXT + ","
                + COLUMN_QUALITY + TYPE_INTEGER + ","
                + COLUMN_OVERVIEW + TYPE_TEXT + ","
                + COLUMN_POSTER_PATH + TYPE_TEXT + ","
                + COLUMN_VOTE_AVERAGE + TYPE_REAL + ","
                + COLUMN_VOTE_COUNT + TYPE_INTEGER
                + ")";
    }

    /* Inner class that defines the table Role */
    public static abstract class RoleEntry implements BaseColumns {
        public static final String TABLE_NAME = Role.class.getSimpleName().toLowerCase();
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

        // Series table Create Statements
        public static final String CREATE_TABLE = String.format(VideoEntityEntry.CREATE_TABLE, TABLE_NAME, null);

    }

    /* Inner class that defines the table VideoCountry */
    public static abstract class VideoCountryEntry implements BaseColumns {
        public static final String TABLE_NAME = "videoCountry";

        // Series table Create Statements
        public static final String CREATE_TABLE = String.format(VideoEntityEntry.CREATE_TABLE, TABLE_NAME, null);
    }

    /* Inner class that defines the table VideoCountry */
    public static abstract class PersonCountryEntry implements BaseColumns {
        public static final String TABLE_NAME = "personCountry";
        public static final String COLUMN_PERSON_ID = "person_id";
        public static final String COLUMN_COUNTRY_ID = "country_id";

        // VideoCountry table Create Statements
        public static final String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME
                + "(" + COLUMN_COUNTRY_ID + TYPE_INTEGER + ","
                + COLUMN_PERSON_ID + TYPE_INTEGER
                + ", PRIMARY KEY ("
                + COLUMN_COUNTRY_ID + ","
                + COLUMN_PERSON_ID + ")"
                + ")";
    }

    /* Inner class that defines the table VideoProduction */
    public static abstract class VideoProductionEntry implements BaseColumns {
        public static final String TABLE_NAME = "videoProduction";

        // Series table Create Statements
        public static final String CREATE_TABLE = String.format(VideoEntityEntry.CREATE_TABLE, TABLE_NAME, null);
    }

    /* Inner class that defines the table VideoRealisator */
    public static abstract class VideoRealisatorEntry implements BaseColumns {
        public static final String TABLE_NAME = "videoRealisator";

        // Series table Create Statements
        public static final String CREATE_TABLE = String.format(VideoEntityEntry.CREATE_TABLE, TABLE_NAME, null);
    }

}
