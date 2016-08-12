package com.erosnow.search.common.metrics;

import static com.codahale.metrics.MetricRegistry.name;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.jvm.FileDescriptorRatioGauge;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;

public class Metrics {

	public final static String REGISTRY_ATTRIBUTE = "com.codahale.metrics.servlet.InstrumentedFilter.registry";

	private final static MetricRegistry registry = new MetricRegistry();

	private Metrics() {
	}

	public static MetricRegistry getRegistry() {
		return registry;
	}

	static {
		registry.register(name("jvm", "gc"), new GarbageCollectorMetricSet());
		registry.register(name("jvm", "memory"), new MemoryUsageGaugeSet());
		registry.register(name("jvm", "thread-states"), new ThreadStatesGaugeSet());
		registry.register(name("jvm", "fd", "usage"), new FileDescriptorRatioGauge());
		registry.register(name("apis"), new CustomMetricSet());
	}

}
