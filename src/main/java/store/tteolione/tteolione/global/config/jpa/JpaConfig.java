package store.tteolione.tteolione.global.config.jpa;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "store.tteolione.tteolione",
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASPECTJ, pattern = "store.tteolione.tteolione.domain.room.repository.redis.ChatRoomRedisRepository"),
                @ComponentScan.Filter(type = FilterType.ASPECTJ, pattern = "store.tteolione.tteolione.domain.room.repository.mongodb.MongoChatRepository")
        }

)
public class JpaConfig {
}
