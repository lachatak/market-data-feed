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
import org.apache.camel.Processor;
import org.kaloz.datafeed.processor.api.instrument.InstrumentPriceRequestMessage;
import org.kaloz.datafeed.processor.infrastructure.acl.ProcessorConverter;
import org.kaloz.messaging.app.client.JmsMessagingConsts;
import org.kaloz.messaging.infrastructure.integration.JsonConsumerProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class InstrumentPriceLoadCommandMessageInitializator implements Processor{

    @Value("${processor.in.queue}")
    private String inQueue;

    @Resource
    private ProcessorConverter processorConverter;

    @Override
    public void process(Exchange exchange) throws Exception {
        Message message = exchange.getIn();
        InstrumentPriceRequestMessage instrumentPriceRequestMessage = exchange.getProperty(JsonConsumerProcessor.ORIG_IN_BODY, InstrumentPriceRequestMessage.class);

        message.setHeader(ProcessorRouteBuilder.PROVIDER, instrumentPriceRequestMessage.getProvider());
        message.setHeader(JmsMessagingConsts.ASYNC_REPLY_TO, inQueue);

        message.setBody(processorConverter.toInstrumentPriceLoadCommandMessage(instrumentPriceRequestMessage));
    }
}
