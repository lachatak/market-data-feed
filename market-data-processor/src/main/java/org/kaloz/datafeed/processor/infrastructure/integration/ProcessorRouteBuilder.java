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

package org.kaloz.datafeed.processor.infrastructure.integration;

import org.apache.camel.spring.SpringRouteBuilder;
import org.kaloz.datafeed.processor.infrastructure.messaging.listener.ProcessorListener;
import org.kaloz.messaging.infrastructure.integration.JsonConsumerProcessor;
import org.kaloz.messaging.infrastructure.integration.JsonProducerProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;

@Component
public class ProcessorRouteBuilder extends SpringRouteBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(ProcessorRouteBuilder.class);

    public static final String PROVIDER = "provider";

    @Value("${processor.in.queue}")
    private String inQueue;

    @Value("${dead.queue}")
    private String deadQueue;

    @Value("${processor.log.trace}")
    private boolean processorTrace;

    @Resource
    private ProcessorListener processorListener;

    @Resource
    private ExecutorService executorService;

    @Resource
    private JsonConsumerProcessor jsonConsumerProcessor;

    @Resource
    private JsonProducerProcessor jsonProducerProcessor;

    @Resource
    private InstrumentPriceLoadCommandMessageInitializator instrumentPriceLoadCommandMessageInitializator;

    @Resource
    private DynamicRouterToFeedsExpression dynamicRouterToFeedsExpression;

    @Resource
    private InstrumentPriceLoadPredicate instrumentPriceLoadPredicate;

    @Override
    public void configure() {

        errorHandler(deadLetterChannel(deadQueue));

        onCompletion().log("Message sent ${body}");

        from(inQueue).routeId("processor")
            .process(jsonConsumerProcessor)
            .bean(processorListener)
            .wireTap("direct:loadInstrumentPrice").executorService(executorService)
            .process(jsonProducerProcessor);

        from("direct:loadInstrumentPrice").routeId("loadInstrumentPrice")
            .filter(instrumentPriceLoadPredicate)
            .process(instrumentPriceLoadCommandMessageInitializator)
            .process(jsonProducerProcessor)
            .dynamicRouter(dynamicRouterToFeedsExpression);

        getContext().setTracing(processorTrace);
    }
}
