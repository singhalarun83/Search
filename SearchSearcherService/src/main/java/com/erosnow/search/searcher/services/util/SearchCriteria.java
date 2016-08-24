package com.erosnow.search.searcher.services.util;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.common.params.FacetParams;

public class SearchCriteria {

	private SolrQuery q;

	public SearchCriteria() {
		super();
		q = new SolrQuery();
	}

	public SearchCriteria setBF(String boostquery) {
		this.q.add("bf", boostquery);
		return this;
	}

	public SearchCriteria filterEq(String fieldName, String value) {
		q.addFilterQuery(fieldName + ":" + value);
		return this;
	}

	public SearchCriteria setFacetQuery(String query, int limit, int minCount) {
		q.addFacetQuery(query);
		q.setFacetLimit(limit);
		q.setFacetMinCount(minCount);
		return this;
	}

	public SearchCriteria filterNotEqual(String fieldName, String value) {
		q.addFilterQuery("-" + fieldName + ":" + value);
		return this;
	}

	public SearchCriteria filterRange(String fieldName, String min, String max) {
		q.addFilterQuery(fieldName + ":[" + min + " TO " + max + "]");
		return this;
	}

	public SearchCriteria addFilter(String filter) {
		q.addFilterQuery(filter);
		return this;
	}

	public SearchCriteria filterDirect(String filter) {
		q.addFilterQuery(filter);
		return this;
	}

	public SearchCriteria removeBoost() {
		q.remove("bf");
		q.remove("boost");
		return this;
	}

	public SearchCriteria addBoost(String booststr) {
		q.add("boost", booststr);
		return this;
	}

	public SearchCriteria filterOr(Map<String, List<String>> fqMap) {
		if (fqMap.isEmpty()) {
			return this;
		}

		FilterStringBuilder filterBuilder = new FilterStringBuilder();
		for (String fieldName : fqMap.keySet()) {
			if (!fqMap.get(fieldName).isEmpty()) {
				FilterStringBuilder fsb = new FilterStringBuilder();
				for (String val : fqMap.get(fieldName)) {
					fsb.or(val);
				}
				filterBuilder.or(fieldName + ":" + fsb.buildFilter());
			}
		}
		q.addFilterQuery(filterBuilder.buildFilter());
		return this;
	}

	public SearchCriteria removeFilterEq(String fieldName, String value) {
		q.removeFilterQuery(fieldName + ":" + value);
		return this;
	}

	public SearchCriteria removeFilterRange(String fieldName, String min, String max) {
		q.removeFilterQuery(fieldName + ":[" + min + " TO " + max + "]");
		return this;
	}

	public SearchCriteria sort(String field, SolrQuery.ORDER order) {
		q.addSort(new SolrQuery.SortClause(field, order));
		return this;
	}

	public SearchCriteria preappendsort(String field, SolrQuery.ORDER order) {
		List<SolrQuery.SortClause> clauses = q.getSorts();
		q.clearSorts();
		if (clauses == null || clauses.size() == 0 || !field.equalsIgnoreCase(clauses.get(0).getItem())) {
			q.addSort(new SolrQuery.SortClause(field, order));
		}
		if (null != clauses) {
			for (SolrQuery.SortClause c : clauses) {
				q.addSort(c);
			}
		}

		return this;
	}

	public SearchCriteria query(String q) {
		this.q.setQuery(q);
		return this;
	}

	public SearchCriteria fl() {
		q.setParam("fl", "*");
		return this;
	}

	public SearchCriteria fl(String[] fields) {
		StringBuilder sb = new StringBuilder();
		for (String field : fields) {
			sb.append(field).append(',');
		}
		sb.deleteCharAt(sb.length() - 1);
		q.setParam("fl", sb.toString());
		return this;
	}

	public SearchCriteria facetSort(String sort) {
		q.setFacetSort(sort);
		return this;
	}

	public SearchCriteria facetField(String field, int limit, int minCount, String sort) {
		q.setFacet(true);
		q.setFacetMinCount(minCount);
		q.setFacetLimit(limit);
		addFacetField(field);
		return this;
	}

	public SearchCriteria removeFacetField(String fieldName) {
		q.removeFacetField(fieldName);
		return this;
	}

	public SearchCriteria addFacetField(String fieldName) {
		q.addFacetField(fieldName);
		return this;
	}

	public SearchCriteria facetRange(String field, double start, double end, double gap) {
		q.addNumericRangeFacet(field, start, end, gap);
		return this;
	}

	public SearchCriteria facetRangeInclusive(String field, double start, double end, double gap) {
		q.addNumericRangeFacet(field, start, end, gap);
		q.add(String.format(Locale.ROOT, "f.%s.%s", field, FacetParams.FACET_RANGE_INCLUDE), "lower");
		q.add(String.format(Locale.ROOT, "f.%s.%s", field, FacetParams.FACET_RANGE_INCLUDE), "upper");
		return this;
	}

	public SearchCriteria groupField(String field, int limit, boolean ngGroup, boolean truncate) {
		q.setParam("group", "true");
		q.setParam("group.field", field);
		q.setParam("group.limit", String.valueOf(limit));
		q.setParam("group.ngroups", String.valueOf(ngGroup));
		q.setParam("group.truncate", String.valueOf(truncate));
		return this;
	}

	public SearchCriteria rows(int rows) {
		q.setRows(rows);
		return this;
	}

	public SearchCriteria start(int start) {
		q.setStart(start);
		return this;
	}

	public SearchCriteria mm(String mm) {
		q.setParam("mm", String.valueOf(mm));
		return this;
	}

	public SearchCriteria shards(String slaveUrl) {
		q.setParam("shards", String.valueOf(slaveUrl));
		return this;
	}

	public SearchCriteria spellCheck(String keyword, Float accuracy, boolean showOnlyMorePopular,
			Integer spellCheckCount, Integer noOfCollations, Integer noOfCollationTries, Integer mm,
			Integer collateMaxCollectDocs, Integer maxCollationEvaluations, String defType, String maxResultsForSuggest,
			boolean maxResultsForSuggestEnable) {
		q.setParam("spellcheck.q", keyword);
		q.setParam("spellcheck", "true");
		q.setParam("spellcheck.collate", "true");
		if (accuracy != null)
			q.setParam("spellcheck.accuracy", accuracy.toString());
		q.setParam("spellcheck.onlyMorePopular", showOnlyMorePopular);
		if (spellCheckCount != null)
			q.setParam("spellcheck.count", spellCheckCount.toString());
		if (noOfCollationTries != null)
			q.setParam("spellcheck.maxCollationTries", noOfCollationTries.toString());
		q.setParam("spellcheck.collateExtendedResults", "true");
		if (noOfCollations != null)
			q.setParam("spellcheck.maxCollations", noOfCollations.toString());
		if (mm != null)
			q.setParam("spellcheck.collateParam.mm", mm.toString());
		if (collateMaxCollectDocs != null)
			q.setParam("spellcheck.collateMaxCollectDocs", collateMaxCollectDocs.toString());
		if (maxCollationEvaluations != null)
			q.setParam("spellcheck.maxCollationEvaluations", maxCollationEvaluations.toString());
		if (defType != null)
			q.setParam("spellcheck.collateParam.defType", defType);
		if (maxResultsForSuggestEnable && maxResultsForSuggest != null) {
			q.setParam("spellcheck.maxResultsForSuggest", maxResultsForSuggest);
		}
		return this;
	}

	public SearchCriteria removeSpellCheck() {
		q.remove("spellcheck.q");
		q.remove("spellcheck");
		q.remove("spellcheck.collate");
		q.remove("spellcheck.accuracy");
		q.remove("spellcheck.onlyMorePopular");
		q.remove("spellcheck.count");
		q.remove("spellcheck.maxCollationTries");
		q.remove("spellcheck.collateExtendedResults");
		q.remove("spellcheck.maxCollations");
		q.remove("spellcheck.collateParam.mm");
		q.remove("spellcheck.collateMaxCollectDocs");
		q.remove("spellcheck.maxCollationEvaluations");
		q.remove("spellcheck.collateParam.mm");
		q.remove("spellcheck.collateMaxCollectDocs");
		q.remove("spellcheck.maxCollationEvaluations");
		q.remove("spellcheck.collateParam.defType");
		q.remove("spellcheck.maxResultsForSuggest");
		return this;
	}

	public SearchCriteria spellCheckBuild(String keyword) {
		q.setParam("spellcheck.build", "true");
		return this;
	}

	public SearchCriteria geo(double lat, double lon, long distance, long maxDistance) {
		StringBuilder sb = new StringBuilder();
		sb.append("{!bbox score=distance sfield=store");
		if (distance == maxDistance) {
			sb.append(" filter=false");
		}
		sb.append(" pt=" + lat + "," + lon);
		sb.append(" d=" + String.valueOf(distance) + "}");
		q.setQuery(sb.toString());
		return this;
	}

	public SearchCriteria addFieldList(String name, String[] values) {
		q.setParam(name, values);
		return this;
	}

	public SearchCriteria setFieldList(String... fields) {
		q.setFields(fields);
		return this;
	}

	public SolrQuery buildQuery() {
		return q;
	}

	public static class FilterStringBuilder {
		private StringBuilder sb;

		public FilterStringBuilder() {
			sb = new StringBuilder();
		}

		public FilterStringBuilder or(String s) {
			if (sb.length() > 1) {
				sb.delete(sb.length() - 1, sb.length());
				sb.append(" OR ").append(s);
			} else {
				sb.append("(" + s);
			}
			sb.append(")");
			return this;
		}

		public FilterStringBuilder and(String s) {
			if (sb.length() > 1) {
				sb.delete(sb.length() - 1, sb.length());
				sb.append(" AND ").append(s);
			} else {
				sb.append("(" + s);
			}
			sb.append(")");
			return this;
		}

		public FilterStringBuilder range(String min, String max) {
			sb = new StringBuilder("[");
			sb.append(min).append(" TO ").append(max).append("]");
			return this;
		}

		public FilterStringBuilder clear() {
			sb = new StringBuilder();
			return this;
		}

		public String buildFilter() {
			String s = sb.toString();
			clear();
			return s;
		}

	}

	public SearchCriteria multBoost(String func) {
		q.setParam("boost", func);
		return this;
	}
}
