package com.example.diplomenproekt.services;
import com.example.diplomenproekt.repositories.KrakenWebSocketClient;
import com.example.diplomenproekt.repositories.ProductRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.boot.context.event.ApplicationReadyEvent;

@Service
public class KrakenService {

    private final ProductRepository productRepository;

    public KrakenService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    private KrakenWebSocketClient client;

    @EventListener(ApplicationReadyEvent.class)
    public void start() throws Exception {
        connectToKraken();
    }

    private void connectToKraken() throws Exception {
        client = new KrakenWebSocketClient(productRepository);
        client.connect(); // Always a fresh instance
    }
    public void reconnect() throws Exception {
        if (client != null && client.isOpen()) {
            client.close();
        }
        client = new KrakenWebSocketClient(productRepository);
        client.connect();
    }
}
