package com.bhasaka.fruitables.core.schedulers;

import com.bhasaka.fruitables.core.configurations.SitemapConfig;
import com.bhasaka.fruitables.core.service.ServiceUtil;

import com.day.cq.commons.Externalizer;
import com.day.cq.dam.api.AssetManager;

import com.day.cq.search.QueryBuilder;

import org.apache.sling.api.resource.ResourceResolver;

import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;

import org.osgi.service.component.annotations.*;
import org.osgi.service.metatype.annotations.Designate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Session;
import javax.jcr.query.QueryManager;
import javax.jcr.query.Query;
import javax.jcr.query.QueryResult;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import java.nio.charset.StandardCharsets;

@Component(
        service = JobConsumer.class,
        property = {
                JobConsumer.PROPERTY_TOPICS + "=custom/sitemap/job"
        }
)
@Designate(ocd = SitemapConfig.class)
public class SitemapJobConsumer implements JobConsumer {

    private static final Logger LOG =
            LoggerFactory.getLogger(SitemapJobConsumer.class);

    @Reference
    private QueryBuilder queryBuilder;

    @Reference
    private ServiceUtil serviceUtil;

    @Reference
    private Externalizer externalizer;

    private String rootPath;
    private String damFolderPath;

    @Activate
    protected void activate(SitemapConfig config) {

        this.rootPath = config.root_path();
        this.damFolderPath = config.dam_folder_path();

        LOG.info("Sitemap Job Consumer Activated");
        LOG.info("Root Path : {}", rootPath);
        LOG.info("DAM Folder Path : {}", damFolderPath);
    }

    @Override
    public JobResult process(Job job) {

        LOG.info("Sitemap Job Started");

        ResourceResolver resolver = null;

        try {

            resolver = serviceUtil.getServiceUserMap();

            Session session = resolver.adaptTo(Session.class);

            /*     Map<String, String> map = new HashMap<>();

            map.put("path", rootPath);
            map.put("type", "cq:Page");
            map.put("p.limit", "-1");

            Query query = queryBuilder.createQuery(
                    PredicateGroup.create(map),
                    session
            );

            SearchResult result = query.getResult();

            LOG.info("Total Pages Found : {}",
                    result.getHits().size());

            StringBuilder xml = new StringBuilder();

            xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

            xml.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">");

            for (Hit hit : result.getHits()) {

                String path = hit.getPath();

                LOG.info("Page Path : {}", path);

                String externalizedUrl =
                        externalizer.publishLink(
                                resolver,
                                path
                        ) + ".html";

                xml.append("<url>");

                xml.append("<loc>");
                xml.append(externalizedUrl);
                xml.append("</loc>");

                xml.append("</url>");
            }

            xml.append("</urlset>");
*/
            QueryManager queryManager = session.getWorkspace().getQueryManager();

            String sql2 =
                    "SELECT * FROM [cq:Page] AS page " +
                            "WHERE ISDESCENDANTNODE(page, '" + rootPath + "')";

            Query query =
                    queryManager.createQuery(sql2, Query.JCR_SQL2);

            QueryResult result = query.execute();

            NodeIterator nodes = result.getNodes();

            StringBuilder xml = new StringBuilder();

            xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            xml.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">");

            int count = 0;

            while (nodes.hasNext()) {

                Node node = nodes.nextNode();

                String path = node.getPath();

                count++;

                LOG.info("Page Path : {}", path);

                String externalizedUrl =
                        externalizer.publishLink(
                                resolver,
                                path
                        ) + ".html";

                xml.append("<url>");
                xml.append("<loc>");
                xml.append(externalizedUrl);
                xml.append("</loc>");
                xml.append("</url>");
            }

            LOG.info("Total Pages Found : {}", count);

            xml.append("</urlset>");

            createSitemapAsset(
                    resolver,
                    xml.toString()
            );

            LOG.info("Sitemap.xml Generated Successfully");

            return JobResult.OK;

        } catch (Exception e) {

            LOG.error("Exception while generating sitemap", e);

            return JobResult.FAILED;

        } finally {

            if (resolver != null && resolver.isLive()) {
                resolver.close();
            }
        }
    }

    private void createSitemapAsset(
            ResourceResolver resolver,
            String xmlContent) {

        try {

            String assetPath =
                    damFolderPath + "/sitemap.xml";

            AssetManager assetManager =
                    resolver.adaptTo(AssetManager.class);

            InputStream inputStream =
                    new ByteArrayInputStream(
                            xmlContent.getBytes(StandardCharsets.UTF_8)
                    );

            assetManager.createAsset(
                    assetPath,
                    inputStream,
                    "application/xml",
                    true
            );

//            resolver.commit();

            LOG.info("Sitemap Stored in DAM : {}", assetPath);

        } catch (Exception e) {

            LOG.error("Error while storing sitemap in DAM", e);
        }
    }
}