package com.microservices.orderservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Component
public class InventoryClient {

    private final RestTemplate restTemplate;
    private final String inventoryBaseUrl;

    public InventoryClient(RestTemplate restTemplate,
                           @Value("${inventory.base-url:http://localhost:8081}") String inventoryBaseUrl) {
        this.restTemplate = restTemplate;
        this.inventoryBaseUrl = inventoryBaseUrl;
    }

    public boolean checkStock(String productName, int qty) {

        String url = UriComponentsBuilder
                .fromUriString(inventoryBaseUrl + "/inventory/check")
                .queryParam("productName", productName)
                .queryParam("qty", qty)
                .toUriString();

        ResponseEntity<Map> response =
                restTemplate.getForEntity(url, Map.class);

        Object available = response.getBody() != null
                ? response.getBody().get("available")
                : null;

        return available instanceof Boolean && (Boolean) available;
    }

    public void reserveStock(String productName, int qty) {

        String url = UriComponentsBuilder
                .fromUriString(inventoryBaseUrl + "/inventory/reserve")
                .queryParam("productName", productName)
                .queryParam("qty", qty)
                .toUriString();

        restTemplate.postForEntity(url, null, Map.class);
    }
}
