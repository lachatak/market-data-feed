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

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelSpringTestSupport;
import org.junit.Test;
import org.kaloz.datafeed.processor.api.instrument.InstrumentPriceLoadCommandMessage;
import org.kaloz.datafeed.yahoo.integration.test.stub.YahooStub;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.kaloz.messaging.app.client.JmsMessagingConsts.ASYNC_REPLY_TO;
import static org.kaloz.messaging.app.client.JmsMessagingConsts.MESSAGE_TYPE;

public class YahooRouteBuilderTest extends CamelSpringTestSupport {

    private static final String yahooMockAnswer = "A,AAA,7/25/2013,1.1,2.2,3.3\r\nB,BBB,7/25/2013,11.11,22.22,33.33";
    private static final String request = "{ \"shortNames\": [\"A\", \"B\"] }";
    private static final String resultA = "{\"provider\":\"YAHOO\",\"shortName\":\"A\",\"longName\":\"AAA\",\"date\":1374706800000,\"price\":1.1}";
    private static final String resultB = "{\"provider\":\"YAHOO\",\"shortName\":\"B\",\"longName\":\"BBB\",\"date\":1374706800000,\"price\":11.11}";

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("classpath:META-INF/spring/applicationContext-camel.xml", "classpath:META-INF/spring/applicationContext-jms.xml", "classpath:applicationContext-test.xml");
    }

    protected RouteBuilder createRouteBuilder() throws Exception {

        final YahooStub yahooStub = applicationContext.getBean(YahooStub.class);
        yahooStub.setMockAnswer(yahooMockAnswer);

        return new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                from("jms:queue:bar").routeId("bar2mock").to("mock:result");
                from("jetty://http://localhost:9090/d/quotes.csv").routeId("mockHttpYahoo").bean(yahooStub, "consumeData");
            }
        };
    }

    @Test
    public void testRoute() throws Exception {

        getMockEndpoint("mock:result").expectedBodiesReceivedInAnyOrder(resultA, resultB, resultA, resultB);
        getMockEndpoint("mock:result").expectedMessageCount(4);

        Map<String, Object> headers = new HashMap<String, Object>();
        headers.put(MESSAGE_TYPE, InstrumentPriceLoadCommandMessage.TYPE);
        headers.put(ASYNC_REPLY_TO, "queue://bar");
        template.sendBodyAndHeaders("jms:queue:yahoo.in", request, headers);
        template.sendBodyAndHeaders("jms:queue:yahoo.in", request, headers);

        assertMockEndpointsSatisfied();
    }

    @Override
    protected CamelContext createCamelContext() throws Exception {
        return (CamelContext) applicationContext.getBean("yahooContext");
    }
}
