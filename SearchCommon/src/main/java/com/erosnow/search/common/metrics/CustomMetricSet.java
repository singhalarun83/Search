package com.erosnow.search.common.metrics;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.codahale.metrics.Meter;
import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricSet;
import com.codahale.metrics.Timer;
import com.erosnow.search.common.util.Listener;

@Service
public class CustomMetricSet implements MetricSet {

	private static final String API_RESP_TIME_METRIC_NAME = "_API_RESP_TIME";
	private static final String PACKETS_STAT_METRIC_NAME = "_PACKETS_STAT";
	private static final String PACKET_SIZE_STAT = "_PACKET_SIZE_STAT";
	private static Map<String, Metric> allMetrics = new ConcurrentHashMap<String, Metric>();

	@PostConstruct
	public void init() throws Exception {
		for (ApiStatsConfig config : ApiStatsConfig.values()) {
			String key = config + API_RESP_TIME_METRIC_NAME;
			allMetrics.put(key, new Timer());
		}
		Field fieldlist[] = Listener.class.getDeclaredFields();
		for (int i = 0; i < fieldlist.length; i++) {
			String key = fieldlist[i].get(null) + PACKETS_STAT_METRIC_NAME;
			allMetrics.put(key, new Meter());
			String key1 = fieldlist[i].get(null) + PACKET_SIZE_STAT;
			allMetrics.put(key1, new Timer());
		}
	}

	public void measureApiResponseTime(String apiName, long duration) {
		String key = apiName + API_RESP_TIME_METRIC_NAME;
		if (allMetrics.get(key) != null) {
			Timer t = (Timer) allMetrics.get(key);
			t.update(duration, TimeUnit.MILLISECONDS);
		}
	}

	public void updateQueueStats(String listenerName) {
		String key = listenerName + PACKETS_STAT_METRIC_NAME;
		if (allMetrics.get(key) != null) {
			Meter m = (Meter) allMetrics.get(key);
			m.mark();
		}
	}

	public void updateQueuePacketSizeStats(String queueName, long packetSize) {
		String key = queueName + PACKET_SIZE_STAT;
		if (allMetrics.get(key) != null) {
			Timer t = (Timer) allMetrics.get(key);
			t.update(packetSize, TimeUnit.MILLISECONDS);
		}
	}

	@Override
	public Map<String, Metric> getMetrics() {
		System.out.println(allMetrics.size());
		return Collections.unmodifiableMap(allMetrics);
	}

	public static void main(String args[]) {
		try {
			Field fieldlist[] = Listener.class.getDeclaredFields();
			for (int i = 0; i < fieldlist.length; i++) {
				System.out.println("name= " + fieldlist[i].get(null));
				System.out.println("-----");
			}
		} catch (Throwable e) {
			System.err.println(e);
		}
	}

}
