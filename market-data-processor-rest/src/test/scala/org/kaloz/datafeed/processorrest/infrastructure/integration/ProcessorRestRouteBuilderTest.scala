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

package org.kaloz.datafeed.processorrest.infrastructure.integration

import org.apache.camel.test.junit4.CamelSpringTestSupport
import org.springframework.context.support.{ClassPathXmlApplicationContext, AbstractXmlApplicationContext}
import org.apache.camel.Exchange
import org.apache.camel.builder.RouteBuilder
import org.kaloz.messaging.app.client.JmsMessagingConsts
import org.kaloz.datafeed.processor.api.instrument.{InstrumentPriceListResponseMessage, InstrumentPriceResponseMessage, InstrumentPriceListRequestMessage, InstrumentPriceRequestMessage}
import org.junit.Test

class ProcessorRestRouteBuilderTest extends CamelSpringTestSupport{

  private val price = "{\"headers\":{\"errorCode\":404}}"
  private val priceList = "{\"headers\":{\"errorCode\":200},\"prices\":[{\"provider\":\"C\",\"shortName\":\"A\",\"longName\":\"AAA\",\"date\":1374706800000,\"price\":11.11},{\"provider\":\"C\",\"shortName\":\"B\",\"longName\":\"BBB\",\"date\":1374706800000,\"price\":11.11}]}";

  protected override def createApplicationContext: AbstractXmlApplicationContext =
    new ClassPathXmlApplicationContext("classpath:META-INF/spring/applicationContext-camel.xml", "classpath:META-INF/spring/applicationContext-jms.xml", "classpath:applicationContext-test.xml")

  protected override def createRouteBuilder: RouteBuilder = {

    return new RouteBuilder {
      def configure {

        onCompletion().log("Sent message ${body}")

        from("jms:queue:processor.in").routeId("processor")
          .choice
            .when(header(JmsMessagingConsts.MESSAGE_TYPE).isEqualTo(InstrumentPriceRequestMessage.TYPE))
              .setHeader(JmsMessagingConsts.MESSAGE_TYPE, simple(InstrumentPriceResponseMessage.TYPE))
              .setBody(simple(price))
              .endChoice
            .when(header(JmsMessagingConsts.MESSAGE_TYPE).isEqualTo(InstrumentPriceListRequestMessage.TYPE))
              .setHeader(JmsMessagingConsts.MESSAGE_TYPE, simple(InstrumentPriceListResponseMessage.TYPE))
              .setBody(simple(priceList))
              .endChoice
            .otherwise
              .to("log:unrecognisedJMSMessages?showAll=true").end

        from("direct:singleresult") setHeader(Exchange.HTTP_METHOD, simple("GET")) to ("http://localhost:9091/processor/instrument/YAHOO/FB?throwExceptionOnFailure=false") to ("mock:singleresult")
        from("direct:listresult") setHeader(Exchange.HTTP_METHOD, simple("GET")) to ("http://localhost:9091/processor/instrument/YAHOO?throwExceptionOnFailure=false") to ("mock:listresult")

      }
    }
  }

  @Test
  def testRoute {
    getMockEndpoint("mock:singleresult").expectedBodiesReceived (price)
    getMockEndpoint("mock:singleresult").expectedHeaderReceived (Exchange.HTTP_RESPONSE_CODE, 404)
    template.sendBody("direct:singleresult", "")

    getMockEndpoint("mock:listresult") expectedBodiesReceived (priceList)
    getMockEndpoint("mock:listresult").expectedHeaderReceived (Exchange.HTTP_RESPONSE_CODE, 200)
    template.sendBody("direct:listresult", "")

    assertMockEndpointsSatisfied()
  }
}
