package com.jayden;

import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;

public class PulsarConsumer {
    public static void main(String[] args) throws PulsarClientException {
        PulsarClient client = PulsarClient.builder()
                .serviceUrl(Config.SERVER)
                .build();

        Consumer<byte[]> consumer = client.newConsumer()
                .topic(Config.TOPIC_NAME)
                .subscriptionName("my-subscription")
                .subscribe();

        // custom consumer
//        Consumer consumer = client.newConsumer()
//                .topic("my-topic")
//                .subscriptionName("my-subscription")
//                .ackTimeout(10, TimeUnit.SECONDS)
//                .subscriptionType(SubscriptionType.Exclusive)
//                .subscribe();

        while (true) {
            // Wait for a message
            Message<byte[]> msg = consumer.receive();
            // async receive
//            CompletableFuture<Message<byte[]>> asyncMessage = consumer.receiveAsync();

            try {
                // Do something with the message
                System.out.printf("Message received: %s", new String(msg.getData()));

                // Acknowledge the message so that it can be deleted by the message broker
                consumer.acknowledge(msg);
            } catch (Exception e) {
                // Message failed to process, redeliver later
                consumer.negativeAcknowledge(msg);
            }
        }
    }
}
