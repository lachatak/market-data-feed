package org.kaloz.cucumber.camel.test.util;

import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class RunnableRepository extends ArrayList<Runnable> {

    public void runRunnables() {
        try{
            for (Runnable runnable : this ) {
                runnable.run();
            }
        } finally {
            clear();
        }
    }
}
