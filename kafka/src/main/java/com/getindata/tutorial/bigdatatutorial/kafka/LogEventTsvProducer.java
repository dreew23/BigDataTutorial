package com.getindata.tutorial.bigdatatutorial.kafka;

import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import com.getindata.tutorial.bigdatatutorial.utils.LogEventTsvGenerator;

public class LogEventTsvProducer {
	public static void main(String[] args) throws InterruptedException {

        if (args.length < 3) {
            System.err.println("Usage: LogEventTsvProducer <kafkaBroker1:port,kafkaBroker2:port> <zkQuorum> <topic>");
            System.exit(1);
        }

		Properties props = new Properties();

		props.put("metadata.broker.list", args[0]);
		props.put("zk.connect", args[1]);
		props.put("serializer.class", "kafka.serializer.DefaultEncoder");
		props.put("producer.type", "sync");
		props.put("request.required.acks", "1");
		props.put("topic", args[2]);

		ProducerConfig config = new ProducerConfig(props);
		Producer<String, byte[]> producer = new Producer<String, byte[]>(config);

		LogEventTsvGenerator generator = new LogEventTsvGenerator();

		for (int i = 0; i < 10000; i++) {

			String logEvent = generator.next();

			KeyedMessage<String, byte[]> data = new KeyedMessage<String, byte[]>(
					props.getProperty("topic"), generator.getKey(), logEvent.getBytes());

			producer.send(data);

			if (i % 200 == 0) {
				 Thread.sleep(500);
				System.out.println("Produced events: " + i);
			}
		}

		producer.close();
	}
}
