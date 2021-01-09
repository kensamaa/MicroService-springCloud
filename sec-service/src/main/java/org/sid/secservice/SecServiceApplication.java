package org.sid.secservice;

import org.sid.secservice.sec.entities.AppRole;
import org.sid.secservice.sec.entities.AppUser;
import org.sid.secservice.sec.service.AccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true)

public class SecServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecServiceApplication.class, args);
    }
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    CommandLineRunner start(AccountService accountService){
        return args -> {
            accountService.addNewRole(new AppRole(null,"USER"));
            accountService.addNewRole(new AppRole(null,"ADMIN"));
            accountService.addNewRole(new AppRole(null,"COSTUMER_MANAGER"));
            accountService.addNewRole(new AppRole(null,"PRODUCT_MANAGER"));
            accountService.addNewRole(new AppRole(null,"BILLS_MANAGER"));

            accountService.addNewUser(new AppUser(null,"admin","1234",new ArrayList<>()));
            accountService.addNewUser(new AppUser(null,"user2","1234",new ArrayList<>()));
            accountService.addNewUser(new AppUser(null,"user3","1234",new ArrayList<>()));
            accountService.addNewUser(new AppUser(null,"user4","1234",new ArrayList<>()));

            accountService.addRoleToUser("admin","ADMIN");
            accountService.addRoleToUser("user2","CUSTOMER_MANAGER");
            accountService.addRoleToUser("user3","PRODUCT_MANAGER");
            accountService.addRoleToUser("user4","BILLS_MANAGER");
        };
    }

}
