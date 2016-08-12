package com.erosnow.search.common.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IndexerLogger {

	private static final Logger LOG = LoggerFactory.getLogger(IndexerLogger.class);

	public static void log(String s) {
		LOG.info(s);
	}

	public static void logEnter(Object obj, Class clazz) {
		indexerLog("Entering listener with object ", obj, clazz.getSimpleName());
	}

	public static void logEnter(Object obj, String clazz) {
		indexerLog("Entering listener with object ", obj, clazz);
	}

	public static void logIndex(Object obj, Class clazz) {
		indexerLog("Going to index with object ", obj, clazz.getSimpleName());
	}

	public static void logIndex(Object obj, String clazz) {
		indexerLog("Going to index with object ", obj, clazz);
	}

	public static void logDBRead(Object obj, Class clazz) {
		indexerLog("Reading DB for object ", obj, clazz.getSimpleName());
	}

	public static void logDBRead(Object obj, String clazz) {
		indexerLog("Reading DB for object ", obj, clazz);
	}

	public static void logExit(Object obj, Class clazz) {
		indexerLog("Exiting listener with object ", obj, clazz.getSimpleName());
	}

	public static void logExit(Object obj, String clazz) {
		indexerLog("Exiting listener with object ", obj, clazz);
	}

	public static void logError(Object obj, Class clazz, String errorMsg) {
		indexerLog("Error while processing ", obj, clazz.getSimpleName(), errorMsg);
	}

	public static void logInfo(Object obj, Class clazz, String infoMsg) {
		indexerLog("", obj, clazz.getSimpleName(), infoMsg);
	}

	public static void logError(Object obj, String clazz, String errorMsg) {
		indexerLog("Error while processing ", obj, clazz, errorMsg);
	}

	public static void logInfo(Object obj, String clazz, String infoMsg) {
		indexerLog("", obj, clazz, infoMsg);
	}

	public static void indexerLog(String msg, Object arg0, String clazz) {
		indexerLog(msg, arg0, clazz, null);
	}

	public static void indexerLog(String msg, Object arg0, String clazz, String errorMsg) {
		StringBuilder builder = new StringBuilder(clazz).append(": ").append(msg).append(arg0);
		if (errorMsg != null) {
			builder.append(" (").append(errorMsg).append(")");
		}
		LOG.info(builder.toString());
	}

	public static void log(String s, Throwable t) {
		LOG.info(s, t);
	}

	public static void log(String s, Object t) {
		LOG.info(s, t);
	}

	public static void log(String s, Object t1, Object t2) {
		LOG.info(s, t1, t2);
	}

	public static void log(String s, Object... t) {
		LOG.info(s, t);
	}
}
