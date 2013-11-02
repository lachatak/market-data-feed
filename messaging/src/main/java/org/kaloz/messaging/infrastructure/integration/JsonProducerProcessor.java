package org.kaloz.messaging.infrastructure.integration;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.kaloz.messaging.app.client.JsonMessage;
import org.kaloz.messaging.infrastructure.serialization.JsonMessageSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static org.kaloz.messaging.app.client.JmsMessagingConsts.MESSAGE_TYPE;

@Component
public class JsonProducerProcessor implements Processor {

    @Resource
    private JsonMessageSerializer jsonMessageSerializer;

    @Override
    public void process(Exchange exchange) {
        if(exchange.getIn().getBody() instanceof JsonMessage){
            JsonMessage message = exchange.getIn().getBody(JsonMessage.class);
            String json = jsonMessageSerializer.getJson(message);
            exchange.getIn().setHeader(MESSAGE_TYPE, message.getMessageType());
            exchange.getIn().setBody(json);
        }
    }
}
