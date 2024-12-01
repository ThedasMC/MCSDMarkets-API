package com.thedasmc.mcsdmarketsapi;

import com.thedasmc.mcsdmarketsapi.enums.TransactionType;
import com.thedasmc.mcsdmarketsapi.request.CreateTransactionRequest;
import com.thedasmc.mcsdmarketsapi.request.PageRequest;
import com.thedasmc.mcsdmarketsapi.response.wrapper.CreateTransactionResponseWrapper;
import com.thedasmc.mcsdmarketsapi.response.wrapper.ItemPageResponseWrapper;
import com.thedasmc.mcsdmarketsapi.response.wrapper.ItemResponseWrapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class MCSDMarketsAPITest {

    //Change this to use your own real api key
    private final MCSDMarketsAPI api = new MCSDMarketsAPI("abc123", "1.21.3", true);

    @Test
    public void testGetItemSuccessfulResponse() throws IOException {
        ItemResponseWrapper responseWrapper = api.getItem("DIAMOND");
        System.out.println(responseWrapper);
        assertTrue(responseWrapper.isSuccessful());
        assertNotNull(responseWrapper.getSuccessfulResponse());
        assertNull(responseWrapper.getErrorResponse());
    }

    @Test
    public void testGetItemUnsuccessfulResponse() throws IOException {
        ItemResponseWrapper responseWrapper = api.getItem("INVALID_MATERIAL");
        System.out.println(responseWrapper);
        assertFalse(responseWrapper.isSuccessful());
        assertNull(responseWrapper.getSuccessfulResponse());
        assertNotNull(responseWrapper.getErrorResponse());
        assertNotNull(responseWrapper.getErrorResponse().getMessage());
    }

    @Test
    public void testCreateTransactionSuccessfulResponse() throws IOException {
        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setPlayerId(UUID.randomUUID());
        request.setTransactionType(TransactionType.SALE);
        request.setMaterial("DIAMOND");
        request.setQuantity(1);

        CreateTransactionResponseWrapper responseWrapper = api.createTransaction(request);
        System.out.println(responseWrapper);
        assertTrue(responseWrapper.isSuccessful());
        assertNotNull(responseWrapper.getSuccessfulResponse());
        assertNull(responseWrapper.getErrorResponse());
    }

    @Test
    public void testCreateTransactionUnsuccessfulResponse() throws IOException {
        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setPlayerId(UUID.randomUUID());
        request.setTransactionType(TransactionType.SALE);
        request.setMaterial("DIAMOND");
        request.setQuantity(0);//Must be > 0, will result in error response

        CreateTransactionResponseWrapper responseWrapper = api.createTransaction(request);
        System.out.println(responseWrapper);
        assertFalse(responseWrapper.isSuccessful());
        assertNull(responseWrapper.getSuccessfulResponse());
        assertNotNull(responseWrapper.getErrorResponse());
    }

    @Test
    public void testGetItemsSuccessfulResponse() throws IOException {
        PageRequest request = new PageRequest();
        request.setPage(0);
        request.setPageSize(10);

        ItemPageResponseWrapper responseWrapper = api.getItems(request);
        System.out.println(responseWrapper);
        assertTrue(responseWrapper.isSuccessful());
        assertNotNull(responseWrapper.getSuccessfulResponse());
        assertFalse(responseWrapper.getSuccessfulResponse().getItems().isEmpty());
        assertNull(responseWrapper.getErrorResponse());
    }
}