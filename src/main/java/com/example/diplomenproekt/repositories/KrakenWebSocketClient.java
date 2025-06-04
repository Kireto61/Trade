package com.example.diplomenproekt.repositories;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class KrakenWebSocketClient extends WebSocketClient {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ConcurrentHashMap<String, String> latestPrices = new ConcurrentHashMap<>();
    private final ProductRepository productRepository;

    @Autowired
    public KrakenWebSocketClient(ProductRepository productRepository) throws Exception {
        super(new URI("wss://ws.kraken.com"));
        this.productRepository = productRepository;
        this.connect();
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("WebSocket Connected");

        String subscriptionMessage = "{\n" +
                                     "  \"event\": \"subscribe\",\n" +
                                     "  \"pair\": [\"XBT/USD\", \"ETH/USD\", \"USDT/USD\", \"XRP/USD\", \"BNB/USD\", \"SOL/USD\", \"USDC/USD\", \"DOGE/USD\", \"TRX/USD\", \"ADA/USD\", \"HYPE/USD\", \"SUI/USD\", \"LINK/USD\", \"AVAX/USD\", \"XLM/USD\", \"BCH/USD\", \"LEO/USD\", \"TON/USD\", \"SHIB/USD\", \"HBAR/USD\"],\n" +
                                     "  \"subscription\": {\n" +
                                     "    \"name\": \"ticker\"\n" +
                                     "  }\n" +
                                     "}\n";
        this.send(subscriptionMessage);
    }

    @Override
    public void onMessage(String message) {
        try {
            if (!message.startsWith("{")) {
                JsonNode node = objectMapper.readTree(message);
                String pair = node.get(3).asText();
                JsonNode priceNode = node.get(1).get("c"); // c = close price
                String latestPrice = priceNode.get(0).asText();

                latestPrices.put(pair, latestPrice);
                this.productRepository.updatePrice(pair.split("/")[0], Double.parseDouble(latestPrice));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ConcurrentHashMap<String, String> getLatestPrices() {
        return latestPrices;
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("WebSocket Closed: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }
}
