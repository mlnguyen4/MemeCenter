package teamsylvanmatthew.memecenter.Database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import teamsylvanmatthew.memecenter.Models.Filter;
import teamsylvanmatthew.memecenter.Models.Rule;

public class MemeCenterDataSource {
    private static final String TAG = "MemeCenterDataSource";
    private SQLiteDatabase database;
    private MemeCenterDatabaseHelper databaseHelper;

    public MemeCenterDataSource(Context context) {
        databaseHelper = new MemeCenterDatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = databaseHelper.getWritableDatabase();
    }

    public void close() {
        databaseHelper.close();
    }


    public ArrayList<String> getRules(long filterId) {
        ArrayList<String> rules = new ArrayList<String>();

        final String query = "SELECT " + Rule.REGEX_COLUMN + " FROM " + Filter.TABLE_NAME + " JOIN " + Rule.TABLE_NAME + " ON " + Filter.FILTER_ID_COLUMN + " = " + Rule.FILTER_ID_FK_COLUMN + " WHERE " + Filter.FILTER_ID_COLUMN + " = " + filterId + ";";

        Cursor cursor = database.rawQuery(query, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        while (!cursor.isAfterLast()) {
            rules.add(cursor.getString(0));
            cursor.moveToNext();
        }
        Log.i(TAG, "query: " + query);

        return rules;
    }

    public boolean filterExists(String name) {
        final String query = "SELECT * FROM " + Filter.TABLE_NAME + " WHERE " + Filter.NAME_COLUMN + " = \"" + name + "\";";

        Cursor cursor = database.rawQuery(query, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        return !cursor.isAfterLast();
    }

    public ArrayList<Filter> getAllFilters() {
        ArrayList<Filter> filters = new ArrayList<Filter>();

        final String query = "SELECT " + Filter.FILTER_ID_COLUMN + ", " + Filter.NAME_COLUMN + " FROM " + Filter.TABLE_NAME + ";";

        Cursor cursor = database.rawQuery(query, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        while (!cursor.isAfterLast()) {
            filters.add(new Filter(cursor.getInt(0), cursor.getString(1)));
            cursor.moveToNext();
        }
        Log.i(TAG, "query: " + query);

        return filters;
    }

    public long addFilter(Filter filter) {
        if (filter != null) {
            ContentValues currentValues = new ContentValues();
            currentValues.put(Filter.NAME_COLUMN, filter.getName());
            return database.insert(Filter.TABLE_NAME, null, currentValues);
        }

        Log.i(TAG, "addFilter: (" + filter.getId() + ", " + filter.getName() + ")");

        return -1;
    }

    public boolean deleteFilter(Filter filter) {
        final String DELETE_FILTER = "DELETE FROM " + Filter.TABLE_NAME + " WHERE " + Filter.FILTER_ID_COLUMN + " = " + filter.getId() + ";";
        final String DELETE_RULES = "DELETE FROM " + Rule.TABLE_NAME + " WHERE " + Rule.FILTER_ID_FK_COLUMN + " = " + filter.getId() + ";";

        database.execSQL(DELETE_FILTER);
        database.execSQL(DELETE_RULES);

        Log.i(TAG, "DELETE: " + DELETE_FILTER);
        Log.i(TAG, "DELETE: " + DELETE_RULES);


        return true;
    }
}
