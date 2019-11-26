package com.example.shoppingreceipt;

import com.example.shoppingreceipt.dao.ReceiptDAO;
import com.example.shoppingreceipt.exception.NotOfferException;
import com.example.shoppingreceipt.service.ReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShoppingreceiptApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingreceiptApplication.class, args);
    }
    @Autowired
    ReceiptService receiptService;
    @Override
    public void run(String... args) throws Exception {
        String input = "Location:CA, 1 books at 17.99, 1 potato chips at 3.99";
//        String s = "Location: NY, 1 book at 17.99, 3 pencils at 2.99";
//        String s = "Location: NY, 2 pencils at 2.99, 1 shirt at 29.99";
        try {
            receiptService.create(input);
        } catch (NotOfferException e) {
            System.out.println(String.format("status: " + e.getStatus() +", msg: " + e.getMessage()));
            e.printStackTrace();
        }
    }

}
