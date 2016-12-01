package teamsylvanmatthew.memecenter.Models;

import java.util.ArrayList;

/**
 * Created by Ling on 11/30/2016.
 */

public class Filter {
    ArrayList<String> filters;

    public Filter(ArrayList<String> filters) {
        this.filters = filters;
    }

    public ArrayList<String> getFilters() {
        return filters;
    }

    public void setFilters(ArrayList<String> filters) {
        this.filters = filters;
    }

    public int size() {
        return this.filters.size();
    }
}
