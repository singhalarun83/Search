package com.erosnow.search.common.model;

import javax.xml.bind.annotation.XmlAttribute;

public class KeyValueObj {

	private String name;
	private String value;

	public String getName() {
		return name;
	}

	@XmlAttribute(name = "name")
	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	@XmlAttribute(name = "value")
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "KeyValueObj [name=" + name + ", value=" + value + "]";
	}

}
