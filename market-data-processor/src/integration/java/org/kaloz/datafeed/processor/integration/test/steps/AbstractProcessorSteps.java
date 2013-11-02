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

package org.kaloz.datafeed.processor.integration.test.steps;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.kaloz.cucumber.camel.test.junit4.AbstractCucumberCamelTestSupport;
import org.kaloz.datafeed.processor.api.instrument.InstrumentPriceCommandMessage;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

import static org.kaloz.messaging.app.client.JmsMessagingConsts.MESSAGE_TYPE;

public abstract class AbstractProcessorSteps extends AbstractCucumberCamelTestSupport {

    @Value("${processor.in.queue}")
    private String inQueue;

    @Value("${dead.queue}")
    private String deadQueue;

    protected void sendPriceMessages(List<String> prices) {
        for (final String message : prices) {
            addRunnables(new Runnable() {
                @Override
                public void run() {
                    template.sendBodyAndHeader(inQueue, message, MESSAGE_TYPE, InstrumentPriceCommandMessage.TYPE);
                }
            });
        }
    }

    protected void sendMessages(final String body, final String type) {
        addRunnables(new Runnable() {
            @Override
            public void run() {
                result = template.send(inQueue, ExchangePattern.InOut, new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        exchange.getIn().setHeader(MESSAGE_TYPE, type);
                        exchange.getIn().setBody(body);
                    }
                });
            }
        });
    }

    public RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {

            @Override
            public void configure() {
                from("jms:queue:A.in").routeId("A2mock").log("Arrived ${body}").to("mock://A.in");
                from(deadQueue).routeId("dead2mock").log("Arrived to dead ${body}").to("mock://dead");
            }
        };
    }
}
