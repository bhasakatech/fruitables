package com.bhasaka.fruitables.core.workflow;

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
        property = {
                "process.label=Reminder Notification Process"
        }
)
public class ReminderNotificationProcess implements WorkflowProcess {

    private static final Logger LOG =
            LoggerFactory.getLogger(ReminderNotificationProcess.class);

    @Reference
    private MessageGatewayService messageGatewayService;

    @Override
    public void execute(
            WorkItem workItem,
            WorkflowSession workflowSession,
            MetaDataMap metaDataMap)
            throws WorkflowException {

        try {

            String payload =
                    workItem.getWorkflowData().getPayload().toString();

            LOG.info("Reminder workflow triggered for payload: {}", payload);

            boolean stillPending = isStillInLegalReview(workItem, workflowSession);

            if (stillPending) {

                LOG.info("Legal Review still pending → sending email");

                sendEmail(workItem);

            } else {

                LOG.info("Legal Review already completed → no email sent");
            }

        } catch (Exception e) {

            LOG.error("Error in ReminderNotificationProcess", e);
            throw new WorkflowException(e);
        }
    }

    private boolean isStillInLegalReview(
            WorkItem workItem,
            WorkflowSession workflowSession) {

        try {

            String payload =
                    workItem.getWorkflowData().getPayload().toString();

            WorkItem[] activeItems =
                    workflowSession.getActiveWorkItems();

            for (WorkItem item : activeItems) {

                String itemPayload =
                        item.getWorkflowData().getPayload().toString();

                LOG.info("Checking active item payload: {}", itemPayload);

                if (payload.equals(itemPayload)) {

                    String stepName =
                            item.getNode().getTitle();

                    LOG.info("Current step: {}", stepName);

                    if ("Legal Review".equals(stepName)) {

                        return true;
                    }
                }
            }

        } catch (Exception e) {

            LOG.error("Error checking workflow state", e);
        }

        return false;
    }

    private void sendEmail(WorkItem workItem) {

        try {

            MessageGateway<SimpleEmail> gateway =
                    messageGatewayService.getGateway(SimpleEmail.class);

            if (gateway == null) {

                LOG.error("MessageGateway is NULL");
                return;
            }

            SimpleEmail email = new SimpleEmail();

            email.setSubject("Legal Review Pending Alert");

            email.setMsg(
                    "Your Legal Review is still pending after 1 minute.\n" +
                            "Please approve or reject it."
            );

            email.addTo("thammineni.sh1995@gmail.com");

            email.setFrom("sreeharsha2210@gmail.com");

            gateway.send(email);

            LOG.info("Reminder email sent successfully");

        } catch (Exception e) {

            LOG.error("Error sending email", e);
        }
    }
}