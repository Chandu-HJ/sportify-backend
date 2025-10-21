package Ecommerce.website.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class SportifyApplication {

	public static void main(String[] args) {
		SpringApplication.run(SportifyApplication.class, args);
	}

}
