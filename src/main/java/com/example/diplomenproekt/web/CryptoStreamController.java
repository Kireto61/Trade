package com.example.diplomenproekt.web;

import com.example.diplomenproekt.repositories.KrakenWebSocketClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import java.time.Duration;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class CryptoStreamController {

    private final KrakenWebSocketClient krakenWebSocketClient;

    public CryptoStreamController(KrakenWebSocketClient krakenWebSocketClient) {
        this.krakenWebSocketClient = krakenWebSocketClient;
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Map<String, String>> streamCryptoPrices() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(seq -> krakenWebSocketClient.getLatestPrices());
    }
}
