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

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.kaloz.datafeed.processor.api.instrument.InstrumentPriceLoadCommandMessage;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class InstrumentPriceLoadCommandMessageConverterTest {

    private InstrumentPriceLoadCommandMessageConverter obj = new InstrumentPriceLoadCommandMessageConverter();

    @Before
    public void init(){
        ReflectionTestUtils.setField(obj, "yahooUrl", "%s" );
    }

    @Test
    public void testValidListData() throws Exception {

        InstrumentPriceLoadCommandMessage instrumentPriceLoadCommandMessage = new InstrumentPriceLoadCommandMessage(Lists.newArrayList("A","B","C","D"));

        String url = obj.toUrl(instrumentPriceLoadCommandMessage);
        assertNotNull(url);

        assertEquals("A+B+C+D", url);
    }

    @Test(expected = RuntimeException.class)
    public void testNullList() throws Exception {

        InstrumentPriceLoadCommandMessage instrumentPriceLoadCommandMessage = new InstrumentPriceLoadCommandMessage();

        obj.toUrl(instrumentPriceLoadCommandMessage);
    }

    @Test(expected = RuntimeException.class)
    public void testEmptyList() throws Exception {

        InstrumentPriceLoadCommandMessage instrumentPriceLoadCommandMessage = new InstrumentPriceLoadCommandMessage(new ArrayList<String>());

        obj.toUrl(instrumentPriceLoadCommandMessage);
    }
}
