package com.erosnow.search.common.activemq;

import java.io.Serializable;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.MessageListener;

public interface ActiveMQManager {

	Long registerPublisher(String queueName, String url, String userName, String password) throws JMSException;

	Long registerPublisher(String queueName, String url, String userName, String password, boolean transacted,
			int acknowledgeMode, int deliveryMode) throws JMSException;

	void unregisterPublisher(Long token) throws JMSException;

	<T extends Serializable> void publish(Long token, List<T> messages) throws JMSException;

	<T extends Serializable> void publish(Long token, T message) throws JMSException;

	Long registerSubscriber(String queueName, String url, String userName, String password, MessageListener listener)
			throws JMSException;

	Long registerSubscriber(String queueName, String url, String userName, String password, MessageListener listener,
			boolean transacted, int acknowledgeMode) throws JMSException;

	void unregisterSubscriber(Long token) throws JMSException;
}
