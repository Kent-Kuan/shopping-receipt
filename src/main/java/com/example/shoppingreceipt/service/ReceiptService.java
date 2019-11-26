package com.example.shoppingreceipt.service;

import com.example.shoppingreceipt.dao.ReceiptDAO;
import com.example.shoppingreceipt.entity.Product;
import com.example.shoppingreceipt.entity.PurchaseProduct;
import com.example.shoppingreceipt.entity.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ReceiptService {
    @Autowired
    ReceiptDAO receiptDAO;

    private String input;
    private BigDecimal subTotal;
    private BigDecimal tax;
    private BigDecimal total;

    public String  create(String input) {
        this.input = input;
        this.subTotal = new BigDecimal(0);
        this.tax = new BigDecimal(0);
        this.total = new BigDecimal(0);
        return generateReceipt();
    }

    private String generateReceipt() {
        String locationState = extractLocationState(input).toUpperCase();
        State state = receiptDAO.findStateByName(locationState);
        List<PurchaseProduct> purchaseProductsList = transformToPurchaseProduct(input);

        List<String> purchasedProductsName = purchaseProductsList.stream().map(PurchaseProduct::getProductName).collect(Collectors.toList());
        Map<String, Product> products = receiptDAO.findCategoriesByProducts(purchasedProductsName);

        for(PurchaseProduct purchaseProduct : purchaseProductsList) {
            Product product = products.get(purchaseProduct.getProductName());
            BigDecimal subPrice = new BigDecimal(purchaseProduct.getPrice()).multiply(new BigDecimal(purchaseProduct.getQuantity()));
            subTotal = subTotal.add(subPrice);
            if(!state.getTaxFreeCategories().contains(product.getCategory()))
                tax = tax.add(subPrice.multiply(new BigDecimal(state.getTax())));
        }

        tax = nearestDotFive(tax);
        total = subTotal.add(tax);

        String receipt = generateHeader() +
                generatePurchaseProducts(purchaseProductsList) +
                generateTotal(subTotal.toString(), tax.toString(), total.toString());
        return receipt;
    }

    private String generateHeader() {
        return String.format("%-20s%-20s%-20s\n\n", "item", "price", "qty");
    }

    private String generatePurchaseProducts(List<PurchaseProduct> purchaseProductsList) {
        final StringBuilder sb = new StringBuilder();
        purchaseProductsList.forEach(purchaseProduct -> {
            sb.append(String.format("%-20s", purchaseProduct.getProductName()));
            sb.append(String.format("$%-20s", purchaseProduct.getPrice()));
            sb.append(String.format("%-20s\n", purchaseProduct.getQuantity()));
        });
        return sb.toString();
    }

    private String generateTotal(String subTotal, String tax, String total) {
        return String.format("%-20s%-20s$%-20s\n", "subtotal:", "", subTotal) +
                String.format("%-20s%-20s$%-20s\n", "tax:", "", tax) +
                String.format("%-20s%-20s$%-20s\n", "total:", "", total);
    }

    private String extractLocationState(String input) {
        return input.substring(input.indexOf(" ") + 1, input.indexOf(","));
    }

    private List<PurchaseProduct> transformToPurchaseProduct(String input) {
        String purchaseProductsString = input.substring(input.indexOf(",") + 1, input.length());
        List<PurchaseProduct> purchaseProductsList = Arrays.stream(purchaseProductsString.split(",")).map(item -> {
            String[] product = item.trim().replaceAll("(\\d+)\\s([a-z\\s]+)\\sat\\s(\\d+.\\d+)","$2,$1,$3").split(",");
            return new PurchaseProduct(product[0], Integer.parseInt(product[1]), product[2]);
        }).collect(Collectors.toList());
        return purchaseProductsList;
    }

    private BigDecimal nearestDotFive(BigDecimal bigDecimal) {
        return bigDecimal.multiply(new BigDecimal(20)).setScale(0, BigDecimal.ROUND_UP).divide(new BigDecimal(20)).setScale(2, BigDecimal.ROUND_UP);
    }

}
