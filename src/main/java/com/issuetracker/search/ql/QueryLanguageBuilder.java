package com.issuetracker.search.ql;

import java.io.Serializable;
import java.util.List;

/**
 * //TODO: document this
 *
 * @author Jiří Holuša
 */
public class QueryLanguageBuilder implements Serializable{

    private StringBuilder query = new StringBuilder();

    public void equality(String property, String value) {
        and();

        query.append(property);
        query.append(" = ");
        query.append("\"");
        query.append(value);
        query.append("\"");
    }

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
