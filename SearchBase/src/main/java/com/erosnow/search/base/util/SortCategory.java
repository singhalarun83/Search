package com.erosnow.search.base.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public enum SortCategory {
	RELEVANCY("rlvncy");

	private String value;
	private boolean visible;

	private SortCategory(String value, boolean visible) {
		this.value = value;
		this.visible = visible;
	}

	private SortCategory(String value) {
		this.value = value;
		this.visible = true;
	}

	public boolean isVisible() {
		return visible;
	}

	public String getValue() {
		return value;
	}

	public static SortCategory fromString(String value) {
		if (!StringUtils.isBlank(value)) {
			for (SortCategory sortCategory : SortCategory.values()) {
				if (sortCategory.getValue().equals(value)) {
					return sortCategory;
				}
			}
		}
		return RELEVANCY;
	}

	public static List<SortCategory> getAllVisible() {
		List<SortCategory> solrSortCategories = new ArrayList<SortCategory>();
		for (SortCategory solrSortCategory : SortCategory.values()) {
			if (solrSortCategory.isVisible()) {
				solrSortCategories.add(solrSortCategory);
			}
		}
		return solrSortCategories;
	}
}
