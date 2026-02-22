package com.thedasmc.mcsdmarketsapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.thedasmc.mcsdmarketsapi.json.deserializer.*;
import com.thedasmc.mcsdmarketsapi.json.serializer.CreateTransactionRequestSerializer;
import com.thedasmc.mcsdmarketsapi.request.*;
import com.thedasmc.mcsdmarketsapi.response.impl.*;
import com.thedasmc.mcsdmarketsapi.response.wrapper.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

/**
 * The main class for interacting with the MCSDMarketsAPI
 */
public class MCSDMarketsAPI {

    private static final String BASE_URL = "https://api.thedasmc.com";
    private static final String GET_PRICE_URI = "/v1/item/{material}";
    private static final String CREATE_TRANSACTION_URI = "/v1/transaction";
    private static final String BATCH_SELL_URI = "/v1/transaction/batch/sale";
    private static final String GET_ITEMS_URI = "/v1/items";
    private static final String GET_BATCH_ITEMS_URI = "/v1/batch/items?materials={materials}";
    private static final String GET_LIMIT_ORDERS_URI = "/v1/limit-orders";
    private static final String SUBMIT_LIMIT_ORDER_URI = "/v1/limit-order";
    private static final String CANCEL_LIMIT_ORDER_URI = "/v1/limit-order/cancel";
    private static final String GET_LIMIT_ORDER_URI = "/v1/limit-order/{playerId}/{limitOrderId}";
    private static final String CASHOUT_LIMIT_ORDERS_URI = "/v1/limit-orders/{playerId}/cashout";
    private static final String GET_PRICE_HISTORY_HOURS = "/v1/item/{material}/price/history/hours/{lookBackHours}";
    private static final String GET_PRICE_HISTORY_DAYS = "/v1/item/{material}/price/history/days/{lookBackDays}";

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
            .registerTypeAdapter(Instant.class, new InstantDeserializer())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
            .create();
    }

    /**
     * Get the material/item data, such as pricing
     * @param materialName The material name. Works with old and new Materials
     * @return {@link ItemResponseWrapper} containing the successful/error responses
     * @throws IOException If an error communicating with the destination fails
     */
    public ItemResponseWrapper getItem(String materialName) throws IOException {
        HttpURLConnection connection = getGetHttpConnection(BASE_URL + GET_PRICE_URI.replace("{material}", materialName.trim().toUpperCase()));
        ItemResponseWrapper priceResponseWrapper = new ItemResponseWrapper();

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            ItemResponse response = readResponse(connection, ItemResponse.class);
            priceResponseWrapper.setSuccessful(true);
            priceResponseWrapper.setSuccessfulResponse(response);
        } else {
            priceResponseWrapper.setErrorResponse(readErrorResponse(connection));
        }

        return priceResponseWrapper;
    }

    /**
     * Execute/Create a transaction
     * @param request {@link CreateTransactionRequest} containing the necessary data to make the request
     * @return A {@link CreateTransactionResponseWrapper} containing the successful/error responses
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
            createTransactionResponseWrapper.setErrorResponse(readErrorResponse(connection));
        }

        return createTransactionResponseWrapper;
    }

    /**
     * Sell items in a batch. Useful for selling multiple items at a time for a single player.
     * @param request A {@link BatchSellRequest} containing the necessary data to make the request
     * @return A {@link BatchSellResponseWrapper} containing the successful/error responses
     * @throws IOException If an error communicating with the destination fails
     */
    public BatchSellResponseWrapper batchSell(BatchSellRequest request) throws IOException {
        if (request.getTransactions() == null || request.getTransactions().isEmpty())
            throw new IllegalArgumentException("No transactions provided");

        HttpURLConnection connection = getPostHttpConnection(BASE_URL + BATCH_SELL_URI, request);
        BatchSellResponseWrapper batchSellResponseWrapper = new BatchSellResponseWrapper();

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            BatchSellResponse response = readResponse(connection, BatchSellResponse.class);
            batchSellResponseWrapper.setSuccessful(true);
            batchSellResponseWrapper.setSuccessfulResponse(response);
        } else {
            batchSellResponseWrapper.setErrorResponse(readErrorResponse(connection));
        }

        return batchSellResponseWrapper;
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
            itemPageResponseWrapper.setErrorResponse(readErrorResponse(connection));
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
            List<ItemResponse> response = readResponseList(connection, ItemResponse.class);
            batchItemResponseWrapper.setSuccessful(true);
            batchItemResponseWrapper.setSuccessfulResponse(response);
        } else {
            batchItemResponseWrapper.setErrorResponse(readErrorResponse(connection));
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

    /**
     * Get a player's limit orders
     * @param request The {@link LimitOrderPageRequest} containing the page and player details. The first page is 0.
     * @return A {@link LimitOrderPageResponseWrapper} containing the successful/error responses
     * @throws IOException If an error communicating with the destination fails
     */
    public LimitOrderPageResponseWrapper getLimitOrders(LimitOrderPageRequest request) throws IOException {
        HttpURLConnection connection = getPostHttpConnection(BASE_URL + GET_LIMIT_ORDERS_URI, request);
        LimitOrderPageResponseWrapper limitOrderPageResponseWrapper = new LimitOrderPageResponseWrapper();

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            LimitOrderPageResponse response = readResponse(connection, LimitOrderPageResponse.class);
            limitOrderPageResponseWrapper.setSuccessful(true);
            limitOrderPageResponseWrapper.setSuccessfulResponse(response);
        } else {
            limitOrderPageResponseWrapper.setErrorResponse(readErrorResponse(connection));
        }

        return limitOrderPageResponseWrapper;
    }

    /**
     * Submit a limit order
     * @param request The {@link SubmitLimitOrderRequest} containing the necessary data to make the request
     * @return A {@link LimitOrderResponseWrapper} containing the successful/error responses
     * @throws IOException If an error communicating with the destination fails
     */
    public LimitOrderResponseWrapper submitLimitOrder(SubmitLimitOrderRequest request) throws IOException {
        HttpURLConnection connection = getPostHttpConnection(BASE_URL + SUBMIT_LIMIT_ORDER_URI, request);
        LimitOrderResponseWrapper limitOrderResponseWrapper = new LimitOrderResponseWrapper();

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            LimitOrderResponse response = readResponse(connection, LimitOrderResponse.class);
            limitOrderResponseWrapper.setSuccessful(true);
            limitOrderResponseWrapper.setSuccessfulResponse(response);
        } else {
            limitOrderResponseWrapper.setErrorResponse(readErrorResponse(connection));
        }

        return limitOrderResponseWrapper;
    }

    /**
     * Cancel a limit order
     * @param request The {@link CancelLimitOrderRequest} containing the necessary data to make the request
     * @return A {@link LimitOrderCancelResponseWrapper} containing the successful/error responses
     * @throws IOException If an error communicating with the destination fails
     */
    public LimitOrderCancelResponseWrapper cancelLimitOrder(CancelLimitOrderRequest request) throws IOException {
        HttpURLConnection connection = getPostHttpConnection(BASE_URL + CANCEL_LIMIT_ORDER_URI, request);
        LimitOrderCancelResponseWrapper limitOrderCancelResponseWrapper = new LimitOrderCancelResponseWrapper();

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            LimitOrderCancelResponse response = readResponse(connection, LimitOrderCancelResponse.class);
            limitOrderCancelResponseWrapper.setSuccessful(true);
            limitOrderCancelResponseWrapper.setSuccessfulResponse(response);
        } else {
            limitOrderCancelResponseWrapper.setErrorResponse(readErrorResponse(connection));
        }

        return limitOrderCancelResponseWrapper;
    }

    /**
     * Get a limit order by player and order ID
     * @param limitOrderId The limitOrderId for the associated limit order
     * @param playerId The playerId for the associated limit order
     * @return A {@link LimitOrderResponseWrapper} containing the successful/error responses
     * @throws IOException If an error communicating with the destination fails
     */
    public LimitOrderResponseWrapper getLimitOrder(Long limitOrderId, UUID playerId) throws IOException {
        HttpURLConnection connection = getGetHttpConnection(BASE_URL + GET_LIMIT_ORDER_URI.replace("{playerId}", playerId.toString()).replace("{limitOrderId}", limitOrderId.toString()));
        LimitOrderResponseWrapper limitOrderResponseWrapper = new LimitOrderResponseWrapper();

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            LimitOrderResponse response = readResponse(connection, LimitOrderResponse.class);
            limitOrderResponseWrapper.setSuccessful(true);
            limitOrderResponseWrapper.setSuccessfulResponse(response);
        } else {
            limitOrderResponseWrapper.setErrorResponse(readErrorResponse(connection));
        }

        return limitOrderResponseWrapper;
    }

    /**
     * Cash out the unclaimed proceeds of a player's limit orders. If a buy order was filled at a lesser price, the difference will be included.
     * @param playerId The playerId for the associated limit orders
     * @return A {@link LimitOrderCashoutResponseWrapper} containing the successful/error responses
     * @throws IOException If an error communicating with the destination fails
     */
    public LimitOrderCashoutResponseWrapper cashoutLimitOrders(UUID playerId) throws IOException {
        HttpURLConnection connection = getPostHttpConnection(BASE_URL + CASHOUT_LIMIT_ORDERS_URI.replace("{playerId}", playerId.toString()), null);
        LimitOrderCashoutResponseWrapper limitOrderResponseWrapper = new LimitOrderCashoutResponseWrapper();

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            LimitOrderCashoutResponse response = readResponse(connection, LimitOrderCashoutResponse.class);
            limitOrderResponseWrapper.setSuccessful(true);
            limitOrderResponseWrapper.setSuccessfulResponse(response);
        } else {
            limitOrderResponseWrapper.setErrorResponse(readErrorResponse(connection));
        }

        return limitOrderResponseWrapper;
    }

    /**
     * Get historical pricing for an item for a given number of hours in the past.
     * @param material The material to get the historical pricing for
     * @param lookBackHours The number of hours in the past to get the historical pricing for
     * @return A {@link HistoricalItemPriceResponseWrapper} containing the successful/error responses
     * @throws IOException If an error communicating with the destination fails
     */
    public HistoricalItemPriceResponseWrapper getHoursHistoricalItemPrice(String material, int lookBackHours) throws IOException {
        return getHistoricalItemPrice(BASE_URL + GET_PRICE_HISTORY_HOURS.replace("{material}", material).replace("{lookBackHours}", String.valueOf(lookBackHours)));
    }

    /**
     * Get historical pricing for an item for a given number of days in the past.
     * @param material The material to get the historical pricing for
     * @param lookBackDays The number of days in the past to get the historical pricing for
     * @return A {@link HistoricalItemPriceResponseWrapper} containing the successful/error responses
     * @throws IOException If an error communicating with the destination fails
     */
    public HistoricalItemPriceResponseWrapper getDaysHistoricalItemPrices(String material, int lookBackDays) throws IOException {
        return getHistoricalItemPrice(BASE_URL + GET_PRICE_HISTORY_DAYS.replace("{material}", material).replace("{lookBackDays}", String.valueOf(lookBackDays)));
    }

    private HistoricalItemPriceResponseWrapper getHistoricalItemPrice(String url) throws IOException {
        HttpURLConnection connection = getGetHttpConnection(url);
        HistoricalItemPriceResponseWrapper historicalItemPriceResponseWrapper = new HistoricalItemPriceResponseWrapper();

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            List<HistoricalItemPriceResponse> response = readResponseList(connection, HistoricalItemPriceResponse.class);
            historicalItemPriceResponseWrapper.setSuccessful(true);
            historicalItemPriceResponseWrapper.setSuccessfulResponse(response);
        } else {
            historicalItemPriceResponseWrapper.setErrorResponse(readErrorResponse(connection));
        }

        return historicalItemPriceResponseWrapper;
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

    private ErrorResponse readErrorResponse(HttpURLConnection connection) throws IOException {
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

    private <T> List<T> readResponseList(HttpURLConnection connection, Class<T> clazz) throws IOException {
        try (InputStreamReader isr = new InputStreamReader(connection.getInputStream())) {
            return gson.fromJson(isr, TypeToken.getParameterized(List.class, clazz).getType());
        } finally {
            connection.disconnect();
        }
    }

}
