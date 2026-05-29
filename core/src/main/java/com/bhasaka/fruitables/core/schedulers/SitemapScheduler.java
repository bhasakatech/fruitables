package com.bhasaka.fruitables.core.schedulers;

import com.bhasaka.fruitables.core.configurations.SitemapConfig;
import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.apache.sling.event.jobs.JobManager;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Component(service = Runnable.class, immediate = true)
@Designate(ocd = SitemapConfig.class)
public class SitemapScheduler implements Runnable {

    private static final Logger LOG =
            LoggerFactory.getLogger(SitemapScheduler.class);

    private static final String JOB_TOPIC =
            "custom/sitemap/job";

    @Reference
    private Scheduler scheduler;

    @Reference
    private JobManager jobManager;

    @Activate
    protected void activate(SitemapConfig config) {

        try {

            LOG.info("Scheduler Activation Started");

            ScheduleOptions options =
                    scheduler.EXPR(config.scheduler_expression());

            options.name("Custom Sitemap Scheduler");

            scheduler.schedule(this, options);

            LOG.info("Scheduler Registered");

        } catch (Exception e) {

            LOG.error("Scheduler activation failed", e);
        }
    }

    @Deactivate
    protected void deactivate() {

        scheduler.unschedule("Custom Sitemap Scheduler");

        LOG.info("Sitemap Scheduler Deactivated");
    }

    @Override
    public void run() {

        LOG.info("Scheduler Started");

        Map<String, Object> jobProperties =
                new HashMap<>();

        jobManager.addJob(
                JOB_TOPIC,
                jobProperties
        );

        LOG.info("Sling Job Triggered");
    }
}