package ca.development.calcount;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;


@SpringBootApplication
public class CalCountApplication {

	public static void main(String[] args) {
		SpringApplication.run(CalCountApplication.class, args);
	}

}