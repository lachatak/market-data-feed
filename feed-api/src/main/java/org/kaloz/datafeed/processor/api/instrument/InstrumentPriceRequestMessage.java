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

package org.kaloz.datafeed.processor.api.instrument;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.kaloz.messaging.app.client.JsonMessage;
import org.springframework.stereotype.Component;

@Component
public class InstrumentPriceRequestMessage extends JsonMessage {

    public static final String TYPE = "InstrumentPriceRequestMessage";

    private String provider;
    private String shortName;

    public InstrumentPriceRequestMessage() {
    }

    public InstrumentPriceRequestMessage(String provider, String shortName) {
        this.provider = provider;
        this.shortName = shortName;
    }

    public String getMessageType(){
        return TYPE;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
