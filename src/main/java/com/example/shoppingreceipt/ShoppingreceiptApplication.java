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
        if(args.length == 0) {
            System.out.println("No input.");
            return;
        }

        String input = args[0];
        try {
            String receipt = receiptService.create(input);
            System.out.println(receipt);
        } catch (NotOfferException e) {
            System.out.println(String.format("status: " + e.getStatus() +", msg: " + e.getMessage()));
            e.printStackTrace();
        }
    }

}
