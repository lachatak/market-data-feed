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
package org.kaloz.datafeed.yahoo.infrastructure.acl;

import org.apache.camel.Converter;
import org.kaloz.datafeed.processor.api.instrument.InstrumentPriceCommandMessage;
import org.kaloz.datafeed.yahoo.infrastructure.integration.dataformat.YahooCSV;
import org.springframework.beans.factory.annotation.Value;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@Converter
public final class YahooCSVConverter {

    @Value("${yahoo.provider.name}")
    private String providerName;

    @Converter
    public InstrumentPriceCommandMessage toInstrumentPriceCommandMessage(YahooCSV data) throws ParseException, NumberFormatException {

        InstrumentPriceCommandMessage instrumentPriceCommandMessage = new InstrumentPriceCommandMessage();
        instrumentPriceCommandMessage.setShortName(data.getShortName());
        instrumentPriceCommandMessage.setLongName(data.getLongName());
        instrumentPriceCommandMessage.setPrice(Double.valueOf(data.getLastPrice()));
        instrumentPriceCommandMessage.setDate(new SimpleDateFormat("MM/dd/yyyy").parse(data.getDate()));
        instrumentPriceCommandMessage.setProvider(providerName);

        return instrumentPriceCommandMessage;
    }

}
