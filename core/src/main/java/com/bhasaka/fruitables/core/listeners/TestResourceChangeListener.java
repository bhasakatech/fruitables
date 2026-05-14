package com.bhasaka.fruitables.core.listeners;

import com.bhasaka.fruitables.core.service.ServiceUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.sling.api.resource.*;
import org.apache.sling.api.resource.observation.ResourceChange;
import org.apache.sling.api.resource.observation.ResourceChangeListener;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.List;

@Slf4j
@Component(
        service = ResourceChangeListener.class,
        property = {
                ResourceChangeListener.PATHS + "=/content/fruitables/us/en",
                ResourceChangeListener.CHANGES + "=ADDED"
        }
)
public class TestResourceChangeListener implements ResourceChangeListener {

    @Reference
    private ServiceUtil serviceUtil;

    @Override
    public void onChange(List<ResourceChange> changes) {

        for (ResourceChange change : changes) {

            String pagePath = change.getPath();

            try (ResourceResolver resolver =
                         serviceUtil.getServiceUserMap()) {

                Resource contentResource =
                        resolver.getResource(pagePath + "/jcr:content");

                if (contentResource != null) {

                    ModifiableValueMap map =
                            contentResource.adaptTo(ModifiableValueMap.class);

                    if (map != null) {

                        map.put("createdByListener", true);

                        resolver.commit();

                        log.info("Property added to page : {}", pagePath);
                    }
                }

            } catch (Exception e) {

                log.error("Error while updating page property", e);
            }
        }
    }
}