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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DynamicRouterToFeedsExpressionTest {

    @Mock
    private Properties providersQueue;

    @Mock
    private Exchange exchange;

    @Mock
    private Message message;

    @InjectMocks
    private DynamicRouterToFeedsExpression obj = new DynamicRouterToFeedsExpression();

    @Before
    public void init(){
        when(providersQueue.get(anyString())).thenReturn("A.in");
        when(message.getHeader(eq(ProcessorRouteBuilder.PROVIDER), eq(String.class))).thenReturn("A");
        when(exchange.getIn()).thenReturn(message);
    }

    @Test
    public void testFirstCall() throws Exception {
        when(exchange.getProperty(anyString(), eq(Boolean.class))).thenReturn(null);

        Object result = obj.evaluate(exchange, Object.class);

        assertEquals("A.in", result);
    }

    @Test
    public void testSecondCall() throws Exception {
        when(exchange.getProperty(anyString(), eq(Boolean.class))).thenReturn(Boolean.TRUE);

        Object result = obj.evaluate(exchange, Object.class);
        assertNull(result);
    }

}
