package store.tteolione.tteolione.global.config.kafka;


import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import store.tteolione.tteolione.domain.room.dto.AggregationDto;
import store.tteolione.tteolione.domain.room.dto.chat.Message;
import store.tteolione.tteolione.global.properties.KafkaAggregationProperties;
import store.tteolione.tteolione.global.properties.KafkaSharingProperties;

import java.util.Map;

@EnableKafka
@Configuration
@RequiredArgsConstructor
public class ProducerConfig {

    private final KafkaSharingProperties sharingProperties;
    private final KafkaAggregationProperties aggregationProperties;

    // Kafka ProducerFactory를 생성하는 Bean 메서드
    @Bean
    public ProducerFactory<String, Message> sharingProducerFactory() {
        return new DefaultKafkaProducerFactory<>(sharingProducerConfiguration());
    }

    // Kafka Producer 구성을 위한 설정값들을 포함한 맵을 반환하는 메서드
    @Bean
    public Map<String, Object> sharingProducerConfiguration() {
        return ImmutableMap.<String, Object>builder()
                .put(org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, sharingProperties.getBroker())
                .put(org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class)
                .put(org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class)
                .build();
    }

    // KafkaTemplate을 생성하는 Bean 메서드
    @Bean
    public KafkaTemplate<String, Message> sharingKafkaTemplate() {
        return new KafkaTemplate<>(sharingProducerFactory());
    }

    @Bean
    public ProducerFactory<String, AggregationDto> aggregationProducerFactory() {
        return new DefaultKafkaProducerFactory<>(sharingProducerConfiguration());
    }

    // Kafka Producer 구성을 위한 설정값들을 포함한 맵을 반환하는 메서드
    @Bean
    public Map<String, Object> aggregationProducerConfiguration() {
        return ImmutableMap.<String, Object>builder()
                .put(org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, aggregationProperties.getBroker())
                .put(org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class)
                .put(org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class)
                .build();
    }

    // KafkaTemplate을 생성하는 Bean 메서드
    @Bean
    public KafkaTemplate<String, AggregationDto> aggregationKafkaTemplate() {
        return new KafkaTemplate<>(aggregationProducerFactory());
    }
}
