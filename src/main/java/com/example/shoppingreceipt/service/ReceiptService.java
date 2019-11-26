package com.example.shoppingreceipt.service;

import com.example.shoppingreceipt.dao.ReceiptDAO;
import com.example.shoppingreceipt.entity.Product;
import com.example.shoppingreceipt.entity.PurchaseProduct;
import com.example.shoppingreceipt.entity.State;
import com.example.shoppingreceipt.exception.NotOfferException;
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

    private BigDecimal subTotal;
    private BigDecimal tax;
    private BigDecimal total;
    private State offerState;
    private Map<String, Product> offeredProducts;
    private List<PurchaseProduct> purchasedProductsList;

    public String create(String input) throws NotOfferException {
        this.subTotal = new BigDecimal(0);
        this.tax = new BigDecimal(0);
        this.total = new BigDecimal(0);
        this.initReceipt(input);
        return generateReceipt();
    }

    private void initReceipt(String input) {
        String locationState = extractLocationState(input).toUpperCase();
        this.offerState = receiptDAO.findStateByName(locationState);
        if(offerState == null)
            throw new NotOfferException(901, "No offer the location state.");

        this.purchasedProductsList = transformToPurchaseProduct(input);
        List<String> purchasedProductsName = purchasedProductsList.stream().map(PurchaseProduct::getProductName).collect(Collectors.toList());
        this.offeredProducts = receiptDAO.findCategoriesByProducts(purchasedProductsName);
        if(offeredProducts.size() != purchasedProductsName.size())
            throw new NotOfferException(902, "No offer the product or category");
    }

    private void calculate() {
        for(PurchaseProduct purchaseProduct : purchasedProductsList) {
            Product product = offeredProducts.get(purchaseProduct.getProductName());
            BigDecimal subPrice = new BigDecimal(purchaseProduct.getPrice()).multiply(new BigDecimal(purchaseProduct.getQuantity()));
            subTotal = subTotal.add(subPrice);
            if(!offerState.getTaxFreeCategories().contains(product.getCategory()))
                tax = tax.add(subPrice.multiply(new BigDecimal(offerState.getTax())));
        }
        tax = nearestDotFive(tax);
        total = subTotal.add(tax);
    }

    private String generateReceipt() throws NotOfferException {
        calculate();
        String receipt = generateHeader() +
                generatePurchaseProducts(purchasedProductsList) +
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
