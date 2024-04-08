package com.thedasmc.mcsdmarketsapi.json.deserializer;

import com.google.gson.*;
import com.thedasmc.mcsdmarketsapi.response.impl.ItemPageResponse;
import com.thedasmc.mcsdmarketsapi.response.impl.ItemResponse;
import com.thedasmc.mcsdmarketsapi.response.impl.PageResponse;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

public class ItemPageResponseDeserializer implements JsonDeserializer<ItemPageResponse> {

    @Override
    public ItemPageResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject root = json.getAsJsonObject();

        JsonObject pageInfoJson = root.getAsJsonObject("pageInfo");
        PageResponse pageInfo = context.deserialize(pageInfoJson, PageResponse.class);

        JsonArray itemsArray = root.getAsJsonArray("items");
        List<ItemResponse> items = itemsArray.asList().stream()
            .map(jsonElement -> (ItemResponse) context.deserialize(jsonElement, ItemResponse.class))
            .collect(Collectors.toList());

        ItemPageResponse itemPageResponse = new ItemPageResponse();
        itemPageResponse.setPageInfo(pageInfo);
        itemPageResponse.setItems(items);

        return itemPageResponse;
    }
}
