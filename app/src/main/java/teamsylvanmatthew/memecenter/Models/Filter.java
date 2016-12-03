package teamsylvanmatthew.memecenter.Models;

import android.provider.BaseColumns;

import java.util.Set;


public class Filter implements BaseColumns {
    public static final String TABLE_NAME = "filter";
    public static final String FILTER_ID_COLUMN = "filterID";
    public static final String NAME_COLUMN = "name";
    String name;
    Set<String> filters;

    public Filter(String name, Set<String> filters) {
        this.name = name;
        this.filters = filters;
    }

    public Set<String> getFilters() {
        return filters;
    }

    public void setFilters(Set<String> filters) {
        this.filters = filters;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int size() {
        return this.filters.size();
    }
}
