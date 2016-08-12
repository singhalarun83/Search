package com.erosnow.search.common.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "consumer_type_configuration")
public class ConsumerTypeConfiguration {
	private Integer id;
	private String name;
	private String queueName;
	private String queueUrl;
	private Integer indexSize = 25;
	private Integer flushInterval = 10;
	private Date created;
	private Date updated;
	private boolean enabled;
	private String queueType;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "queue_name")
	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	@Column(name = "queue_url")
	public String getQueueUrl() {
		return queueUrl;
	}

	public void setQueueUrl(String queueUrl) {
		this.queueUrl = queueUrl;
	}

	@Column(name = "created")
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	@Column(name = "updated")
	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	@Column(name = "enabled")
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Column(name = "index_size")
	public Integer getIndexSize() {
		return indexSize;
	}

	public void setIndexSize(Integer indexSize) {
		this.indexSize = indexSize;
	}

	@Column(name = "flush_interval")
	public Integer getFlushInterval() {
		return flushInterval;
	}

	public void setFlushInterval(Integer flushInterval) {
		this.flushInterval = flushInterval;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConsumerTypeConfiguration other = (ConsumerTypeConfiguration) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public void setQueueType(String queueType) {
		this.queueType = queueType;
	}

	@Column(name = "queue_type")
	public String getQueueType() {
		return queueType;
	}

	@Override
	public String toString() {
		return "ConsumerTypeConfiguration [id=" + id + ", name=" + name + ", queueName=" + queueName + ", queueUrl="
				+ queueUrl + ", indexSize=" + indexSize + ", flushInterval=" + flushInterval + ", created=" + created
				+ ", updated=" + updated + ", enabled=" + enabled + ", queueType=" + queueType + "]";
	}

}
