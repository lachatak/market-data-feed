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

package org.kaloz.datafeed.processor.app;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kaloz.datafeed.processor.domain.instrument.model.InstrumentPrice;
import org.kaloz.datafeed.processor.domain.instrument.model.InstrumentPriceListRequest;
import org.kaloz.datafeed.processor.domain.instrument.model.InstrumentPriceRequest;
import org.kaloz.datafeed.processor.domain.instrument.service.InstrumentPriceRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProcessorServiceTest {

    @Mock
    private InstrumentPriceRepository instrumentPriceRepository;

    @InjectMocks
    private ProcessorService obj = new ProcessorService();

    @Test
    public void testSaveInstrumentPrice(){
        InstrumentPrice instrumentPrice = new InstrumentPrice();
        obj.saveInstrumentPrice(instrumentPrice);

        verify(instrumentPriceRepository, times(1)).save(eq(instrumentPrice));
    }

    @Test
    public void testFindByProviderAndShortName(){
        InstrumentPrice instrumentPrice = new InstrumentPrice();
        when(instrumentPriceRepository.findByProviderAndShortName(eq("A"), eq("B"))).thenReturn(instrumentPrice);

        InstrumentPriceRequest instrumentPriceRequest = new InstrumentPriceRequest("A", "B");
        Optional<InstrumentPrice> result = obj.findByProviderAndShortName(instrumentPriceRequest);

        assertEquals(instrumentPrice, result.get());
    }

    @Test
    public void testFindByProvider(){
        List<InstrumentPrice> instrumentPrices = Lists.newArrayList(new InstrumentPrice(), new InstrumentPrice());
        when(instrumentPriceRepository.findByProvider(eq("A"))).thenReturn(instrumentPrices);

        InstrumentPriceListRequest instrumentPriceListRequest = new InstrumentPriceListRequest("A");
        List<InstrumentPrice> result = obj.findByProvider(instrumentPriceListRequest);

        assertEquals(2, result.size());
    }
}
