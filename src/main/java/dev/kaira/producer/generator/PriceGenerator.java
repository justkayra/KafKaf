package dev.kaira.producer.generator;

import io.smallrye.mutiny.Multi;
import io.smallrye.reactive.messaging.kafka.Record;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Random;

@ApplicationScoped

public class PriceGenerator {
    private static final Logger LOG = Logger.getLogger(String.valueOf(PriceGenerator.class));

    private Random random = new Random();

    private List<Price> prices = List.of(
            new Price(1, "HItachi", 13),
            new Price(2, "Sanyo", 5),
            new Price(3, "Technics", 11),
            new Price(4, "NEC", 16),
            new Price(5, "Panasonic", 12),
            new Price(6, "Sharp", -7),
            new Price(7, "Aiwa", 11),
            new Price(8, "Crown", 7),
            new Price(9, "Akai", 20));

    @Outgoing("price-values")
    public Multi<Record<Integer, String>> generate() {
        return Multi.createFrom().ticks().every(Duration.ofMillis(500))
                .onOverflow().drop()
                .map(tick -> {
                    Price station = prices.get(random.nextInt(prices.size()));
                    double temperature = BigDecimal.valueOf(random.nextGaussian() * 15 + station.cost)
                            .setScale(1, RoundingMode.HALF_UP)
                            .doubleValue();

                    LOG.infov("station: {0}, temperature: {1}", station.name, temperature);
                    return Record.of(station.id, Instant.now() + ";" + temperature);
                });
    }

    @Outgoing("price-pool")
    public Multi<Record<Integer, String>> weatherStations() {
        return Multi.createFrom().items(prices.stream()
                .map(s -> Record.of(
                        s.id,
                        "{ \"id\" : " + s.id +
                                ", \"name\" : \"" + s.name + "\" }"))
        );
    }

    private static class Price {

        int id;
        String name;
        int cost;

        public Price(int id, String name, int averageTemperature) {
            this.id = id;
            this.name = name;
            this.cost = averageTemperature;
        }
    }
}
