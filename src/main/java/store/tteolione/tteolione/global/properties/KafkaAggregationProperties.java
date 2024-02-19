package store.tteolione.tteolione.global.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "kafka.aggregation")
public class KafkaAggregationProperties {
    private String topic;
    private String groupId;
    private String broker;
}
