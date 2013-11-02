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

import javax.annotation.Resource
import org.kaloz.messaging.infrastructure.serialization.JsonMessageSerializer
import org.kaloz.messaging.infrastructure.integration.JsonMessageRegistry
import org.springframework.stereotype.Component
import org.apache.camel.{Processor, Exchange}
import org.kaloz.messaging.app.client.{JsonMessage, JmsMessagingConsts}
import org.apache.camel.component.restlet.RestletConstants
import org.restlet.Response
import org.restlet.data.{MediaType, Status}

@Component
class HTTPErrorCodeProcessor extends Processor {

  @Resource
  var jsonMessageSerializer: JsonMessageSerializer = _

  @Resource
  var jsonMessageRegistry: JsonMessageRegistry = _

  override def process(exchange:Exchange) {
    val messageType = jsonMessageRegistry.getRegisteredMessageClass(exchange.getIn.getHeader(JmsMessagingConsts.MESSAGE_TYPE, classOf[String]))
    val body = exchange.getIn.getBody(classOf[String])
    val jsonMessage:JsonMessage = jsonMessageSerializer.getObject(body, messageType)

    val response = exchange.getProperty(RestletConstants.RESTLET_RESPONSE, classOf[Response])
    response.setStatus(Status.valueOf(jsonMessage.getErrorCode))
    response.setEntity(body, MediaType.APPLICATION_JSON)

    exchange.getOut.setBody(response)
  }
}
