package teamsylvanmatthew.memecenter.Database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import teamsylvanmatthew.memecenter.Models.Filter;
import teamsylvanmatthew.memecenter.Models.Rule;

public class MemeCenterDataSource {
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


    public ArrayList<String> getRules(int id) {
        ArrayList<String> rules = new ArrayList<String>();

        final String query = "SELECT " + Rule.REGEX_COLUMN + " FROM " + Filter.TABLE_NAME + " JOIN " + Rule.TABLE_NAME + " ON " + Filter.FILTER_ID_COLUMN + " = " + Rule.FILTER_ID_FK_COLUMN + " WHERE " + Filter.FILTER_ID_COLUMN + " = " + id + ";";

        Cursor cursor = database.rawQuery(query, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        while (!cursor.isAfterLast()) {
            rules.add(cursor.getString(0));
            cursor.moveToNext();
        }

        return rules;
    }

    public ArrayList<String> getFilters() {
        ArrayList<String> filters = new ArrayList<String>();

        final String query = "SELECT " + Filter.NAME_COLUMN + " FROM " + Filter.TABLE_NAME + ";";

        Cursor cursor = database.rawQuery(query, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        while (!cursor.isAfterLast()) {
            filters.add(cursor.getString(0));
            cursor.moveToNext();
        }

        return filters;
    }

    public boolean addFilter(Filter filter) {
        if (filter != null) {
            ContentValues currentValues = new ContentValues();

            currentValues.put(Filter.NAME_COLUMN, filter.getName());
            database.insert(Filter.TABLE_NAME, null, currentValues);

            return true;
        }

        return false;
    }
}
