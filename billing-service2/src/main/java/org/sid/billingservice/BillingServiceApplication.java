package org.sid.billingservice;

import org.sid.billingservice.entities.Bill;
import org.sid.billingservice.entities.ProductItem;
import org.sid.billingservice.feign.CustomerRestClient;
import org.sid.billingservice.feign.ProductRestClient;
import org.sid.billingservice.model.Customer;
import org.sid.billingservice.model.Product;
import org.sid.billingservice.repository.BillRepository;
import org.sid.billingservice.repository.ProductItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.PagedModel;

import java.util.Collection;
import java.util.Date;
import java.util.Random;

@SpringBootApplication
@EnableFeignClients
public class BillingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BillingServiceApplication.class, args);
    }
    @Bean
    CommandLineRunner start(
                            BillRepository billRepository,
                            ProductItemRepository productItemRepository,
                            CustomerRestClient customerRestClient,
                            ProductRestClient productRestClient)
    {
        return args -> {
            Customer customer = customerRestClient.getCustomerByID(1L);
            Bill bill1 = billRepository.save(new Bill(null,new Date(),null,customer.getId(),null));
            PagedModel<Product> productPagedModel = productRestClient.pageProducts();
            productPagedModel.forEach(p->{
                ProductItem productItem = new ProductItem();
                productItem.setPrice(p.getPrice());
                productItem.setQuantity(1 + new Random().nextInt(100));
                productItem.setBill(bill1);
                productItem.setProductID(p.getId());
                productItemRepository.save(productItem);
            });
        };
    }

}
