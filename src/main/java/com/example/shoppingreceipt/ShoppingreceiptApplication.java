package com.example.shoppingreceipt;

import com.example.shoppingreceipt.dao.ReceiptDAO;
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
    ReceiptDAO receiptDAO;
    @Autowired
    ReceiptService receiptService;
    @Override
    public void run(String... args) throws Exception {
        String input = "Location: CA, 1 book at 17.99, 1 potato chips at 3.99";
//        String s = "Location: NY, 1 book at 17.99, 3 pencils at 2.99";
//        String s = "Location: NY, 2 pencils at 2.99, 1 shirt at 29.99";
        input = input.toLowerCase();
        if(!validInputPattern(input)){
            System.out.println("Input format error.");
        }
        else{
            System.out.println(receiptService.create(input));
        }
    }

    private boolean validInputPattern(String input) {
        return input.trim().toLowerCase().matches("location:\\s[a-z]{2}(,\\s\\d+\\s[a-z\\s]+\\sat\\s\\d+\\.\\d+)+");
    }

}
