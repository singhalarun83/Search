package com.erosnow.search.common.temp;

import javax.xml.bind.annotation.XmlAttribute;

public class SolrSchemaField implements Comparable<SolrSchemaField> {

	private String name = "";
	private String type = "";
	private String indexed = "";
	private String stored = "";
	private String required = "";
	private String default1 = "";
	private String multiValued = "";

	@XmlAttribute(name = "name")
	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute(name = "type")
	public void setType(String type) {
		this.type = type;
	}

	@XmlAttribute(name = "indexed")
	public void setIndexed(String indexed) {
		this.indexed = indexed;
	}

	@XmlAttribute(name = "stored")
	public void setStored(String stored) {
		this.stored = stored;
	}

	@XmlAttribute(name = "required")
	public void setRequired(String required) {
		this.required = required;
	}

	@XmlAttribute(name = "default")
	public void setDefault1(String default1) {
		this.default1 = default1;
	}

	@XmlAttribute(name = "multiValued")
	public void setMultiValued(String multiValued) {
		this.multiValued = multiValued;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public String getIndexed() {
		return indexed;
	}

	public String getStored() {
		return stored;
	}

	public String getRequired() {
		return required;
	}

	public String getDefault1() {
		return default1;
	}

	public String getMultiValued() {
		return multiValued;
	}

	@Override
	public String toString() {
		return "SolrSchemaField [name=" + name + ", type=" + type + ", indexed=" + indexed + ", stored=" + stored
				+ ", required=" + required + ", default1=" + default1 + ", multiValued=" + multiValued + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SolrSchemaField other = (SolrSchemaField) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public int compareTo(SolrSchemaField o) {
		return this.getName().compareTo(o.getName());
	}

}
