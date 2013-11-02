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

package org.kaloz.datafeed.yahoo.infrastructure.acl;

import org.apache.camel.Converter;
import org.kaloz.datafeed.processor.api.instrument.InstrumentPriceLoadCommandMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

@Converter
public final class InstrumentPriceLoadCommandMessageConverter {

    @Value("${yahoo.url}")
    private String yahooUrl;

    @Converter
    public String toUrl(InstrumentPriceLoadCommandMessage instrumentPriceLoadCommandMessage) {
        if(instrumentPriceLoadCommandMessage.getShortNames()==null || instrumentPriceLoadCommandMessage.getShortNames().isEmpty()) {
            throw new RuntimeException("Empty list cannot be converted to a proper URL!");
        }
        return String.format(yahooUrl, StringUtils.collectionToDelimitedString(instrumentPriceLoadCommandMessage.getShortNames(), "+"));
    }

}
