package org.kaloz.messaging.infrastructure.integration;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kaloz.messaging.app.client.JsonMessage;
import org.kaloz.messaging.infrastructure.serialization.JsonMessageSerializer;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import static org.kaloz.messaging.app.client.JmsMessagingConsts.MESSAGE_TYPE;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class JsonConsumerProcessorTest {

    @Mock
    private JsonMessageSerializer jsonMessageSerializer;

    @Mock
    private JsonMessageRegistry jsonMessageRegistry;

    @Mock
    private Exchange exchange;

    @Mock
    private Message message;

    @InjectMocks
    private JsonConsumerProcessor obj = new JsonConsumerProcessor();

    @Test
    public void testProcessHasHeader(){
        when(message.getHeader(eq(MESSAGE_TYPE), eq(String.class))).thenReturn("TestType");
        when(exchange.getIn()).thenReturn(message);
        when(jsonMessageRegistry.getRegisteredMessageClass(eq("TestType"))).thenAnswer(new Answer<Class<? extends JsonMessage>>() {
            @Override
            public Class<? extends JsonMessage> answer(InvocationOnMock invocationOnMock) throws Throwable {
                return TestMessage.class;
            }
        });
        when(message.getBody(eq(String.class))).thenReturn("message");
        TestMessage testMessage = new TestMessage();
        when(jsonMessageSerializer.getObject(eq("message"), eq(TestMessage.class))).thenReturn(testMessage);
        obj.process(exchange);

        verify(message, times(1)).setBody(eq(testMessage));
        verify(exchange, times(1)).setProperty(eq(JsonConsumerProcessor.ORIG_IN_BODY), eq(testMessage));
    }

    @Test
    public void testProcessNoHeader(){
        when(message.getHeader(eq(MESSAGE_TYPE), eq(String.class))).thenReturn(null);
        when(exchange.getIn()).thenReturn(message);

        obj.process(exchange);

        verify(message, never()).setBody(anyObject());
        verify(exchange, never()).setProperty(anyString(), anyObject());
    }

    public static class TestMessage extends JsonMessage {

        @Override
        public String getMessageType() {
            return "TestMessage";
        }
    }
}
