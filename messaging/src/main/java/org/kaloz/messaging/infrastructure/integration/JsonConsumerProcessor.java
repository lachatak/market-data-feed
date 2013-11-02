package org.kaloz.messaging.infrastructure.integration;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.kaloz.messaging.app.client.JsonMessage;
import org.kaloz.messaging.infrastructure.serialization.JsonMessageSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static org.kaloz.messaging.app.client.JmsMessagingConsts.MESSAGE_TYPE;

@Component
public class JsonConsumerProcessor implements Processor {

    public static final String ORIG_IN_BODY = "origInBody";

    @Resource
    private JsonMessageSerializer jsonMessageSerializer;

    @Resource
    private JsonMessageRegistry jsonMessageRegistry;

    @Override
    public void process(Exchange exchange)  {
        if(exchange.getIn().getHeader(MESSAGE_TYPE, String.class)!=null){
            Class<? extends JsonMessage> messageClass = jsonMessageRegistry.getRegisteredMessageClass(exchange.getIn().getHeader(MESSAGE_TYPE, String.class));
            JsonMessage message = jsonMessageSerializer.getObject(exchange.getIn().getBody(String.class), messageClass);
            exchange.getIn().setBody(message);

            exchange.setProperty(ORIG_IN_BODY, message);
        }
    }
}
