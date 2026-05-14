package com.bhasaka.fruitables.core.schedulers;

import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;

@Component(service = JobConsumer.class, immediate = true, property = {Constants.SERVICE_DESCRIPTION})
public class AssetDeleteMetadata implements JobConsumer {
    @Override
    public JobResult process(Job job) {
        return null;
    }
}
