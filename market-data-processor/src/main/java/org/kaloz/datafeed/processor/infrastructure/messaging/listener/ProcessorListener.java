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

package org.kaloz.datafeed.processor.infrastructure.messaging.listener;

import com.google.common.base.Optional;
import org.kaloz.datafeed.processor.api.instrument.*;
import org.kaloz.datafeed.processor.app.ProcessorService;
import org.kaloz.datafeed.processor.domain.instrument.model.InstrumentPrice;
import org.kaloz.datafeed.processor.domain.instrument.model.InstrumentPriceListRequest;
import org.kaloz.datafeed.processor.domain.instrument.model.InstrumentPriceRequest;
import org.kaloz.datafeed.processor.infrastructure.acl.ProcessorConverter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class ProcessorListener {

    @Resource
    private ProcessorService processorService;

    @Resource
    private ProcessorConverter processorConverter;

    public void processInstrumentPriceCommandMessage(InstrumentPriceCommandMessage instrumentPriceCommandMessage) {
        InstrumentPrice instrumentPrice = processorConverter.toInstrumentPrice(instrumentPriceCommandMessage);
        processorService.saveInstrumentPrice(instrumentPrice);
    }

    public InstrumentPriceResponseMessage processInstrumentPriceRequestMessage(InstrumentPriceRequestMessage instrumentPriceRequestMessage) {
        InstrumentPriceRequest instrumentPriceRequest = processorConverter.toInstrumentPriceRequest(instrumentPriceRequestMessage);
        Optional<InstrumentPrice> price = processorService.findByProviderAndShortName(instrumentPriceRequest);
        return processorConverter.toInstrumentPriceResponseMessage(price);
    }

    public InstrumentPriceListResponseMessage processInstrumentPriceListRequestMessage(InstrumentPriceListRequestMessage instrumentPriceListRequestMessage) {
        InstrumentPriceListRequest instrumentPriceListRequest = processorConverter.toInstrumentPriceListRequest(instrumentPriceListRequestMessage);
        List<InstrumentPrice> instrumentPrices = processorService.findByProvider(instrumentPriceListRequest);
        return processorConverter.toInstrumentPriceListResponseMessage(instrumentPrices);
    }
}
