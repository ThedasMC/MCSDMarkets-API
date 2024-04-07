package com.thedasmc.mcsdmarketsapi.json.deserializer;

import com.google.gson.*;
import com.thedasmc.mcsdmarketsapi.response.impl.GetItemsResponse;
import com.thedasmc.mcsdmarketsapi.response.impl.PageResponse;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

public class GetItemsResponseDeserializer implements JsonDeserializer<GetItemsResponse> {

    @Override
    public GetItemsResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject root = json.getAsJsonObject();

        JsonObject pageInfoJson = root.getAsJsonObject("pageInfo");
        PageResponse pageInfo = context.deserialize(pageInfoJson, PageResponse.class);

        JsonArray itemsArray = root.getAsJsonArray("items");
        List<GetItemsResponse.ItemResponse> items = new LinkedList<>();

        itemsArray.forEach(jsonElement -> {
            JsonObject obj = jsonElement.getAsJsonObject();

            GetItemsResponse.ItemResponse itemResponse = new GetItemsResponse.ItemResponse();
            itemResponse.setMaterial(obj.get("material").getAsString());
            itemResponse.setBasePrice(BigDecimal.valueOf(obj.get("basePrice").getAsDouble()));
            itemResponse.setCurrentPrice(BigDecimal.valueOf(obj.get("currentPrice").getAsDouble()));
            itemResponse.setInventory(obj.get("inventory").getAsInt());
            itemResponse.setVersionAdded(obj.get("versionAdded").getAsString());

            items.add(itemResponse);
        });

        GetItemsResponse getItemsResponse = new GetItemsResponse();
        getItemsResponse.setPageInfo(pageInfo);
        getItemsResponse.setItems(items);

        return getItemsResponse;
    }
}
