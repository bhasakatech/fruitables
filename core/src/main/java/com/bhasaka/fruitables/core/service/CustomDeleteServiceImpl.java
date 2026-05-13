package com.bhasaka.fruitables.core.service;

import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.commons.ReferenceSearch;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.*;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;


import javax.jcr.RepositoryException;
import javax.jcr.Session;

import java.util.*;

@Slf4j
@Component(service = CustomDeleteService.class)
public class CustomDeleteServiceImpl implements CustomDeleteService {

    private static final String FALLBACK_PAGE =
            "/content/fruitables/us/en/blog-xf-page";

    @Reference
    private ServiceUtil serviceUtil;

    @Override
    public void processDelete(String pagePath) {

        try (ResourceResolver resolver =
                     serviceUtil.getServiceUserMap()) {

            Session session = resolver.adaptTo(Session.class);

            PageManager pageManager =
                    resolver.adaptTo(PageManager.class);

            Resource pageResource =
                    resolver.getResource(pagePath);

            if (pageResource == null) {

                log.error("Page not found : {}", pagePath);
                return;
            }

            log.info("================================================");
            log.info("Starting delete process for : {}", pagePath);
            log.info("================================================");

            // =====================================================
            // STEP 1 : HANDLE PAGE REFERENCES
            // =====================================================

            updatePageReferences(
                    resolver,
                    session,
                    pagePath
            );

            // =====================================================
            // STEP 2 : FIND PAGE ASSETS
            // =====================================================

            Set<String> assetPaths =
                    findAssetsInPage(pageResource);

            log.info("Assets found in page : {}", assetPaths);

            // =====================================================
            // STEP 3 : DELETE UNUSED ASSETS
            // =====================================================

            for (String assetPath : assetPaths) {

                boolean isReferenced =
                        isAssetReferencedElsewhere(
                                resolver,
                                assetPath,
                                pagePath
                        );

                if (!isReferenced) {

                    Resource assetResource =
                            resolver.getResource(assetPath);

                    if (assetResource != null) {

                        log.info(
                                "Deleting unused asset : {}",
                                assetPath
                        );

                        resolver.delete(assetResource);

                    } else {

                        log.warn(
                                "Asset resource not found : {}",
                                assetPath
                        );
                    }

                } else {

                    log.info(
                            "Asset is referenced elsewhere, skipping delete : {}",
                            assetPath
                    );
                }
            }

            // =====================================================
            // STEP 4 : DELETE PAGE
            // =====================================================

            log.info("Deleting page : {}", pagePath);

            pageManager.delete(
                    pageResource,
                    false,
                    true
            );

            session.save();

            log.info("================================================");
            log.info("Delete completed successfully");
            log.info("================================================");

        } catch (Exception e) {

            log.error(
                    "Error during delete process for page : {}",
                    pagePath,
                    e
            );
        }
    }

    // =========================================================
    // UPDATE PAGE REFERENCES
    // =========================================================

    private void updatePageReferences(ResourceResolver resolver,
                                      Session session,
                                      String pagePath)
            throws Exception {

        ReferenceSearch referenceSearch =
                new ReferenceSearch();

        referenceSearch.setExact(true);

        Map<String, ReferenceSearch.Info> references =
                referenceSearch.search(
                        resolver,
                        pagePath
                );

        if (references.isEmpty()) {

            log.info(
                    "No references found for page : {}",
                    pagePath
            );

            return;
        }

        log.info(
                "Total references found : {}",
                references.size()
        );

        for (Map.Entry<String, ReferenceSearch.Info> entry
                : references.entrySet()) {

            String referencedPage =
                    entry.getKey();

            log.info(
                    "Updating reference in : {}",
                    referencedPage
            );

            Resource refResource =
                    resolver.getResource(referencedPage);

            if (refResource != null) {

                replaceReference(
                        refResource,
                        pagePath,
                        FALLBACK_PAGE
                );
            }
        }

        session.save();
    }

    // =========================================================
    // RECURSIVE PROPERTY UPDATE
    // =========================================================

    private void replaceReference(Resource resource,
                                  String oldPath,
                                  String newPath) {

        ModifiableValueMap map =
                resource.adaptTo(ModifiableValueMap.class);

        if (map != null) {

            for (Map.Entry<String, Object> entry
                    : map.entrySet()) {

                Object value = entry.getValue();

                if (value instanceof String &&
                        StringUtils.equals(
                                value.toString(),
                                oldPath
                        )) {

                    log.info(
                            "Replacing reference {} -> {}",
                            oldPath,
                            newPath
                    );

                    map.put(
                            entry.getKey(),
                            newPath
                    );
                }
            }
        }

        for (Resource child : resource.getChildren()) {

            replaceReference(
                    child,
                    oldPath,
                    newPath
            );
        }
    }

    // =========================================================
    // FIND ASSETS IN PAGE
    // =========================================================

    private Set<String> findAssetsInPage(Resource pageResource) {

        Set<String> assetPaths =
                new HashSet<>();

        scanAssets(
                pageResource,
                assetPaths
        );

        return assetPaths;
    }

    private void scanAssets(Resource resource,
                            Set<String> assetPaths) {

        ValueMap valueMap =
                resource.getValueMap();

        for (Map.Entry<String, Object> entry
                : valueMap.entrySet()) {

            Object value =
                    entry.getValue();

            if (value instanceof String) {

                String str =
                        value.toString();

                if (str.startsWith("/content/dam")) {

                    log.info(
                            "Asset found : {}",
                            str
                    );

                    assetPaths.add(str);
                }
            }
        }

        for (Resource child : resource.getChildren()) {

            scanAssets(
                    child,
                    assetPaths
            );
        }
    }

    // =========================================================
    // CHECK ASSET REFERENCES
    // =========================================================

    private boolean isAssetReferencedElsewhere(
            ResourceResolver resolver,
            String assetPath,
            String deletingPage)
            throws Exception {

        ReferenceSearch referenceSearch =
                new ReferenceSearch();

        referenceSearch.setExact(true);

        Map<String, ReferenceSearch.Info> refs =
                referenceSearch.search(
                        resolver,
                        assetPath
                );

        if (refs.isEmpty()) {

            log.info(
                    "No references found for asset : {}",
                    assetPath
            );

            return false;
        }

        for (String refPage : refs.keySet()) {

            log.info(
                    "Asset reference found in : {}",
                    refPage
            );

            if (!refPage.startsWith(deletingPage)) {

                return true;
            }
        }

        return false;
    }
}
