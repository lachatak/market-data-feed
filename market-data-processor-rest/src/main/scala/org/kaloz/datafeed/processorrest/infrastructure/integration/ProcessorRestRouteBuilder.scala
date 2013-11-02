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

import org.springframework.stereotype.Component
import org.slf4j.{LoggerFactory, Logger}
import org.apache.camel._
import builder.RouteBuilder

import org.springframework.beans.factory.annotation.Value
import javax.annotation.Resource
import org.kaloz.datafeed.processorrest.infrastructure.acl.ProcessorRestConverter
import org.kaloz.messaging.infrastructure.integration.JsonProducerProcessor
import org.apache.camel.component.restlet.RestletConstants

@Component
class ProcessorRestRouteBuilder extends RouteBuilder {

  val LOG: Logger = LoggerFactory.getLogger(classOf[ProcessorRestRouteBuilder])

  @Value("${processor.in.queue}")
  var processorInQueue: String = _

  @Value("${processor-rest.port}")
  var restPort:String = _

  @Value("${processor-rest.log.trace}")
  var processorRestTrace: Boolean = _

  @Resource
  var processorRestConverter: ProcessorRestConverter = _

  @Resource
  var jsonProducerProcessor: JsonProducerProcessor = _

  @Resource
  var httpErrorCodeProcessor: HTTPErrorCodeProcessor = _

  override def configure {

    onCompletion().log("Sent message ${body}")

    interceptSendToEndpoint(processorInQueue) process (jsonProducerProcessor)

    from("restlet:http://0.0.0.0:"+restPort+"/processor/instrument/{provider}/{instrument}?restletMethod=get")
      .routeId("getInstrument")
      .log("Provider ${header.provider} - instrumentId ${header.instrument}")
      .transform(method(processorRestConverter, "toInstrumentPriceRequestMessage"))
      .to("direct:processJmsMessage")

    from("restlet:http://0.0.0.0:"+restPort+"/processor/instrument/{provider}?restletMethod=get")
      .routeId("getAllInstruments")
      .log("Provider ${header.provider} - All instruments")
      .transform(method(processorRestConverter, "toInstrumentPriceListRequestMessage"))
      .to("direct:processJmsMessage")

    from("direct:processJmsMessage")
      .log("Arrived from processor ${body}")
      .routeId("processJmsMessage")
      .setProperty(RestletConstants.RESTLET_RESPONSE, header(RestletConstants.RESTLET_RESPONSE))
      .to(ExchangePattern.OutIn, processorInQueue)
      .process(httpErrorCodeProcessor)

    getContext.setTracing(processorRestTrace)
  }

}
