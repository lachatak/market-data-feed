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

package org.kaloz.datafeed.processor.integration.test.steps;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.kaloz.datafeed.processor.api.instrument.InstrumentPriceListRequestMessage;
import org.kaloz.datafeed.processor.api.instrument.InstrumentPriceRequestMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ProcessorSteps extends AbstractProcessorSteps {

    private static final Logger LOG = LoggerFactory.getLogger(ProcessorSteps.class);

    @When("^instrument price command messages arrive with following content$")
    public void instrument_price_command_messages_arrive_with_following_content(List<String> prices) {
        sendPriceMessages(prices);
    }

    @When("^the client asks for the following instrument price$")
    public void the_client_asks_for_the_following_instrument_price(List<String> prices) {
        sendMessages(prices.get(0), InstrumentPriceRequestMessage.TYPE);
    }

    @Then("^the response message should have \"([^\"]*)\" header with the value \"([^\"]*)\"$")
    public void the_response_message_should_have_header_with_the_value(String expectedHeader, String expectedHeaderValue) {
        assertExpectedHeader(expectedHeader, expectedHeaderValue);
    }

    @Then("^the response message bodies should exactly match with$")
    public void the_response_message_bodies_should_exactly_match_with(List<String> expectedBodies) {
        assertExpectedBodyReceived(expectedBodies.get(0));
    }

    @When("^instrument price list request message arrives with the following content$")
    public void instrument_price_list_request_message_arrives_with_the_following_content(List<String> listRequest) {
        sendMessages(listRequest.get(0), InstrumentPriceListRequestMessage.TYPE);
    }

    //TODO COMMON
    @Then("^the \"([^\"]*)\" queue should contain (\\d+) messages$")
    public void the_queue_should_contain_messages(String expectedQueue, int expectedCount) {
        assertExpectedMessageCount(expectedQueue, expectedCount);
    }

    @Then("^the message in the \"([^\"]*)\" queue should have \"([^\"]*)\" header with the value \"([^\"]*)\"$")
    public void the_message_in_the_queue_should_have_header_with_the_value(String expectedQueue, String expectedHeader, String expectedHeaderValue) {
        assertExpectedHeader(expectedQueue, expectedHeader, expectedHeaderValue);
    }

    @Then("^the message bodies in the \"([^\"]*)\" queue should exactly match with$")
    public void the_message_bodies_in_the_given_queue_should_exactly_match_with(String expectedQueue, List<String> expectedMessages) {
        assertExpectedBodiesReceived(expectedQueue, expectedMessages);
    }
}
