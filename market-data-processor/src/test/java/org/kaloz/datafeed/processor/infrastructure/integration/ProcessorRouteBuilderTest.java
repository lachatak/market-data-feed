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

import org.apache.camel.*;
import org.apache.camel.test.junit4.CamelSpringTestSupport;
import org.junit.Test;
import org.kaloz.datafeed.processor.api.instrument.InstrumentPriceCommandMessage;
import org.kaloz.datafeed.processor.api.instrument.InstrumentPriceRequestMessage;
import org.kaloz.datafeed.processor.api.instrument.InstrumentPriceResponseMessage;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.kaloz.messaging.app.client.JmsMessagingConsts.MESSAGE_TYPE;

public class ProcessorRouteBuilderTest extends CamelSpringTestSupport {

    private static final String priceCommand = "{\"provider\":\"YAHOO\",\"shortName\":\"B\",\"longName\":\"BBB\",\"date\":1374706800000,\"price\":11.11}";
    private static final String priceRequest = "{\"provider\":\"YAHOO\",\"shortName\":\"B\"}";
    private static final String price = "{\"headers\":{\"errorCode\":200},\"price\":{\"provider\":\"YAHOO\",\"shortName\":\"B\",\"longName\":\"BBB\",\"date\":1374706800000,\"price\":11.11}}";

    @Produce(uri = "jms:queue:processor.in")
    protected ProducerTemplate template;

    @Override
    protected AbstractXmlApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext("META-INF/spring/applicationContext-camel.xml", "META-INF/spring/applicationContext-jms.xml", "applicationContext-test.xml");
    }

    @Test
    public void testRoute() throws Exception {
        template.sendBodyAndHeader(priceCommand, MESSAGE_TYPE, InstrumentPriceCommandMessage.TYPE);

        Exchange result = template.send("jms:queue:processor.in", ExchangePattern.InOut, new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                exchange.getIn().setHeader(MESSAGE_TYPE, InstrumentPriceRequestMessage.TYPE);
                exchange.getIn().setBody(priceRequest);
            }
        });

        assertEquals("Expected result body should be equal", price, result.getOut().getBody(String.class));
        assertEquals("Expected JMSType header should be equal", InstrumentPriceResponseMessage.TYPE, result.getOut().getHeader(MESSAGE_TYPE));
    }

}
