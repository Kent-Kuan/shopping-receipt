package com.example.shoppingreceipt;

import com.example.shoppingreceipt.dao.ReceiptDAO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ShoppingreceiptApplicationTests {

    @Autowired
    ReceiptDAO receiptDAO;
    @Test
    void contextLoads() {
        receiptDAO.findStateByName("CN");
    }

}
