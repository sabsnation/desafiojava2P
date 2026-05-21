package com.desafio.api;

import com.desafio.util.Validador;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class ViaCepService {

    private static final String BASE_URL = "https://viacep.com.br/ws/";
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    private final Gson gson = new Gson();

    public ViaCepResponse buscarPorCep(String cep) throws IOException, InterruptedException {
        String cepNormalizado = Validador.normalizarCep(cep);
        Validador.validarCep(cepNormalizado);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + cepNormalizado + "/json/"))
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Falha ao consultar ViaCEP. HTTP " + response.statusCode());
        }

        ViaCepResponse dados = gson.fromJson(response.body(), ViaCepResponse.class);
        if (dados == null || dados.isErro()) {
            throw new IllegalArgumentException("CEP não encontrado na API ViaCEP.");
        }

        return dados;
    }
}
