package org.sid.eurikadiscovery;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurikaDiscoveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurikaDiscoveryApplication.class, args);
	}
	

}


