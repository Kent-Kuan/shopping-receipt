package com.example.shoppingreceipt.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
public class State {
    private String state;
    private String tax;
    private Set<String> taxFreeCategories;
}
