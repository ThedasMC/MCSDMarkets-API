package com.thedasmc.mcsdmarketsapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thedasmc.mcsdmarketsapi.json.deserializer.CreateTransactionResponseDeserializer;
import com.thedasmc.mcsdmarketsapi.json.serializer.CreateTransactionRequestSerializer;
import com.thedasmc.mcsdmarketsapi.request.CreateTransactionRequest;
import com.thedasmc.mcsdmarketsapi.response.impl.ErrorResponse;
import com.thedasmc.mcsdmarketsapi.response.impl.CreateTransactionResponse;
import com.thedasmc.mcsdmarketsapi.response.wrapper.CreateTransactionResponseWrapper;
import com.thedasmc.mcsdmarketsapi.response.wrapper.PriceResponseWrapperWrapper;

import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * The main class for interacting with the MCSDMarketsAPI
 */
public class MCSDMarketsAPI {

    private static final String BASE_URL = "https://api.thedasmc.com";
    private static final String BASE_URL_TEST = "http://localhost";
    private static final String GET_PRICE_URI = "/v1/item/{material}/price";
    private static final String CREATE_TRANSACTION_URI = "/v1/transaction";

    private final String apiKey;
    private final boolean testMode;
    private final Gson gson;

    public MCSDMarketsAPI(String apiKey) {
        this(apiKey, false);
    }

    public MCSDMarketsAPI(String apiKey, boolean testMode) {
        this.testMode = testMode;
        this.apiKey = apiKey;

        this.gson = new GsonBuilder()
            .registerTypeAdapter(CreateTransactionResponse.class, new CreateTransactionResponseDeserializer())
            .registerTypeAdapter(CreateTransactionRequest.class, new CreateTransactionRequestSerializer())
            .create();
    }

    /**
     * Get the price of a material/item
     * @param materialName The >= 1.13 material name
     * @return {@link PriceResponseWrapperWrapper} containing the successful/error responses
     * @throws IOException If an error communicating with the destination fails
     */
    public PriceResponseWrapperWrapper getPrice(String materialName) throws IOException {
        HttpURLConnection connection = getGetHttpConnection(
            getBaseUrl() + GET_PRICE_URI.replace("{material}", materialName.trim().toUpperCase()));
        PriceResponseWrapperWrapper priceResponseWrapper = new PriceResponseWrapperWrapper();

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            String priceString = getResponseAsString(connection);
            BigDecimal price = BigDecimal.valueOf(Double.parseDouble(priceString));
            priceResponseWrapper.setSuccessful(true);
            priceResponseWrapper.setSuccessfulResponse(price);
        } else {
            priceResponseWrapper.setErrorResponse(getErrorResponse(connection));
        }

        return priceResponseWrapper;
    }

    /**
     * Execute/Create a transaction
     * @param request {@link CreateTransactionRequest} containing the necessary data to make the request
     * @return A {@link CreateTransactionResponse} containing the successful/error responses
     * @throws IOException If an error communicating with the destination fails
     */
    public CreateTransactionResponseWrapper createTransaction(CreateTransactionRequest request) throws IOException {
        HttpURLConnection connection = getPostHttpConnection(getBaseUrl() + CREATE_TRANSACTION_URI, request);
        CreateTransactionResponseWrapper createTransactionResponseWrapper = new CreateTransactionResponseWrapper();

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            CreateTransactionResponse response = gson.fromJson(new InputStreamReader(connection.getInputStream()), CreateTransactionResponse.class);
            createTransactionResponseWrapper.setSuccessful(true);
            createTransactionResponseWrapper.setSuccessfulResponse(response);
        } else {
            createTransactionResponseWrapper.setErrorResponse(getErrorResponse(connection));
        }

        return createTransactionResponseWrapper;
    }

    private String getBaseUrl() {
        return testMode ? BASE_URL_TEST : BASE_URL;
    }

    private HttpURLConnection getGetHttpConnection(String url) throws IOException {
        HttpURLConnection connection = getHttpConnection(url);
        connection.setRequestMethod("GET");

        return connection;
    }

    private HttpURLConnection getPostHttpConnection(String url, Object body) throws IOException {
        HttpURLConnection connection = getHttpConnection(url);
        connection.setRequestMethod("POST");

        if (body != null) {
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            try (OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8)) {
                osw.write(gson.toJson(body));
            }
        }

        return connection;
    }

    private HttpURLConnection getHttpConnection(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("x-api-key", apiKey);

        return connection;
    }

    private ErrorResponse getErrorResponse(HttpURLConnection connection) {
        return gson.fromJson(new InputStreamReader(connection.getErrorStream()), ErrorResponse.class);
    }

    private String getResponseAsString(HttpURLConnection connection) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder builder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                if (builder.length() > 0)
                    builder.append(System.lineSeparator());

                builder.append(line);
            }

            return builder.toString();
        }
    }

}
