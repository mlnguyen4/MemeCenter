package teamsylvanmatthew.memecenter.Models;

import java.util.Set;

/**
 * Created by Ling on 11/30/2016.
 */

public class Filter {
    Set<String> filters;

    public Filter(Set<String> filters) {
        this.filters = filters;
    }

    public Set<String> getFilters() {
        return filters;
    }

    public void setFilters(Set<String> filters) {
        this.filters = filters;
    }

    public int size() {
        return this.filters.size();
    }
}
