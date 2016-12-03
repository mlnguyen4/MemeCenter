package teamsylvanmatthew.memecenter.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import teamsylvanmatthew.memecenter.Models.Filter;
import teamsylvanmatthew.memecenter.Models.Rule;

public class MemeCenterDatabaseHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "memecenter.db";

    /* CREATE TABLE */
    private static final String CREATE_TABLE_FILTER = "CREATE TABLE \"" + Filter.TABLE_NAME + "\" (" + "`" + Filter.FILTER_ID_COLUMN
            + "` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," + "`" + Filter.NAME_COLUMN + "` TEXT NOT NULL" + ");";

    private static final String CREATE_TABLE_RULE = "CREATE TABLE \"" + Rule.TABLE_NAME + "\" (" + "`" + Rule.RULE_ID_COLUMN
            + "` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," + "`" + Rule.FILTER_ID_FK_COLUMN + "` INTEGER NOT NULL," + "`" + Rule.REGEX_COLUMN + "` TEXT NOT NULL" + ");";


    /* DELETE TABLE */
    private static final String DELETE_TABLE_FILTER = "DROP TABLE IF EXISTS " + Filter.TABLE_NAME;

    private static final String DELETE_TABLE_RULE = "DROP TABLE IF EXISTS " + Rule.TABLE_NAME;


    public MemeCenterDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        System.out.println("Executing Query: SQL_CREATE_TABLE ");
        database.execSQL(CREATE_TABLE_FILTER);
        database.execSQL(CREATE_TABLE_RULE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        System.out.println("Executing Query: SQL_DELETE_TABLE ");
        database.execSQL(DELETE_TABLE_FILTER);
        database.execSQL(DELETE_TABLE_RULE);
        onCreate(database);
    }

    @Override
    public void onDowngrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        super.onDowngrade(database, oldVersion, newVersion);
        onUpgrade(database, oldVersion, newVersion);
    }


}
