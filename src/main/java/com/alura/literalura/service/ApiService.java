package com.alura.literalura.service;

import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class ApiService {

    private final String URL = "https://gutendex.com/books/?search=";

    public String buscarLibro(String titulo) {
        try {
            String limpio = titulo.trim().replace(" ", "%20");
            HttpClient cliente = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL + limpio))
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = cliente.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            return "{\"error\":\"" + e.getMessage().replace("\"","'") + "\"}";
        }
    }
}
