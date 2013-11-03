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

import org.kaloz.messaging.infrastructure.serialization.JsonMessageSerializer
import org.kaloz.messaging.infrastructure.integration.JsonMessageRegistry
import org.apache.camel.{Message, Exchange}
import org.kaloz.messaging.app.client.{JmsMessagingConsts, JsonMessage}
import org.mockito.Matchers.{eq => mockEq}
import org.mockito.{MockitoAnnotations, InjectMocks, Mock}
import org.apache.camel.component.restlet.RestletConstants
import org.restlet.Response
import org.restlet.data.{MediaType, Status}
import org.specs2.mutable.SpecificationWithJUnit
import org.specs2.mock.Mockito
import org.specs2.mock.mockito.MockitoFunctions

class HTTPErrorCodeProcessorTest extends SpecificationWithJUnit with Mockito with MockitoFunctions {

  def fixture = new {
    val body = "{\"headers\":{\"errorCode\":200},\"price\":{\"provider\":\"A\",\"shortName\":\"B\",\"longName\":\"BBB\",\"date\":1374706800000,\"price\":11.11}}"

    @Mock var outMessage:Message = _
    @Mock var inMessage:Message = _
    @Mock var response:Response = _
    @Mock var exchange:Exchange = _
    @Mock var jsonMessage:TestMessage = _
    @Mock var jsonMessageSerializer:JsonMessageSerializer = _
    @Mock var jsonMessageRegistry:JsonMessageRegistry = _

    @InjectMocks val httpErrorCodeProcessor = new HTTPErrorCodeProcessor

    MockitoAnnotations.initMocks(this)

    inMessage.getBody(mockEq(classOf[String])) returns body
    inMessage.getHeader(mockEq(JmsMessagingConsts.MESSAGE_TYPE), mockEq(classOf[String])) returns "TestMessage"

    exchange.getIn returns inMessage
    exchange.getOut returns outMessage
    exchange.getProperty(mockEq(RestletConstants.RESTLET_RESPONSE), mockEq(classOf[Response])) returns response

    jsonMessage.getErrorCode returns 404

    jsonMessageSerializer.getObject(mockEq(body), mockEq(classOf[TestMessage])) returns jsonMessage

    doReturn(classOf[TestMessage]).when(jsonMessageRegistry).getRegisteredMessageClass(mockEq("TestMessage"))
  }

  "The out body should be set correctly" in {


   val f = fixture

    f.httpErrorCodeProcessor.process(f.exchange)

    //    http://blog.appfusions.com/scalatest-vs-specs2/
    //    https://gist.github.com/seratch/1414177
    //    http://etorreborre.github.io/specs2/guide/org.specs2.guide.Structure.html#Structure

    got {
      one(f.response).setStatus(mockEq(Status.valueOf(404)))
      one(f.response).setEntity(f.body, MediaType.APPLICATION_JSON)
      one(f.outMessage).setBody(f.response)
    }
  }

  class TestMessage extends JsonMessage{
    def getMessageType = "TEST"
  }
}
