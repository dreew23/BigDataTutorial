package com.getindata.tutorial.bigdatatutorial.kafka;

import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import com.getindata.tutorial.bigdatatutorial.utils.LogEventTsvGenerator;

public class LogEventTsvProducer {
	public static void main(String[] args) {

		Properties props = new Properties();

		props.put("metadata.broker.list", args[0]);
		props.put("zk.connect", args[1]);
		props.put("serializer.class", "kafka.serializer.DefaultEncoder");
		props.put("producer.type", "sync");
		props.put("request.required.acks", "1");
		props.put("topic", args[2]);

		ProducerConfig config = new ProducerConfig(props);
		Producer<String, String> producer = new Producer<String, String>(config);

		LogEventTsvGenerator generator = new LogEventTsvGenerator();

		for (int i = 0; i < 10000; i++) {

			String logEvent = generator.next();

			KeyedMessage<String, String> data = new KeyedMessage<String, String>(
					props.getProperty("topic"), generator.getKey(), logEvent);

			producer.send(data);
		}

		producer.close();
	}
}
