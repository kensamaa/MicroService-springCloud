package org.sid.customerservice;

import org.sid.customerservice.entites.Customer;
import org.sid.customerservice.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;

@SpringBootApplication
public class CustomerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerServiceApplication.class, args);
	}
	@Bean
	CommandLineRunner start(CustomerRepository customerrepository, RepositoryRestConfiguration conf) {
		conf.exposeIdsFor(Customer.class);
		return args->{
			customerrepository.save(new Customer(null,"mohamed","mohamed@gmail.com"));
			customerrepository.save(new Customer(null,"amine","amine@gmail.com"));
			customerrepository.save(new Customer(null,"imane","imane@gmail.com"));
			customerrepository.findAll().forEach(c->{
				System.out.println(c.toString());
			});
		};
	}
}

