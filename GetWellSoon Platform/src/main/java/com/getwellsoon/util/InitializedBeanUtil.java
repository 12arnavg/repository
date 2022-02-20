package com.getwellsoon.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class InitializedBeanUtil implements ApplicationContextAware {
	public InitializedBeanUtil() {}
	private static ApplicationContext appContext;
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		appContext = applicationContext;
	}

	public static <A extends Object> A getInitializedBean (Class<A> clazz) {
		return appContext.getBean(clazz);
	}
}
