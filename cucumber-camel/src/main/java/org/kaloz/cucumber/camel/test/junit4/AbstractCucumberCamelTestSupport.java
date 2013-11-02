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

package org.kaloz.cucumber.camel.test.junit4;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.spring.SpringCamelContext;
import org.apache.camel.test.junit4.CamelSpringTestSupport;
import org.kaloz.cucumber.camel.test.util.RunnableRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public abstract class AbstractCucumberCamelTestSupport extends CamelSpringTestSupport {

    @Resource
    private RunnableRepository runnableRepository;

    protected Exchange result;

    @PostConstruct
    public void init() throws Exception {
        setUp();
    }

    @Resource
    private AbstractApplicationContext applicationContext;

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return applicationContext;
    }

    protected void assertExpectedBodiesReceived(String expectedQueue, final List<String> expectedMessages, Runnable... runnables) {
        getMockEndPoint(expectedQueue).expectedBodiesReceivedInAnyOrder(expectedMessages);
        addRunnables(runnables);
    }

    protected void assertExpectedMessageCount(String expectedQueue, final int expectedCount, Runnable... runnables) {
        getMockEndPoint(expectedQueue).expectedMessageCount(expectedCount);
        addRunnables(runnables);
    }

    protected void assertExpectedHeader(String expectedQueue, String expectedHeader, String expectedHeaderValue, Runnable... runnables) {
        getMockEndPoint(expectedQueue).expectedHeaderReceived(expectedHeader, expectedHeaderValue);
        addRunnables(runnables);
    }

    protected void assertExpectedBodyReceived(final String expectedBody){
        addRunnables(new Runnable() {
            @Override
            public void run() {
                assertEquals("Expected body should be equal", expectedBody, result.getOut().getBody(String.class));
            }
        });
    }

    protected void assertExpectedHeader(final String expectedHeader, final String expectedHeaderValue){
        addRunnables(new Runnable() {
            @Override
            public void run() {
                assertEquals(String.format("Expected %s header should be equal", expectedHeader), expectedHeaderValue, result.getOut().getHeader(expectedHeader).toString());
            }
        });
    }

    protected void assertMockEndpointsSatisfied() {
        try {
            runnableRepository.runRunnables();
            super.assertMockEndpointsSatisfied();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void addRunnables(Runnable... runnables) {
        if (runnables != null && runnables.length > 0) {
            addRunnables(Arrays.asList(runnables));
        }
    }

    protected void addRunnables(List<Runnable> runnables) {
        this.runnableRepository.addAll(runnables);
    }

    protected MockEndpoint getMockEndPoint(String queue) {
        MockEndpoint mock = getMockEndpoint(getMockNameForQueue(queue));
        mock.setResultWaitTime(5000);
        mock.setSleepForEmptyTest(5000);
        return mock;
    }

    private String getMockNameForQueue(String queue) {
        if (queue.startsWith("queue://")) {
            return "mock:" + queue.substring(8);
        }
        throw new IllegalArgumentException(queue);
    }

    @Override
    protected CamelContext createCamelContext() throws Exception {
        return springCamelContext(applicationContext, getContextName());
    }


    public SpringCamelContext springCamelContext(ApplicationContext applicationContext, String contextName) throws Exception {
        if (applicationContext != null) {
            Map<String, SpringCamelContext> contexts = applicationContext.getBeansOfType(SpringCamelContext.class);
            if (contexts.size() == 1) {
                return applicationContext.getBean(contexts.keySet().iterator().next(), SpringCamelContext.class);
            } else if (contexts.size() > 1 && contextName != null && contexts.containsKey(contextName)) {
                return contexts.get(contextName);
            }
        }
        SpringCamelContext answer = new SpringCamelContext();
        answer.setApplicationContext(applicationContext);
        return answer;
    }

    protected String getContextName() {
        return null;
    }
}
