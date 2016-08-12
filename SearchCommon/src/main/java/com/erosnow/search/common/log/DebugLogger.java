package com.erosnow.search.common.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DebugLogger {

	private static final Logger LOG = LoggerFactory.getLogger(DebugLogger.class);

	public static void log(String s) {
		LOG.info(s);
	}

	public static void log(Object s) {
		LOG.info(s.toString());
	}
	
	public static void log(String s, Throwable t) {
		LOG.info(s, t);
	}

	public static void log(String s, Object t) {
		LOG.info(s, t);
	}

	public static void log(String s, Object o1, Object o2) {
		LOG.info(s, o1, o2);
	}

	public static void log(String s, Object... o) {
		LOG.info(s, o);
	}
}
