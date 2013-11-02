package org.kaloz.messaging.infrastructure.integration;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kaloz.messaging.app.client.JsonMessage;
import org.kaloz.messaging.infrastructure.serialization.JsonMessageSerializer;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.kaloz.messaging.app.client.JmsMessagingConsts.MESSAGE_TYPE;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class JsonProducerProcessorTest {

    @Mock
    private JsonMessageSerializer jsonMessageSerializer;

    @Mock
    private Exchange exchange;

    @Mock
    private Message message;

    @InjectMocks
    private JsonProducerProcessor obj = new JsonProducerProcessor();

    @Test
    public void testProcessHasHeader(){
        JsonConsumerProcessorTest.TestMessage testMessage = new JsonConsumerProcessorTest.TestMessage();
        when(message.getBody()).thenReturn(testMessage);
        when(message.getBody(eq(JsonMessage.class))).thenReturn(testMessage);
        when(exchange.getIn()).thenReturn(message);
        when(jsonMessageSerializer.getJson(eq(testMessage))).thenReturn("Test");
        obj.process(exchange);

        verify(message, times(1)).setBody(eq("Test"));
        verify(message, times(1)).setHeader(eq(MESSAGE_TYPE), eq(testMessage.getMessageType()));
    }

    @Test
    public void testProcessNoHeader(){
        when(message.getBody()).thenReturn("Test");
        when(exchange.getIn()).thenReturn(message);

        obj.process(exchange);

        verify(message, never()).setBody(anyObject());
        verify(message, never()).setHeader(anyString(), anyString());
    }
}
