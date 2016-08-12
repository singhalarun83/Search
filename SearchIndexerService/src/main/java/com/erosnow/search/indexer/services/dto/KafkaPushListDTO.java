package com.erosnow.search.indexer.services.dto;

import java.io.Serializable;

public class KafkaPushListDTO implements Serializable {

	private static final long serialVersionUID = -412949474517353036L;

	private Serializable dtos;
	private Long key;

	public KafkaPushListDTO(Serializable dtos, Long key) {
		this.key = key;
		this.dtos = dtos;
	}

	public Serializable getDtos() {
		return dtos;
	}

	public Long getKey() {
		return key;
	}

}
