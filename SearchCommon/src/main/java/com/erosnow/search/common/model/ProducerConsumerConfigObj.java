package com.erosnow.search.common.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "properties")
public class ProducerConsumerConfigObj {

	private List<ProducerConfigObj> producerConfigObj;

	public List<ProducerConfigObj> getProducerConfigObj() {
		return producerConfigObj;
	}

	@XmlElement(name = "topic")
	public void setProducerConfigObj(List<ProducerConfigObj> producerConfigObj) {
		this.producerConfigObj = producerConfigObj;
	}

	@Override
	public String toString() {
		return "ProducerConsumerConfigObj [producerConfigObj=" + producerConfigObj + "]";
	}

}
