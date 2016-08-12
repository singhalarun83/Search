package com.erosnow.search.indexer.services.dto;

import java.io.Serializable;

public class KafkaPushDTO implements Serializable {

	private static final long serialVersionUID = -2095880654258856962L;

	private Serializable payload;
	private Long key;

	public KafkaPushDTO(Serializable payload, Long key) {
		this.payload = payload;
		this.key = key;
	}

	public Serializable getPayload() {
		return payload;
	}

	public Long getKey() {
		return key;
	}
}
