package store.tteolione.tteolione;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class TteolioneApplication {

	public static void main(String[] args) {
		SpringApplication.run(TteolioneApplication.class, args);
	}

}
