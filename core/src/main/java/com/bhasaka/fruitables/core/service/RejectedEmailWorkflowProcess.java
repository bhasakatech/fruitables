package com.bhasaka.fruitables.core.service;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.mailer.MessageGateway;
import com.day.cq.mailer.MessageGatewayService;
import org.apache.commons.mail.SimpleEmail;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(
        service = WorkflowProcess.class,
        immediate = true,
        property = {
                "process.label = Rejected Email Workflow Process"
        }
)
public class RejectedEmailWorkflowProcess implements WorkflowProcess,EmailService {

    private static final Logger LOG = LoggerFactory.getLogger(RejectedEmailWorkflowProcess.class);
    private String comments;

    @Reference
    private MessageGatewayService messageGatewayService;

    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap) throws WorkflowException {
        try {

            MetaDataMap metaData = workItem.getWorkflowData().getMetaDataMap();
            comments = metaData.get("comments", String.class);
            String initiator = workItem.getWorkflow().getInitiator();
            LOG.info("Initiator: {}", initiator);
            LOG.info("Comments: {}", comments);
            sendEmail();

        } catch(Exception e ) {
            LOG.error("Exception while calling email service method. Exception : ", e);
        }
    }

    @Override
    public void sendEmail() {

        LOG.info("STEP 1: Getting gateway");

        MessageGateway<SimpleEmail> gateway =
                messageGatewayService.getGateway(SimpleEmail.class);

        if (gateway == null) {
            LOG.error("MessageGateway is NULL. Check SMTP config.");
            return;
        }
        try {
            LOG.info("STEP 2: Creating email");
            SimpleEmail email = new SimpleEmail();
            email.setSubject("Welcome to Bhasaka Content Governance Platform");
            email.setMsg("Hello Customer,\n\nYour legal review has been rejected.\n Comments : "+comments);
            email.addTo("thammineni.sh1995@gmail.com");
            email.setFrom("sreeharsha2210@gmail.com");
            LOG.info("STEP 3: Sending email");
            gateway.send(email);
            LOG.info("STEP 4: Email sent successfully");

        } catch (Exception e) {
            LOG.error("FULL EMAIL ERROR : ", e);
        }
    }
}
