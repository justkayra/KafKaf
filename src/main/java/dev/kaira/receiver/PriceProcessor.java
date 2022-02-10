package dev.kaira.receiver;

import io.smallrye.reactive.messaging.kafka.Record;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PriceProcessor {

    private final Logger logger = Logger.getLogger(PriceProcessor.class);

    @Incoming("prices")
    public void receive(Record<Integer, String> record) {
        logger.infof("Got : %d - %s", record.key(), record.value());
    }

}
