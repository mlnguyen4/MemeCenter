package teamsylvanmatthew.memecenter.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import teamsylvanmatthew.memecenter.Models.Filter;

public class MemeCenterDatabaseHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "memecenter.db";

    private static final String SQL_CREATE_TABLE = "CREATE TABLE \"" + Filter.TABLE_NAME + "\" (" + "`" + Filter.FILTER_ID_COLUMN
            + "` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," + "`" + Filter.NAME_COLUMN + "` TEXT NOT NULL" + ");";


    private static final String SQL_DELETE_QUERY = "DROP TABLE IF EXISTS " + Filter.TABLE_NAME;


    public MemeCenterDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("Executing Query: SQL_CREATE_TABLE ");
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_QUERY);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        super.onDowngrade(database, oldVersion, newVersion);
        onUpgrade(database, oldVersion, newVersion);
    }


}
