package com.thedasmc.mcsdmarketsapi.json.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.thedasmc.mcsdmarketsapi.request.CreateTransactionRequest;

import java.lang.reflect.Type;

public class CreateTransactionRequestSerializer implements JsonSerializer<CreateTransactionRequest> {

    @Override
    public JsonElement serialize(CreateTransactionRequest src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject root = new JsonObject();
        root.addProperty("playerId", src.getPlayerId() == null ? null : src.getPlayerId().toString());
        root.addProperty("transactionType", src.getTransactionType() == null ? null : src.getTransactionType().name());
        root.addProperty("material", src.getMaterial());
        root.addProperty("quantity", src.getQuantity());

        return root;
    }
}
