package com.bhasaka.fruitables.core.listeners;

import com.day.cq.replication.Preprocessor;
import com.day.cq.replication.ReplicationAction;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.ReplicationOptions;
import com.day.cq.replication.*;
import lombok.extern.slf4j.Slf4j;
import org.osgi.service.component.annotations.Component;

@Slf4j
@Component(service = Preprocessor.class)
public class EventPreProcessor implements Preprocessor {

    @Override
    public void preprocess(ReplicationAction action,
                           ReplicationOptions options)
            throws ReplicationException {

        String path = action.getPath();

        if (action.getType() == ReplicationActionType.ACTIVATE) {

            log.info("Page is trying to publish : {}", path);

            if (path.contains("/content/fruitables/us/en/testevent")) {

                log.error("Publishing blocked for : {}", path);

                throw new ReplicationException(
                        "Publishing is not allowed for temp pages"
                );
            }
        }
    }
}

