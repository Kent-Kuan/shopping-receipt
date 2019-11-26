package com.example.shoppingreceipt.config;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TaxFreeCategoriesTypeHandler implements TypeHandler<Set<String>> {
    @Override
    public void setParameter(PreparedStatement ps, int i, Set<String> parameter, JdbcType jdbcType) throws SQLException {
        // not need implement code
    }

    @Override
    public Set<String> getResult(ResultSet rs, String columnName) throws SQLException {
        String[] categoriesString = rs.getString(columnName).replaceAll("\\[|\\]|\\s", "").trim().split(",");
        Set<String> categories = new HashSet<>();
        Arrays.stream(categoriesString).forEach(categories::add);
        return categories;
    }

    @Override
    public Set<String> getResult(ResultSet rs, int columnIndex) throws SQLException {
        String[] categoriesString = rs.getString(columnIndex).replaceAll("\\[|\\]|\\s", "").trim().split(",");
        Set<String> categories = new HashSet<>();
        Arrays.stream(categoriesString).forEach(categories::add);
        return categories;
    }

    @Override
    public Set<String> getResult(CallableStatement cs, int columnIndex) throws SQLException {
        String[] categoriesString = cs.getString(columnIndex).replaceAll("\\[|\\]|\\s", "").trim().split(",");
        Set<String> categories = new HashSet<>();
        Arrays.stream(categoriesString).forEach(categories::add);
        return categories;
    }
}
