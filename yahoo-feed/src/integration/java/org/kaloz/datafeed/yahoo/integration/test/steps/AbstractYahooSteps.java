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

package org.kaloz.datafeed.yahoo.integration.test.steps;

import org.apache.camel.builder.RouteBuilder;
import org.kaloz.cucumber.camel.test.junit4.AbstractCucumberCamelTestSupport;
import org.kaloz.datafeed.processor.api.instrument.InstrumentPriceLoadCommandMessage;
import org.kaloz.datafeed.yahoo.integration.test.stub.YahooStub;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.kaloz.messaging.app.client.JmsMessagingConsts.ASYNC_REPLY_TO;
import static org.kaloz.messaging.app.client.JmsMessagingConsts.MESSAGE_TYPE;

public abstract class AbstractYahooSteps extends AbstractCucumberCamelTestSupport {

    @Value("${yahoo.in.queue}")
    private String inQueue;

    @Value("${dead.queue}")
    private String deadQueue;

    @Resource
    private YahooStub yahooStub;

    protected void triggerYahooDataLoad(final String replyTo, final String body) {
        addRunnables(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> headers = new HashMap<String, Object>();
                headers.put(MESSAGE_TYPE, InstrumentPriceLoadCommandMessage.TYPE);
                headers.put(ASYNC_REPLY_TO, replyTo);
                template.sendBodyAndHeaders(inQueue, body, headers);
            }
        });
    }

    protected void setYahooAnswer(List<String> prices) {
        yahooStub.setMockAnswer(prices);
    }

    public RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {

            @Override
            public void configure() {
                from("jms:queue:reply").routeId("reply2mock").log("Arrived ${body}").to("mock://reply");
                from(deadQueue).routeId("dead2mock").log("Arrived to dead ${body}").to("mock://dead");
            }
        };
    }

    protected String getContextName(){
        return "yahooContext";
    }
}
