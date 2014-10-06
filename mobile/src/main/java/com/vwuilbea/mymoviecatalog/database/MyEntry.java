package com.vwuilbea.mymoviecatalog.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by vwuilbea on 24/09/2014.
 */
public abstract class MyEntry {

    private static final String LOG = MyEntry.class.getSimpleName();

    public abstract int getId();
    protected abstract String getTableName();
    protected abstract String[] getAllColumns();
    protected abstract ContentValues getContentValues();
    protected abstract void initFromCursor(Cursor cursor, SQLiteDatabase dbR);
    protected abstract void addDependencies(SQLiteDatabase dbW, SQLiteDatabase dbR);
    protected abstract void removeDependencies(SQLiteDatabase dbW, String[] selectionArgs);


    public int getFromDb(SQLiteDatabase dbR, boolean init) {
        String[] projection = getAllColumns();
        String WHERE = BaseColumns._ID + "=?";
        String[] selectionArgs = {String.valueOf(getId())};
        Cursor cursor = dbR.query(getTableName(),projection,WHERE,selectionArgs,null,null,null);
        if(cursor.getCount()>1) return DatabaseHelper.MULTIPLE_RESULTS;
        if(init && cursor.getCount()>0) initFromCursor(cursor, dbR);
        else return cursor.getCount();
        return DatabaseHelper.OK;
    }

    public boolean isInDb(SQLiteDatabase dbR) {
        return getFromDb(dbR, false) != 0;
    }

    public int putInDB(SQLiteDatabase dbW, SQLiteDatabase dbR) {
        if (!isInDb(dbR)) {
            // Insert the new row, returning the primary key value of the new row
            long newRowId;
            newRowId = dbW.insert(getTableName(),DatabaseHelper.NULL,getContentValues());
            if (newRowId == getId()) {
                Log.d(LOG,"new "+getTableName()+" row insert : "+newRowId);
                //We can add other tables rows
                addDependencies(dbW, dbR);
            }
            else {
                Log.d(LOG,getTableName()+" row id ( "+newRowId+" ) is different of : "+getId());
                return DatabaseHelper.ERROR;
            }
        }
        else {
            Log.d(LOG,getTableName()+" row is already in DB : "+getId());
        }
        return DatabaseHelper.OK;
    }

    public int removeFromDB(SQLiteDatabase dbW) {
        String selection = BaseColumns._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(getId()) };
        removeDependencies(dbW, selectionArgs);
        return dbW.delete(getTableName(), selection, selectionArgs);
    }

    public int updateInDb(SQLiteDatabase dbW) {
        String whereClause = BaseColumns._ID + "=?";
        String[] whereArgs = {String.valueOf(getId())};

        return dbW.update(getTableName(),getContentValues(),whereClause,whereArgs);
    }

}
