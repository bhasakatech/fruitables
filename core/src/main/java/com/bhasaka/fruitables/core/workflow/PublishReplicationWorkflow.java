package com.bhasaka.fruitables.core.workflow;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.Replicator;

import lombok.extern.slf4j.Slf4j;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import javax.jcr.Session;

@Component(service = WorkflowProcess.class, immediate = true, property = {"process.label = Page Publishing workflow"})
@Slf4j
public class PublishReplicationWorkflow implements WorkflowProcess {

    @Reference
    Replicator replicator;

    @Override
    public void execute(WorkItem item, WorkflowSession session, MetaDataMap args) throws WorkflowException {

        log.info("PublishReplicationWorkflow started");
        String publishPayload = item.getWorkflowData().getPayload().toString();

        Session publishSession = session.adaptTo(Session.class);
        try {
            replicator.replicate(publishSession, ReplicationActionType.ACTIVATE,publishPayload);
        } catch (ReplicationException e) {
            log.error("PublishReplicationWorkflow failed", e);
            throw new WorkflowException(e);
        }
    }
}
