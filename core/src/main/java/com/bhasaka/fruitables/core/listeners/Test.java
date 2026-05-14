package com.bhasaka.fruitables.core.listeners;

import lombok.extern.slf4j.Slf4j;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;

@Component(service = EventHandler.class, immediate = true, property = {EventConstants.EVENT_TOPIC+"=com/day/cq/replication",
                                                                       EventConstants.EVENT_FILTER+"=(type=ACTIVATE)"})
@Slf4j
public class Test implements EventHandler {

    @Override
    public void handleEvent(Event event) {

        try {

            log.info("Replication Event Triggered");

            String[] properties = event.getPropertyNames();

            for(String property : properties){

                log.info("Event Practice Property : {}", property);

                log.info("Event Practice Value : {}",
                        event.getProperty(property));
            }

        } catch (Exception e) {

            log.error("Event Practice Error", e);
        }
    }
}

