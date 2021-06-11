package com.luv2code.springdemo.conversationSupport;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class SessionConversationAspect {

	private final ConversationIdRequestProcessor conversationIdRequestProcessor;

	public SessionConversationAspect() {
		this.conversationIdRequestProcessor = new ConversationIdRequestProcessor();
	}

	@Pointcut("execution(* org.springframework.security.web.servlet.support.csrf.CsrfRequestDataValueProcessor.getExtraHiddenFields(..) ) && args(request) )")
	protected void getExtraHiddenFields(HttpServletRequest request) {}

	@AfterReturning(pointcut = "getExtraHiddenFields(request)", returning = "hiddenFields")
	protected void addExtraHiddenFields(HttpServletRequest request,
			Map<String, String> hiddenFields) {
		Map<String, String> extraFields = conversationIdRequestProcessor
				.getExtraHiddenFields(request);
		hiddenFields.putAll(extraFields);
	}

}