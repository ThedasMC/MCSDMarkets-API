package com.thedasmc.mcsdmarketsapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thedasmc.mcsdmarketsapi.json.deserializer.CreateTransactionResponseDeserializer;
import com.thedasmc.mcsdmarketsapi.json.deserializer.ItemPageResponseDeserializer;
import com.thedasmc.mcsdmarketsapi.json.deserializer.ItemResponseDeserializer;
import com.thedasmc.mcsdmarketsapi.json.serializer.CreateTransactionRequestSerializer;
import com.thedasmc.mcsdmarketsapi.request.CreateTransactionRequest;
import com.thedasmc.mcsdmarketsapi.request.ItemPageRequest;
import com.thedasmc.mcsdmarketsapi.response.impl.CreateTransactionResponse;
import com.thedasmc.mcsdmarketsapi.response.impl.ErrorResponse;
import com.thedasmc.mcsdmarketsapi.response.impl.ItemPageResponse;
import com.thedasmc.mcsdmarketsapi.response.impl.ItemResponse;
import com.thedasmc.mcsdmarketsapi.response.wrapper.CreateTransactionResponseWrapper;
import com.thedasmc.mcsdmarketsapi.response.wrapper.ItemPageResponseWrapper;
import com.thedasmc.mcsdmarketsapi.response.wrapper.ItemResponseWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * The main class for interacting with the MCSDMarketsAPI
 */
public class MCSDMarketsAPI {

    private static final String BASE_URL = "https://api.thedasmc.com";
    private static final String BASE_URL_TEST = "http://localhost";
    private static final String GET_PRICE_URI = "/v1/item/{material}";
    private static final String CREATE_TRANSACTION_URI = "/v1/transaction";
    private static final String GET_ITEMS_URI = "/v1/items";

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
            .registerTypeAdapter(ItemResponseDeserializer.class, new ItemResponseDeserializer())
            .registerTypeAdapter(ItemPageResponse.class, new ItemPageResponseDeserializer())
            .registerTypeAdapter(CreateTransactionRequest.class, new CreateTransactionRequestSerializer())
            .create();
    }

    /**
     * Get the material/item data, such as pricing
     * @param materialName The >= 1.13 material name
     * @return {@link ItemResponseWrapper} containing the successful/error responses
     * @throws IOException If an error communicating with the destination fails
     */
    public ItemResponseWrapper getItem(String materialName) throws IOException {
        HttpURLConnection connection = getGetHttpConnection(
            getBaseUrl() + GET_PRICE_URI.replace("{material}", materialName.trim().toUpperCase()));
        ItemResponseWrapper priceResponseWrapper = new ItemResponseWrapper();

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            ItemResponse response = gson.fromJson(new InputStreamReader(connection.getInputStream()), ItemResponse.class);
            priceResponseWrapper.setSuccessful(true);
            priceResponseWrapper.setSuccessfulResponse(response);
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

    /**
     * Get available items by page
     * @param request The {@link ItemPageRequest} containing the page details and mcVersion
     * @return A {@link ItemPageResponseWrapper} containing the successful/error responses
     * @throws IOException If an error communicating with the destination fails
     */
    public ItemPageResponseWrapper getItems(ItemPageRequest request) throws IOException {
        HttpURLConnection connection = getPostHttpConnection(getBaseUrl() + GET_ITEMS_URI, request);
        ItemPageResponseWrapper itemPageResponseWrapper = new ItemPageResponseWrapper();

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            ItemPageResponse response = gson.fromJson(new InputStreamReader(connection.getInputStream()), ItemPageResponse.class);
            itemPageResponseWrapper.setSuccessful(true);
            itemPageResponseWrapper.setSuccessfulResponse(response);
        } else {
            itemPageResponseWrapper.setErrorResponse(getErrorResponse(connection));
        }

        return itemPageResponseWrapper;
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
