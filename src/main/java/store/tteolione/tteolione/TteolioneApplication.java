package store.tteolione.tteolione;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableFeignClients
@EnableScheduling
@SpringBootApplication
public class TteolioneApplication {

	public static void main(String[] args) {
		SpringApplication.run(TteolioneApplication.class, args);
	}

}
