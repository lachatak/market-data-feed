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

package org.kaloz.datafeed.yahoo.integration.test.stub;

import org.apache.camel.Exchange;
import org.springframework.util.StringUtils;

import java.util.List;

public class YahooStub {

    private String messages;

    public void setMockAnswer(String messages) {
        this.messages = messages;
    }

    public void setMockAnswer(List<String> messages) {
        this.messages = StringUtils.collectionToDelimitedString(messages, "\r\n");
    }

    public void consumeData(Exchange exchange) {
        exchange.getOut().setBody(messages);
    }
}
