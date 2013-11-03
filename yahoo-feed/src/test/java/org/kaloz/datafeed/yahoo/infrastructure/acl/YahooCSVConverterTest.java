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

import org.junit.Before;
import org.junit.Test;
import org.kaloz.datafeed.processor.api.instrument.InstrumentPriceCommandMessage;
import org.kaloz.datafeed.yahoo.infrastructure.integration.dataformat.YahooCSV;
import org.springframework.test.util.ReflectionTestUtils;

import java.text.SimpleDateFormat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class YahooCSVConverterTest {

    private YahooCSVConverter obj = new YahooCSVConverter();

    @Before
    public void init(){
        ReflectionTestUtils.setField(obj, "providerName", "YAHOO" );
    }

    @Test
    public void testYahooCSVConverter() throws Exception {

        YahooCSV yahooCSV = new YahooCSV();
        yahooCSV.setShortName("A");
        yahooCSV.setLongName("AA");
        yahooCSV.setDate("11/12/2013");
        yahooCSV.setDividendYield("10.1");
        yahooCSV.setLastPrice("10.2");
        yahooCSV.setPeRatio("0.5");

        InstrumentPriceCommandMessage data = obj.toInstrumentPriceCommandMessage(yahooCSV);
        assertNotNull(data);

        assertEquals("A", data.getShortName());
        assertEquals("AA", data.getLongName());
        assertEquals("11/12/2013", new SimpleDateFormat("M/dd/yyyy").format(data.getDate()));
        assertEquals(10.2, data.getPrice().doubleValue(), 0);
        assertEquals("YAHOO", data.getProvider());
    }

}
