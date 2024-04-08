package com.thedasmc.mcsdmarketsapi.json.deserializer;

import com.google.gson.*;
import com.thedasmc.mcsdmarketsapi.response.impl.ItemResponse;

import java.lang.reflect.Type;
import java.math.BigDecimal;

public class ItemResponseDeserializer implements JsonDeserializer<ItemResponse> {

    @Override
    public ItemResponse deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject obj = jsonElement.getAsJsonObject();

        ItemResponse itemResponse = new ItemResponse();
        itemResponse.setMaterial(obj.get("material").getAsString());
        itemResponse.setBasePrice(BigDecimal.valueOf(obj.get("basePrice").getAsDouble()));
        itemResponse.setCurrentPrice(BigDecimal.valueOf(obj.get("currentPrice").getAsDouble()));
        itemResponse.setInventory(obj.get("inventory").getAsInt());
        itemResponse.setVersionAdded(obj.get("versionAdded").getAsString());

        return itemResponse;
    }
}
