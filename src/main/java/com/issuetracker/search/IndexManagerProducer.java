package com.issuetracker.search;

import com.github.holmistr.esannotations.indexing.AnnotationIndexManager;
import org.elasticsearch.client.Client;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

/**
 * Producer of search index manager so it can be injected
 * in any bean.
 *
 * @author Jiří Holuša
 */
public class IndexManagerProducer {

    @Inject
    @com.issuetracker.search.TransportClient
    private Client client;

    private AnnotationIndexManager manager;

    /**
     * Returns instance of AnnotationIndexManager ready to use.
     *
     * @return
     */
    @Produces
    public AnnotationIndexManager getIndexManager() {
        if(manager == null) {
            manager = new AnnotationIndexManager(client);
        }

        return manager;
    }
}
