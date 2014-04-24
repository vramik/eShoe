package com.issuetracker.search;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import javax.enterprise.inject.Produces;

/**
 * Producer for Elasticsearch client so it can be injected into
 * any bean.
 *
 * @author Jiří Holuša
 */
public class EsTransportClientProducer {

    private Client client;

    /**
     * Returns properly configured TransportClient ready to use.
     *
     * @return
     */
    @Produces
    @com.issuetracker.search.TransportClient
    public Client getTransportClient() {
        if(client == null) {
            client = new TransportClient()
                    .addTransportAddress(new InetSocketTransportAddress("localhost", 9300));
        }

        return client;
    }
}
