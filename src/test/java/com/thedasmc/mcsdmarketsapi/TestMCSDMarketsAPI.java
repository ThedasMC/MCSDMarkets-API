package com.thedasmc.mcsdmarketsapi;

import java.net.HttpURLConnection;

public class TestMCSDMarketsAPI extends MCSDMarketsAPI {

    private final HttpURLConnection connection;

    public TestMCSDMarketsAPI(String apiKey, String mcVersion, HttpURLConnection connection) {
        super(apiKey, mcVersion);
        this.connection = connection;
    }

    @Override
    HttpURLConnection getHttpConnection(String urlString) {
        return connection;
    }
}
