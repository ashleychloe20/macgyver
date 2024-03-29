package io.macgyver.chat.hipchat;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.ning.http.client.AsyncHttpClient;

public class HipChat {

	Logger logger = LoggerFactory.getLogger(HipChat.class);
	String apiKey;

	private static AsyncHttpClient sharedInstance = null;

	public HipChat() {
		this.client = createClientIfNecessary();
	}

	public HipChat(AsyncHttpClient client) {
		Preconditions.checkNotNull(client);
		this.client = client;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	AsyncHttpClient client;

	protected AsyncHttpClient getClient() {
		Preconditions.checkNotNull(client, "client not initialized");
		return client;
	}

	public void sendMessage(String roomId, String user, String message) {
		sendMessage(roomId, user, message, false, null);
	}

	public String truncateUser(String userId) {
		if (userId != null && userId.length() > 14) {
			return userId.substring(0, 14);
		}
		return userId;
	}

	protected AsyncHttpClient createClientIfNecessary() {
		synchronized (AsyncHttpClient.class) {
			if (sharedInstance == null) {
				sharedInstance = new AsyncHttpClient();
			}
			return sharedInstance;
		}
	}

	public void sendMessage(String roomId, String user, String message,
			boolean notify, String color) {
		Map<String, String> params = new HashMap<String, String>();

		if (!Strings.isNullOrEmpty(roomId)) {
			params.put("room_id", roomId);
		}

		if (!Strings.isNullOrEmpty(message)) {
			params.put("message", message);
		}

		if (!Strings.isNullOrEmpty(user)) {
			params.put("from", truncateUser(user));
		}

		if (notify) {
			params.put("notify", notify ? "1" : "0");
		}
		if (color != null) {
			params.put("color", color);
		}
		sendMessageToRoom(params);
	}

	public void sendMessageToRoom(Map<String, String> params) {

		try {

			String query = String.format(
					HipChatConstants.ROOMS_MESSAGE_QUERY_FORMAT,
					HipChatConstants.JSON_FORMAT, apiKey);

			String url = HipChatConstants.API_BASE
					+ HipChatConstants.ROOMS_MESSAGE + query;
			StringBuilder sb = new StringBuilder();
			for (Map.Entry<String, String> entry : params.entrySet()) {
				sb.append("&" + entry.getKey() + "="
						+ URLEncoder.encode(entry.getValue(), "UTF-8"));
			}

			String payload = sb.toString();
			if (payload.startsWith("&")) {
				payload = payload.substring(1);
			}
			getClient()
					.preparePost(url)
					.addHeader("Content-Type",
							"application/x-www-form-urlencoded")
					.setBody(payload).execute();
		} catch (IOException e) {
			logger.warn("error sending message", e);
		}
	}
}
