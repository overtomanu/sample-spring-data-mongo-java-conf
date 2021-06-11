package com.luv2code.springdemo.config;

import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class MySpringMvcDispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
	
	static {
		// https://stackoverflow.com/questions/9117030/jul-to-slf4j-bridge
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();
	}

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] {RootConfig.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[] { WebConfig.class };
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

}






