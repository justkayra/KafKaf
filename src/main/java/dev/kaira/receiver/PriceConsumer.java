package dev.kaira.receiver;

import io.smallrye.reactive.messaging.kafka.api.IncomingKafkaRecordMetadata;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;

import javax.enterprise.context.ApplicationScoped;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class PriceConsumer {

    @Incoming("prices")
    public CompletionStage<Void> consume(Message<Double> msg) {
        // access record metadata
        var metadata = msg.getMetadata(IncomingKafkaRecordMetadata.class).orElseThrow();
        // process the message payload.
        double price = msg.getPayload();
        // Acknowledge the incoming message (commit the offset)
        return msg.ack();
    }

}
