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

package org.kaloz.messaging.infrastructure.integration;

import org.kaloz.messaging.app.client.JsonMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class JsonMessageRegistry {

    private static final Logger LOG = LoggerFactory.getLogger(JsonMessageRegistry.class);

    private Map<String, Class<? extends JsonMessage>> messages = new HashMap<String, Class<? extends JsonMessage>>();

    public <T extends JsonMessage> void registerMessage(String name, Class<T> message){
        messages.put(name, message);
        LOG.info("JsonMessage with name '{}' has been registered : {}", name, message);
    }

    public Class<? extends JsonMessage> getRegisteredMessageClass(String messageTypeHeader) {
        if(!messages.containsKey(messageTypeHeader)){
            throw new IllegalArgumentException("Message is not registered: " + messageTypeHeader);
        }
        return messages.get(messageTypeHeader);
    }
}
