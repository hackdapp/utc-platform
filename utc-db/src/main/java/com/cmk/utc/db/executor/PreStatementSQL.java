package com.cmk.utc.db.executor;

public class PreStatementSQL {
    private String sql;

    private Object[][] placeValue;

    public PreStatementSQL(String sql, Object[][] placeValue) {
        this.sql = sql;
        this.placeValue = placeValue;
    }

    public String getSql() {
        return sql;
    }

    public Object[][] getPlaceValue() {
        return placeValue;
    }
}
