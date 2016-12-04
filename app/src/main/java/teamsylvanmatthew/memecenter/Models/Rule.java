package teamsylvanmatthew.memecenter.Models;


public class Rule {
    public static final String TABLE_NAME = "rule";
    public static final String RULE_ID_COLUMN = "ruleID";
    public static final String FILTER_ID_FK_COLUMN = "filter_ID_FK";
    public static final String REGEX_COLUMN = "regex";

    private long ruleId;
    private long filter_fk_id;
    private String regex;

    public Rule(long filter_fk_id, String regex) {
        this.filter_fk_id = filter_fk_id;
        this.regex = regex;
    }

    public Rule(long ruleId, long filter_fk_id, String regex) {
        this.ruleId = ruleId;
        this.filter_fk_id = filter_fk_id;
        this.regex = regex;
    }

    public long getRuleId() {
        return ruleId;
    }

    public void setRuleId(long ruleId) {
        this.ruleId = ruleId;
    }

    public long getFilter_fk_id() {
        return filter_fk_id;
    }

    public void setFilter_fk_id(long filter_fk_id) {
        this.filter_fk_id = filter_fk_id;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }
}
