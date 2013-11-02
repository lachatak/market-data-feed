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

package org.kaloz.datafeed.processor.api.instrument;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.kaloz.messaging.app.client.JsonMessage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InstrumentPriceLoadCommandMessage extends JsonMessage{

    public static final String TYPE = "InstrumentPriceLoadCommandMessage";

    private List<String> shortNames;

    public InstrumentPriceLoadCommandMessage(){
    }

    public InstrumentPriceLoadCommandMessage(List<String> shortNames) {
        this.shortNames = shortNames;
    }

    public String getMessageType(){
        return TYPE;
    }

    public void setShortNames(List<String> shortNames) {
        this.shortNames = shortNames;
    }

    public List<String> getShortNames() {
        return shortNames;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
