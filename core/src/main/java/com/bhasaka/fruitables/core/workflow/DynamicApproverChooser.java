package com.bhasaka.fruitables.core.workflow;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.ParticipantStepChooser;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import org.osgi.service.component.annotations.Component;

@Component(service = ParticipantStepChooser.class, immediate = true,
        property = {
                "chooser.label= Dynamic Approver Chooser"
        })
public class DynamicApproverChooser implements ParticipantStepChooser {
    @Override
    public String getParticipant(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap args) throws WorkflowException {


        return "legal-auditors";
    }
}



