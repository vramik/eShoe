package com.issuetracker.search.ql;

import java.io.Serializable;
import java.util.List;

/**
 * Helper builder for creating string queries based on SearchQueryLanguage.g grammar.
 *
 * @author Jiří Holuša
 */
public class QueryLanguageBuilder implements Serializable{

    private StringBuilder query = new StringBuilder();

    /**
     * Expresses single match against certain value.
     *
     * @param property name of the property that should match value
     * @param value
     */
    public void equality(String property, String value) {
        and();

        query.append(property);
        query.append(" = ");
        query.append("\"");
        query.append(value);
        query.append("\"");
    }

    /**
     * Expresses that property should match at least one of the values
     * provided in value list.
     *
     * @param property property to be matched
     * @param values
     */
    public void in(String property, List<String> values) {
        and();

        query.append(property);
        query.append(" IN (");

        boolean first = true;
        for(String value: values) {
            if(!first) {
                query.append(",");
            }
            query.append("\"");
            query.append(value);
            query.append("\"");

            first = false;
        }
        query.append(")");
    }

    /**
     * Expresses fuzzy search.
     *
     * @param property property to be matched
     * @param value
     */
    public void tilda(String property, String value) {
        and();

        query.append(property);
        query.append(" ~ ");
        query.append("\"");
        query.append(value);
        query.append("\"");
    }

    public String getQuery() {
        return query.toString();
    }

    public void clear() {
        query = new StringBuilder();
    }

    private void and() {
        if(query.length() != 0) {
            query.append(" AND ");
        }
    }
}
