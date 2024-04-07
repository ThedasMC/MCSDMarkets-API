package com.thedasmc.mcsdmarketsapi.request;

public class GetItemsRequest extends PageRequest {

    private String mcVersion;

    public String getMcVersion() {
        return mcVersion;
    }

    public void setMcVersion(String mcVersion) {
        this.mcVersion = mcVersion;
    }

    @Override
    public String toString() {
        return "GetItemsRequest{" +
            "mcVersion='" + mcVersion + '\'' +
            "} " + super.toString();
    }
}
