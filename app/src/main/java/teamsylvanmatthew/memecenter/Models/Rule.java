package teamsylvanmatthew.memecenter.Models;


public class Rule {
    public static final String TABLE_NAME = "rule";
    public static final String RULE_ID_COLUMN = "ruleID";
    public static final String FILTER_ID_FK_COLUMN = "filter_ID_FK";
    public static final String REGEX_COLUMN = "regex";

    private String regex;

    public Rule(String regex) {
        this.regex = regex;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }
}
