package com.bhasaka.fruitables.core.listeners;

import lombok.extern.slf4j.Slf4j;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.servlets.post.Modification;
import org.apache.sling.servlets.post.SlingPostProcessor;
import org.osgi.service.component.annotations.Component;

import javax.jcr.Node;
import javax.jcr.Session;
import java.util.List;

@Slf4j
@Component(service = SlingPostProcessor.class)
public class EventPostProcessor implements SlingPostProcessor {

    @Override
    public void process(SlingHttpServletRequest request,
                        List<Modification> modifications)
            throws Exception {

        log.info("EventPostProcessor process started");

        String title = request.getParameter("jcr:title");

        if (title == null) {
            return;
        }

        Session session = request.getResourceResolver()
                .adaptTo(Session.class);

        for (Modification modification : modifications) {

            String path = modification.getSource();

            log.info("Modified Path : {}", path);

            // Skip property paths
            if (path.endsWith("jcr:title")) {

                String nodePath = path.substring(0, path.lastIndexOf("/"));

                if (session.nodeExists(nodePath)) {

                    Node node = session.getNode(nodePath);

                    node.setProperty("jcr:title", title.toUpperCase());

                    log.info("Converted title to uppercase at {}", nodePath);
                }
            }
        }

        session.save();
    }
}