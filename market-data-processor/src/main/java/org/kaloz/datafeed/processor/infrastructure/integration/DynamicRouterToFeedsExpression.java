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
import org.apache.camel.Expression;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Properties;

@Component
public class DynamicRouterToFeedsExpression implements Expression {

    private static final String MESSAGE_SENT_TO_RECEPIENT = "sent";

    @Resource(name = "instrumentProviderQueues")
    private Properties providersQueue;

    @Override
    public <T> T evaluate(Exchange exchange, Class<T> type) {
        if(Boolean.TRUE.equals(exchange.getProperty(MESSAGE_SENT_TO_RECEPIENT, Boolean.class)))
        {
            return null;
        } else {
            exchange.setProperty(MESSAGE_SENT_TO_RECEPIENT, Boolean.TRUE);
            return (T)providersQueue.get(exchange.getIn().getHeader(ProcessorRouteBuilder.PROVIDER, String.class).toLowerCase());
        }
    }
}
