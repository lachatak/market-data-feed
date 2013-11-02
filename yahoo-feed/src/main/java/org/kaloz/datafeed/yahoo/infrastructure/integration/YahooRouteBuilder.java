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

package org.kaloz.datafeed.yahoo.infrastructure.integration;

import org.apache.camel.Exchange;
import org.apache.camel.component.jms.JmsConstants;
import org.apache.camel.model.dataformat.BindyType;
import org.apache.camel.spring.SpringRouteBuilder;
import org.kaloz.datafeed.processor.api.instrument.InstrumentPriceCommandMessage;
import org.kaloz.datafeed.yahoo.infrastructure.integration.dataformat.YahooCSV;
import org.kaloz.messaging.infrastructure.integration.JsonConsumerProcessor;
import org.kaloz.messaging.infrastructure.integration.JsonProducerProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;

import static org.kaloz.messaging.app.client.JmsMessagingConsts.ASYNC_REPLY_TO;

@Component
public class YahooRouteBuilder extends SpringRouteBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(YahooRouteBuilder.class);

    @Value("${yahoo.in.queue}")
    private String inQueue;

    @Value("${dead.queue}")
    private String deadQueue;

    @Value("${yahoo.log.trace}")
    private boolean yahooTrace;

    @Resource
    private ExecutorService executorService;

    @Resource
    private JsonConsumerProcessor jsonConsumerProcessor;

    @Resource
    private JsonProducerProcessor jsonProducerProcessor;

    @Override
    public void configure() {

        errorHandler(deadLetterChannel(deadQueue));

        from(inQueue).routeId("yahoo")
                .process(jsonConsumerProcessor)
                .convertBodyTo(String.class)
                .setHeader(Exchange.HTTP_METHOD, constant("GET"))
                .setHeader(Exchange.HTTP_URI, body(String.class))
                .to("http://dummy")
                .to("direct:splitter");

        from("direct:splitter").routeId("splitter")
                .unmarshal().bindy(BindyType.Csv, YahooCSV.class)
                .split(body()).streaming().executorService(executorService)
                .to("direct:reply");

        from("direct:reply").routeId("reply")
                .convertBodyTo(InstrumentPriceCommandMessage.class)
                .setHeader(JmsConstants.JMS_DESTINATION, header(ASYNC_REPLY_TO))
                .process(jsonProducerProcessor)
                .to("jms:queue:asyncreplydummy");

        getContext().setTracing(yahooTrace);
    }

}
