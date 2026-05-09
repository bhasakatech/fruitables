package com.bhasaka.fruitables.core.workflow;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.adobe.granite.workflow.model.WorkflowModel;

import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(
        service = WorkflowProcess.class,
        property = {
                "process.label=Start Reminder Workflow"
        }
)
public class StartReminderWorkflowProcess implements WorkflowProcess {

    private static final Logger LOG =
            LoggerFactory.getLogger(StartReminderWorkflowProcess.class);

    @Override
    public void execute(
            WorkItem workItem,
            WorkflowSession workflowSession,
            MetaDataMap metaDataMap)
            throws WorkflowException {

        try {

            String modelPath =
                    "/var/workflow/models/BhasakaContentEnhancement";

            LOG.info("Starting Reminder Workflow for payload: {}",
                    workItem.getWorkflowData().getPayload());

            WorkflowModel model =
                    workflowSession.getModel(modelPath);

            workflowSession.startWorkflow(
                    model,
                    workItem.getWorkflowData()
            );
            LOG.info("Reminder workflow started successfully");

        } catch (Exception e) {

            LOG.error("Error starting reminder workflow", e);
            throw new WorkflowException(e);
        }
    }
}
