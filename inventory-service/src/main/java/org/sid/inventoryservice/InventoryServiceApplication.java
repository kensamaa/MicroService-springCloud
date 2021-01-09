package org.sid.inventoryservice;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
public class InventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);
	}
	@Bean
	CommandLineRunner start(ProductRepository productRepository, RepositoryRestConfiguration conf) {
		conf.exposeIdsFor(Product.class);
		return args->{
			productRepository.save(new Product(null,"ordinateur",7888,12));
			productRepository.save(new Product(null,"phone",2000,10));
			productRepository.save(new Product(null,"imprimante",1888,2));
			
			
			productRepository.findAll().forEach(p->{
				System.out.println(p.getName());
			});
		};
	}
}


@Entity
@Data @NoArgsConstructor @AllArgsConstructor @ToString
class Product{
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String name;
	private double price;
	private double quantity;
}
@CrossOrigin
@RepositoryRestResource
interface ProductRepository extends JpaRepository<Product, Long>{
	
}
