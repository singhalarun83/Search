package com.erosnow.search.common.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;

public class CommonUtil {

	public static Map<String, Object> getAllKnownProperties(Environment env) {
		Map<String, Object> rtn = new HashMap<>();
		if (env instanceof ConfigurableEnvironment) {
			for (PropertySource<?> propertySource : ((ConfigurableEnvironment) env).getPropertySources()) {
				if (propertySource.getClass().getSimpleName().equals("ResourcePropertySource")
						&& propertySource instanceof EnumerablePropertySource) {
					for (String key : ((EnumerablePropertySource) propertySource).getPropertyNames()) {
						rtn.put(key, propertySource.getProperty(key));
					}
				}
			}
		}
		return rtn;
	}

	public static long getCurrentTimeMillis() {
		return System.currentTimeMillis();
	}
}
