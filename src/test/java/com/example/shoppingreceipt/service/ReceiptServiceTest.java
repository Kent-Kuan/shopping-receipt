package com.example.shoppingreceipt.service;

import com.example.shoppingreceipt.entity.PurchaseProduct;
import com.example.shoppingreceipt.exception.NotOfferException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReceiptServiceTest {

    @Autowired
    ReceiptService receiptService;

    @Test
    public void givenErrorInput_thenCatchNotOfferException() {
        String errorFormatInput1 = "Location:CA, 1 book at 17.99, 1 potato chips at 3.99";
        String errorFormatInput2 = "Location:CA, 1 book at 17.99,, 1 potato chips at 3.99,,";
        String errorFormatInput3 = "Location location :CA, 1 book at 17.99,, 1 potato chips at 3.99";
        assertThrows(NotOfferException.class, () -> receiptService.create(errorFormatInput1));
        assertThrows(NotOfferException.class, () -> receiptService.create(errorFormatInput2));
        assertThrows(NotOfferException.class, () -> receiptService.create(errorFormatInput3));
    }

    @Test
    public void givenNotOfferState_thenCatchNotOfferException() {
        String notOfferStateInput1 = "Location: AA, 1 book at 17.99, 1 potato chips at 3.99";
        String notOfferStateInput2 = "Location: AB, 1 book at 17.99, 1 potato chips at 3.99";
        String notOfferStateInput3 = "Location: AC, 1 book at 17.99, 1 potato chips at 3.99";
        assertThrows(NotOfferException.class, () -> receiptService.create(notOfferStateInput1));
        assertThrows(NotOfferException.class, () -> receiptService.create(notOfferStateInput2));
        assertThrows(NotOfferException.class, () -> receiptService.create(notOfferStateInput3));
    }

    @Test
    public void givenNotOfferProduct_thenCatchNotOfferException() {
        String notOfferProductInput1 = "Location: CA, 1 books at 17.99, 1 potato chips at 3.99";
        String notOfferProductInput2 = "Location: CA, 1 books at 17.99, 1 jean at 3.99";
        String notOfferProductInput3 = "Location: CA, 1 toy at 17.99, 1 potato chips at 3.99";
        assertThrows(NotOfferException.class, () -> receiptService.create(notOfferProductInput1));
        assertThrows(NotOfferException.class, () -> receiptService.create(notOfferProductInput2));
        assertThrows(NotOfferException.class, () -> receiptService.create(notOfferProductInput3));
    }

    @Test
    public void givenCorrectInput1_thenReturnReceiptString() {
        String input = "Location: CA, 1 book at 17.99, 1 potato chips at 3.99";
        List<PurchaseProduct> fakePurchaseProduct = Stream.of(new PurchaseProduct("book", 1, "17.99"),
                new PurchaseProduct("potato chips", 1, "3.99")).collect(Collectors.toList());
        Map<String, String> fakeTotalMap = new HashMap<>();
        fakeTotalMap.put("subTotal", "21.98");
        fakeTotalMap.put("tax", "1.80");
        fakeTotalMap.put("total", "23.78");
        String expected = generateReceiptTest("CA", fakePurchaseProduct, fakeTotalMap);
        String actual = receiptService.create(input);
        assertEquals(expected, actual);
    }

    @Test
    public void givenCorrectInput2_thenReturnReceiptString() {
        String input = "Location: NY, 1 book at 17.99, 3 pencils at 2.99 ";
        List<PurchaseProduct> fakePurchaseProduct = Stream.of(new PurchaseProduct("book", 1, "17.99"),
                new PurchaseProduct("pencils", 3, "2.99")).collect(Collectors.toList());
        Map<String, String> fakeTotalMap = new HashMap<>();
        fakeTotalMap.put("subTotal", "26.96");
        fakeTotalMap.put("tax", "2.40");
        fakeTotalMap.put("total", "29.36");
        String expected = generateReceiptTest("NY", fakePurchaseProduct, fakeTotalMap);
        String actual = receiptService.create(input);
        assertEquals(expected, actual);
    }

    @Test
    public void givenCorrectInput3_thenReturnReceiptString() {
        String input = "Location: NY, 2 pencils at 2.99, 1 shirt at 29.99 ";
        List<PurchaseProduct> fakePurchaseProduct = Stream.of(new PurchaseProduct("pencils", 2, "2.99"),
                new PurchaseProduct("shirt", 1, "29.99")).collect(Collectors.toList());
        Map<String, String> fakeTotalMap = new HashMap<>();
        fakeTotalMap.put("subTotal", "35.97");
        fakeTotalMap.put("tax", "0.55");
        fakeTotalMap.put("total", "36.52");
        String expected = generateReceiptTest("NY", fakePurchaseProduct, fakeTotalMap);
        String actual = receiptService.create(input);
        assertEquals(expected, actual);
    }

    private String generateReceiptTest(String state, List<PurchaseProduct> purchaseProductsList, Map<String, String> totalMap) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-20s%-20s%-20s\n\n", "item", "price", "qty"));
        purchaseProductsList.forEach(purchaseProduct -> {
            sb.append(String.format("%-20s", purchaseProduct.getProductName()));
            sb.append(String.format("$%-20s", purchaseProduct.getPrice()));
            sb.append(String.format("%-20s\n", purchaseProduct.getQuantity()));
        });
        sb.append(String.format("%-20s%-20s$%-20s\n", "subtotal:", "", totalMap.get("subTotal")) +
                String.format("%-20s%-20s$%-20s\n", "tax:", "", totalMap.get("tax")) +
                String.format("%-20s%-20s$%-20s\n", "total:", "", totalMap.get("total")));
        return sb.toString();
    }

}