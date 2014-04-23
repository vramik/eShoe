package com.issuetracker.search;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import javax.enterprise.inject.Produces;

/**
 * //TODO: document this
 *
 * @author Jiří Holuša
 */
public class EsTransportClientProducer {

    private Client client;

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
