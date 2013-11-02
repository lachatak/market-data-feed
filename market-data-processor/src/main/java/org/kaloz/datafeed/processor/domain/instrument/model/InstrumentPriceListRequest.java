package org.kaloz.datafeed.processor.domain.instrument.model;

public class InstrumentPriceListRequest {

    private String provider;

    public InstrumentPriceListRequest(String provider) {
        this.provider = provider;
    }

    public String getProvider() {
        return provider;
    }
}
