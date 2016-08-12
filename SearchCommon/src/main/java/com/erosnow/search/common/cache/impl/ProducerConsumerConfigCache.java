package com.erosnow.search.common.cache.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;

import com.erosnow.search.common.cache.Cache;
import com.erosnow.search.common.entity.ProducerConsumerConfiguration;
import com.erosnow.search.common.model.KeyValueObj;
import com.erosnow.search.common.model.ProducerConfigObj;
import com.erosnow.search.common.model.ProducerConsumerConfigObj;

import kafka.consumer.ConsumerConfig;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.javaapi.producer.Producer;
import kafka.producer.ProducerConfig;

@Cache(name = "producerConsumerConfigCache")
public class ProducerConsumerConfigCache {

	private static final Logger LOG = LoggerFactory.getLogger(ProducerConsumerConfigCache.class);

	public final static String PRODUCER_TYPE = "producer";
	public final static String CONSUMER_TYPE = "consumer";

	private final Map<String, ProducerConfig> typeToProducerConfig = new HashMap<String, ProducerConfig>();
	private final Map<String, Producer<String, Serializable>> typeToProducer = new HashMap<String, Producer<String, Serializable>>();
	private final Map<String, Map<String, ConsumerConfig>> typeToConsumerGroupConfig = new HashMap<String, Map<String, ConsumerConfig>>();
	private final Map<String, Map<String, ConsumerConnector>> typeToConsumerGroup = new HashMap<String, Map<String, ConsumerConnector>>();
	private final Map<String, Integer> typeToNumOfPartitions = new HashMap<String, Integer>();

	public void addConsumer(String type, String groupId, ConsumerConnector consumerObj) {
		Map<String, ConsumerConnector> consumerGroup = typeToConsumerGroup.get(type);
		if (consumerGroup == null) {
			consumerGroup = new HashMap<String, ConsumerConnector>();
		}
		consumerGroup.put(groupId, consumerObj);
		typeToConsumerGroup.put(type, consumerGroup);
	}

	public ConsumerConnector getConsumer(String type, String groupId) {
		Map<String, ConsumerConnector> map = typeToConsumerGroup.get(type);
		if (map != null) {
			return map.get(groupId);
		}
		return null;
	}

	public ProducerConsumerConfigObj loadXML(String filePath) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(ProducerConsumerConfigObj.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			return (ProducerConsumerConfigObj) jaxbUnmarshaller
					.unmarshal(new DefaultResourceLoader().getResource(filePath).getFile());

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error while loading file " + filePath);
		}
	}

	public void loadConfig(String filePath) {
		ProducerConsumerConfigObj pc = loadXML(filePath);
		LOG.info(pc.toString());
		if (pc != null && pc.getProducerConfigObj().size() > 0) {
			for (ProducerConfigObj obj : pc.getProducerConfigObj()) {
				if (obj.getConfig().equalsIgnoreCase(PRODUCER_TYPE)) {
					addProducerConfig(obj.getName(), getProperties(obj.getProperties()));
				} else if (obj.getConfig().equalsIgnoreCase(CONSUMER_TYPE)) {
					addConsumerConfig(obj.getName(), getProperties(obj.getProperties()), obj.getGroupId());
				}
			}
		}
	}

	private Properties getProperties(List<KeyValueObj> keyValueList) {
		Properties props = new Properties();
		if (keyValueList != null && keyValueList.size() > 0) {
			for (KeyValueObj obj : keyValueList) {
				props.put(obj.getName(), obj.getValue());
			}
		}
		return props;
	}

	public void setConfig(List<ProducerConsumerConfiguration> producerConsumerConfigs) {
		if (producerConsumerConfigs == null)
			return;
		ProducerConsumerConfiguration prevconfig = null;
		String prevGroupId = "";
		Properties props = null;
		for (ProducerConsumerConfiguration config : producerConsumerConfigs) {
			if (prevconfig == null || !config.getTopicName().equals(prevconfig.getTopicName())
					|| !config.getConfig().equals(prevconfig.getConfig())) {
				if (prevconfig != null) {
					setProducerConsumer(prevconfig, props, prevGroupId);
				}
				LOG.info("Next topic or type found.");
				props = new Properties();
				prevconfig = config;
				prevGroupId = "";
			}
			LOG.info("topic->{}:config->{}:key->{}:value->{}", new Object[] { config.getTopicName(), config.getConfig(),
					config.getPropertyName(), config.getPropertyValue() });
			if (config.getPropertyName().equalsIgnoreCase("group.id")) {
				prevGroupId = config.getPropertyValue();
			}
			props.put(config.getPropertyName(), config.getPropertyValue());
		}
		setProducerConsumer(prevconfig, props, prevGroupId);
	}

	private void setProducerConsumer(ProducerConsumerConfiguration prevconfig, Properties props, String prevGroupId) {
		if (PRODUCER_TYPE.equalsIgnoreCase(prevconfig.getConfig())) {
			addProducerConfig(prevconfig.getTopicName(), props);
		} else if (CONSUMER_TYPE.equalsIgnoreCase(prevconfig.getConfig())) {
			if (!prevGroupId.equals("")) {
				LOG.info("Preparing consumer for group {}", prevGroupId);
				addConsumerConfig(prevconfig.getTopicName(), props, prevGroupId);
			} else {
				LOG.error("Group id not found for topic {}", prevconfig.getTopicName());
			}
		} else {
			LOG.error("Consumer type {} is not found.", prevconfig.getConfig());
		}
	}

	public void addProducerConfig(String type, Properties props) {
		ProducerConfig producerConfig = new ProducerConfig(props);
		typeToProducerConfig.put(type, producerConfig);
		typeToProducer.put(type, new Producer<String, Serializable>(producerConfig));
	}

	public void addConsumerConfig(String type, Properties props, String groupId) {
		props.put("group.id", groupId); // overriding group id even if present
		Map<String, ConsumerConfig> consumerGroupConfig = typeToConsumerGroupConfig.get(type);
		if (consumerGroupConfig == null) {
			consumerGroupConfig = new HashMap<String, ConsumerConfig>();
		}
		consumerGroupConfig.put(groupId, new ConsumerConfig(props));
		if (props.getProperty("num.partitions") != null)
			typeToNumOfPartitions.put(type, Integer.parseInt(props.getProperty("num.partitions")));
		typeToConsumerGroupConfig.put(type, consumerGroupConfig);
	}

	public Integer getNumOfPartitionsForType(String type) {
		return typeToNumOfPartitions.get(type);
	}

	public Producer<String, Serializable> getProducer(String type) {
		return typeToProducer.get(type);
	}

	public ProducerConfig getProducerConfig(String type) {
		return typeToProducerConfig.get(type);
	}

	public ConsumerConfig getConsumerConfig(String type, String groupId) {
		Map<String, ConsumerConfig> map = typeToConsumerGroupConfig.get(type);
		if (map != null) {
			return map.get(groupId);
		}
		return null;
	}

	public static void main(String args[]) {
		System.out.println(new ProducerConsumerConfigCache().loadXML(
				ProducerConsumerConfigObj.class.getClassLoader().getResource("ProducerConsumerConfig.xml").getFile()));
	}

	@Override
	public String toString() {
		return "ProducerConsumerConfigCache [typeToProducerConfig=" + typeToProducerConfig + ", typeToProducer="
				+ typeToProducer + ", typeToConsumerGroupConfig=" + typeToConsumerGroupConfig
				+ ", typeToNumOfPartitions=" + typeToNumOfPartitions + "]";
	}

}
