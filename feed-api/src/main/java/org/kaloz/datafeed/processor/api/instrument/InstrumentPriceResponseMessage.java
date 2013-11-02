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
import org.kaloz.messaging.app.client.JsonResponseMessage;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class InstrumentPriceResponseMessage extends JsonResponseMessage {

    public static final String TYPE = "InstrumentPriceResponseMessage";

    private InstrumentPriceResource price;

    public String getMessageType(){
        return TYPE;
    }

    public InstrumentPriceResource getPrice() {
        return price;
    }

    public void setPrice(InstrumentPriceResource instrumentPriceResource) {
        this.price = instrumentPriceResource;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }}
