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

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.commons.httpclient.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kaloz.datafeed.processor.api.instrument.InstrumentPriceListRequestMessage;
import org.kaloz.datafeed.processor.api.instrument.InstrumentPriceRequestMessage;
import org.kaloz.messaging.app.client.JmsMessagingConsts;
import org.kaloz.messaging.app.client.JsonMessage;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InstrumentPriceLoadPredicateTest {

    @Mock
    private Exchange exchange;

    @Mock
    private Message message;

    @Mock
    private JsonMessage jsonMessage;

    private InstrumentPriceLoadPredicate obj = new InstrumentPriceLoadPredicate();

    @Before
    public void init(){
        when(exchange.getIn()).thenReturn(message);
        when(message.getBody(eq(JsonMessage.class))).thenReturn(jsonMessage);
    }

    @Test
    public void testValidMessage(){
        when(message.getHeader(eq(JmsMessagingConsts.MESSAGE_TYPE), eq(String.class))).thenReturn(InstrumentPriceRequestMessage.TYPE);
        when(jsonMessage.getErrorCode()).thenReturn(HttpStatus.SC_NOT_FOUND);

        boolean result = obj.matches(exchange);

        assertEquals(true, result);
    }

    @Test
    public void testNotRelevantMessageMessageType(){
        when(message.getHeader(eq(JmsMessagingConsts.MESSAGE_TYPE), eq(String.class))).thenReturn(InstrumentPriceListRequestMessage.TYPE);
        when(jsonMessage.getErrorCode()).thenReturn(HttpStatus.SC_NOT_FOUND);

        boolean result = obj.matches(exchange);

        assertEquals(false, result);
    }

    @Test
    public void testNotFoundHttpCode(){
        when(message.getHeader(eq(JmsMessagingConsts.MESSAGE_TYPE), eq(String.class))).thenReturn(InstrumentPriceRequestMessage.TYPE);
        when(jsonMessage.getErrorCode()).thenReturn(HttpStatus.SC_OK);

        boolean result = obj.matches(exchange);

        assertEquals(false, result);
    }
}
