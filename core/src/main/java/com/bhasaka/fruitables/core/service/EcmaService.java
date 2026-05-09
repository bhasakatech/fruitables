package com.bhasaka.fruitables.core.service;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(
        service = WorkflowProcess.class,
        immediate = true,
        property = {
                "process.label = Ecma Service Process"
        }
)
public class EcmaService implements WorkflowProcess {

    private static final Logger LOG = LoggerFactory.getLogger(EcmaService.class);

    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap) throws WorkflowException {
        String payloadType = workItem.getWorkflowData().getPayloadType();
        LOG.info("Payload Type: {}", payloadType);

        if (payloadType != null && payloadType.equalsIgnoreCase("JCR_PATH")) {

            ResourceResolver resolver = workflowSession.adaptTo(ResourceResolver.class);

            if (resolver == null) {
                LOG.error("ResourceResolver is NULL");
                return;
            }

            String payloadPath = workItem.getWorkflowData().getPayload().toString();
            LOG.info("Payload Path: {}", payloadPath);

            Resource resource = resolver.getResource(payloadPath + "/jcr:content");

            if (resource == null) {
                LOG.error("Resource not found at path: {}", payloadPath + "/jcr:content");
                return;
            }

            ValueMap valueMap = resource.adaptTo(ValueMap.class);
            if (valueMap == null) {
                LOG.error("ValueMap is NULL");
                return;
            }

            String comments = valueMap.get("comments", "");
            String decision = valueMap.get("status", "");

            LOG.info("Read from page -> Comments: {}", comments);
            LOG.info("Read from page -> Decision: {}", decision);

            MetaDataMap metaDataMap1 = workItem.getWorkflowData().getMetaDataMap();

            metaDataMap1.put("comments", comments);
            metaDataMap1.put("status", decision);

            LOG.info("Data stored into workflow metadata");

        } else {
            LOG.warn("Payload type is not JCR_PATH");
        }
    }
}
