package com.thedasmc.mcsdmarketsapi;

import com.thedasmc.mcsdmarketsapi.enums.TransactionType;
import com.thedasmc.mcsdmarketsapi.request.CreateTransactionRequest;
import com.thedasmc.mcsdmarketsapi.request.GetItemsRequest;
import com.thedasmc.mcsdmarketsapi.response.wrapper.CreateTransactionResponseWrapper;
import com.thedasmc.mcsdmarketsapi.response.wrapper.GetItemsResponseWrapper;
import com.thedasmc.mcsdmarketsapi.response.wrapper.PriceResponseWrapperWrapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class MCSDMarketsAPITest {

    //Change this to use your own real api key
    private final MCSDMarketsAPI api = new MCSDMarketsAPI("abc123", true);

    @Test
    public void testGetPriceSuccessfulResponse() throws IOException {
        PriceResponseWrapperWrapper responseWrapper = api.getPrice("DIAMOND");
        System.out.println(responseWrapper);
        assertTrue(responseWrapper.isSuccessful());
        assertNotNull(responseWrapper.getSuccessfulResponse());
        assertTrue(responseWrapper.getSuccessfulResponse().compareTo(BigDecimal.ZERO) > 0);
        assertNull(responseWrapper.getErrorResponse());
    }

    @Test
    public void testGetPriceUnsuccessfulResponse() throws IOException {
        PriceResponseWrapperWrapper responseWrapper = api.getPrice("INVALID_MATERIAL");
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
        GetItemsRequest request = new GetItemsRequest();
        request.setPage(0);
        request.setPageSize(10);
        request.setMcVersion("1.0");

        GetItemsResponseWrapper responseWrapper = api.getItems(request);
        System.out.println(responseWrapper);
        assertTrue(responseWrapper.isSuccessful());
        assertNotNull(responseWrapper.getSuccessfulResponse());
        assertNull(responseWrapper.getErrorResponse());
    }

    @Test
    public void testGetItemsUnsuccessfulResponse() throws IOException {
        GetItemsRequest request = new GetItemsRequest();
        request.setPage(0);
        request.setPageSize(10);
        request.setMcVersion("1");//Invalid version string

        GetItemsResponseWrapper responseWrapper = api.getItems(request);
        System.out.println(responseWrapper);
        assertFalse(responseWrapper.isSuccessful());
        assertNull(responseWrapper.getSuccessfulResponse());
        assertNotNull(responseWrapper.getErrorResponse());
    }
}