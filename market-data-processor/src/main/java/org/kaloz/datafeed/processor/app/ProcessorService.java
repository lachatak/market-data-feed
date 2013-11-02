/*
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

package org.kaloz.datafeed.processor.app;

import com.google.common.base.Optional;
import org.kaloz.datafeed.processor.domain.instrument.model.InstrumentPrice;
import org.kaloz.datafeed.processor.domain.instrument.model.InstrumentPriceListRequest;
import org.kaloz.datafeed.processor.domain.instrument.model.InstrumentPriceRequest;
import org.kaloz.datafeed.processor.domain.instrument.service.InstrumentPriceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class ProcessorService {

    private static final Logger LOG = LoggerFactory.getLogger(ProcessorService.class);

    @Resource
    private InstrumentPriceRepository instrumentPriceRepository;

    public void saveInstrumentPrice(InstrumentPrice instrumentPrice) {
        instrumentPriceRepository.save(instrumentPrice);
    }

    public Optional<InstrumentPrice> findByProviderAndShortName(InstrumentPriceRequest instrumentPriceRequest) {
        return Optional.fromNullable(instrumentPriceRepository.findByProviderAndShortName(instrumentPriceRequest.getProvider(), instrumentPriceRequest.getShortName()));
    }

    public List<InstrumentPrice> findByProvider(InstrumentPriceListRequest instrumentPriceListRequest) {
        return instrumentPriceRepository.findByProvider(instrumentPriceListRequest.getProvider());
    }
}
