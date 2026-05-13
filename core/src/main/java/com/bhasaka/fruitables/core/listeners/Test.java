package com.bhasaka.fruitables.core.listeners;

import com.bhasaka.fruitables.core.service.ServiceUtil;
import com.day.cq.replication.ReplicationAction;
import com.day.cq.replication.ReplicationActionType;
import lombok.extern.slf4j.Slf4j;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;

@Component(service = EventHandler.class, property = {EventConstants.EVENT_TOPIC+"="+ ReplicationAction.EVENT_TOPIC})
@Slf4j
public class Test implements EventHandler {

    @Reference
    ResourceResolver resourceResolver;
    ServiceUtil serviceUtil;

    @Override
    public void handleEvent(Event event) {
        try{
            log.info("Event practice: '{}'", event);

            String actionType = event.getProperty(ReplicationAction.PN_ACTION_TYPE).toString();
            String path = event.getProperty("path").toString();
            String userId = event.getProperty("userId").toString();

            if(actionType!=null){
                ReplicationActionType replicationActionType = ReplicationActionType.fromName(actionType);
                if(replicationActionType.ACTIVATE.equals(actionType)){
                    log.info("Event Practice Page Published");
                    log.info("Event Handler Path Practice : '{}'", path);
                    log.info("Event Handler Practice Path : '{}'", userId);

                }
           }
        } catch (Exception e) {
            log.info("Event Handler Page Not Triggered");
        }
    }
}

