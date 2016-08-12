package com.erosnow.search.common.metrics;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.graphite.Graphite;
import com.codahale.metrics.graphite.GraphiteReporter;

public class MetricsReporter {

	private GraphiteReporter reporter;

	public void reportToGraphite(String reportingPrefix, String hostname, int port) {
		if (reporter == null) {
			final Graphite graphite = new Graphite(new InetSocketAddress(hostname, port));
			reporter = GraphiteReporter.forRegistry(Metrics.getRegistry()).prefixedWith(reportingPrefix)
					.convertRatesTo(TimeUnit.SECONDS).convertDurationsTo(TimeUnit.MILLISECONDS).filter(MetricFilter.ALL)
					.build(graphite);
		}
		reporter.start(2, TimeUnit.SECONDS);
	}

	public void stopReporting() {
		if (reporter != null)
			reporter.stop();
	}

}
