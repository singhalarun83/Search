package com.erosnow.search.common.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class AbstractDaoImpl {
	
	@Autowired
	private SessionFactory sessionFactory;

	protected Session getSession(){
		return sessionFactory.getCurrentSession();
	}

	protected <T> Criteria createEntityCriteria(Class<T> persistentClass){
		return getSession().createCriteria(persistentClass);
	}

}
