/*
 * Copyright 2013 Krisztian Lachata
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kaloz.datafeed.processor.infrastructure.acl;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import org.apache.commons.httpclient.HttpStatus;
import org.kaloz.datafeed.processor.api.instrument.*;
import org.kaloz.datafeed.processor.domain.instrument.model.InstrumentPrice;
import org.kaloz.datafeed.processor.domain.instrument.model.InstrumentPriceListRequest;
import org.kaloz.datafeed.processor.domain.instrument.model.InstrumentPriceRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProcessorConverter {

    public InstrumentPrice toInstrumentPrice(InstrumentPriceCommandMessage instrumentPriceCommandMessage) {

        InstrumentPrice instrumentPrice = new InstrumentPrice();
        instrumentPrice.setPrice(instrumentPriceCommandMessage.getPrice());
        instrumentPrice.setProvider(instrumentPriceCommandMessage.getProvider());
        instrumentPrice.setShortName(instrumentPriceCommandMessage.getShortName());
        instrumentPrice.setLongName(instrumentPriceCommandMessage.getLongName());
        instrumentPrice.setDate(instrumentPriceCommandMessage.getDate());

        return instrumentPrice;
    }

    public InstrumentPriceRequest toInstrumentPriceRequest(InstrumentPriceRequestMessage instrumentPriceRequestMessage) {
        return new InstrumentPriceRequest(instrumentPriceRequestMessage.getProvider(), instrumentPriceRequestMessage.getShortName());
    }

    public InstrumentPriceResponseMessage toInstrumentPriceResponseMessage(Optional<InstrumentPrice> priceOptional) {

        InstrumentPriceResponseMessage instrumentPriceResponseMessage = new InstrumentPriceResponseMessage();
        if(priceOptional.isPresent()){
            InstrumentPrice instrumentPrice = priceOptional.get();
            instrumentPriceResponseMessage.setPrice(getInstrumentPriceResource(instrumentPrice));
        } else {
            instrumentPriceResponseMessage.setErrorCode(HttpStatus.SC_NOT_FOUND);
        }

        return instrumentPriceResponseMessage;
    }

    public InstrumentPriceLoadCommandMessage toInstrumentPriceLoadCommandMessage(InstrumentPriceRequestMessage instrumentPriceRequestMessage) {
        return new InstrumentPriceLoadCommandMessage(Lists.newArrayList(instrumentPriceRequestMessage.getShortName()));
    }

    public InstrumentPriceListRequest toInstrumentPriceListRequest(InstrumentPriceListRequestMessage instrumentPriceListRequestMessage) {
        return new InstrumentPriceListRequest(instrumentPriceListRequestMessage.getProvider());
    }

    public InstrumentPriceListResponseMessage toInstrumentPriceListResponseMessage(List<InstrumentPrice> instrumentPriceList) {

        List<InstrumentPriceResource> priceResourceList = Lists.newArrayList();
        for(InstrumentPrice instrumentPrice: instrumentPriceList){
            priceResourceList.add(getInstrumentPriceResource(instrumentPrice));
        }
        return new InstrumentPriceListResponseMessage(priceResourceList);
    }

    private InstrumentPriceResource getInstrumentPriceResource(InstrumentPrice instrumentPrice){

        InstrumentPriceResource instrumentPriceResource = new InstrumentPriceResource();
        instrumentPriceResource.setProvider(instrumentPrice.getProvider());
        instrumentPriceResource.setShortName(instrumentPrice.getShortName());
        instrumentPriceResource.setLongName(instrumentPrice.getLongName());
        instrumentPriceResource.setPrice(instrumentPrice.getPrice());
        instrumentPriceResource.setDate(instrumentPrice.getDate());

        return instrumentPriceResource;
    }
}
