package com.thedasmc.mcsdmarketsapi;

import com.thedasmc.mcsdmarketsapi.enums.TransactionType;
import com.thedasmc.mcsdmarketsapi.request.*;
import com.thedasmc.mcsdmarketsapi.response.wrapper.*;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MCSDMarketsAPITest {

    @Test
    public void testGetItemSuccessfulResponse() throws IOException {
        final String jsonResponse =
            "{\n" +
            "    \"material\": \"DIAMOND\",\n" +
            "    \"basePrice\": 99.99,\n" +
            "    \"currentPrice\": 99.99,\n" +
            "    \"inventory\": 100,\n" +
            "    \"versionAdded\": \"1.0\"\n" +
            "}";

        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(connection.getInputStream()).thenReturn(asStream(jsonResponse));

        MCSDMarketsAPI api = getApi(connection);
        ItemResponseWrapper responseWrapper = api.getItem("DIAMOND");
        assertTrue(responseWrapper.isSuccessful());
        assertNotNull(responseWrapper.getSuccessfulResponse());
        assertNull(responseWrapper.getErrorResponse());
        assertEquals("DIAMOND", responseWrapper.getSuccessfulResponse().getMaterial());
        assertEquals(BigDecimal.valueOf(99.99), responseWrapper.getSuccessfulResponse().getBasePrice());
        assertEquals(BigDecimal.valueOf(99.99), responseWrapper.getSuccessfulResponse().getCurrentPrice());
        assertEquals(100, responseWrapper.getSuccessfulResponse().getInventory());
        assertEquals("1.0", responseWrapper.getSuccessfulResponse().getVersionAdded());

        verify(connection).disconnect();
    }

    @Test
    public void testGetItemUnsuccessfulResponse() throws IOException {
        final String jsonResponse =
            "{\n" +
            "   \"message\": \"Item INVALID_ITEM not found!\"\n" +
            "}";

        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_NOT_FOUND);
        when(connection.getErrorStream()).thenReturn(asStream(jsonResponse));

        MCSDMarketsAPI api = getApi(connection);
        ItemResponseWrapper responseWrapper = api.getItem("INVALID_MATERIAL");
        assertFalse(responseWrapper.isSuccessful());
        assertNull(responseWrapper.getSuccessfulResponse());
        assertNotNull(responseWrapper.getErrorResponse());
        assertNotNull(responseWrapper.getErrorResponse().getMessage());

        verify(connection).disconnect();
    }

    @Test
    public void testCreateTransactionSuccessfulResponse() throws IOException {
        final UUID playerId = UUID.randomUUID();
        final long now = System.currentTimeMillis();

        final String jsonResponse =
            "{\n" +
            "    \"transactionId\": 12345,\n" +
            "    \"playerId\": \"" + playerId + "\",\n" +
            "    \"transactionType\": \"SALE\",\n" +
            "    \"material\": \"DIAMOND\",\n" +
            "    \"quantity\": 1,\n" +
            "    \"executed\": " + now + "\n" +
            "}";

        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(connection.getOutputStream()).thenReturn(mock(java.io.OutputStream.class));
        when(connection.getInputStream()).thenReturn(asStream(jsonResponse));

        MCSDMarketsAPI api = getApi(connection);
        CreateTransactionResponseWrapper responseWrapper = api.createTransaction(new CreateTransactionRequest());
        assertTrue(responseWrapper.isSuccessful());
        assertNotNull(responseWrapper.getSuccessfulResponse());
        assertNull(responseWrapper.getErrorResponse());
        assertEquals(12345, responseWrapper.getSuccessfulResponse().getTransactionId());
        assertEquals(playerId, responseWrapper.getSuccessfulResponse().getPlayerId());
        assertEquals(TransactionType.SALE, responseWrapper.getSuccessfulResponse().getTransactionType());
        assertEquals("DIAMOND", responseWrapper.getSuccessfulResponse().getMaterial());
        assertEquals(1, responseWrapper.getSuccessfulResponse().getQuantity());
        assertEquals(now, responseWrapper.getSuccessfulResponse().getExecuted().toEpochMilli());

        //Verify output stream is used and closed
        verify(connection).getOutputStream();
        verify(connection.getOutputStream()).close();
        verify(connection).disconnect();
    }

    @Test
    public void testCreateTransactionUnsuccessfulResponse() throws IOException {
        final String jsonResponse =
            "{\n" +
            "    \"message\": \"quantity must be > 0\"\n" +
            "}";

        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_BAD_REQUEST);
        when(connection.getOutputStream()).thenReturn(mock(java.io.OutputStream.class));
        when(connection.getErrorStream()).thenReturn(asStream(jsonResponse));

        MCSDMarketsAPI api = getApi(connection);
        CreateTransactionResponseWrapper responseWrapper = api.createTransaction(new CreateTransactionRequest());
        assertFalse(responseWrapper.isSuccessful());
        assertNull(responseWrapper.getSuccessfulResponse());
        assertNotNull(responseWrapper.getErrorResponse());
        assertNotNull(responseWrapper.getErrorResponse().getMessage());

        //Verify output stream is used and closed
        verify(connection).getOutputStream();
        verify(connection.getOutputStream()).close();
        verify(connection).disconnect();
    }

    @Test
    public void testBatchSellSuccessfulResponse() throws IOException {
        final String jsonResponse =
            "{\n" +
            "    \"transactions\": [\n" +
            "        {\n" +
            "            \"transactionId\": 68,\n" +
            "            \"playerId\": \"b5a3e507-e849-4f1e-86d6-1b2b5b6f0d78\",\n" +
            "            \"transactionType\": \"SALE\",\n" +
            "            \"material\": \"DIAMOND\",\n" +
            "            \"quantity\": 1,\n" +
            "            \"executed\": 1748304546481\n" +
            "        },\n" +
            "        {\n" +
            "            \"transactionId\": 69,\n" +
            "            \"playerId\": \"b5a3e507-e849-4f1e-86d6-1b2b5b6f0d78\",\n" +
            "            \"transactionType\": \"SALE\",\n" +
            "            \"material\": \"OAK_LOG\",\n" +
            "            \"quantity\": 1,\n" +
            "            \"executed\": 1748304546481\n" +
            "        }\n" +
            "    ],\n" +
            "    \"unsellableMaterials\": [\n" +
            "        \"INVALID_ITEM_MATERIAL\"\n" +
            "    ]\n" +
            "}";

        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(connection.getOutputStream()).thenReturn(mock(java.io.OutputStream.class));
        when(connection.getInputStream()).thenReturn(asStream(jsonResponse));

        BatchSellRequest request = new BatchSellRequest();
        request.setTransactions(Collections.singletonList(new BatchTransactionRequest()));//If the transaction list is null or empty, an IllegalArgumentException is thrown

        MCSDMarketsAPI api = getApi(connection);
        BatchSellResponseWrapper responseWrapper = api.batchSell(request);
        assertTrue(responseWrapper.isSuccessful());
        assertNotNull(responseWrapper.getSuccessfulResponse());
        assertNull(responseWrapper.getErrorResponse());
        assertEquals(2, responseWrapper.getSuccessfulResponse().getTransactions().size());
        assertEquals(1, responseWrapper.getSuccessfulResponse().getUnsellableMaterials().size());
    }

    @Test
    public void testBatchSellThrowsExceptionWhenNoTransactionsSpecified() {
        MCSDMarketsAPI api = getApi();
        assertThrows(IllegalArgumentException.class, () -> api.batchSell(new BatchSellRequest()));
    }

    @Test
    public void testGetItemsSuccessfulResponse() throws IOException {
        final String jsonResponse =
            "{\n" +
            "    \"pageInfo\": {\n" +
            "        \"page\": 0,\n" +
            "        \"pages\": 1\n" +
            "    },\n" +
            "    \"items\": [\n" +
            "        {\n" +
            "            \"material\": \"ACACIA_LOG\",\n" +
            "            \"basePrice\": 8.00,\n" +
            "            \"currentPrice\": 8.00,\n" +
            "            \"inventory\": 100,\n" +
            "            \"versionAdded\": \"1.7\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"material\": \"DIAMOND\",\n" +
            "            \"basePrice\": 200.00,\n" +
            "            \"currentPrice\": 200.00,\n" +
            "            \"inventory\": 97,\n" +
            "            \"versionAdded\": \"1.0\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"material\": \"OAK_LOG\",\n" +
            "            \"basePrice\": 8.00,\n" +
            "            \"currentPrice\": 8.00,\n" +
            "            \"inventory\": 95,\n" +
            "            \"versionAdded\": \"1.0\"\n" +
            "        }\n" +
            "    ]\n" +
            "}";

        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(connection.getOutputStream()).thenReturn(mock(java.io.OutputStream.class));
        when(connection.getInputStream()).thenReturn(asStream(jsonResponse));

        MCSDMarketsAPI api = getApi(connection);
        ItemPageResponseWrapper responseWrapper = api.getItems(new PageRequest());
        assertTrue(responseWrapper.isSuccessful());
        assertNotNull(responseWrapper.getSuccessfulResponse());
        assertEquals(3, responseWrapper.getSuccessfulResponse().getItems().size());
        assertNotNull(responseWrapper.getSuccessfulResponse().getPageInfo());
        assertNull(responseWrapper.getErrorResponse());

        verify(connection).disconnect();
    }

    @Test
    public void testGetBatchItemsSuccessfulResponse() throws IOException {
        final String jsonResponse =
            "[\n" +
            "    {\n" +
            "        \"material\": \"DIAMOND\",\n" +
            "        \"basePrice\": 200.00,\n" +
            "        \"currentPrice\": 200.00,\n" +
            "        \"inventory\": 97,\n" +
            "        \"versionAdded\": \"1.0\"\n" +
            "    },\n" +
            "    {\n" +
            "        \"material\": \"OAK_LOG\",\n" +
            "        \"basePrice\": 8.00,\n" +
            "        \"currentPrice\": 8.00,\n" +
            "        \"inventory\": 95,\n" +
            "        \"versionAdded\": \"1.0\"\n" +
            "    }\n" +
            "]";

        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(connection.getOutputStream()).thenReturn(mock(java.io.OutputStream.class));
        when(connection.getInputStream()).thenReturn(asStream(jsonResponse));

        MCSDMarketsAPI api = getApi(connection);
        BatchItemResponseWrapper responseWrapper = api.getItems("DIAMOND", "OAK_LOG");
        assertTrue(responseWrapper.isSuccessful());
        assertNotNull(responseWrapper.getSuccessfulResponse());
        assertEquals(2, responseWrapper.getSuccessfulResponse().size());
        assertNull(responseWrapper.getErrorResponse());

        verify(connection).disconnect();
    }

    @Test
    public void testGetBatchItemsThrowsExceptionWhenNoItemsSpecified() {
        MCSDMarketsAPI api = getApi();
        assertThrows(IllegalArgumentException.class, () -> api.getItems(Collections.emptyList()));
    }

    @Test
    public void testGetLimitOrdersSuccessfulResponse() throws IOException {
        final String jsonResponse =
            "{\n" +
            "  \"page\": 0,\n" +
            "  \"pages\": 10,\n" +
            "  \"limitOrders\": [\n" +
            "    {\n" +
            "      \"limitOrderId\": 12345,\n" +
            "      \"material\": \"DIAMOND\",\n" +
            "      \"playerId\": \"3ee0ea1f-6d09-449f-9d3a-e5ae29c6995a\",\n" +
            "      \"submitted\": \"1756250877349\",\n" +
            "      \"transactionType\": \"PURCHASE\",\n" +
            "      \"limitPrice\": 99.99,\n" +
            "      \"quantity\": 1\n" +
            "    }\n" +
            "  ]\n" +
            "}";

        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(connection.getOutputStream()).thenReturn(mock(OutputStream.class));
        when(connection.getInputStream()).thenReturn(asStream(jsonResponse));

        MCSDMarketsAPI api = getApi(connection);
        LimitOrderPageResponseWrapper responseWrapper = api.getLimitOrders(new LimitOrderPageRequest());

        assertTrue(responseWrapper.isSuccessful());
        assertNotNull(responseWrapper.getSuccessfulResponse());
        assertEquals(1, responseWrapper.getSuccessfulResponse().getLimitOrders().size());
        assertNull(responseWrapper.getErrorResponse());
    }

    @Test
    public void testSubmitLimitOrderSuccessfulResponse() throws IOException {
        final String jsonResponse =
            "{\n" +
            "  \"limitOrderId\": 12345,\n" +
            "  \"material\": \"DIAMOND\",\n" +
            "  \"playerId\": \"3ee0ea1f-6d09-449f-9d3a-e5ae29c6995a\",\n" +
            "  \"submitted\": \"1756250877349\",\n" +
            "  \"transactionType\": \"PURCHASE\",\n" +
            "  \"limitPrice\": 99.99,\n" +
            "  \"quantity\": 1\n" +
            "}";

        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(connection.getOutputStream()).thenReturn(mock(OutputStream.class));
        when(connection.getInputStream()).thenReturn(asStream(jsonResponse));

        MCSDMarketsAPI api = getApi(connection);
        LimitOrderResponseWrapper responseWrapper = api.submitLimitOrder(new SubmitLimitOrderRequest());

        assertTrue(responseWrapper.isSuccessful());
        assertNotNull(responseWrapper.getSuccessfulResponse());
        assertNull(responseWrapper.getErrorResponse());
        assertEquals(12345L, responseWrapper.getSuccessfulResponse().getLimitOrderId());
        assertNotNull(responseWrapper.getSuccessfulResponse().getPlayerId());
    }

    @Test
    public void testCancelLimitOrderSuccessfulResponse() throws IOException {
        final String jsonResponse =
            "{\n" +
            "  \"limitOrderId\": 12345,\n" +
            "  \"material\": \"DIAMOND\",\n" +
            "  \"refundAmount\": 99.99,\n" +
            "  \"partialPayoutAmount\": 99.99,\n" +
            "  \"refundQuantity\": 100\n" +
            "}";

        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(connection.getOutputStream()).thenReturn(mock(OutputStream.class));
        when(connection.getInputStream()).thenReturn(asStream(jsonResponse));

        MCSDMarketsAPI api = getApi(connection);
        LimitOrderCancelResponseWrapper responseWrapper = api.cancelLimitOrder(new CancelLimitOrderRequest());

        assertTrue(responseWrapper.isSuccessful());
        assertNotNull(responseWrapper.getSuccessfulResponse());
        assertNull(responseWrapper.getErrorResponse());
        assertEquals(12345L, responseWrapper.getSuccessfulResponse().getLimitOrderId());
        assertEquals(BigDecimal.valueOf(99.99), responseWrapper.getSuccessfulResponse().getRefundAmount());
        assertEquals(BigDecimal.valueOf(99.99), responseWrapper.getSuccessfulResponse().getPartialPayoutAmount());
        assertEquals(100, responseWrapper.getSuccessfulResponse().getRefundQuantity());
    }

    @Test
    public void testGetLimitOrderSuccessfulResponse() throws IOException {
        final String jsonResponse =
            "{\n" +
            "  \"limitOrderId\": 12345,\n" +
            "  \"material\": \"DIAMOND\",\n" +
            "  \"playerId\": \"3ee0ea1f-6d09-449f-9d3a-e5ae29c6995a\",\n" +
            "  \"submitted\": \"1756250877349\",\n" +
            "  \"transactionType\": \"PURCHASE\",\n" +
            "  \"limitPrice\": 99.99,\n" +
            "  \"quantity\": 1\n" +
            "}";

        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(connection.getOutputStream()).thenReturn(mock(OutputStream.class));
        when(connection.getInputStream()).thenReturn(asStream(jsonResponse));

        MCSDMarketsAPI api = getApi(connection);
        LimitOrderResponseWrapper responseWrapper = api.getLimitOrder(12345L, UUID.randomUUID());

        assertTrue(responseWrapper.isSuccessful());
        assertNotNull(responseWrapper.getSuccessfulResponse());
        assertNull(responseWrapper.getErrorResponse());
        assertEquals(12345L, responseWrapper.getSuccessfulResponse().getLimitOrderId());
        assertNotNull(responseWrapper.getSuccessfulResponse().getPlayerId());
    }

    @Test
    public void testGetHoursHistoricalItemPriceSuccessfulResponse() throws IOException {
        final String jsonResponse =
            "[\n" +
            "  {\n" +
            "    \"material\": \"DIAMOND\",\n" +
            "    \"executed\": \"2025-09-20T22:11:17.343\",\n" +
            "    \"price\": 99.99\n" +
            "  }\n" +
            "]";

        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(connection.getOutputStream()).thenReturn(mock(OutputStream.class));
        when(connection.getInputStream()).thenReturn(asStream(jsonResponse));

        MCSDMarketsAPI api = getApi(connection);
        HistoricalItemPriceResponseWrapper responseWrapper = api.getHoursHistoricalItemPrice("DIAMOND", 24);

        assertTrue(responseWrapper.isSuccessful());
        assertNotNull(responseWrapper.getSuccessfulResponse());
        assertFalse(responseWrapper.getSuccessfulResponse().isEmpty());
        assertNull(responseWrapper.getErrorResponse());
        assertNotNull(responseWrapper.getSuccessfulResponse().get(0).getExecuted());
    }

    @Test
    public void testGetDaysHistoricalItemPriceSuccessfulResponse() throws IOException {
        final String jsonResponse =
            "[\n" +
                "  {\n" +
                "    \"material\": \"DIAMOND\",\n" +
                "    \"executed\": \"2025-09-20T22:11:17.343\",\n" +
                "    \"price\": 99.99\n" +
                "  }\n" +
                "]";

        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(connection.getOutputStream()).thenReturn(mock(OutputStream.class));
        when(connection.getInputStream()).thenReturn(asStream(jsonResponse));

        MCSDMarketsAPI api = getApi(connection);
        HistoricalItemPriceResponseWrapper responseWrapper = api.getDaysHistoricalItemPrices("DIAMOND", 7);

        assertTrue(responseWrapper.isSuccessful());
        assertNotNull(responseWrapper.getSuccessfulResponse());
        assertFalse(responseWrapper.getSuccessfulResponse().isEmpty());
        assertNull(responseWrapper.getErrorResponse());
        assertNotNull(responseWrapper.getSuccessfulResponse().get(0).getExecuted());
    }

    private MCSDMarketsAPI getApi() {
        return new MCSDMarketsAPI("abc123", "1.21.3");
    }

    private MCSDMarketsAPI getApi(HttpURLConnection connection) {
        return new TestMCSDMarketsAPI("abc123", "1.21.3", connection);
    }

    private InputStream asStream(String json) {
        return new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
    }
}