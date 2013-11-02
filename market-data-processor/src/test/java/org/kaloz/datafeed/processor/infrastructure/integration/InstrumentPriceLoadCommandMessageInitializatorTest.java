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

package org.kaloz.datafeed.processor.infrastructure.integration;

import com.google.common.collect.Lists;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultMessage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kaloz.datafeed.processor.api.instrument.InstrumentPriceLoadCommandMessage;
import org.kaloz.datafeed.processor.api.instrument.InstrumentPriceRequestMessage;
import org.kaloz.datafeed.processor.infrastructure.acl.ProcessorConverter;
import org.kaloz.messaging.app.client.JmsMessagingConsts;
import org.kaloz.messaging.infrastructure.integration.JsonConsumerProcessor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InstrumentPriceLoadCommandMessageInitializatorTest {

    @Mock
    private Exchange exchange;

    @Mock
    private ProcessorConverter processorConverter;

    @InjectMocks
    private InstrumentPriceLoadCommandMessageInitializator obj = new InstrumentPriceLoadCommandMessageInitializator();

    @Before
    public void init(){
        ReflectionTestUtils.setField(obj, "inQueue", "in.queue");
        when(exchange.getProperty(eq(JsonConsumerProcessor.ORIG_IN_BODY), eq(InstrumentPriceRequestMessage.class))).thenReturn(new InstrumentPriceRequestMessage("A", "B"));
    }

    @Test
    public void testInitialization() throws Exception {
        Message message = new DefaultMessage();
        when(exchange.getIn()).thenReturn(message);
        InstrumentPriceLoadCommandMessage instrumentPriceLoadCommandMessage = new InstrumentPriceLoadCommandMessage(Lists.newArrayList("B"));
        when(processorConverter.toInstrumentPriceLoadCommandMessage(any(InstrumentPriceRequestMessage.class))).thenReturn(instrumentPriceLoadCommandMessage);

        obj.process(exchange);

        assertEquals("A", message.getHeader(ProcessorRouteBuilder.PROVIDER));
        assertEquals("in.queue", message.getHeader(JmsMessagingConsts.ASYNC_REPLY_TO));
        assertEquals(instrumentPriceLoadCommandMessage, message.getBody(InstrumentPriceLoadCommandMessage.class));
    }
}
