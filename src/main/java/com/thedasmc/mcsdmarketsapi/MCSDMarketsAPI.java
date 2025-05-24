package com.thedasmc.mcsdmarketsapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.thedasmc.mcsdmarketsapi.json.deserializer.CreateTransactionResponseDeserializer;
import com.thedasmc.mcsdmarketsapi.json.deserializer.ItemPageResponseDeserializer;
import com.thedasmc.mcsdmarketsapi.json.deserializer.ItemResponseDeserializer;
import com.thedasmc.mcsdmarketsapi.json.serializer.CreateTransactionRequestSerializer;
import com.thedasmc.mcsdmarketsapi.request.CreateTransactionRequest;
import com.thedasmc.mcsdmarketsapi.request.PageRequest;
import com.thedasmc.mcsdmarketsapi.response.impl.CreateTransactionResponse;
import com.thedasmc.mcsdmarketsapi.response.impl.ErrorResponse;
import com.thedasmc.mcsdmarketsapi.response.impl.ItemPageResponse;
import com.thedasmc.mcsdmarketsapi.response.impl.ItemResponse;
import com.thedasmc.mcsdmarketsapi.response.wrapper.BatchItemResponseWrapper;
import com.thedasmc.mcsdmarketsapi.response.wrapper.CreateTransactionResponseWrapper;
import com.thedasmc.mcsdmarketsapi.response.wrapper.ItemPageResponseWrapper;
import com.thedasmc.mcsdmarketsapi.response.wrapper.ItemResponseWrapper;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * The main class for interacting with the MCSDMarketsAPI
 */
public class MCSDMarketsAPI {

    private static final String BASE_URL = "https://api.thedasmc.com";
    private static final String GET_PRICE_URI = "/v1/item/{material}";
    private static final String GET_LEGACY_PRICE_URI = "/v1/item/{material}/{data}";
    private static final String CREATE_TRANSACTION_URI = "/v1/transaction";
    private static final String GET_ITEMS_URI = "/v1/items";
    private static final String GET_BATCH_ITEMS_URI = "/v1/batch/items?materials={materials}";

    private final String apiKey;
    private final String mcVersion;
    private final Gson gson;

    public MCSDMarketsAPI(String apiKey, String mcVersion) {
        this.apiKey = apiKey;
        this.mcVersion = mcVersion;

        this.gson = new GsonBuilder()
            .registerTypeAdapter(CreateTransactionResponse.class, new CreateTransactionResponseDeserializer())
            .registerTypeAdapter(ItemResponseDeserializer.class, new ItemResponseDeserializer())
            .registerTypeAdapter(ItemPageResponse.class, new ItemPageResponseDeserializer())
            .registerTypeAdapter(CreateTransactionRequest.class, new CreateTransactionRequestSerializer())
            .create();
    }

    /**
     * Get the material/item data, such as pricing
     * @param materialName The material name. Works with old and new Materials
     * @param data The legacy data used when creating an ItemStack (<1.13)
     * @return {@link ItemResponseWrapper} containing the successful/error responses
     * @throws IOException If an error communicating with the destination fails
     */
    public ItemResponseWrapper getItem(String materialName, Integer data) throws IOException {
        HttpURLConnection connection;

        if (data == null) {
            connection = getGetHttpConnection(
                BASE_URL + GET_PRICE_URI.replace("{material}", materialName.trim().toUpperCase()));
        } else {
            connection = getHttpConnection(
                BASE_URL + GET_LEGACY_PRICE_URI.replace("{material}", materialName.trim().toUpperCase()).replace("{data}", String.valueOf(data)));
        }

        ItemResponseWrapper priceResponseWrapper = new ItemResponseWrapper();

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            ItemResponse response = readResponse(connection, ItemResponse.class);
            priceResponseWrapper.setSuccessful(true);
            priceResponseWrapper.setSuccessfulResponse(response);
        } else {
            priceResponseWrapper.setErrorResponse(getErrorResponse(connection));
        }

        return priceResponseWrapper;
    }

    public ItemResponseWrapper getItem(String materialName) throws IOException {
        return getItem(materialName, null);
    }

    /**
     * Execute/Create a transaction
     * @param request {@link CreateTransactionRequest} containing the necessary data to make the request
     * @return A {@link CreateTransactionResponse} containing the successful/error responses
     * @throws IOException If an error communicating with the destination fails
     */
    public CreateTransactionResponseWrapper createTransaction(CreateTransactionRequest request) throws IOException {
        HttpURLConnection connection = getPostHttpConnection(BASE_URL + CREATE_TRANSACTION_URI, request);
        CreateTransactionResponseWrapper createTransactionResponseWrapper = new CreateTransactionResponseWrapper();

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            CreateTransactionResponse response = readResponse(connection, CreateTransactionResponse.class);
            createTransactionResponseWrapper.setSuccessful(true);
            createTransactionResponseWrapper.setSuccessfulResponse(response);
        } else {
            createTransactionResponseWrapper.setErrorResponse(getErrorResponse(connection));
        }

        return createTransactionResponseWrapper;
    }

    /**
     * Get available items by page
     * @param request The {@link PageRequest} containing the page details
     * @return A {@link ItemPageResponseWrapper} containing the successful/error responses
     * @throws IOException If an error communicating with the destination fails
     */
    public ItemPageResponseWrapper getItems(PageRequest request) throws IOException {
        HttpURLConnection connection = getPostHttpConnection(BASE_URL + GET_ITEMS_URI, request);
        ItemPageResponseWrapper itemPageResponseWrapper = new ItemPageResponseWrapper();

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            ItemPageResponse response = readResponse(connection, ItemPageResponse.class);
            itemPageResponseWrapper.setSuccessful(true);
            itemPageResponseWrapper.setSuccessfulResponse(response);
        } else {
            itemPageResponseWrapper.setErrorResponse(getErrorResponse(connection));
        }

        return itemPageResponseWrapper;
    }

    /**
     * Get multiple items at once. This is useful when you need to get multiple items' prices for a batch sale, as an example.
     * @param materials The materials to get the data for
     * @return A {@link BatchItemResponseWrapper} containing the successful/error responses
     * @throws IOException If an error communicating with the destination fails
     */
    public BatchItemResponseWrapper getItems(Collection<String> materials) throws IOException {
        materials = new HashSet<>(materials);

        if (materials.isEmpty())
            throw new IllegalArgumentException("No materials provided");

        HttpURLConnection connection = getGetHttpConnection(BASE_URL + GET_BATCH_ITEMS_URI.replace("{materials}", String.join(",", materials)));
        BatchItemResponseWrapper batchItemResponseWrapper = new BatchItemResponseWrapper();

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            List<ItemResponse> response = readResponseList(connection);
            batchItemResponseWrapper.setSuccessful(true);
            batchItemResponseWrapper.setSuccessfulResponse(response);
        } else {
            batchItemResponseWrapper.setErrorResponse(getErrorResponse(connection));
        }

        return batchItemResponseWrapper;
    }

    /**
     * Get multiple items at once. This is useful when you need to get multiple items' prices for a batch sale, as an example.
     * @param materials The materials to get the data for
     * @return A {@link BatchItemResponseWrapper} containing the successful/error responses
     * @throws IOException If an error communicating with the destination fails
     */
    public BatchItemResponseWrapper getItems(String... materials) throws IOException {
        return getItems(Arrays.asList(materials));
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

    HttpURLConnection getHttpConnection(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("x-api-key", apiKey);
        connection.setRequestProperty("x-client-version", mcVersion);

        return connection;
    }

    private ErrorResponse getErrorResponse(HttpURLConnection connection) throws IOException {
        try (InputStreamReader isr = new InputStreamReader(connection.getErrorStream())) {
            return gson.fromJson(isr, ErrorResponse.class);
        } finally {
            connection.disconnect();
        }
    }

    private <T> T readResponse(HttpURLConnection connection, Class<T> clazz) throws IOException {
        try (InputStreamReader isr = new InputStreamReader(connection.getInputStream())) {
            return gson.fromJson(isr, clazz);
        } finally {
            connection.disconnect();
        }
    }

    private <T> List<T> readResponseList(HttpURLConnection connection) throws IOException {
        try (InputStreamReader isr = new InputStreamReader(connection.getInputStream())) {
            return gson.fromJson(isr, new TypeToken<List<T>>(){}.getType());
        } finally {
            connection.disconnect();
        }
    }

}
