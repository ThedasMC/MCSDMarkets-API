package com.thedasmc.mcsdmarketsapi.json.deserializer;

import com.google.gson.*;
import com.thedasmc.mcsdmarketsapi.enums.TransactionType;
import com.thedasmc.mcsdmarketsapi.response.impl.CreateTransactionResponse;

import java.lang.reflect.Type;
import java.time.Instant;
import java.util.UUID;

public class CreateTransactionResponseDeserializer implements JsonDeserializer<CreateTransactionResponse> {

    @Override
    public CreateTransactionResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        CreateTransactionResponse response = new CreateTransactionResponse();

        JsonObject root = json.getAsJsonObject();
        response.setTransactionId(root.get("transactionId").getAsLong());
        response.setPlayerId(getPlayerId(root));
        response.setTransactionType(getTransactionType(root));
        response.setMaterial(root.get("material").getAsString());
        response.setQuantity(root.get("quantity").getAsInt());
        response.setExecuted(getExecuted(root));

        return response;
    }

    private UUID getPlayerId(JsonObject root) {
        String playerIdString = root.get("playerId").getAsString();
        return UUID.fromString(playerIdString);
    }

    private TransactionType getTransactionType(JsonObject root) {
        String transactionTypeString = root.get("transactionType").getAsString();
        return TransactionType.valueOf(transactionTypeString);
    }

    private Instant getExecuted(JsonObject root) {
        return Instant.ofEpochMilli(root.get("executed").getAsLong());
    }
}
