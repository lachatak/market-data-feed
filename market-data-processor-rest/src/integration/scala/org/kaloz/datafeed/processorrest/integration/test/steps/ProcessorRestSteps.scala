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

import cucumber.api.java.en.{Then, When, Given}
import java.util
import cucumber.api.DataTable

class ProcessorRestSteps extends AbstractProcessorRestSteps{

  @Given("^the processor data base is empty$")
  def the_processor_data_base_is_empty() {
    clearRegisteredProcessorResponses
  }

  @Given("^if the processor gets a request message it should send back the following response and JMSType$")
  def if_the_processor_gets_a_request_message_it_should_send_back_the_following_response_and_JMSType(args:DataTable ){
    registerProcessorResponses(args.asMaps.get(0).get("request"), (args.asMaps.get(0).get("response"), args.asMaps.get(0).get("JMSType")))
  }

  @When("^the client asks for the instrument price \"([^\"]*)\" from the provider \"([^\"]*)\"$")
  def the_client_asks_for_the_price_from_the_provider(price:String, provider:String){
    loadProviderSpecificPrices(provider, price)
  }

  @When("^the client asks for the all instrument prices from the provider \"([^\"]*)\"$")
  def the_client_asks_for_the_all_prices_from_the_provider(provider:String){
    loadAllInstrumentsForProvider(provider)
  }

  @Then("^the response message body should be$")
  def the_response_message_body_should_be(list:util.List[String]) {
    assertExpectedBodyReceived(list.get(0))
  }

  @Then("^the response message should have \"([^\"]*)\" header with the value \"([^\"]*)\"$")
  def the_response_message_should_have_header_with_the_value(expectedHeader:String, expectedHeaderValue:String)  {
    assertExpectedHeader(expectedHeader, expectedHeaderValue)
  }
}
