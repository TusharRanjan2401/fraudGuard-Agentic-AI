package com.fraudDetection.FraudGuard.Config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.transaction.KafkaTransactionManager;

import com.fraudDetection.FraudGuard.dto.EnrichedTransactionDto;
import com.fraudDetection.FraudGuard.dto.FraudInvestigationEvent;
import com.fraudDetection.FraudGuard.dto.TransactionKafkaDto;

@Configuration
@EnableKafka
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    @Primary
    public ProducerFactory<String, TransactionKafkaDto> transactionProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        configProps.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "tx-transaction-service");
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, TransactionKafkaDto> transactionKafkaTemplate() {
        return new KafkaTemplate<>(transactionProducerFactory());
    }

    @Bean(name = "kafkaTransactionManager")
    public KafkaTransactionManager<String, TransactionKafkaDto> kafkaTransactionManager() {
        return new KafkaTransactionManager<>(transactionProducerFactory());
    }

    @Bean
    public ProducerFactory<String, EnrichedTransactionDto> enrichedProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        configProps.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "tx-fraud-service");
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, EnrichedTransactionDto> enrichedKafkaTemplate() {
        return new KafkaTemplate<>(enrichedProducerFactory());
    }

    @Bean(name = "kafkaEnrichedTransactionManager")
    public KafkaTransactionManager<String, EnrichedTransactionDto> enrichedKafkaTransactionManager() {
        return new KafkaTransactionManager<>(enrichedProducerFactory());
    }

    @Bean
    public ProducerFactory<String, FraudInvestigationEvent> fraudInvestigationProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapServers);
        configProps.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        configProps.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class);

        return new DefaultKafkaProducerFactory<>(
                configProps);
    }

    @Bean
    public KafkaTemplate<String, FraudInvestigationEvent>fraudInvestigationKafkaTemplate() {
        return new KafkaTemplate<>(fraudInvestigationProducerFactory());
}

    @Bean(name = "transactionKafkaListenerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, TransactionKafkaDto> transactionKafkaListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, TransactionKafkaDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(
                consumerConfigs(), new org.apache.kafka.common.serialization.StringDeserializer(),
                new JsonDeserializer<>(TransactionKafkaDto.class, false)));
        return factory;
    }

    @Bean(name = "enrichedKafkaListenerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, EnrichedTransactionDto> enrichedKafkaListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, EnrichedTransactionDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(
                consumerConfigs(), new org.apache.kafka.common.serialization.StringDeserializer(),
                new JsonDeserializer<>(EnrichedTransactionDto.class, false)));
        return factory;
    }

    // Common consumer configs
    private Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.fraudDetection.FraudGuard.dto");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        return props;
    }
}
