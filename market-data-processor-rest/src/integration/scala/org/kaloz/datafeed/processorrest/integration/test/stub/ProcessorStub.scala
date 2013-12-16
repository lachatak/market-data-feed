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

package org.kaloz.datafeed.processorrest.integration.test.stub


class ProcessorStub {

  var requestResponses = Map.empty[String, (String, String)]

  def registerProcessorResponses(request: String, response: (String, String)) {
    requestResponses = requestResponses + (request -> response)
  }

  def response(request: String) = requestResponses(request)

  def clearRegisteredProcessorResponses {
    requestResponses = Map.empty[String, (String, String)]
  }

}


