package teamsylvanmatthew.memecenter.Models;

import android.provider.BaseColumns;

import java.util.Set;


public class Filter implements BaseColumns {
    public static final String TABLE_NAME = "filter";
    public static final String FILTER_ID_COLUMN = "filterID";
    public static final String NAME_COLUMN = "name";
    private String name;

    public Filter(String name, Set<String> filters) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
