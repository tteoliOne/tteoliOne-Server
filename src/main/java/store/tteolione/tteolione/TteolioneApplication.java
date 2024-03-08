package store.tteolione.tteolione;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableFeignClients
@EnableScheduling
@SpringBootApplication
@EnableMongoRepositories(basePackages = "store.tteolione.tteolione.domain.room.repository.mongodb")
//@EnableRedisRepositories(basePackages = "store.tteolione.tteolione.domain.room.entity.redis")
public class TteolioneApplication {

	public static void main(String[] args) {
		SpringApplication.run(TteolioneApplication.class, args);
	}

}
