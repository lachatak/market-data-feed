package org.kaloz.datafeed.processor.domain.instrument.model;

public class InstrumentPriceRequest {

    private String provider;
    private String shortName;

    public InstrumentPriceRequest(String provider, String shortName) {
        this.shortName = shortName;
        this.provider = provider;
    }

    public String getShortName() {
        return shortName;
    }

    public String getProvider() {
        return provider;
    }
}
