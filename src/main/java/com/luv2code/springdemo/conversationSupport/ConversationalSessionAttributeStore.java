package com.luv2code.springdemo.conversationSupport;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.support.SessionAttributeStore;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

/**
 * My implementation creates a conversation map on the session - to allow each
 * conversation to have its own store without adding the cid on the object name.
 * In this way - the cid and the session attributes are kept separated.
 * 
 * https://web.archive.org/web/20160503155432/http://duckranger.com/2012/11/add-conversation-support-to-spring-mvc
 * 
 * @author Nimo Naamani
 */

public class ConversationalSessionAttributeStore
		implements SessionAttributeStore, InitializingBean {

	@Autowired
	private RequestMappingHandlerAdapter requestMappingHandlerAdapter;
	private Logger logger = LoggerFactory
			.getLogger(ConversationalSessionAttributeStore.class.getName());

	private int keepAliveConversations = 10;

	public final static String CID_FIELD = "_cid";
	public final static String SESSION_MAP = "sessionConversationMap";

	@Override
	public void storeAttribute(WebRequest request, String attributeName,
			Object attributeValue) {
		Assert.notNull(request, "WebRequest must not be null");
		Assert.notNull(attributeName, "Attribute name must not be null");
		Assert.notNull(attributeValue, "Attribute value must not be null");

		String cId = getConversationId(request);
		if (cId == null || cId.trim().length() == 0) {
			cId = UUID.randomUUID().toString();
		}
		request.setAttribute(CID_FIELD, cId, WebRequest.SCOPE_REQUEST);
		logger.debug("storeAttribute - storing bean reference for ("
				+ attributeName + ").");
		store(request, attributeName, attributeValue, cId);
	}

	@Override
	public Object retrieveAttribute(WebRequest request, String attributeName) {
		Assert.notNull(request, "WebRequest must not be null");
		Assert.notNull(attributeName, "Attribute name must not be null");
		String cId = getConversationId(request);

		if (cId != null) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"retrieveAttribute - retrieving bean reference for ("
								+ attributeName + ") for conversation ("
								+ cId + ").");
			}
			return getConversationStore(request, cId)
					.get(attributeName);
		} else {
			return null;
		}
	}

	@Override
	public void cleanupAttribute(WebRequest request, String attributeName) {
		Assert.notNull(request, "WebRequest must not be null");
		Assert.notNull(attributeName, "Attribute name must not be null");
		String cId = getConversationId(request);

		if (logger.isDebugEnabled()) {
			logger.debug("cleanupAttribute - removing bean reference for ("
					+ attributeName + ") from conversation ("
					+ cId + ").");
		}

		Map<String, Object> conversationStore = getConversationStore(request,
				cId);
		conversationStore.remove(attributeName);

		// Delete the conversation store from the session if empty
		if (conversationStore.isEmpty()) {
			getSessionConversationsMap(request)
					.remove(cId);
		}
	}

	/**
	 * Retrieve a specific conversation's map of objects from the session. Will
	 * create the conversation map if it does not exist.
	 * 
	 * The conversation map is stored inside a session map - which is a map of
	 * maps. If this does not exist yet- it will be created too.
	 * 
	 * @param request        - the incoming request
	 * @param conversationId - the conversation id we are dealing with
	 * @return - the conversation's map
	 */
	private Map<String, Object> getConversationStore(WebRequest request,
			String conversationId) {

		Map<String, Object> conversationMap = getSessionConversationsMap(
				request).get(conversationId);
		if (conversationId != null && conversationMap == null) {
			conversationMap = new HashMap<String, Object>();
			getSessionConversationsMap(request).put(conversationId,
					conversationMap);
		}
		return conversationMap;
	}

	/**
	 * Get the session's conversations map.
	 * 
	 * @param request - the request
	 * @return - LinkedHashMap of all the conversations and their maps
	 */
	private LinkedHashMap<String, Map<String, Object>> getSessionConversationsMap(
			WebRequest request) {
		@SuppressWarnings("unchecked")
		LinkedHashMap<String, Map<String, Object>> sessionMap = (LinkedHashMap<String, Map<String, Object>>) request
				.getAttribute(SESSION_MAP, WebRequest.SCOPE_SESSION);
		if (sessionMap == null) {
			sessionMap = new LinkedHashMap<String, Map<String, Object>>();
			request.setAttribute(SESSION_MAP, sessionMap,
					WebRequest.SCOPE_SESSION);
		}
		return sessionMap;
	}

	/**
	 * Store an object on the session. If the configured maximum number of live
	 * conversations to keep is reached - clear out the oldest conversation. (If
	 * max number is configured as 0 - no removal will happen)
	 * 
	 * @param request        - the web request
	 * @param attributeName  - the name of the attribute
	 *                       (from @SessionAttributes)
	 * @param attributeValue - the value to store
	 */
	private void store(WebRequest request, String attributeName,
			Object attributeValue, String cId) {
		LinkedHashMap<String, Map<String, Object>> sessionConversationsMap = getSessionConversationsMap(
				request);
		if (keepAliveConversations > 0
				&& sessionConversationsMap.size() >= keepAliveConversations
				&& !sessionConversationsMap.containsKey(cId)) {
			// clear oldest conversation
			String key = sessionConversationsMap.keySet().iterator().next();
			sessionConversationsMap.remove(key);
		}
		getConversationStore(request, cId).put(attributeName, attributeValue);

	}

	public int getKeepAliveConversations() { return keepAliveConversations; }

	public void setKeepAliveConversations(int numConversationsToKeep) {
		keepAliveConversations = numConversationsToKeep;
	}

	/**
	 * Helper method to get conversation id from the web request
	 * 
	 * @param request - Incoming request
	 * @return - the conversationId (note that this is a request parameter, and
	 *         only gets there on form submit)
	 */
	private String getConversationId(WebRequest request) {
		String cid = request.getParameter(CID_FIELD);
		if (cid == null) {
			cid = (String) request.getAttribute(CID_FIELD,
					WebRequest.SCOPE_REQUEST);
		}
		return cid;
	}

	/**
	 * Required for wiring the RequestMappingHandlerAdapter
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		requestMappingHandlerAdapter.setSessionAttributeStore(this);
	}

}
