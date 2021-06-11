package com.luv2code.springdemo.conversationSupport;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.support.RequestDataValueProcessor;

/**
 * This processor is used to add the conversation id as a hidden field on the
 * form If the conversation id exists on the request.
 * 
 * @author Nimo Naamani http://duckranger.com
 * 
 */

public class ConversationIdRequestProcessor
		implements RequestDataValueProcessor {

	@Override
	public String processFormFieldValue(HttpServletRequest request, String name,
			String value, String type) {
		return value;
	}

	@Override
	public Map<String, String> getExtraHiddenFields(
			HttpServletRequest request) {

		// Uncomment below code if overriding requestDataValueProcessor bean
		// definition in security config
		// https://stackoverflow.com/questions/29752698/spring-mvc-session-conversation-multiple-tabs-spring-security-with-csrf
		/*
		CsrfRequestDataValueProcessor csrfRDVP = new CsrfRequestDataValueProcessor();
		Map<String, String> hiddenFields = csrfRDVP
				.getExtraHiddenFields(request);
		*/
		Map<String, String> hiddenFields = new HashMap<>();
		if (request.getAttribute(
				ConversationalSessionAttributeStore.CID_FIELD) != null) {
			hiddenFields.put(ConversationalSessionAttributeStore.CID_FIELD,
					request.getAttribute(
							ConversationalSessionAttributeStore.CID_FIELD)
							.toString());
		}
		return hiddenFields;
	}

	@Override
	public String processUrl(HttpServletRequest request, String url) {
		return url;
	}

	@Override
	public String processAction(HttpServletRequest request, String action,
			String httpMethod) {
		return action;
	}

}