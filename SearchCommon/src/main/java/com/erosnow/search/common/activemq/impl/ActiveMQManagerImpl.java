package com.erosnow.search.common.activemq.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.erosnow.search.common.activemq.ActiveMQManager;

@Component("activeMQManager")
public class ActiveMQManagerImpl implements ActiveMQManager {

	private static final Logger LOG = LoggerFactory.getLogger(ActiveMQManagerImpl.class);

	Map<Long, ActiveMQProducer> tokenToPublisherQueue = new ConcurrentHashMap<Long, ActiveMQProducer>();
	Map<Long, ActiveMQConsumer> tokenToSubscriberQueue = new ConcurrentHashMap<Long, ActiveMQConsumer>();

	private class ActiveMQProducer {
		private final Session session;
		private final MessageProducer messageProducer;
		private final Connection connection;

		public ActiveMQProducer(Session session, MessageProducer producer, Connection connection) {
			this.session = session;
			this.messageProducer = producer;
			this.connection = connection;
		}

		public Session getSession() {
			return session;
		}

		public MessageProducer getMessageProducer() {
			return messageProducer;
		}

		public Connection getConnection() {
			return connection;
		}
	}

	private class ActiveMQConsumer {
		private final MessageConsumer consumer;
		private final Session session;
		private final Connection connection;

		public ActiveMQConsumer(MessageConsumer consumer, Session session, Connection connection) {
			this.consumer = consumer;
			this.session = session;
			this.connection = connection;
		}

		public MessageConsumer getConsumer() {
			return consumer;
		}

		public Session getSession() {
			return session;
		}

		public Connection getConnection() {
			return connection;
		}
	}

	@Override
	public Long registerPublisher(String queueName, String url, String userName, String password) throws JMSException {
		return registerPublisher(queueName, url, userName, password, false, Session.AUTO_ACKNOWLEDGE,
				DeliveryMode.PERSISTENT);
	}

	@Override
	public Long registerPublisher(String queueName, String url, String userName, String password, boolean transacted,
			int acknowledgeMode, int deliveryMode) throws JMSException {
		Connection connection = null;
		Session session = null;
		MessageProducer producer = null;
		try {
			connection = getConnection(userName, password, url);
			connection.start();
			session = connection.createSession(transacted, acknowledgeMode);
			Destination destination = session.createQueue(queueName);
			producer = session.createProducer(destination);
			producer.setDeliveryMode(deliveryMode);
			Long token = RandomUtils.nextLong();
			ActiveMQProducer activeMQProducer = new ActiveMQProducer(session, producer, connection);
			tokenToPublisherQueue.put(token, activeMQProducer);
			return token;
		} catch (JMSException e) {
			close(producer, session, connection);
			throw e;
		} catch (RuntimeException e) {
			close(producer, session, connection);
			throw e;
		}
	}

	@Override
	public void unregisterPublisher(Long token) throws JMSException {
		ActiveMQProducer activeMQProducer = tokenToPublisherQueue.get(token);
		if (activeMQProducer != null) {
			close(activeMQProducer.getMessageProducer(), activeMQProducer.getSession(),
					activeMQProducer.getConnection());
		}
		tokenToPublisherQueue.remove(token);
	}

	@Override
	public <T extends Serializable> void publish(Long token, List<T> messages) throws JMSException {
		ActiveMQProducer activeMQProducer = tokenToPublisherQueue.get(token);
		if (activeMQProducer == null) {
			throw new RuntimeException("Producer not registered");
		}

		for (Serializable message : messages) {
			ObjectMessage obj = activeMQProducer.getSession().createObjectMessage(message);
			activeMQProducer.getMessageProducer().send(obj);
		}
	}

	@Override
	public <T extends Serializable> void publish(Long token, T message) throws JMSException {
		ActiveMQProducer activeMQProducer = tokenToPublisherQueue.get(token);
		if (activeMQProducer == null) {
			throw new RuntimeException("Producer not registered");
		}

		ObjectMessage obj = activeMQProducer.getSession().createObjectMessage(message);
		activeMQProducer.getMessageProducer().send(obj);
	}

	@Override
	public Long registerSubscriber(String queueName, String url, String userName, String password,
			MessageListener listener) throws JMSException {
		return registerSubscriber(queueName, url, userName, password, listener, false, Session.AUTO_ACKNOWLEDGE);
	}

	@Override
	public Long registerSubscriber(String queueName, String url, String userName, String password,
			MessageListener listener, boolean transacted, int acknowledgeMode) throws JMSException {
		Connection connection = null;
		Session session = null;
		MessageConsumer consumer = null;
		try {
			connection = getConnection(userName, password, url);
			connection.start();
			session = connection.createSession(transacted, acknowledgeMode);
			Destination destination = createQueue(session, queueName);

			consumer = session.createConsumer(destination);
			consumer.setMessageListener(listener);
			Long token = RandomUtils.nextLong();
			ActiveMQConsumer activeMQConsumer = new ActiveMQConsumer(consumer, session, connection);
			tokenToSubscriberQueue.put(token, activeMQConsumer);
			return token;
		} catch (JMSException e) {
			close(consumer, session, connection);
			throw e;
		} catch (RuntimeException e) {
			close(consumer, session, connection);
			throw e;
		}
	}

	@Override
	public void unregisterSubscriber(Long token) throws JMSException {
		ActiveMQConsumer activeMQConsumer = tokenToSubscriberQueue.get(token);
		if (activeMQConsumer != null) {
			close(activeMQConsumer.getConsumer(), activeMQConsumer.getSession(), activeMQConsumer.getConnection());
		}
		tokenToSubscriberQueue.remove(token);
	}

	private Connection getConnection(String userName, String password, String url) throws JMSException {
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(userName, password, url);
		return connectionFactory.createConnection();
	}

	private Destination createQueue(Session session, String queueName) throws JMSException {
		return session.createQueue(queueName);
	}

	private void close(MessageConsumer consumer, Session session, Connection connection) {
		if (consumer != null) {
			try {
				consumer.setMessageListener(null);
				consumer.close();
			} catch (Exception e) {
				LOG.error("Error in closing consumer", e);
			}
		}
		close(session, connection);
	}

	private void close(MessageProducer producer, Session session, Connection connection) {
		if (producer != null) {
			try {
				producer.close();
			} catch (Exception e) {
				LOG.error("Error in closing producer", e);
			}
		}
		close(session, connection);
	}

	private void close(Session session, Connection connection) {
		if (session != null) {
			try {
				session.close();
			} catch (Exception e) {
				LOG.error("Error in closing session", e);
			}
		}
		if (connection != null) {
			try {
				connection.close();
			} catch (Exception e) {
				LOG.error("Error in closing connection", e);
			}
		}
	}
}
