package com.issuetracker.search;

import com.github.holmistr.esannotations.indexing.AnnotationIndexManager;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

/**
 * //TODO: document this
 *
 * @author Jiří Holuša
 */
public class IndexManagerProducer {

    @Inject
    @com.issuetracker.search.TransportClient
    private Client client;

    private AnnotationIndexManager manager;

    @Produces
    public AnnotationIndexManager getIndexManager() {
        if(manager == null) {
            manager = new AnnotationIndexManager(client);
        }

        return manager;
    }
}
