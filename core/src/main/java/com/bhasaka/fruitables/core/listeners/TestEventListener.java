package com.bhasaka.fruitables.core.listeners;

import com.bhasaka.fruitables.core.service.ServiceUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.*;

import javax.jcr.Session;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;
import javax.jcr.observation.ObservationManager;

@Slf4j
@Component(service = EventListener.class, immediate = true)
public class TestEventListener implements EventListener {

    @Reference
    private ServiceUtil serviceUtil;

    private ResourceResolver resolver;
    private Session session;
    private ObservationManager observationManager;

    @Activate
    protected void activate() {

        try {

            resolver = serviceUtil.getServiceUserMap();

            session = resolver.adaptTo(Session.class);

            if (session != null) {

                observationManager =
                        session.getWorkspace().getObservationManager();

                observationManager.addEventListener(
                        this,
                        Event.NODE_ADDED,
                        "/content/fruitables/us/en",
                        true,
                        null,
                        null,
                        false
                );

                log.info("JCR Event Listener Registered");
            }

        } catch (Exception e) {

            log.error("Error registering listener", e);
        }
    }

    @Override
    public void onEvent(EventIterator events) {

        while (events.hasNext()) {

            try {

                Event event = events.nextEvent();

                log.info("Node Created : {}", event.getPath());

            } catch (Exception e) {

                log.error("Error processing event", e);
            }
        }
    }

    @Deactivate
    protected void deactivate() {

        try {

            if (observationManager != null) {

                observationManager.removeEventListener(this);

                log.info("JCR Event Listener Removed");
            }

        } catch (Exception e) {

            log.error("Error removing listener", e);

        }
    }
}