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
import org.junit.Test;
import org.kaloz.datafeed.processor.api.instrument.*;
import org.kaloz.datafeed.processor.domain.instrument.model.InstrumentPrice;
import org.kaloz.datafeed.processor.domain.instrument.model.InstrumentPriceListRequest;
import org.kaloz.datafeed.processor.domain.instrument.model.InstrumentPriceRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.Assert.*;

public class ProcessorConverterTest {

    private static final String date = "11/12/2013";

    private ProcessorConverter obj = new ProcessorConverter();

    @Test
    public void testToInstrumentPrice() throws ParseException{

        InstrumentPriceCommandMessage instrumentPriceCommandMessage = new InstrumentPriceCommandMessage();
        instrumentPriceCommandMessage.setProvider("A");
        instrumentPriceCommandMessage.setShortName("B");
        instrumentPriceCommandMessage.setLongName("C");
        instrumentPriceCommandMessage.setDate(new SimpleDateFormat("M/dd/yyyy").parse(date));
        instrumentPriceCommandMessage.setPrice(20d);

        InstrumentPrice result = obj.toInstrumentPrice(instrumentPriceCommandMessage);

        assertNotNull(result);

        assertEquals("A", result.getProvider());
        assertEquals("B", result.getShortName());
        assertEquals("C", result.getLongName());
        assertEquals("11/12/2013", new SimpleDateFormat("M/dd/yyyy").format(result.getDate()));
        assertEquals(20d, result.getPrice().doubleValue(), 0);
    }

    @Test
    public void testToInstrumentPriceRequest() throws ParseException{

        InstrumentPriceRequestMessage instrumentPriceRequestMessage = new InstrumentPriceRequestMessage();
        instrumentPriceRequestMessage.setProvider("A");
        instrumentPriceRequestMessage.setShortName("B");

        InstrumentPriceRequest result = obj.toInstrumentPriceRequest(instrumentPriceRequestMessage);

        assertNotNull(result);

        assertEquals("A", result.getProvider());
        assertEquals("B", result.getShortName());
    }

    @Test
    public void testToInstrumentPriceResponseMessageHasPrice() throws ParseException{

        InstrumentPrice instrumentPrice = new InstrumentPrice();
        instrumentPrice.setProvider("A");
        instrumentPrice.setShortName("B");
        instrumentPrice.setLongName("C");
        instrumentPrice.setDate(new SimpleDateFormat("M/dd/yyyy").parse(date));
        instrumentPrice.setPrice(20d);

        InstrumentPriceResponseMessage result = obj.toInstrumentPriceResponseMessage(Optional.of(instrumentPrice));

        assertNotNull(result);

        assertEquals("A", result.getPrice().getProvider());
        assertEquals("B", result.getPrice().getShortName());
        assertEquals("C", result.getPrice().getLongName());
        assertEquals("11/12/2013", new SimpleDateFormat("M/dd/yyyy").format(result.getPrice().getDate()));
        assertEquals(20d, result.getPrice().getPrice().doubleValue(), 0);
        assertEquals(HttpStatus.SC_OK, result.getErrorCode(), 0);
    }

    @Test
    public void testToInstrumentPriceResponseMessageNotFound() throws ParseException{

        Optional<InstrumentPrice> instrumentPriceOptional = Optional.absent();

        InstrumentPriceResponseMessage result = obj.toInstrumentPriceResponseMessage(instrumentPriceOptional);

        assertNotNull(result);

        assertNull(result.getPrice());
        assertEquals(HttpStatus.SC_NOT_FOUND, result.getErrorCode(), 0);
    }

    @Test
    public void testToInstrumentPriceLoadCommandMessage() throws ParseException{

        InstrumentPriceRequestMessage instrumentPriceRequestMessage = new InstrumentPriceRequestMessage();
        instrumentPriceRequestMessage.setProvider("A");
        instrumentPriceRequestMessage.setShortName("B");

        InstrumentPriceLoadCommandMessage result = obj.toInstrumentPriceLoadCommandMessage(instrumentPriceRequestMessage);

        assertNotNull(result);

        assertEquals(1, result.getShortNames().size());
        assertEquals("B", result.getShortNames().get(0));
    }

    @Test
    public void testToInstrumentPriceListRequest() throws ParseException{

        InstrumentPriceListRequestMessage instrumentPriceListRequestMessage = new InstrumentPriceListRequestMessage();
        instrumentPriceListRequestMessage.setProvider("A");

        InstrumentPriceListRequest result = obj.toInstrumentPriceListRequest(instrumentPriceListRequestMessage);

        assertNotNull(result);

        assertEquals("A", result.getProvider());
    }

    @Test
    public void testToInstrumentPriceListResponseMessage() throws ParseException{

        List<InstrumentPrice> instrumentPriceList = Lists.newArrayList(new InstrumentPrice(), new InstrumentPrice());

        InstrumentPriceListResponseMessage result = obj.toInstrumentPriceListResponseMessage(instrumentPriceList);

        assertNotNull(result);

        assertEquals(2, result.getPrices().size(), 0);
    }
}
