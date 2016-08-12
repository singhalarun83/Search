package com.erosnow.search.common.cache.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.erosnow.search.common.cache.Cache;
import com.erosnow.search.common.entity.SearchProperty;
import com.erosnow.search.common.log.DebugLogger;
import com.erosnow.search.common.model.PropertyValueAttribute;
import com.erosnow.search.common.util.DateUtils;
import com.erosnow.search.common.util.SearchPropertyEnum;

@Cache(name = "searchPropertyCache")
public class SearchPropertyCache {

	private static final Logger LOG = LoggerFactory.getLogger(SearchPropertyCache.class);

	public SearchPropertyCache(List<SearchProperty> dbProperties) {
		for (SearchProperty property : dbProperties) {
			DebugLogger.log(property);
			putInCache(property.getName(), property.getValue());
		}
	}

	private void putInCache(String key, String value) {
		if (properties.containsKey(key)) {
			properties.get(key).setDefaultValue(value);
		} else {
			properties.put(key, new PropertyValueAttribute(value));
		}
	}

	public void overrideFileProperties(Map<String, Object> properties) {
		for (Map.Entry<String, Object> entry : properties.entrySet()) {
			LOG.info("Override: " + entry.getKey() + " " + entry.getValue());
			putInCache(entry.getKey(), entry.getValue().toString());
		}
	}

	public void clear() {
		properties.clear();
	}

	private final Map<String, PropertyValueAttribute> properties = new HashMap<String, PropertyValueAttribute>();

	public PropertyValueAttribute getPropertyValue(SearchPropertyEnum property) {
		PropertyValueAttribute pValue = properties.get(property.getName());
		if (pValue == null) {
			pValue = new PropertyValueAttribute(property.getValue());
		}
		return pValue;
	}

	public String getProperty(String property) {
		PropertyValueAttribute pValue = properties.get(property);
		if (pValue == null)
			return null;
		else
			return pValue.getDefaultValue();
	}

	public String getProperty(SearchPropertyEnum p) {
		String value = getProperty(p.getName());
		if (StringUtils.isEmpty(value)) {
			return p.getValue();
		}
		return value;

	}

	public Boolean getBooleanProperty(SearchPropertyEnum p) {
		String prop = getProperty(p);
		if ("1".equals(prop) || "true".equalsIgnoreCase(prop)) {
			return true;
		} else {
			return false;
		}
	}

	public Double getDoubleProperty(SearchPropertyEnum p) {
		String value = getProperty(p);
		try {
			return Double.parseDouble(value);
		} catch (NumberFormatException ex) {
			LOG.error("Incorrect property: {} found in file. Default value will be returned {}.", p.getName(),
					p.getValue());
			return Double.parseDouble(p.getValue());
		}
	}

	public Integer getIntegerProperty(SearchPropertyEnum p) {
		String value = getProperty(p);
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException ex) {
			LOG.error("Incorrect property: {} found in file. Default value will be returned {}.", p.getName(),
					p.getValue());
			return Integer.parseInt(p.getValue());
		}
	}

	public Long getLongProperty(SearchPropertyEnum p) {
		String value = getProperty(p);
		try {
			return Long.parseLong(value);
		} catch (NumberFormatException ex) {
			LOG.error("Incorrect property: {} found in file. Default value will be returned {}.", p.getName(),
					p.getValue());
			return Long.parseLong(p.getValue());
		}
	}

	public List<Long> getListLongProperty(SearchPropertyEnum p) {
		List<Long> response = new ArrayList<Long>();
		for (String v : getProperty(p).split(",")) {
			try {
				response.add(Long.parseLong(v));
			} catch (NumberFormatException ex) {
				LOG.error(
						"Incorrect property: {} found in file. Values already added or empty list will be returned {}.",
						p.getName(), response.toString());
			}
		}
		return response;
	}

	public List<Integer> getListProperty(SearchPropertyEnum p) {
		List<Integer> response = new ArrayList<Integer>();
		for (String v : getProperty(p).split(",")) {
			try {
				response.add(Integer.parseInt(v));
			} catch (NumberFormatException ex) {
				LOG.error(
						"Incorrect property: {} found in file. Values already added or empty list will be returned {}.",
						p.getName(), response.toString());
			}
		}
		return response;
	}

	public List<String> getListStringProperty(SearchPropertyEnum p, String splitter) {
		List<String> response = new ArrayList<String>();
		for (String v : getProperty(p).split(splitter)) {
			response.add(v);
		}
		return response;
	}

	public List<String> getListStringProperty(SearchPropertyEnum p) {
		return getListStringProperty(p, ",");
	}

	public Map<String, String> getAllPropertiesByPrefix(String prefix) {
		Map<String, String> propertyMap = new HashMap<String, String>();
		for (Entry<String, PropertyValueAttribute> property : properties.entrySet()) {
			if (property.getKey().startsWith(prefix)) {
				propertyMap.put(property.getKey().trim().replaceAll(prefix, ""), getProperty(property.getKey()));
			}
		}
		return propertyMap;
	}

	public Date getReloadCacheReferenceTime() {

		Date date = null;
		if (!StringUtils.isEmpty(getProperty(SearchPropertyEnum.RELOAD_CACHE_REFERNECE_TIME))) {
			try {
				date = new Date(DateUtils.getNextInterval(DateUtils.getCurrentDate(),
						DateUtils.stringToDate(getProperty(SearchPropertyEnum.RELOAD_CACHE_REFERNECE_TIME),
								getProperty(SearchPropertyEnum.RELOAD_CACHE_REFERNECE_FORMAT)),
						getIntegerProperty(SearchPropertyEnum.RELOAD_CACHE_INTERVAL)));
			} catch (Exception e) {
				LOG.error("Incorrect property found in file for reload cache reference time.");
				date = DateUtils.getCurrentDate();
			}
		} else {
			date = DateUtils.getCurrentDate();
		}
		return date;
	}
}
