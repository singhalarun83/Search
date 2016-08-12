package com.erosnow.search.common.temp;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "fields")
public class SolrSchemaFields {
	
	@Override
	public String toString() {
		return "SolrSchemaFields [fields=" + fields + "]";
	}
	private List<SolrSchemaField> fields;

	public List<SolrSchemaField> getFields() {
		return fields;
	}
	@XmlElement(name = "field")
	public void setFields(List<SolrSchemaField> fields) {
		this.fields = fields;
	}
	
	

}
