package com.erosnow.search.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "producer_consumer_configuration")
public class ProducerConsumerConfiguration implements Serializable {

	private static final long serialVersionUID = -1003515733604437526L;
	private int id;
	private String topicName;
	private String config;
	private String propertyName;
	private String propertyValue;

	@Column(name = "topic_name", nullable = false, length = 100)
	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	@Column(name = "config", nullable = false, length = 100)
	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
	}

	@Column(name = "property_name", nullable = false, length = 100)
	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	@Column(name = "property_value", nullable = false, length = 100)
	public String getPropertyValue() {
		return propertyValue;
	}

	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "ProducerConsumer [id=" + id + ", topicName=" + topicName + ", config=" + config + ", propertyName="
				+ propertyName + ", propertyValue=" + propertyValue + "]";
	}

}
