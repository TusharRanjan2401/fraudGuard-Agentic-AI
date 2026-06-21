package com.fraudDetection.FraudGuard.services;
import com.fraudDetection.FraudGuard.dto.FraudInvestigationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FraudInvestigationProducer {
    private final KafkaTemplate<String,FraudInvestigationEvent> kafkaTemplate;
    
    public void publishInvestigationEvent(Long alertId){
        FraudInvestigationEvent event = FraudInvestigationEvent
                .builder()
                .alertId(alertId)
                .build();

        kafkaTemplate.send("fraud_investigation_topic", event);
    }
}
