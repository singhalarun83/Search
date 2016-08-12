package com.erosnow.search.common.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class ProducerConfigObj {

	private String name;
	private String config;
	private String groupId;
	private List<KeyValueObj> properties;

	public String getName() {
		return name;
	}

	@XmlAttribute(name = "name")
	public void setName(String name) {
		this.name = name;
	}

	public String getConfig() {
		return config;
	}

	@XmlAttribute(name = "config")
	public void setConfig(String config) {
		this.config = config;
	}

	public String getGroupId() {
		return groupId;
	}

	@XmlAttribute(name = "groupId")
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public List<KeyValueObj> getProperties() {
		return properties;
	}

	@XmlElement(name = "property")
	public void setProperties(List<KeyValueObj> properties) {
		this.properties = properties;
	}

	@Override
	public String toString() {
		return "ProducerConfigObj [name=" + name + ", config=" + config + ", groupId=" + groupId + ", properties="
				+ properties + "]";
	}
	
}
