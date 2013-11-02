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

package org.kaloz.datafeed.yahoo.integration.test.steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class YahooFeedSteps extends AbstractYahooSteps {

    private static final Logger LOG = LoggerFactory.getLogger(YahooFeedSteps.class);

    @Given("^yahoo answers with$")
    public void yahoo_answers_with(List<String> prices) {
        setYahooAnswer(prices);
    }

    @When("^an instrument price load command message is sent to the yahoo IN queue with \"([^\"]*)\" replyTo and content$")
    public void an_instrument_price_load_command_message_is_sent_to_the_yahoo_IN_queue_with_replyTo_and_content(String replyTo, List<String> body) {
        triggerYahooDataLoad(replyTo, body.get(0));
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
