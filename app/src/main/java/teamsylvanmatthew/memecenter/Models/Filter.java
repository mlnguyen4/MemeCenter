package teamsylvanmatthew.memecenter.Models;

import android.provider.BaseColumns;


public class Filter implements BaseColumns {
    public static final String TABLE_NAME = "filter";
    public static final String FILTER_ID_COLUMN = "filterID";
    public static final String NAME_COLUMN = "name";
    private long id;
    private String name;

    public Filter(String name) {
        this.name = name;
    }

    public Filter(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
