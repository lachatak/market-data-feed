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
import org.apache.camel.Predicate;
import org.apache.commons.httpclient.HttpStatus;
import org.kaloz.datafeed.processor.api.instrument.InstrumentPriceRequestMessage;
import org.kaloz.messaging.app.client.JsonMessage;
import org.springframework.stereotype.Component;

import static org.kaloz.messaging.app.client.JmsMessagingConsts.MESSAGE_TYPE;

@Component
public class InstrumentPriceLoadPredicate implements Predicate{

    @Override
    public boolean matches(Exchange exchange) {
        JsonMessage message = exchange.getIn().getBody(JsonMessage.class);
        return exchange.getIn().getHeader(MESSAGE_TYPE, String.class).equals(InstrumentPriceRequestMessage.TYPE) && message.getErrorCode() == HttpStatus.SC_NOT_FOUND;
    }
}
