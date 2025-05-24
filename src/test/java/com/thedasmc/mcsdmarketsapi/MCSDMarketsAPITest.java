package com.thedasmc.mcsdmarketsapi;

import com.thedasmc.mcsdmarketsapi.enums.TransactionType;
import com.thedasmc.mcsdmarketsapi.request.CreateTransactionRequest;
import com.thedasmc.mcsdmarketsapi.request.PageRequest;
import com.thedasmc.mcsdmarketsapi.response.wrapper.BatchItemResponseWrapper;
import com.thedasmc.mcsdmarketsapi.response.wrapper.CreateTransactionResponseWrapper;
import com.thedasmc.mcsdmarketsapi.response.wrapper.ItemPageResponseWrapper;
import com.thedasmc.mcsdmarketsapi.response.wrapper.ItemResponseWrapper;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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