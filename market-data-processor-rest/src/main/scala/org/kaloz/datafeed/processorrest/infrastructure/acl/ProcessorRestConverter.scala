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

import org.apache.camel.Header
import org.kaloz.datafeed.processor.api.instrument.{InstrumentPriceListRequestMessage, InstrumentPriceRequestMessage}
import org.springframework.stereotype.Component

@Component
class ProcessorRestConverter {

  def toInstrumentPriceRequestMessage(@Header("provider") provider: String, @Header("instrument") instrument: String) = new InstrumentPriceRequestMessage( provider, instrument)

  def toInstrumentPriceListRequestMessage(@Header("provider") provider: String) = new InstrumentPriceListRequestMessage( provider )

}
