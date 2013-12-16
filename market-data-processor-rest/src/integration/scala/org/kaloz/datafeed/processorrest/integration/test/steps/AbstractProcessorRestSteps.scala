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

package org.kaloz.datafeed.processorrest.integration.test.steps

import java.lang.String
import org.springframework.beans.factory.annotation.Value
import org.apache.camel.builder.RouteBuilder
import org.kaloz.messaging.app.client.JmsMessagingConsts
import org.apache.camel.{Processor, ExchangePattern, Exchange}
import scala.Predef._
import javax.annotation.Resource
import org.kaloz.datafeed.processorrest.integration.test.stub.ProcessorStub
import org.kaloz.cucumber.camel.test.junit4.AbstractCucumberCamelTestSupport

class AbstractProcessorRestSteps extends AbstractCucumberCamelTestSupport {

  val instrumentOfProvider = "http://localhost:%s/processor/instrument/%s/%s?throwExceptionOnFailure=false"
  val allInstrumentOfProvider = "http://localhost:%s/processor/instrument/%s?throwExceptionOnFailure=false"

  @Value("${processor.in.queue}")
  var inQueue: String = _

  @Value("${processor-rest.port}")
  var restPort:String = _

  @Resource
  var processorStub:ProcessorStub = _

  def loadProviderSpecificPrices(provider:String, instrument:String) {
    loadInstruments(String.format(instrumentOfProvider, restPort, provider, instrument ))
  }

  def loadAllInstrumentsForProvider(provider:String) {
    loadInstruments(String.format(allInstrumentOfProvider, restPort, provider ))
  }

  private def loadInstruments(url:String) {
    addRunnables(new Runnable {
      def run {
        result = template.send(url, ExchangePattern.InOut, new Processor(){
          def process(exchange: Exchange) {
            exchange.getIn.setHeader(Exchange.HTTP_METHOD, "GET")
          }
        })
      }
    })
  }

  def registerProcessorResponses(request:String, response:(String, String)) {
    processorStub.registerProcessorResponses(request, response)
  }

  def clearRegisteredProcessorResponses {
    processorStub.clearRegisteredProcessorResponses
  }

  override def createRouteBuilder = {
    new RouteBuilder {
      def configure {

        onCompletion().log("Processor Stub sending back ${body}").to("log:test?showAll=true")

        from(inQueue).routeId("processorStub")
          .process(new Processor {
          def process(exchange: Exchange){
              processorStub.response(exchange.getIn.getBody(classOf[String])) match {
                case (response, jmsType) =>{
                  exchange.getIn.setBody(response)
                  exchange.getIn.setHeader(JmsMessagingConsts.MESSAGE_TYPE, jmsType)
                }
              }
            }
        })

      }
    }
  }
}
