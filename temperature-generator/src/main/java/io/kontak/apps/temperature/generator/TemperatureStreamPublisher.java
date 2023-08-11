package io.kontak.apps.temperature.generator;

import io.kontak.apps.event.TemperatureReadingRecord;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
public class TemperatureStreamPublisher {

    private final Sinks.Many<Message<TemperatureReadingRecord>> messageProducer = Sinks.many().multicast().onBackpressureBuffer();

    public Flux<Message<TemperatureReadingRecord>> getMessageProducer() {
        return messageProducer.asFlux();
    }

    public void publish(TemperatureReadingRecord temperatureReading) {
        messageProducer.tryEmitNext(
                MessageBuilder.withPayload(temperatureReading)
                        .setHeader("identifier", temperatureReading.thermometerId())
                        .build()
        );
    }
}
