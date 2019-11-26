package com.example.shoppingreceipt.dao;

import com.example.shoppingreceipt.config.TaxFreeCategoriesTypeHandler;
import com.example.shoppingreceipt.entity.Product;
import com.example.shoppingreceipt.entity.State;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface ReceiptDAO {
    @Select("SELECT * FROM state_tax WHERE state = #{state}")
    @Results(id = "orderResult", value = {
            @Result(property = "taxFreeCategories", column = "tax_free_categories", typeHandler = TaxFreeCategoriesTypeHandler.class)
    })
    State findStateByName(@Param("state") String state);
    @Select({"<script>",
            "SELECT *",
            "FROM product",
            "WHERE name IN",
            "<foreach item='item' index='index' collection='products'",
            "open='(' separator=',' close=')'>",
            "#{item}",
            "</foreach>",
            "</script>" })
    @MapKey("name")
    Map<String, Product> findCategoriesByProducts(@Param("products")List<String> products);

}
