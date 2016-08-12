package com.erosnow.search.common.model;

public class PropertyValueAttribute {

	private String defaultValue;

	PropertyValueAttribute() {
	}

	public PropertyValueAttribute(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

}
