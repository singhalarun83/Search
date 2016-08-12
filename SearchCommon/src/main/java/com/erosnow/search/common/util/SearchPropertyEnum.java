package com.erosnow.search.common.util;

public enum SearchPropertyEnum {
	SEARCH_API_DEFAULT_LIMIT("search.api.default.limit", "200"),
	SEARCH_MAX_NUMBER_OF_RESULTS("search.max.results", "50"),
	SEARCH_MAX_START("search.max.start", "2000"),
	SEARCH_KEYWORD_LIMIT("search.keyword.limit", "10"),
	SEARCH_KEYWORD_SPLIT_REGEX("search.keyword.split.regex", "[^a-z0-9]+|(?<=[a-z])(?=[0-9])|(?<=[0-9])(?=[a-z])"),
	SEARCH_KEYWORD_CHAR_LIMIT("search.keyword.char.limit", "200"),
	ENABLE_SHARDS_TOLERANT("enable.shards.tolerant", "true"),
	ENABLE_SHARDS_INFO("enable.shards.tolerant", "true"),
	SOLR_QUERY_HTTPCLIENT_POST("solr.query.httpclient.post", "0"),
	MAX_SOLR_QUERY_LENGTH_ALLOWED("max.solr.query.lenght.allowed", "25000"),
	
	PARTIAL_SEARCH_ENABLED("partial.search.enabled", "true"),
	PARTIAL_SEARCH_MM("partial.search.mm", "1"), 
	
	SPELLCHECK_ENABLED("spellcheck.enabled", "true"),
	SPELLCHECK_ACCURACY("spellcheck.accuracy", "0.7"),
    SPELLCHECK_LOWER_ACCURACY("spellcheck.lower.accuracy", "0.6"),
    SPELLCHECK_ACCURACY_LIMIT("spellcheck.accuracy.limit", "4"),
    SPELLCHECK_ONLY_MORE_POPULAR("spellcheck.showOnlyMorePopular", "false"),
    SPELLCHECK_COUNT("spellcheck.count", "5"),
    SPELLCHECK_COLLATION_COUNT("spellcheck.collation.count", "2"),
    SPELLCHECK_COLLATION_TRIES("spellcheck.collation.tries", "2"),
    SPELLCHECK_MINIMUM_MATCH("spellcheck.mm", "10"),
    SPELLCHECK_MAX_COLLATE_DOCS("spellcheck.maxCollateDocs", "0"),
    SPELLCHECK_MAX_EVALUATIONS("spellcheck.maxCollationEvaluations", "10000"),
    SPELLCHECK_MIN_BREAK_LENGTH("spellcheck.min.break.length", "7"),
    SPELLCHECK_SORT_HITS("spellcheck.sort.hits", "true"),
    SPELLCHECK_DEFTYPE("spellcheck.defType", "dismax"),
    SPELLCHECK_MAX_RESULTS_FOR_SUGGEST("spellcheck.max.result.for.suggest", "10"),
    SPELLCHECK_MAX_RESULTS_FOR_SUGGEST_ENABLE("spellcheck.max.result.for.suggest.enable", "false"),
	
	RELOAD_CACHE_ENABLED("reload.cache.enabled","true"),
	RELOAD_CACHE_REFERNECE_TIME("reload.cache.reference.time","2016-06-06 15:00:00"),
	RELOAD_CACHE_REFERNECE_FORMAT("reload.cache.reference.format","yyyy-MM-dd HH:mm:ss"),
	RELOAD_CACHE_INTERVAL("reload.cache.interval","900000"),
	RELOAD_CACHE_PASSWORD("reload.cache.password","en@1234$"),
	
	GRAPHITE_HOST("graphite.host","localhost"),
	GRAPHITE_PORT("graphite.port","2003"),
	GRAPHITE_REPORT_ENABLE("graphite.report.enable","true"),
	GRAPHITE_COMPONENT_INDEXER("graphite.component.indexer","idx"),
	APPLICATION_ENVIRONMENT("application.environment","dev"),
	
	CACHE_CLEANUP_INTERVAL("cache.cleanup.interval", "60000"),
	INDEXER_FLUSH_INTERVAL("indexer.flush.interval", "600000"),
	AGGR_SAMPLE_CACHE_MAX_SIZE("aggr.cams.cache.max.size", "100000"),
	AGGR_SAMPLE_CACHE_TTL("aggr.cams.cache.ttl", "30000"),
	
	FLUSH_CORE_THREAD_POOL_SIZE("flush.core.thread.pool.size", "10"),
	KAFKA_PARTITION_BY_ACCUMULATION("kafka.partition.by.accumulation", "true"),
	NUMBER_OF_PARTITION("number.of.partition", "10"), 
	
	INDEXER_SOLR_TIMEOUT("pog.solr.timeout", "10000"),
    INDEXER_SOLR_CONNECTION_TIMEOUT("pog.solr.connection.timeout", "10000"),
    INDEXER_SOLR_CONNECTION_PER_HOST("pog.solr.connection.per.host", "1024"),
    INDEXER_SOLR_MAX_TOTAL_CONNECTION("pog.solr.max.total.connection", "4096"),
    INDEXER_SOLR_MAX_RETRY("pog.solr.max.retry", "1"),
    
    INDEXER_DELTA_QUERY_INTERVAL("delta.query.interval.day", "1"),
    INDEXER_DELETE_QUERY_INTERVAL("delete.query.interval.day", "1"),
    
    SEARCHER_SOLR_TIMEOUT("pog.solr.timeout", "10000"),
    SEARCHER_SOLR_CONNECTION_TIMEOUT("pog.solr.connection.timeout", "10000"),
    SEARCHER_SOLR_CONNECTION_PER_HOST("pog.solr.connection.per.host", "1024"),
    SEARCHER_SOLR_MAX_TOTAL_CONNECTION("pog.solr.max.total.connection", "4096"),
    SEARCHER_SOLR_MAX_RETRY("pog.solr.max.retry", "1"),
	
	
	TEST_PROPERTY("testName","t1");

	private String name;
    private String value;

    private SearchPropertyEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }
}
