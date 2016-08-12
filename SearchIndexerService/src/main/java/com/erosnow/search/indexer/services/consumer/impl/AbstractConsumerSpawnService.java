package com.erosnow.search.indexer.services.consumer.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.erosnow.search.indexer.services.consumer.ConsumerSpawnService;
import com.erosnow.search.indexer.services.dto.KafkaPushDTO;
import com.erosnow.search.indexer.services.dto.KafkaPushListDTO;

public abstract class AbstractConsumerSpawnService implements ConsumerSpawnService, ApplicationContextAware {
	protected ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public List<Serializable> partition(Serializable dtoSs, int numOfPartition) {
		List<KafkaPushDTO> dtos = getObjectType(dtoSs);
		Map<Long, List<KafkaPushDTO>> partitionData = new HashMap<Long, List<KafkaPushDTO>>();
		for (KafkaPushDTO dto : dtos) {
			Long key = null;
			if (dto.getKey() != null) {
				key = dto.getKey();
			} else {
				key = Long.valueOf(String.valueOf(dto.hashCode()));
			}
			Long partition = Long.valueOf(key.longValue() % numOfPartition);
			if (!partitionData.containsKey(partition)) {
				partitionData.put(partition, new ArrayList<KafkaPushDTO>());
			}
			partitionData.get(partition).add(dto);
		}
		List<Serializable> toReturn = new ArrayList<Serializable>();
		for (List<KafkaPushDTO> partitionDtos : partitionData.values()) {
			Long key = null;
			for (KafkaPushDTO dto : partitionDtos) {
				if (dto.getKey() != null) {
					key = dto.getKey();
					break;
				}
			}
			if (key == null) {
				key = Long.valueOf(String.valueOf(partitionDtos.get(0).hashCode()));
			}
			KafkaPushListDTO dto = new KafkaPushListDTO((Serializable) partitionDtos, key);
			toReturn.add(dto);
		}
		return toReturn;
	}

	public List<Serializable> partition(Serializable dtoSs) {
		List<KafkaPushDTO> dtos = getObjectType(dtoSs);
		List<Serializable> finalList = new ArrayList<Serializable>();
		for (KafkaPushDTO dto : dtos) {
			Long key = null;
			if (dto.getKey() != null) {
				key = dto.getKey();
			} else {
				key = Long.valueOf(String.valueOf(dto.hashCode()));
			}
			List<KafkaPushDTO> listOfDtos = new ArrayList<KafkaPushDTO>();
			listOfDtos.add(dto);
			KafkaPushListDTO dtoList = new KafkaPushListDTO((Serializable) listOfDtos, key);
			finalList.add(dtoList);
		}
		return finalList;
	}

	@SuppressWarnings("unchecked")
	public List<KafkaPushDTO> getObjectType(Serializable object) {
		List<KafkaPushDTO> pushedDtos = null;
		if (object instanceof KafkaPushDTO) {
			pushedDtos = new ArrayList<KafkaPushDTO>();
			pushedDtos.add((KafkaPushDTO) object);
		} else {
			pushedDtos = (List<KafkaPushDTO>) object;
		}
		return pushedDtos;
	}
}
