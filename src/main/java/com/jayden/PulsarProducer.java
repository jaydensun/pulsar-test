package com.jayden;

import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.Schema;

/**
 * document: http://pulsar.apache.org/docs/en/client-libraries-java/
 */
public class PulsarProducer {

    public static void main(String[] args) throws PulsarClientException {
        PulsarClient client = PulsarClient.builder()
                .serviceUrl(Config.SERVER)
                .build();
        Producer<byte[]> producer = client.newProducer(Schema.BYTES)
                .topic(Config.TOPIC_NAME).create();
        // sync send
        producer.send("my-sync-message".getBytes());

        // async send
        producer.sendAsync("my-async-message".getBytes()).thenAccept(msgId -> {
            System.out.printf("Message with ID %s successfully sent\n", msgId);
        });

        // sync send message with additional items
        producer.newMessage()
                .key("my-message-key")
                .value("my-sync-message-with-additional-items".getBytes())
                .property("my-key", "my-value")
                .property("my-other-key", "my-other-value")
                .send();

        // async send message with additional items
        producer.newMessage()
                .key("my-message-key")
                .value("my-async-message-with-additional-items".getBytes())
                .property("my-key", "my-value")
                .property("my-other-key", "my-other-value")
                .sendAsync();

        producer.close();

//        producer.closeAsync()
//                .thenRun(() -> System.out.println("Producer closed"))
//                .exceptionally((ex) -> {
//                    System.err.println("Failed to close producer: " + ex);
//                    return null;
//                });
        client.close();
    }

}
