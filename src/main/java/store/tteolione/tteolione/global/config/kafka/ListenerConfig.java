package store.tteolione.tteolione.global.config.kafka;


import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import store.tteolione.tteolione.domain.room.dto.AggregationDto;
import store.tteolione.tteolione.domain.room.dto.chat.Message;
import store.tteolione.tteolione.global.properties.KafkaAggregationProperties;
import store.tteolione.tteolione.global.properties.KafkaSharingProperties;

import java.util.Map;

@EnableKafka
@Configuration
@RequiredArgsConstructor
public class ListenerConfig {

    private final KafkaSharingProperties sharingProperties;
    private final KafkaAggregationProperties aggregationProperties;

    // KafkaListener 컨테이너 팩토리를 생성하는 Bean 메서드
    @Bean
    ConcurrentKafkaListenerContainerFactory<String, Message> kafkaSharingContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Message> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(kafkaSharingConsumer());
        return factory;
    }

    // Kafka ConsumerFactory를 생성하는 Bean 메서드
    @Bean
    public ConsumerFactory<String, Message> kafkaSharingConsumer() {
        JsonDeserializer<Message> deserializer = new JsonDeserializer<>();
        // 패키지 신뢰 오류로 인해 모든 패키지를 신뢰하도록 작성
        deserializer.addTrustedPackages("*");

        // Kafka Consumer 구성을 위한 설정값들을 설정 -> 변하지 않는 값이므로 ImmutableMap을 이용하여 설정
        Map<String, Object> consumerConfigurations =
                ImmutableMap.<String, Object>builder()
                        .put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, sharingProperties.getBroker())
                        .put(ConsumerConfig.GROUP_ID_CONFIG, sharingProperties.getGroupId())
                        .put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class)
                        .put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer)
                        .put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest")
                        .build();

        return new DefaultKafkaConsumerFactory<>(consumerConfigurations, new StringDeserializer(), deserializer);
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, AggregationDto> kafkaAggregationContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, AggregationDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(kafkaAggregationConsumer());
        return factory;
    }

    // Kafka ConsumerFactory를 생성하는 Bean 메서드
    @Bean
    public ConsumerFactory<String, AggregationDto> kafkaAggregationConsumer() {
        JsonDeserializer<AggregationDto> deserializer = new JsonDeserializer<>();
        // 패키지 신뢰 오류로 인해 모든 패키지를 신뢰하도록 작성
        deserializer.addTrustedPackages("*");

        // Kafka Consumer 구성을 위한 설정값들을 설정 -> 변하지 않는 값이므로 ImmutableMap을 이용하여 설정
        Map<String, Object> consumerConfigurations =
                ImmutableMap.<String, Object>builder()
                        .put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, aggregationProperties.getBroker())
                        .put(ConsumerConfig.GROUP_ID_CONFIG, aggregationProperties.getGroupId())
                        .put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class)
                        .put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer)
                        .put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest")
                        .build();

        return new DefaultKafkaConsumerFactory<>(consumerConfigurations, new StringDeserializer(), deserializer);
    }
}
