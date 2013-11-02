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

package org.kaloz.cucumber.camel.test.steps;

import cucumber.api.java.After;
import org.apache.camel.CamelContext;
import org.apache.camel.component.mock.MockEndpoint;
import org.kaloz.cucumber.camel.test.util.RunnableRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.List;

public class CucumberCamelHooks {

    private static final Logger LOG = LoggerFactory.getLogger(CucumberCamelHooks.class);

    @Resource
    private volatile List<CamelContext> contexts;

    @Resource
    private RunnableRepository runnableRepository;

    @After
    public void down() throws Exception {
        LOG.info("Run runnables");
        runnableRepository.runRunnables();

        for(CamelContext context:contexts){

            LOG.info("assertMocks for " + context);
            MockEndpoint.assertIsSatisfied(context);

            LOG.info("resetMocks for " + context);
            MockEndpoint.resetMocks(context);
        }
    }
}
