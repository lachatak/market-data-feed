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

package org.kaloz.datafeed.processorrest.infrastructure.acl

import org.specs2.mutable.SpecificationWithJUnit

class ProcessorRestConverterTest extends SpecificationWithJUnit {

  val obj = new ProcessorRestConverter

  "The shortName and the provider string equals to A and B" in {
    val result = obj.toInstrumentPriceRequestMessage("A", "B")

    result.getProvider must equalTo("A")
    result.getShortName must equalTo("B")
  }


  "The provider string equals to A" in {
    val result = obj.toInstrumentPriceListRequestMessage("A")

    result.getProvider must equalTo("A")
  }

}
