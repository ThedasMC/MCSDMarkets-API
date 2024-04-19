package com.thedasmc.mcsdmarketsapi.request;

public class ItemPageRequest extends PageRequest {

    private String mcVersion;

    public String getMcVersion() {
        return mcVersion;
    }

    /**
     * Sets the MC version of this server, so it will return compatible items
     * @param mcVersion The version string such as 1.20.4, 1.20, etc.
     */
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
